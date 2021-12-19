package com.assignment.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class JwtResponse implements Serializable {

	private static final long serialVersionUID = 6301071617799371151L;
	
	private String jwtToken;

}