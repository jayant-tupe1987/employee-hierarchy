
package com.assignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.dto.UserDto;
import com.assignment.service.impl.UserServiceImpl;

import io.swagger.annotations.ApiOperation;

@RestController

@RequestMapping("users")
public class UserController {

	
	@Autowired
	private UserServiceImpl userService;

	@ApiOperation(value = "Authenticate User.")
	@PostMapping(value = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody UserDto authenticationRequest) throws Exception {
		return new ResponseEntity<>(userService.authenticateAndGenerateToken(authenticationRequest), HttpStatus.OK);
	}

	@ApiOperation(value = "Register User.")
	@PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveUser(@RequestBody UserDto user) throws Exception {
		return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
	}

}
