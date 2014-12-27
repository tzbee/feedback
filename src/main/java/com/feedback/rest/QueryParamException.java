package com.feedback.rest;

/**
 * Exception occurring when query parameters are wrongly formatted
 */
public class QueryParamException extends Exception {
	private static final long serialVersionUID = 1L;

	public QueryParamException(String message) {
		super(message);
	}

	public QueryParamException() {
		this("Wrong query parameter format");
	}
}
