package com.assignment.configuration.auth;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.assignment.constant.ApiConstants;
import com.assignment.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = -3210132727117883946L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		ApiResponse apiError = ApiResponse.getErrorResponse(HttpStatus.BAD_REQUEST.value(),
				StringUtils.hasText(authException.getLocalizedMessage())
						? authException.getLocalizedMessage() + ApiConstants.INCORRECT_TOKEN
						: authException.getMessage() + ApiConstants.INCORRECT_TOKEN);
		ServletServerHttpResponse res = new ServletServerHttpResponse(response);
		res.setStatusCode(HttpStatus.UNAUTHORIZED);
		res.getServletResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		res.getBody().write(new ObjectMapper().writeValueAsString(apiError).getBytes());
		res.flush();
		res.close();
	}
}