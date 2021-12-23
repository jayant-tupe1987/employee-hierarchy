package com.assignment.controller;

import java.util.HashMap;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.constant.ApiConstants;
import com.assignment.dto.ApiResponse;
import com.assignment.service.EmployeeService;

@RestController
@RequestMapping("hierarchy")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createHierarchy(@RequestBody @Valid HashMap<String, String> relationships) {
		return new ResponseEntity<>(ApiResponse.getSuccessResponse(employeeService.createHierarchy(relationships),
				ApiConstants.CREATE_HIERARCHY_SUCCESS), HttpStatus.CREATED);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getHierarchy() {
		String response = employeeService.buildHierarchy();
		return new ResponseEntity<>(ApiResponse.getSuccessResponse(response), HttpStatus.OK);
	}

	@GetMapping(value = "/{name}/superiors", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<?> getSuperiors(@PathVariable("name") @NotBlank String staffName) {
		return new ResponseEntity<>(ApiResponse.getSuccessResponse(employeeService.getSuperiors(staffName)),
				HttpStatus.OK);
	}
}
