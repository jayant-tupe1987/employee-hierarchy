package com.assignment.service.impl;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.assignment.configuration.auth.JwtTokenUtil;
import com.assignment.constant.ApiConstants;
import com.assignment.dto.JwtResponse;
import com.assignment.dto.UserDto;
import com.assignment.entity.User;
import com.assignment.exception.GenericException;
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

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public JwtResponse authenticateAndGenerateToken(UserDto userDto) {
		// authenticate(authenticationRequest.getUsername(),
		// authenticationRequest.getPassword());

		User user = userRepository.findByName(userDto.getUsername());
		if (Objects.isNull(user)) {
			throw new BadCredentialsException("User: " + userDto.getUsername() + " is not allowed ");
		} else if (!bcryptEncoder.matches(userDto.getPassword(), user.getEncryptedPassword())) {
			throw new BadCredentialsException("User Credentials are incorrect ");
		} else {
			final UserDetails userDetails = loadUserByUsername(userDto.getUsername());
			return new JwtResponse(jwtTokenUtil.generateToken(userDetails));
		}

	}

	@Override
	public User save(UserDto userDto) {
		User user = new User();
		user.setName(userDto.getUsername());
		user.setEncryptedPassword(bcryptEncoder.encode(userDto.getPassword()));
		return userRepository.save(user);
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

	private void authenticate(String username, String password) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new GenericException(ApiConstants.USER_DISABLED, e);
		} catch (BadCredentialsException e) {
			throw new GenericException(ApiConstants.INVALID_CREDENTIALS, e);
		}
	}
}
