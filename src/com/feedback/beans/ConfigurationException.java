package com.feedback.beans;

/**
 * An error in the feedback configuration properties
 *
 */
public class ConfigurationException extends Exception {
	private static final long serialVersionUID = 1L;

	public ConfigurationException(String message) {
		super(message);
	}
}
