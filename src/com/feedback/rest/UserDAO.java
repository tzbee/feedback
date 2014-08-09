package com.feedback.rest;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.feedback.beans.User;
import com.feedback.dao.LocalEntityManagerFactory;

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
		em.persist(userKey);
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
}
