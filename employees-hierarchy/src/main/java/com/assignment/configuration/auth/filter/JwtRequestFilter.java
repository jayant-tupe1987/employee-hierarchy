package com.assignment.configuration.auth.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.assignment.configuration.auth.JwtTokenUtil;
import com.assignment.constant.ApiConstants;
import com.assignment.exception.GenericException;
import com.assignment.exception.GenericExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private GenericExceptionHandler genericExceptionHandler;

	private final RequestMatcher requestMatcher = new AntPathRequestMatcher("/users/**");

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		try {
			if (!requestMatcher.matches(request)) {
				final String requestTokenHeader = request.getHeader("Authorization");
				String username = null;
				String jwtToken = null;
				// JWT Token is in the form "Bearer token". Remove Bearer word and get
				// only the Token
				if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
					jwtToken = requestTokenHeader.substring(7);
					try {
						username = jwtTokenUtil.getUsernameFromToken(jwtToken);
					} catch (IllegalArgumentException e) {
						throw new GenericException(ApiConstants.UNABLE_TO_GET_JWT_TOKEN);
					} catch (ExpiredJwtException e) {
						throw new GenericException(ApiConstants.JWT_TOKEN_EXPIRED);
					} catch (Exception e) {
						throw new GenericException(ApiConstants.JWT_TOKEN_EMPTY);
					}
				} else {
					throw new GenericException(ApiConstants.JWT_TOKEN_NO_BEARAER);
				}

				// Once we get the token validate it.
				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
					// if token is valid configure Spring Security to manually set authentication
					if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						usernamePasswordAuthenticationToken
								.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						// After setting the Authentication in the context, we specify
						// that the current user is authenticated. So it passes the
						// Spring Security Configurations successfully.
						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					}
				} else {
					logger.error(ApiConstants.JWT_TOKEN_EMPTY);
					throw new GenericException(ApiConstants.JWT_TOKEN_EMPTY);
				}
			}
			chain.doFilter(request, response);
		} catch (Exception e) {
			ResponseEntity<?> res = genericExceptionHandler.handleAllExceptions(e, null);
			ObjectMapper mapper = new ObjectMapper();
			PrintWriter out = response.getWriter();
			out.print(mapper.writeValueAsString(res));
			out.flush();
			return;
		}
	}

}