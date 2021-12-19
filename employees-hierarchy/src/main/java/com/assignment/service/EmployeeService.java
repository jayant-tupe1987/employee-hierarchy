package com.assignment.service;

import java.util.HashMap;


public interface EmployeeService {

	String buildRelationship(HashMap<String, String> relationships);

	String buildHierarchy();

	String getSuperiors(String staffName);

}
