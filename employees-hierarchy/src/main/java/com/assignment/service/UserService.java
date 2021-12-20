package com.assignment.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.assignment.dto.JwtResponse;
import com.assignment.dto.UserDto;

public interface UserService extends UserDetailsService {

	public String save(UserDto userDto);

	public JwtResponse authenticateAndGenerateToken(UserDto authenticationRequest);

}
