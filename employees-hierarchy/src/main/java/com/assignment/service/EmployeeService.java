package com.assignment.service;

import java.util.Map;


public interface EmployeeService {

	String createHierarchy(Map<String, String> relationships);
	
	String updateHierarchy(Map<String, String> relationships);

	String buildHierarchy();

	String getSuperiors(String staffName);

}
