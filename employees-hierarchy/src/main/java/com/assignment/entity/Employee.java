package com.assignment.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "EMPLOYEE")
@Getter
@Setter
public class Employee implements Serializable {

	private static final long serialVersionUID = 8194494545160391633L;

	@Id
	// @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_id_gen")
	// @SequenceGenerator(name = "emp_id_gen", sequenceName = "EMPLOYEE_ID_SEQ",
	// allocationSize = 1)
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "NAME", nullable = false, unique = true)
	private String name;

	@Column(name = "SUPERVISOR_ID")
	private Long supervisorId;

	
	/*
	 * @CreatedDate
	 * 
	 * @Column(name = "created_at", nullable = false, updatable = false) private
	 * LocalDateTime createdAt;
	 * 
	 * @CreatedBy
	 * 
	 * @Column(name = "created_by", nullable = true) private Long createdBy;
	 * 
	 * @LastModifiedDate
	 * 
	 * @Column(name = "updated_at", nullable = false) private LocalDateTime
	 * updatedAt;
	 * 
	 * @LastModifiedBy
	 * 
	 * @Column(name = "updated_by", nullable = true) private Long updatedBy;
	 */
}
