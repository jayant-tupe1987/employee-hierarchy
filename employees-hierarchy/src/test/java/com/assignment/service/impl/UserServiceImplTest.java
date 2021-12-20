package com.assignment.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.assignment.configuration.auth.JwtTokenUtil;
import com.assignment.constant.ApiConstants;
import com.assignment.dto.JwtResponse;
import com.assignment.dto.UserDto;
import com.assignment.entity.User;
import com.assignment.repository.UserRepository;
import com.assignment.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

	@InjectMocks
	private UserService userService = new UserServiceImpl();

	@Mock
	private UserRepository userRepository;

	private PasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

	@Mock
	private JwtTokenUtil jwtTokenUtil;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(userService, "bcryptEncoder", bcryptEncoder);
	}

	@Test
	public void testAuthenticateAndGenerateTokenScenValid() {
		String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXlhbnQiLCJleHAiOjE2NDAwMjc3ODEsImlhdCI6MTYzOTk0MTM4MX0.nG989O_Hfoaf9X1GJpwRcIL0Vp86pr7dS3owC2TnAhPiwadDHhmSZ2ON3YlKglCBGep98IGO9q6jQSmFkXijXg";
		UserDto authenticationRequest = new UserDto();
		authenticationRequest.setUsername("Jayant");
		authenticationRequest.setPassword("Jayant123");
		User user = new User();
		user.setName("Jayant");
		user.setEncryptedPassword(bcryptEncoder.encode(authenticationRequest.getPassword()));
		doReturn(user).when(userRepository).findByName("Jayant");
		doReturn(token).when(jwtTokenUtil).generateToken(any());
		JwtResponse jwtResponse = userService.authenticateAndGenerateToken(authenticationRequest);
		assertEquals(jwtResponse.getJwtToken(), token);
		Mockito.verify(jwtTokenUtil, Mockito.times(1)).generateToken(any());
	}

	@Test(expected = BadCredentialsException.class)
	public void testAuthenticateAndGenerateTokenScenInValidUserInPasswordIncorrect() {
		UserDto authenticationRequest = new UserDto();
		authenticationRequest.setUsername("Jayant");
		authenticationRequest.setPassword("Jayant123");
		User user = new User();
		user.setName("Jayant");
		user.setEncryptedPassword(bcryptEncoder.encode("1212"));
		doReturn(user).when(userRepository).findByName("Jayant");
		userService.authenticateAndGenerateToken(authenticationRequest);
	}

	@Test(expected = UsernameNotFoundException.class)
	public void testAuthenticateAndGenerateTokenScenInValidUserUserNull() {
		UserDto authenticationRequest = new UserDto();
		authenticationRequest.setUsername("Jayant");
		authenticationRequest.setPassword("Jayant123");
		doReturn(null).when(userRepository).findByName("Jayant");
		userService.authenticateAndGenerateToken(authenticationRequest);
	}

	@Test
	public void testSaveScenValid() {
		UserDto authenticationRequest = new UserDto();
		authenticationRequest.setUsername("Jayant");
		authenticationRequest.setPassword("Jayant123");
		User user = new User();
		user.setName("Jayant");
		user.setEncryptedPassword(bcryptEncoder.encode(authenticationRequest.getPassword()));
		String actualOutput = userService.save(authenticationRequest);
		assertEquals(ApiConstants.REGISTER_SUCCESS.replace(":user", user.getName()), actualOutput);
	}

}
