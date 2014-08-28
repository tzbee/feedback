package com.feedback.dao;

/**
 * Error occurring when no resource has been found
 */
public class NoResourceFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoResourceFoundException() {
		super("No resource found");
	}

	public NoResourceFoundException(String message) {
		super(message);
	}
}
