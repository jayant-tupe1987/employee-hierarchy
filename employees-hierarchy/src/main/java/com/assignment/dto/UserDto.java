package com.assignment.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto implements Serializable {

	private static final long serialVersionUID = 6820519577090785289L;

	@NotNull
	private String username;
	@NotNull
	private String password;

}