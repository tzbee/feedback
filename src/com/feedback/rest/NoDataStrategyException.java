package com.feedback.rest;

/**
 * Exception occurring when no data processing strategy has been selected
 */
public class NoDataStrategyException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoDataStrategyException() {
		super("No data strategy available");
	}
}
