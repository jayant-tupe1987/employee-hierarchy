package com.assignment.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.assignment.constant.ApiConstants;
import com.assignment.dto.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ApiResponse apiError = ApiResponse.getErrorResponse(HttpStatus.BAD_REQUEST.value(),
				StringUtils.hasText(ex.getLocalizedMessage())
						? ApiConstants.DATA_FORMAT_EXCEPTION + ex.getLocalizedMessage()
						: ApiConstants.DATA_FORMAT_EXCEPTION + ex.getMessage());
		log.error(ex.getLocalizedMessage());
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ Throwable.class })
	public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
		if (ex instanceof BadCredentialsException) {
			ApiResponse apiError = ApiResponse.getErrorResponse(HttpStatus.UNAUTHORIZED.value(),
					StringUtils.hasText(ex.getLocalizedMessage()) ? ex.getLocalizedMessage() : ex.getMessage());
			log.error(ex.getLocalizedMessage());
			return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
		} else if (ex instanceof DisabledException) {
			ApiResponse apiError = ApiResponse.getErrorResponse(HttpStatus.FORBIDDEN.value(),
					StringUtils.hasText(ex.getLocalizedMessage()) ? ex.getLocalizedMessage() : ex.getMessage());
			log.error(ex.getLocalizedMessage());
			return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
		} else if (ex instanceof UsernameNotFoundException) {
			ApiResponse apiError = ApiResponse.getErrorResponse(HttpStatus.NOT_FOUND.value(),
					StringUtils.hasText(ex.getLocalizedMessage()) ? ex.getLocalizedMessage() : ex.getMessage());
			log.error(ex.getLocalizedMessage());
			return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
		} else if (ex instanceof GenericException) {
			ApiResponse apiError = ApiResponse.getErrorResponse(HttpStatus.BAD_REQUEST.value(),
					StringUtils.hasText(ex.getLocalizedMessage()) ? ex.getLocalizedMessage() : ex.getMessage());
			log.error(ex.getLocalizedMessage());
			return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
		} else {
			ApiResponse apiError = ApiResponse.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					StringUtils.hasText(ex.getLocalizedMessage()) ? ex.getLocalizedMessage() : ex.getMessage());
			log.error(ex.getLocalizedMessage());
			return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errors = new ArrayList<String>();
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}
		ApiResponse apiError = ApiResponse.getErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage(),
				errors);
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}
}
