package com.feedback.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.feedback.beans.User;
import com.feedback.beans.UserAccountType;
import com.feedback.beans.UserKeyBuilder;

public class UserDAO {

	/**
	 * Create a user key in the data source
	 * 
	 * @param userKey
	 *            user key object to save
	 */
	public void createUserKey(UserKeyBuilder userKey) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();
		em.merge(userKey);
		tx.commit();
	}

	/**
	 * Find a user key object by its user key
	 * 
	 * @param userKey
	 *            the id identifying the user key
	 * 
	 */
	public UserKeyBuilder findUserKeyByID(int userKey) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		return em.find(UserKeyBuilder.class, userKey);
	}

	/**
	 * Create new user account in the data source
	 * 
	 * @param user
	 *            the user object to save
	 */
	public void createUser(User user) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();
		em.persist(user);
		tx.commit();
	}

	/**
	 * Delete a user key
	 * 
	 * @param userKey
	 *            the user key to delete
	 */
	public void deleteUserKey(int userKey) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		// Find the key object
		UserKeyBuilder userKeyBuilder = em.find(UserKeyBuilder.class, userKey);

		// Remove it from the database
		tx.begin();
		em.remove(userKeyBuilder);
		tx.commit();
	}

	/**
	 * Find a user's account type
	 * 
	 * @param userID
	 *            id of the user
	 */

	public UserAccountType findUserAccountType(String userID) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		return (UserAccountType) em
				.createQuery(
						"SELECT u.accountType FROM User u WHERE u.userName= :userID")
				.setParameter("userID", userID).getSingleResult();
	}

	/**
	 * Find a user by its id
	 * 
	 * @param userID
	 *            id of the user to find
	 * @throws NoUserException
	 */
	public User findUserByID(String userID) throws NoUserException {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		if (null == userID) {
			throw new NoUserException();
		}

		User user = em.find(User.class, userID);

		if (null == user) {
			throw new NoUserException();
		}

		return user;
	}
}
