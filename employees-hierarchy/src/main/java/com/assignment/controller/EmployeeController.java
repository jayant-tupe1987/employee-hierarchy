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

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("hierarchy")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@ApiOperation(value = "Create Hierarchy.")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createHierarchy(@RequestBody @Valid HashMap<String, String> relationships) {
		return new ResponseEntity<>(ApiResponse.getSuccessResponse(employeeService.createHierarchy(relationships),
				ApiConstants.CREATE_HIERARCHY_SUCCESS), HttpStatus.CREATED);
	}

	@ApiOperation(value = "Get Hierarchy.")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getHierarchy() {
		return new ResponseEntity<>(ApiResponse.getSuccessResponse(employeeService.buildHierarchy()), HttpStatus.OK);
	}

	@ApiOperation(value = "Get Supervisors")
	@GetMapping(value = "/{name}/superiors", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<?> getSuperiors(@PathVariable("name") @NotBlank String staffName) {
		return new ResponseEntity<>(ApiResponse.getSuccessResponse(employeeService.getSuperiors(staffName)),
				HttpStatus.OK);
	}
}
