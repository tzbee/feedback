package com.feedback.rest;

public class NoDataStrategyException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoDataStrategyException() {
		super("No data strategy available");
	}
}
