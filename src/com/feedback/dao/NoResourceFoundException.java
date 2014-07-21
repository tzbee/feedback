package com.feedback.dao;

public class NoResourceFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoResourceFoundException() {
		super("No resource found");
	}

	public NoResourceFoundException(String message) {
		super(message);
	}
}
