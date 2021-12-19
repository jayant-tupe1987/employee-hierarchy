package com.assignment.exception;

public class GenericException extends RuntimeException {

	private static final long serialVersionUID = -8963131724769405164L;

	private final String message;

	public GenericException(String message, Throwable cause) {
		super();
		this.message = message;
	}

	public GenericException(String message) {
		super();
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
