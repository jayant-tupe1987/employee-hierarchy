package com.assignment.constant;

public class ApiConstants {

	public static final String ERROR = "Error";
	public static final String DATA_FORMAT_EXCEPTION = "Please send the data in correct format. ";
	public static final String SUCCESS = "Success";
	public static final String USER_DISABLED = "User Disabled";
	public static final String USER_NOT_ALLOWED = " is not allowed";
	public static final String INVALID_CREDENTIALS = "Invalid Credentials";
	public static final String REGISTER_SUCCESS = ":user registered successfully.";
	public static final String UPDATE_HIERARCHY_SUCCESS = "Employee Hierarchy Updated Successfully.";
	public static final String CREATE_HIERARCHY_SUCCESS = "Employee Hierarchy Created Successfully.";
	public static final String FETCH_HIERARCHY_SUCCESS = "Available Hierarchy Fetched Successfully.";
	public static final String NO_USER_FOUND = "User not found with username: ";
	public static final String MULTI_ROOT = "There are multiple roots present, please check your hierarchy for submission";
	public static final String EMPLY_HIERARCHY = "Request has no hierarchy";
	public static final String LOOP_HIERARCHY = "There is looping issue with the supervisors payload, please check your hierarchy for submission";
	public static final String NO_EMPLOYEES = "There are no employees present in the company";
	public static final String UPDATE_NOT_ALLOWED = "Update not allowed as there are no employees present in the company";
	public static final String NO_EMPLOYEE_WITH_NAME = "\"There are no employees present in the company with the name:";
	public static final String ROOT_USER_PROVIDED = "User provided id root user";
	public static final String BLANK_EMPLOYEE_NAMES = "Request has blank/null Employee Name. Check supervisor names:";
	public static final String BLANK_SUPERVISORS_NAMES = "Request has blank/null Supervisor Name. Check employee names:";
	public static final String SELF_SUPERWISER = "Request has conflict in hierarchy i.e."
			+ "One or more employess are self superwiser.Check Names :";
	public static final String INCORRECT_TOKEN = ". Bearer Token is either not provided or value provided is in-correct or expired";

}
