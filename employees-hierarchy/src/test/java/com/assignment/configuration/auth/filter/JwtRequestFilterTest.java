package com.assignment.configuration.auth.filter;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.any;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

import com.assignment.configuration.auth.JwtTokenUtil;
import com.assignment.constant.ApiConstants;
import com.assignment.dto.ApiResponse;
import com.assignment.exception.GenericException;
import com.assignment.exception.GenericExceptionHandler;

@RunWith(MockitoJUnitRunner.class)
public class JwtRequestFilterTest {

	@InjectMocks
	JwtRequestFilter jwtRequestFilter = new JwtRequestFilter();

	@Mock
	private UserDetailsService userDetailsService;

	@Mock
	private GenericExceptionHandler genericExceptionHandler;

	private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

	private static String token;

	private UserDetails userDetails;

	private Long expiryTime;

	private String secret;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(jwtTokenUtil, "expiryTime", new Long(86400000));
		ReflectionTestUtils.setField(jwtTokenUtil, "secret", "PBKDF2WithHmacqweFqasd2sghSHA256");
		userDetails = new User("Jayant", "Jayant123", new ArrayList<>());
		token = jwtTokenUtil.generateToken(userDetails);
		ReflectionTestUtils.setField(jwtRequestFilter, "jwtTokenUtil", jwtTokenUtil);
	}

	@Test
	public void testDoFilterInternalValid() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain chain = new MockFilterChain();
		request.addHeader("Authorization", "Bearer " + token);
		request.setAttribute("Authorization", "Bearer " + token);
		doReturn(userDetails).when(userDetailsService).loadUserByUsername("Jayant");
		jwtRequestFilter.doFilterInternal(request, response, chain);
	}

	@Test(expected = BadCredentialsException.class)
	public void testDoFilterInternalInValidUnableToGetJWT() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain chain = new MockFilterChain();
		request.addHeader("Authorization", "Bearer ");

		ApiResponse apiError = ApiResponse.getErrorResponse(HttpStatus.BAD_REQUEST.value(),
				ApiConstants.INCORRECT_TOKEN);
		ResponseEntity<?> res = new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
		doReturn(userDetails).when(userDetailsService).loadUserByUsername("Jayant");
		doReturn(res).when(genericExceptionHandler)
				.handleAllExceptions(new GenericException(ApiConstants.INCORRECT_TOKEN), null);
		jwtRequestFilter.doFilterInternal(request, response, chain);
		verify(genericExceptionHandler, Mockito.times(1)).handleAllExceptions(any(), any());
	}

	@Test(expected = AuthenticationServiceException.class)
	public void testDoFilterInternalInValidTokenWithNoBearer() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain chain = new MockFilterChain();
		request.addHeader("Authorization", "ghhsds ");

		ApiResponse apiError = ApiResponse.getErrorResponse(HttpStatus.BAD_REQUEST.value(),
				ApiConstants.INCORRECT_TOKEN);
		ResponseEntity<?> res = new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
		doReturn(userDetails).when(userDetailsService).loadUserByUsername("Jayant");
		doReturn(res).when(genericExceptionHandler)
				.handleAllExceptions(new GenericException(ApiConstants.INCORRECT_TOKEN), null);
		jwtRequestFilter.doFilterInternal(request, response, chain);
		verify(genericExceptionHandler, Mockito.times(1)).handleAllExceptions(any(), any());
	}

	@Test(expected = BadCredentialsException.class)
	public void testDoFilterInternalInValidToken() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain chain = new MockFilterChain();
		request.addHeader("Authorization",
				"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXlhbnQiLCJleHAiOjE2NDAyODg4ODEsImlhdCI6MTY0MDIwMjQ4MX0.Hmt6qlnYhc8AgQoYopyWaTJaDqwHkU6-z5OMBdRJY5xkEWIntagisSjLzb1mIJ6ZDYjw2mlptzluwKQ8xByg ");

		ApiResponse apiError = ApiResponse.getErrorResponse(HttpStatus.BAD_REQUEST.value(),
				ApiConstants.INCORRECT_TOKEN);
		ResponseEntity<?> res = new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
		doReturn(userDetails).when(userDetailsService).loadUserByUsername("Jayant");
		jwtRequestFilter.doFilterInternal(request, response, chain);
	}
	
	@Test(expected = InsufficientAuthenticationException.class)
	public void testDoFilterInternalInValidTokenExpired() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain chain = new MockFilterChain();
		request.addHeader("Authorization",
				"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXlhbnQiLCJleHAiOjE2NDAyODAzMzQsImlhdCI6MTY0MDI3OTczNH0.NVBo8MtP18wyL-SmZNIEnwlq0z3CM15GLBk78_VMLGK4lv9-tLuML4h4LXvIXuZMI9saj9wyi6gWFi8UJCqfsg");
		ApiResponse apiError = ApiResponse.getErrorResponse(HttpStatus.BAD_REQUEST.value(),
				ApiConstants.INCORRECT_TOKEN);
		ResponseEntity<?> res = new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
		doReturn(userDetails).when(userDetailsService).loadUserByUsername("Jayant");
		jwtRequestFilter.doFilterInternal(request, response, chain);
	}
}
