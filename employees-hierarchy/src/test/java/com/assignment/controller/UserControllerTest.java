package com.assignment.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Matchers.any;
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

import com.assignment.constant.ApiConstants;
import com.assignment.dto.ApiResponse;
import com.assignment.dto.JwtResponse;
import com.assignment.dto.UserDto;
import com.assignment.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

	@InjectMocks
	UserController userController = new UserController();

	@Mock
	private UserService userService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testRegisterUserSceValid() {
		UserDto user = new UserDto();
		user.setUsername("Jayant");
		ResponseEntity<?> responseExpected = new ResponseEntity<>(
				ApiResponse.getSuccessResponse(ApiConstants.REGISTER_SUCCESS.replace(":user", user.getUsername())),
				HttpStatus.OK);
		doReturn(ApiConstants.REGISTER_SUCCESS.replace(":user", user.getUsername())).when(userService).save(user);
		ResponseEntity<?> responseReturned = userController.registerUser(user);
		assertEquals(responseExpected, responseReturned);
		Mockito.verify(userService, Mockito.times(1)).save(user);
	}

	@Test
	public void testcreateAuthenticationTokenSceValid() {
		UserDto user = new UserDto();
		user.setUsername("Jayant");
		JwtResponse jwtResponse = new JwtResponse("12232sds.sds.sddsd.");
		ResponseEntity<?> responseExpected = new ResponseEntity<>(jwtResponse, HttpStatus.OK);
		doReturn(jwtResponse).when(userService).authenticateAndGenerateToken(user);
		ResponseEntity<?> responseReturned = userController.createAuthenticationToken(user);
		assertEquals(responseExpected, responseReturned);
		Mockito.verify(userService, Mockito.times(1)).authenticateAndGenerateToken(user);
	}

}
