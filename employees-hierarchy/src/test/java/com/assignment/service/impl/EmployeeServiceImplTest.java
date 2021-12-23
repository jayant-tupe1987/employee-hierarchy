package com.assignment.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.assignment.constant.ApiConstants;
import com.assignment.entity.Employee;
import com.assignment.exception.GenericException;
import com.assignment.repository.EmployeeRepository;
import com.assignment.service.EmployeeService;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceImplTest {

	@InjectMocks
	private EmployeeService employeeService = new EmployeeServiceImpl();

	@Mock
	private EmployeeRepository employeeRepository;

	private String empName;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		empName = "Jayant";
	}

	@Test
	public void testCreateHierarchyScenValid() {
		HashMap<String, String> relationships = new LinkedHashMap<>();
		relationships.put("Sophie", "Jonas");
		relationships.put("Nick", "Sophie");
		relationships.put("Barbara", "Nick");
		relationships.put("Pete", "Nick");
		doReturn(new Employee()).when(employeeRepository).findByName(any());
		doReturn(new Long(1)).when(employeeRepository).count();
		List<Employee> rootSupervisorCountList = new ArrayList<Employee>();
		Employee e = new Employee();
		e.setId(1L);
		e.setName("Jonus");
		e.setSupervisorId(null);
		rootSupervisorCountList.add(e);
		doReturn(rootSupervisorCountList).when(employeeRepository).findBySupervisorIdIsNull();
		Employee e1 = new Employee();
		e1.setId(2L);
		e1.setName("Sophie");
		e1.setSupervisorId(1L);
		List<Employee> childList1 = new ArrayList<Employee>();
		childList1.add(e1);
		doReturn(childList1).when(employeeRepository).findBySupervisorId(1L);
		Employee e2 = new Employee();
		e2.setId(3L);
		e2.setName("Nick");
		e2.setSupervisorId(2L);
		List<Employee> childList2 = new ArrayList<Employee>();
		childList2.add(e2);
		doReturn(childList2).when(employeeRepository).findBySupervisorId(2L);
		Employee e3 = new Employee();
		e3.setId(4L);
		e3.setName("Barbara");
		e3.setSupervisorId(3L);
		List<Employee> childList3 = new ArrayList<Employee>();
		childList3.add(e3);
		Employee e4 = new Employee();
		e4.setId(5L);
		e4.setName("Pete");
		e4.setSupervisorId(3L);
		childList3.add(e4);
		doReturn(childList3).when(employeeRepository).findBySupervisorId(3L);
		String actualBuildHierarchy = employeeService.createHierarchy(relationships);
		String expectedBuildHierarchy = "{\"Jonus\": {\"Sophie\": {\"Nick\": {\"Barbara\": {}  , \"Pete\": {} } } } }";
		assertEquals(actualBuildHierarchy, expectedBuildHierarchy);
	}

	@Test
	public void testUpdateHierarchyScenValid() {
		HashMap<String, String> relationships = new LinkedHashMap<>();
		relationships.put("Sophie", "Jonas");
		relationships.put("Nick", "Sophie");
		relationships.put("Barbara", "Nick");
		relationships.put("Pete", "Nick");
		doReturn(new Employee()).when(employeeRepository).findByName(any());
		doReturn(new Long(1)).when(employeeRepository).count();
		List<Employee> rootSupervisorCountList = new ArrayList<Employee>();
		Employee e = new Employee();
		e.setId(1L);
		e.setName("Jonus");
		e.setSupervisorId(null);
		rootSupervisorCountList.add(e);
		doReturn(rootSupervisorCountList).when(employeeRepository).findBySupervisorIdIsNull();
		Employee e1 = new Employee();
		e1.setId(2L);
		e1.setName("Sophie");
		e1.setSupervisorId(1L);
		List<Employee> childList1 = new ArrayList<Employee>();
		childList1.add(e1);
		doReturn(childList1).when(employeeRepository).findBySupervisorId(1L);
		Employee e2 = new Employee();
		e2.setId(3L);
		e2.setName("Nick");
		e2.setSupervisorId(2L);
		List<Employee> childList2 = new ArrayList<Employee>();
		childList2.add(e2);
		doReturn(childList2).when(employeeRepository).findBySupervisorId(2L);
		Employee e3 = new Employee();
		e3.setId(4L);
		e3.setName("Barbara");
		e3.setSupervisorId(3L);
		List<Employee> childList3 = new ArrayList<Employee>();
		childList3.add(e3);
		Employee e4 = new Employee();
		e4.setId(5L);
		e4.setName("Pete");
		e4.setSupervisorId(3L);
		childList3.add(e4);
		doReturn(childList3).when(employeeRepository).findBySupervisorId(3L);
		String actualBuildHierarchy = employeeService.updateHierarchy(relationships);
		String expectedBuildHierarchy = "{\"Jonus\": {\"Sophie\": {\"Nick\": {\"Barbara\": {}  , \"Pete\": {} } } } }";
		assertEquals(actualBuildHierarchy, expectedBuildHierarchy);
	}
	@Test(expected = GenericException.class)
	public void testCreateHierarchyScenInValidBlankSuperVisor() {
		try {
			HashMap<String, String> relationships = new LinkedHashMap<>();
			relationships.put("Sophie", "Jonas");
			relationships.put("Nick", "");
			relationships.put("Barbara", "Nick");
			relationships.put("Pete", "Nick");
			employeeService.createHierarchy(relationships);
		} catch (final GenericException e) {
			final String msg = ApiConstants.BLANK_SUPERVISORS_NAMES + "Nick";
			assertEquals(msg, e.getMessage());
			throw e;
		}
	}

	@Test(expected = GenericException.class)
	public void testCreateHierarchyScenInValidBlankEmployees() {
		try {
			HashMap<String, String> relationships = new LinkedHashMap<>();
			relationships.put("Sophie", "Jonas");
			relationships.put("", "Nick");
			relationships.put("Barbara", "Jay");
			relationships.put("Pete", "Raj");
			employeeService.createHierarchy(relationships);
		} catch (final GenericException e) {
			final String msg = ApiConstants.BLANK_EMPLOYEE_NAMES + "Nick";
			assertEquals(msg, e.getMessage());
			throw e;
		}
	}
	@Test(expected = GenericException.class)
	public void testCreateHierarchyScenInValidSelfSupervisor() {
		try {
			HashMap<String, String> relationships = new LinkedHashMap<>();
			relationships.put("Sophie", "Sophie");
			relationships.put("Jay", "Nick");
			relationships.put("Raj", "Jay");
			relationships.put("Pete", "Raj");
			employeeService.createHierarchy(relationships);
		} catch (final GenericException e) {
			final String msg = ApiConstants.SELF_SUPERWISER + "Sophie";
			assertEquals(msg, e.getMessage());
			throw e;
		}
	}
	@Test(expected = GenericException.class)
	public void testCreateHierarchyScenInValidMultiRoot() {
		HashMap<String, String> relationships = new LinkedHashMap<>();
		relationships.put("Sophie", "Jonas");
		relationships.put("Nick", "Sophie");
		relationships.put("Barbara", "Nick");
		relationships.put("Pete", "Jack");
		doReturn(new Employee()).when(employeeRepository).findByName(any());
		doReturn(new Long(2)).when(employeeRepository).count();
		List<Employee> rootSupervisorCountList = new ArrayList<Employee>();
		Employee e = new Employee();
		e.setId(1L);
		e.setName("Jonus");
		e.setSupervisorId(null);
		rootSupervisorCountList.add(e);
		Employee e1 = new Employee();
		e1.setId(2L);
		e1.setName("Jack");
		e1.setSupervisorId(1L);
		rootSupervisorCountList.add(e1);
		doReturn(rootSupervisorCountList).when(employeeRepository).findBySupervisorIdIsNull();
		employeeService.createHierarchy(relationships);
	}

	@Test(expected = GenericException.class)
	public void testCreateHierarchyScenInValidLoop() {
		HashMap<String, String> relationships = new LinkedHashMap<>();
		relationships.put("Sophie", "Jonas");
		relationships.put("Nick", "Sophie");
		relationships.put("Jack", "Nick");
		relationships.put("Jonas", "Jack");
		doReturn(new Employee()).when(employeeRepository).findByName(any());
		doReturn(new Long(2)).when(employeeRepository).count();
		List<Employee> rootSupervisorCountList = new ArrayList<Employee>();
		doReturn(rootSupervisorCountList).when(employeeRepository).findBySupervisorIdIsNull();
		employeeService.createHierarchy(relationships);
	}

	@Test(expected = GenericException.class)
	public void testCreateHierarchyScenInValidNoEmployee() {
		HashMap<String, String> relationships = new LinkedHashMap<>();
		relationships.put("Sophie", "Jonas");
		relationships.put("Nick", "Sophie");
		relationships.put("Jack", "Nick");
		relationships.put("Jonas", "Jack");
		doReturn(new Employee()).when(employeeRepository).findByName(any());
		doReturn(new Long(0)).when(employeeRepository).count();
		List<Employee> rootSupervisorCountList = new ArrayList<Employee>();
		doReturn(rootSupervisorCountList).when(employeeRepository).findBySupervisorIdIsNull();
		employeeService.createHierarchy(relationships);
	}

	@Test(expected = GenericException.class)
	public void testGetSuperiorsScenInValidNoEmployees() {
		try {
			doReturn(new Long(0)).when(employeeRepository).count();
			employeeService.getSuperiors("Jayant");
		} catch (final GenericException e) {
			final String msg = ApiConstants.NO_EMPLOYEES;
			assertEquals(msg, e.getMessage());
			throw e;
		}
	}

	@Test(expected = GenericException.class)
	public void testGetSuperiorsScenInValidNoEmployeeWithName() {
		try {
			doReturn(new Long(1)).when(employeeRepository).count();
			doReturn(null).when(employeeRepository).findByName(any());
			employeeService.getSuperiors(empName);
		} catch (final GenericException e) {
			final String msg = ApiConstants.NO_EMPLOYEE_WITH_NAME + empName;
			assertEquals(msg, e.getMessage());
			throw e;
		}
	}

	@Test(expected = GenericException.class)
	public void testGetSuperiorsScenInValidRootUser() {

		try {
			Employee e = new Employee();
			e.setId(1L);
			e.setName(empName);
			doReturn(new Long(1)).when(employeeRepository).count();
			doReturn(e).when(employeeRepository).findByName(any());
			employeeService.getSuperiors(empName);
		} catch (final GenericException e) {
			final String msg = ApiConstants.ROOT_USER_PROVIDED;
			assertEquals(msg, e.getMessage());
			throw e;
		}
	}

	@Test
	public void testGetSuperiorsScenValidUser() {
		Employee e3 = new Employee();
		e3.setId(3L);
		e3.setName(empName);
		e3.setSupervisorId(1L);
		doReturn(new Long(6)).when(employeeRepository).count();
		doReturn(e3).when(employeeRepository).findByName(any());
		Employee e1 = new Employee();
		e1.setId(1L);
		e1.setName("Jonus");
		e1.setSupervisorId(2L);
		doReturn(Optional.of(e1)).when(employeeRepository).findById(1L);
		Employee e2 = new Employee();
		e2.setId(2L);
		e2.setName("Jack");
		e2.setSupervisorId(null);
		doReturn(Optional.of(e2)).when(employeeRepository).findById(2L);
		String actualValue = employeeService.getSuperiors(empName);
		assertEquals("{\"Jack\": {\"Jonus\": {\"Jayant\": {} } } }", actualValue);
	}

}
