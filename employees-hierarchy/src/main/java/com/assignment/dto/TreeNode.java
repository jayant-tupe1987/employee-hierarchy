package com.assignment.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TreeNode<T> {

	private T name;

	private Long id;

	private TreeNode<T> parent = null;

	private List<TreeNode<T>> children;

}
