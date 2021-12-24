package com.assignment.controller;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.constant.ApiConstants;
import com.assignment.dto.ApiResponse;
import com.assignment.service.EmployeeService;

/**
 * EmployeeController has apis to create, update and get employee hierarchy 
 * @author Jayant
 *
 */
@RestController
@RequestMapping("hierarchy")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createHierarchy(@RequestBody @Valid Map<String, String> relationships) {
		return new ResponseEntity<>(ApiResponse.getSuccessResponse(employeeService.createHierarchy(relationships),
				ApiConstants.CREATE_HIERARCHY_SUCCESS), HttpStatus.CREATED);
	}

	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateHierarchy(@RequestBody @Valid Map<String, String> relationships) {
		return new ResponseEntity<>(ApiResponse.getSuccessResponse(employeeService.updateHierarchy(relationships),
				ApiConstants.UPDATE_HIERARCHY_SUCCESS), HttpStatus.OK);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getHierarchy() {
		return new ResponseEntity<>(
				ApiResponse.getSuccessResponse(employeeService.buildHierarchy(), ApiConstants.FETCH_HIERARCHY_SUCCESS),
				HttpStatus.OK);
	}

	@GetMapping(value = "/{name}/superiors", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<?> getSuperiors(@PathVariable("name") @NotBlank String staffName) {
		return new ResponseEntity<>(ApiResponse.getSuccessResponse(employeeService.getSuperiors(staffName),
				ApiConstants.FETCH_HIERARCHY_SUCCESS), HttpStatus.OK);
	}
}
