package com.assignment.dto;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import com.assignment.constant.ApiConstants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse implements Serializable {
	
	private static final long serialVersionUID = 2663274349091702934L;
	
	private String status;
	private Integer code;
	private String message;
	private Object data;

	public static ApiResponse getSuccessResponse(Object data) {
		return new ApiResponse(ApiConstants.SUCCESS, HttpStatus.OK.value(), ApiConstants.SUCCESS, data);
	}

	public static ApiResponse getSuccessResponse(Object data, String message) {
		return new ApiResponse(ApiConstants.SUCCESS, HttpStatus.OK.value(), message, data);
	}

	public static ApiResponse getSuccessResponse(Integer status, String message, Object data) {
		return new ApiResponse(ApiConstants.SUCCESS, status, message, data);
	}

	public static ApiResponse getSuccessResponse(Object data, Integer status) {
		return new ApiResponse(ApiConstants.SUCCESS, status, ApiConstants.SUCCESS, data);
	}

	public static ApiResponse getErrorResponse(Integer status, String message, Object data) {
		return new ApiResponse(ApiConstants.ERROR, status, message, data);
	}

	public static ApiResponse getErrorResponse(Integer status, String message) {
		return new ApiResponse(ApiConstants.ERROR, status, message, null);
	}
}
