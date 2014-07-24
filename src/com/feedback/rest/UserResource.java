package com.feedback.rest;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.feedback.beans.User;

@Path("accounts")
public class UserResource {

	/**
	 * Data access object performing data base operations
	 */
	private UserDAO userDAO = new UserDAO();

	/**
	 * Create a new user key
	 * 
	 * @param accountTypeStr
	 *            the account type linked to the key
	 * 
	 * @throws BadRequestException
	 *             if the account type is wrong
	 */
	@POST
	@Path("key/{accountType}")
	public void createUserKey(@PathParam("accountType") String accountTypeStr)
			throws BadRequestException {
		UserAccountType accountType;

		try {
			accountType = UserAccountType.toAccountType(accountTypeStr);
			UserKeyBuilder userKeyBuilder = new UserKeyBuilder();
			userKeyBuilder.setAccountType(accountType);
			userKeyBuilder.build();

			this.userDAO.createUserKey(userKeyBuilder);
		} catch (UserAccountTypeException e) {
			throw new BadRequestException();
		}
	}

	/**
	 * Create a user account based on a user key
	 * 
	 * @param userKey
	 *            the key to create the user from
	 */
	@POST
	@Path("{userKey}")
	public void createUserAccount(@PathParam("userKey") int userKey) {
		User user = new User();
		UserKeyBuilder userKeyBuilder = this.userDAO.findUserKeyByID(userKey);

		user.setAccountType(userKeyBuilder.getAccountType());
		user.setUserName(userKeyBuilder.getUserName());
		user.setPassword(userKeyBuilder.getPassword());

		this.userDAO.createUser(user);
	}
}