package com.assignment.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.assignment.constant.ApiConstants;
import com.assignment.dto.ApiResponse;
import com.assignment.service.EmployeeService;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeControllerTest {

	@InjectMocks
	EmployeeController employeeController = new EmployeeController();

	@Mock
	private EmployeeService employeeService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCreateHierarchySceValid() {
		HashMap<String, String> relationships = new LinkedHashMap<>();
		relationships.put("Sophie", "Jonas");
		relationships.put("Nick", "Sophie");
		relationships.put("Barbara", "Nick");
		relationships.put("Pete", "Nick");
		String expectedBuildHierarchy = "{\"Jonus\": {\"Sophie\": {\"Nick\": {\"Barbara\": {}  , \"Pete\": {} } } } }";
		ResponseEntity responseExpected = new ResponseEntity<>(
				ApiResponse.getSuccessResponse(expectedBuildHierarchy, ApiConstants.CREATE_HIERARCHY_SUCCESS),
				HttpStatus.CREATED);
		doReturn(expectedBuildHierarchy).when(employeeService).createHierarchy(relationships);
		ResponseEntity responseReturned = employeeController.createHierarchy(relationships);
		assertEquals(responseExpected, responseReturned);
		Mockito.verify(employeeService, Mockito.times(1)).createHierarchy(any());
	}

	@Test
	public void testGetHierarchySceValid() {
		String expectedBuildHierarchy = "{\"Jonus\": {\"Sophie\": {\"Nick\": {\"Barbara\": {}  , \"Pete\": {} } } } }";
		ResponseEntity responseExpected = new ResponseEntity<>(ApiResponse.getSuccessResponse(expectedBuildHierarchy),
				HttpStatus.OK);
		doReturn(expectedBuildHierarchy).when(employeeService).buildHierarchy();
		ResponseEntity responseReturned = employeeController.getHierarchy();
		assertEquals(responseExpected, responseReturned);
		Mockito.verify(employeeService, Mockito.times(1)).buildHierarchy();
	}

	@Test
	public void testGetSuperiorsSceValid() {
		String expectedBuildHierarchy = "{\"Jonus\": {\"Sophie\": {\"Nick\": {\"Barbara\": {}  , \"Pete\": {} } } } }";
		ResponseEntity responseExpected = new ResponseEntity<>(
				ApiResponse.getSuccessResponse("{\"Jonus\": {\"Sophie\": {\"Nick\": {} } } }"), HttpStatus.OK);
		doReturn("{\"Jonus\": {\"Sophie\": {\"Nick\": {} } } }").when(employeeService)
				.getSuperiors(any());
		ResponseEntity responseReturned = employeeController.getSuperiors("Nick");
		assertEquals(responseExpected, responseReturned);
		Mockito.verify(employeeService, Mockito.times(1)).getSuperiors("Nick");
	}

}
