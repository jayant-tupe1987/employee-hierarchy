package com.assignment.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements Serializable {

	private static final long serialVersionUID = 3177370202899363311L;

	@Id
	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(name = "encrypted_password", nullable = false)
	private String encryptedPassword;
}
