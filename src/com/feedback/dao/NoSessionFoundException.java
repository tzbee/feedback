package com.feedback.dao;

public class NoSessionFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoSessionFoundException(String message) {
		super(message);
	}
}
