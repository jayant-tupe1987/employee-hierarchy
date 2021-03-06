
package com.assignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.dto.ApiResponse;
import com.assignment.dto.UserDto;
import com.assignment.service.UserService;

/**
 * UserController has apis to register, authenticate user
 * @author Jayant
 *
 */
@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping(value = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody UserDto request) {
		return new ResponseEntity<>(userService.authenticateAndGenerateToken(request), HttpStatus.OK);
	}

	@PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerUser(@RequestBody  UserDto user) {
		return new ResponseEntity<>(ApiResponse.getSuccessResponse(userService.save(user)), HttpStatus.OK);
	}

}
