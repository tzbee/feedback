package com.feedback.beans;

/**
 * An error in account type identification
 */
public class UserAccountTypeException extends Exception {
	private static final long serialVersionUID = 1L;

	public UserAccountTypeException() {
		super("Wrong account type");
	}
}
