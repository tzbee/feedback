package com.feedback.rest;

public class NoDataStrategy extends Exception {
	private static final long serialVersionUID = 1L;

	public NoDataStrategy() {
		super("No data strategy available");
	}
}
