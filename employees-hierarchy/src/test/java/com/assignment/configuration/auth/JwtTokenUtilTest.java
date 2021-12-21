package com.assignment.configuration.auth;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class JwtTokenUtilTest {

	@InjectMocks
	JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

	private Long expiryTime;

	private String secret;

	private static String token;

	private UserDetails userDetails;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(jwtTokenUtil, "expiryTime", new Long(86400000));
		ReflectionTestUtils.setField(jwtTokenUtil, "secret", "PBKDF2WithHmacqweFqasd2sghSHA256");
		userDetails = new User("Jayant", "Jayant123", new ArrayList<>());
		token = jwtTokenUtil.generateToken(userDetails);
	}

	@Test
	public void getUsernameFromTokenTestScenValid() {
		String user = jwtTokenUtil.getUsernameFromToken(token);
		assertEquals(userDetails.getUsername(), user);
	}

	@Test
	public void validateTokenTestScenValid() {
		boolean isvalidToken = jwtTokenUtil.validateToken(token, userDetails);
		assertEquals(true, isvalidToken);
	}

}
