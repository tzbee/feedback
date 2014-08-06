package com.feedback.rest;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.feedback.beans.User;

/**
 * Restful service handling all high level user operations
 */
@Path("users")
public class UserResource {

	/**
	 * Data access object performing database operations
	 */
	private UserDAO userDAO = new UserDAO();

	/**
	 * Create a new user key
	 * 
	 * @param accountTypeKey
	 *            the account type linked to the key
	 * 
	 * @throws BadRequestException
	 *             if the account type is wrong
	 * 
	 * @return the user key created
	 */
	@POST
	@Path("key")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public int createUserKey(@FormParam("accountType") String accountTypeKey,
			@FormParam("mail") String mail) throws BadRequestException {
		UserAccountType accountType;
		UserKeyBuilder userKeyBuilder;

		try {

			// Create the account type from the key
			accountType = UserAccountType.toAccountType(accountTypeKey);

			// Create the user key
			userKeyBuilder = new UserKeyBuilder();
			userKeyBuilder.setAccountType(accountType);
			userKeyBuilder.setUserName(mail);
			userKeyBuilder.build();

			// Save the user key in the database
			this.userDAO.createUserKey(userKeyBuilder);

		} catch (UserAccountTypeException e) {
			throw new BadRequestException();
		}

		return userKeyBuilder.getKeyValue();
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

	/**
	 * Authenticate the user
	 * 
	 * @param userName
	 *            The user name identifying the user
	 * 
	 * @param password
	 *            The password used for authentication
	 */
	@POST
	@Path("authentication")
	public void authenticate(@FormParam("userName") String userName,
			@FormParam("password") String password)
			throws NotAuthorizedException {
		// TODO Authentication
	}
}