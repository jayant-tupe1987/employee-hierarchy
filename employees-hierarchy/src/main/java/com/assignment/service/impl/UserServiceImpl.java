package com.assignment.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.assignment.configuration.auth.JwtTokenUtil;
import com.assignment.constant.ApiConstants;
import com.assignment.dto.JwtResponse;
import com.assignment.dto.UserDto;
import com.assignment.entity.User;
import com.assignment.repository.UserRepository;
import com.assignment.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	public JwtResponse authenticateAndGenerateToken(UserDto userDto) {
		
		final UserDetails userDetails = loadUserByUsername(userDto.getUsername());
		
		if (!bcryptEncoder.matches(userDto.getPassword(), userDetails.getPassword())) {
			throw new BadCredentialsException(ApiConstants.INVALID_CREDENTIALS);
		} else {
			return new JwtResponse(jwtTokenUtil.generateToken(userDetails));
		}
	}

	@Override
	public String save(UserDto userDto) {
		User user = new User();
		user.setName(userDto.getUsername());
		user.setEncryptedPassword(bcryptEncoder.encode(userDto.getPassword()));
		userRepository.save(user);
		return ApiConstants.REGISTER_SUCCESS.replace(":user", user.getName());
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByName(username);
		if (user == null) {
			throw new UsernameNotFoundException(ApiConstants.NO_USER_FOUND + username);
		}
		return new org.springframework.security.core.userdetails.User(user.getName(), user.getEncryptedPassword(),
				new ArrayList<>());
	}
}
