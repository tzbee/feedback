package com.feedback.beans;

/**
 * An error in the scale configuration
 */
public class ScaleException extends Exception {
	private static final long serialVersionUID = 1L;

	public ScaleException() {
		super("Wrong scale!");
	}
}
