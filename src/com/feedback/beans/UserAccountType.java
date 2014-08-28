package com.feedback.beans;

/**
 * Enumerated properties for user account types
 */
public enum UserAccountType {
	ADMIN, OWNER, USER;

	/**
	 * Convert a string representation of an account type to the corresponding
	 * account type enumeration
	 * 
	 * @param str
	 *            the string representation of the account type
	 * 
	 * @return the corresponding account type enumeration
	 * 
	 * @throws UserAccountTypeException
	 *             if no Account object is found for this string representation
	 */
	public static UserAccountType toAccountType(String str)
			throws UserAccountTypeException {
		switch (str) {
		case "admin":
			return ADMIN;
		case "owner":
			return OWNER;
		case "user":
			return USER;
		default:
			throw new UserAccountTypeException();
		}
	}
}
