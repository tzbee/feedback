package com.feedback.rest;

/**
 * Error occurring when an item name is empty
 *
 */
public class EmptyItemNameException extends Exception {
	private static final long serialVersionUID = 1L;

	public EmptyItemNameException() {
		super("Item name should not be empty");
	}
}
