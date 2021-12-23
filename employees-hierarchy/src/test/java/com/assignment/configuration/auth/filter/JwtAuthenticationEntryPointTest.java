package com.assignment.configuration.auth.filter;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;

import com.assignment.configuration.auth.JwtAuthenticationEntryPoint;

@RunWith(MockitoJUnitRunner.class)
public class JwtAuthenticationEntryPointTest {

	@InjectMocks
	JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint = new JwtAuthenticationEntryPoint();

	@Test
	public void testDoFilterInternalInValidTokenExpired() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		AuthenticationException authException = new InsufficientAuthenticationException(
				"Full authentication is required to access this resource. Bearer Token is either not provided or value provided is in-correct or expired");
		jwtAuthenticationEntryPoint.commence(request, response, authException);
		assertEquals(response.getStatus(), HttpStatus.UNAUTHORIZED.value());
	}
}
