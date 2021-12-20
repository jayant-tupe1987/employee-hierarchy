package com.assignment.service;

import java.util.HashMap;


public interface EmployeeService {

	String createHierarchy(HashMap<String, String> relationships);

	String buildHierarchy();

	String getSuperiors(String staffName);

}
