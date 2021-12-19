package com.assignment.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.assignment.entity.Employee;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

	Employee findByName(String name);

	List<Employee> findBySupervisorIdIsNull();

	List<Employee> findBySupervisorId(Long id);
	

}