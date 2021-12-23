package com.assignment.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.assignment.constant.ApiConstants;
import com.assignment.dto.TreeNode;
import com.assignment.entity.Employee;
import com.assignment.exception.GenericException;
import com.assignment.repository.EmployeeRepository;
import com.assignment.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Value("${superier.level}")
	private String superierLevel;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public String updateHierarchy(Map<String, String> relationships) {
		if (employeeRepository.count() == 0) {
			throw new GenericException(ApiConstants.UPDATE_NOT_ALLOWED);
		}
		return createOrUpdateHierarchy(relationships);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public String createHierarchy(Map<String, String> relationships) {
		employeeRepository.deleteAll();
		return createOrUpdateHierarchy(relationships);
	}

	private String createOrUpdateHierarchy(Map<String, String> relationships) {
		mapValidator(relationships);
		List<String> supervisorsList = new ArrayList<>(relationships.values());
		saveAllEmployees(relationships, supervisorsList);
		return buildHierarchy();
	}

	private void saveAllEmployees(Map<String, String> relationships, List<String> supervisorsList) {
		supervisorsList.forEach(t -> {
			if (Objects.isNull(employeeRepository.findByName(t))) {
				Employee e = new Employee();
				e.setName(t);
				employeeRepository.save(e);
			}
		});

		relationships.forEach((k, v) -> {
			Employee supervisor = employeeRepository.findByName(v);
			Employee employee = employeeRepository.findByName(k);
			if (Objects.isNull(employee)) {
				Employee e = new Employee();
				e.setName(k);
				e.setSupervisorId(supervisor.getId());
				employeeRepository.save(e);
			} else {
				employee.setSupervisorId(supervisor.getId());
				employeeRepository.save(employee);
			}
		});
	}

	private void mapValidator(Map<String, String> relationships) {

		List<String> blankEmploees = relationships.entrySet().parallelStream()
				.filter(e -> !StringUtils.hasText(e.getKey())).map(Map.Entry::getValue).collect(Collectors.toList());

		List<String> blankSupervisors = relationships.entrySet().parallelStream()
				.filter(e -> !StringUtils.hasText(e.getValue())).map(Map.Entry::getKey).collect(Collectors.toList());

		List<String> superwiseOfSelf = relationships.entrySet().parallelStream()
				.filter(e -> StringUtils.hasText(e.getValue()) && StringUtils.hasText(e.getKey())
						&& e.getKey().equalsIgnoreCase(e.getValue()))
				.map(Map.Entry::getKey).collect(Collectors.toList());

		if (!CollectionUtils.isEmpty(blankSupervisors)) {
			throw new GenericException(
					ApiConstants.BLANK_SUPERVISORS_NAMES + blankSupervisors.stream().collect(Collectors.joining(",")));
		}
		if (!CollectionUtils.isEmpty(blankEmploees)) {
			throw new GenericException(
					ApiConstants.BLANK_EMPLOYEE_NAMES + blankEmploees.stream().collect(Collectors.joining(",")));
		}
		if (!CollectionUtils.isEmpty(superwiseOfSelf)) {
			throw new GenericException(
					ApiConstants.SELF_SUPERWISER + superwiseOfSelf.stream().collect(Collectors.joining(",")));
		}
	}

	@Override
	public String buildHierarchy() {
		String buildHierarchy = "";
		if (employeeRepository.count() == 0) {
			throw new GenericException(ApiConstants.NO_EMPLOYEES);
		} else {
			List<Employee> rootSupervisorCountList = employeeRepository.findBySupervisorIdIsNull();
			if (CollectionUtils.isEmpty(rootSupervisorCountList)) {
				throw new GenericException(ApiConstants.LOOP_HIERARCHY);
			}
			if (rootSupervisorCountList.size() > 1) {
				throw new GenericException(ApiConstants.MULTI_ROOT);
			}
			Employee rootEmployee = rootSupervisorCountList.get(0);
			TreeNode<String> rootNodeEmployee = new TreeNode<>();
			rootNodeEmployee.setName(rootEmployee.getName());
			rootNodeEmployee.setParent(null);
			rootNodeEmployee.setId(rootEmployee.getId());
			buildTreeNode(rootNodeEmployee);
			buildHierarchy = buildString(rootNodeEmployee, buildHierarchy);
			if (StringUtils.hasText(buildHierarchy)) {
				buildHierarchy = "{" + buildHierarchy + "}";
			}
		}
		log.info("Employee Hierarchy is: " + buildHierarchy);
		return buildHierarchy;
	}

	private String buildString(TreeNode<String> rootNodeEmployee, String buildHierarchy) {
		buildHierarchy = buildHierarchy.concat("\"" + rootNodeEmployee.getName() + "\"" + ": {");
		if (!CollectionUtils.isEmpty(rootNodeEmployee.getChildren())) {
			int i = 1;
			for (TreeNode<String> t : rootNodeEmployee.getChildren()) {
				buildHierarchy = buildString(t, buildHierarchy);
				if (rootNodeEmployee.getChildren().size() > 1 && rootNodeEmployee.getChildren().size() - i != 0) {
					buildHierarchy = buildHierarchy.concat(" , ");
				}
				i++;
			}
		}
		buildHierarchy = buildHierarchy.concat("} ");
		return buildHierarchy;
	}

	private void buildTreeNode(TreeNode<String> rootNodeEmployee) {

		List<Employee> childList = employeeRepository.findBySupervisorId(rootNodeEmployee.getId());
		if (!CollectionUtils.isEmpty(childList)) {
			List<TreeNode<String>> children = new ArrayList<>();
			for (Employee e : childList) {
				TreeNode<String> empNode = new TreeNode<>();
				empNode.setName(e.getName());
				empNode.setParent(rootNodeEmployee);
				empNode.setId(e.getId());
				children.add(empNode);
				buildTreeNode(empNode);
			}
			rootNodeEmployee.setChildren(children);
		}
	}

	@Override
	public String getSuperiors(String empName) {
		String buildHierarchy = "";
		if (employeeRepository.count() == 0) {
			throw new GenericException(ApiConstants.NO_EMPLOYEES);
		} else {
			Employee e = employeeRepository.findByName(empName);
			if (Objects.isNull(e)) {
				throw new GenericException(ApiConstants.NO_EMPLOYEE_WITH_NAME + empName);
			} else if (Objects.isNull(e.getSupervisorId())) {
				throw new GenericException(ApiConstants.ROOT_USER_PROVIDED);
			} else {
				TreeNode<String> empNode = new TreeNode<>();
				empNode.setName(e.getName());
				empNode.setId(e.getId());
				empNode.setChildren(null);
				Optional<Employee> superVisor = employeeRepository.findById(e.getSupervisorId());
				if (superVisor.isPresent()) {
					Employee superVisorEmp = superVisor.get();
					TreeNode<String> superVisorNode = new TreeNode<>();
					superVisorEmp.setName(superVisorEmp.getName());
					superVisorNode.setId(superVisorEmp.getId());
					List<TreeNode<String>> children = new ArrayList<>();
					children.add(empNode);
					superVisorNode.setChildren(children);
					empNode.setParent(superVisorNode);
					Integer superierLevelCounter = Integer.parseInt(superierLevel);
					TreeNode<String> supervisorNode = setParenNode(empNode, superierLevelCounter);
					buildHierarchy = buildString(supervisorNode, buildHierarchy);
					if (StringUtils.hasText(buildHierarchy)) {
						buildHierarchy = "{" + buildHierarchy + "}";
					}
				}
			}
		}
		log.info("Supervisor hierarchy for " + empName + " is " + buildHierarchy);
		return buildHierarchy;
	}

	public TreeNode<String> setParenNode(TreeNode<String> node, Integer superierLevelCounter) {
		TreeNode<String> parentNode = node.getParent();
		if (Objects.isNull(parentNode.getId())) {
			return node;
		}
		Optional<Employee> superVisor = employeeRepository.findById(parentNode.getId());
		if (superVisor.isPresent() && superierLevelCounter > 0) {
			Employee superVisorEmp = superVisor.get();
			parentNode.setName(superVisorEmp.getName());
			List<TreeNode<String>> children = new ArrayList<>();
			children.add(node);
			parentNode.setChildren(children);
			node.setParent(parentNode);
			TreeNode<String> parentParentNode = new TreeNode<String>();
			parentParentNode.setId(superVisorEmp.getSupervisorId());
			parentNode.setParent(parentParentNode);
			node = setParenNode(parentNode, superierLevelCounter - 1);
		}
		return node;
	}

}
