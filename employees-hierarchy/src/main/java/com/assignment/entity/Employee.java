package com.assignment.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "employee")
@Getter
@Setter
public class Employee implements Serializable {

	private static final long serialVersionUID = 8194494545160391633L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@SequenceGenerator(name = "emp_id_gen", sequenceName = "EMPLOYEE_ID_SEQ", allocationSize = 1)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "name", nullable = false, unique = true)
	private String name;
	
	@Column(name = "supervisor_id")
	private Long supervisorId;
}
