package com.assignment.configuration.auth.filter;

import static org.mockito.Mockito.doReturn;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

import com.assignment.configuration.auth.JwtTokenUtil;

@RunWith(MockitoJUnitRunner.class)
public class JwtRequestFilterTest {

	@InjectMocks
	JwtRequestFilter jwtRequestFilter = new JwtRequestFilter();

	@Mock
	private UserDetailsService userDetailsService;

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

}
