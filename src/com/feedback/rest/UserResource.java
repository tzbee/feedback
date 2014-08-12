package com.feedback.rest;

import java.util.Collection;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.feedback.authentication.Permission;
import com.feedback.authentication.PermissionMap;
import com.feedback.beans.User;
import com.feedback.beans.UserAccountType;
import com.feedback.beans.UserAccountTypeException;
import com.feedback.beans.UserKeyBuilder;
import com.feedback.dao.UserDAO;
import com.google.common.collect.Multimap;

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
	 * USER KEY RESOURCE
	 */

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
	 * Delete a user key
	 * 
	 * @param userKey
	 *            the user key to delete
	 */

	@DELETE
	@Path("/key/{userKey}")
	public void deleteUserKey(@PathParam("userKey") int userKey) {
		// Delete the user key from the database
		this.userDAO.deleteUserKey(userKey);
	}

	/**
	 * USER ACCOUNT RESOURCE
	 */

	/**
	 * Create a user account based on a user key
	 * 
	 * @param userKey
	 *            the key to create the user from
	 * 
	 * @throws BadRequestException
	 *             if the key does not exist or is badly formatted
	 */
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void createUserAccount(@FormParam("userKey") int userKey)
			throws BadRequestException {
		User user = new User();
		UserKeyBuilder userKeyBuilder = this.userDAO.findUserKeyByID(userKey);

		if (null == userKeyBuilder) {
			throw new BadRequestException();
		}

		user.setAccountType(userKeyBuilder.getAccountType());
		user.setUserName(userKeyBuilder.getUserName());
		user.setPassword(userKeyBuilder.getPassword());

		this.userDAO.createUser(user);
	}

	/**
	 * AUTHENTICATION RESOURCE
	 */

	/**
	 * Check if user account has a certain permission
	 * 
	 * @param userName
	 *            the id of the user
	 * @param permission
	 *            the permission to check
	 * @throws ForbiddenException
	 *             if the user does not possess the given permission
	 */
	private void checkUserPermission(String userName, Permission permission)
			throws ForbiddenException {
		Multimap<UserAccountType, Permission> permissionMap = PermissionMap.PERMISSION_MAP;

		// Find the user's account type
		UserAccountType userAccountType = this.userDAO
				.findUserAccountType(userName);

		if (!permissionMap.get(userAccountType).contains(permission)) {
			throw new ForbiddenException();
		}

	}

	/**
	 * Get all permissions for one user
	 * 
	 * @param userName
	 *            name identifying the user
	 */
	@GET
	@Path("{userName}/permissions")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Permission> getUserPermissions(
			@PathParam("userName") String userName) {

		// Find the user's account type
		UserAccountType userAccountType = this.userDAO
				.findUserAccountType(userName);

		return PermissionMap.PERMISSION_MAP.get(userAccountType);
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
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void authenticate(@FormParam("userName") String userName)
			throws ForbiddenException {
		// TODO Authentication
		checkUserPermission(userName, Permission.RATE);
	}
}