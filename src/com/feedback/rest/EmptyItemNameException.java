package com.feedback.rest;

public class EmptyItemNameException extends Exception {
	private static final long serialVersionUID = 1L;

	public EmptyItemNameException() {
		super("Item name should not be empty");
	}
}
