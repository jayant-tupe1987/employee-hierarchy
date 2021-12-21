package com.assignment.exception;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.assignment.constant.ApiConstants;
import com.assignment.controller.UserController;
import com.assignment.dto.ApiResponse;
import com.assignment.dto.UserDto;

@RunWith(MockitoJUnitRunner.class)
public class GenericExceptionHandlerTest {

	@InjectMocks
	GenericExceptionHandler genericExceptionHandler = new GenericExceptionHandler();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testHandleHttpMessageNotReadableValid() {
		byte[] content = {};
		HttpInputMessage httpInputMessage = new MockHttpInputMessage(content);
		HttpMessageNotReadableException ex = new HttpMessageNotReadableException("In correct Input Data format",
				httpInputMessage);
		ApiResponse apiError = ApiResponse.getErrorResponse(HttpStatus.BAD_REQUEST.value(),
				StringUtils.hasText(ex.getLocalizedMessage())
						? ApiConstants.DATA_FORMAT_EXCEPTION + ex.getLocalizedMessage()
						: ApiConstants.DATA_FORMAT_EXCEPTION + ex.getMessage());
		ResponseEntity<?> actRes = genericExceptionHandler.handleHttpMessageNotReadable(ex, null, null, null);
		ResponseEntity<?> expRes = new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
		assertEquals(expRes, actRes);
	}

	@Test
	public void testHandleAllExceptionsScenBadCredentialsException() {
		BadCredentialsException ex = new BadCredentialsException(ApiConstants.INVALID_CREDENTIALS);
		ApiResponse apiError = ApiResponse.getErrorResponse(HttpStatus.UNAUTHORIZED.value(),
				StringUtils.hasText(ex.getLocalizedMessage()) ? ex.getLocalizedMessage() : ex.getMessage());
		ResponseEntity<?> actRes = genericExceptionHandler.handleAllExceptions(ex, null);
		ResponseEntity<?> expRes = new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
		assertEquals(expRes, actRes);
	}

	@Test
	public void testHandleAllExceptionsScenDisabledException() {
		DisabledException ex = new DisabledException(ApiConstants.USER_DISABLED);
		ApiResponse apiError = ApiResponse.getErrorResponse(HttpStatus.FORBIDDEN.value(),
				StringUtils.hasText(ex.getLocalizedMessage()) ? ex.getLocalizedMessage() : ex.getMessage());
		ResponseEntity<?> actRes = genericExceptionHandler.handleAllExceptions(ex, null);
		ResponseEntity<?> expRes = new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
		assertEquals(expRes, actRes);
	}

	@Test
	public void testHandleAllExceptionsScenUsernameNotFoundException() {
		UsernameNotFoundException ex = new UsernameNotFoundException(ApiConstants.NO_USER_FOUND);
		ApiResponse apiError = ApiResponse.getErrorResponse(HttpStatus.NOT_FOUND.value(),
				StringUtils.hasText(ex.getLocalizedMessage()) ? ex.getLocalizedMessage() : ex.getMessage());
		ResponseEntity<?> actRes = genericExceptionHandler.handleAllExceptions(ex, null);
		ResponseEntity<?> expRes = new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
		assertEquals(expRes, actRes);
	}

	@Test
	public void testHandleAllExceptionsScenGenericException() {
		GenericException ex = new GenericException(ApiConstants.LOOP_HIERARCHY);
		ApiResponse apiError = ApiResponse.getErrorResponse(HttpStatus.BAD_REQUEST.value(),
				StringUtils.hasText(ex.getLocalizedMessage()) ? ex.getLocalizedMessage() : ex.getMessage());
		ResponseEntity<?> actRes = genericExceptionHandler.handleAllExceptions(ex, null);
		ResponseEntity<?> expRes = new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
		assertEquals(expRes, actRes);
	}

	@Test
	public void testHandleAllExceptionsScenException() {
		Exception ex = new Exception(ApiConstants.ERROR);
		ApiResponse apiError = ApiResponse.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				StringUtils.hasText(ex.getLocalizedMessage()) ? ex.getLocalizedMessage() : ex.getMessage());
		ResponseEntity<?> actRes = genericExceptionHandler.handleAllExceptions(ex, null);
		ResponseEntity<?> expRes = new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
		assertEquals(expRes, actRes);
	}

	@Test
	public void testHandleMethodArgumentNotValidScenException() throws NoSuchMethodException, SecurityException {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(null, null);
		MethodParameter parameter = new MethodParameter(
				new UserController().getClass().getMethod("createAuthenticationToken", UserDto.class), 0);
		MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, errors);
		ApiResponse apiError = ApiResponse.getErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage(),
				new ArrayList<>());
		ResponseEntity<?> actRes = genericExceptionHandler.handleMethodArgumentNotValid(ex, null, null, null);
		ResponseEntity<?> expRes = new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
		assertEquals(expRes, actRes);
	}

}
