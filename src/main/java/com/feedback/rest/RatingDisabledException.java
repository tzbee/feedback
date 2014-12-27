package com.feedback.rest;

/**
 * Exception occurring when rating has been disabled an no feedback should be
 * allowed
 */
public class RatingDisabledException extends Exception {
	private static final long serialVersionUID = 1L;

	public RatingDisabledException(int itemID) {
		super("Rating is disabled for item of id " + itemID);
	}
}
