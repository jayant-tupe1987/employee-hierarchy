package com.assignment.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserDto implements Serializable {

	private static final long serialVersionUID = 6820519577090785289L;

	private String username;

	private String password;

}