package com.assignment.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.assignment.dto.JwtResponse;
import com.assignment.dto.UserDto;
import com.assignment.entity.User;

public interface UserService extends UserDetailsService {

	public User save(UserDto userDto);

	public JwtResponse authenticateAndGenerateToken(UserDto authenticationRequest);

}
