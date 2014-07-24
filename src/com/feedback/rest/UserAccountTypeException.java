package com.feedback.rest;

public class UserAccountTypeException extends Exception {
	private static final long serialVersionUID = 1L;

	public UserAccountTypeException() {
		super("Wrong account type");
	}
}
