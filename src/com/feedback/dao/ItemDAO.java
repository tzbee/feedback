package com.feedback.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.feedback.beans.AbstractItem;
import com.feedback.beans.FeedbackSession;
import com.feedback.beans.FeedbackUnit;
import com.feedback.beans.Item;

/**
 * Handles all item related database operations
 */
public class ItemDAO {
	/**
	 * Save an item in the system
	 * 
	 * @param item
	 *            item to save
	 */
	public void saveItem(Item item) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();
		em.persist(item);
		tx.commit();
	}

	/**
	 * Find all items registered in the system
	 * 
	 * @return a collection of all registered items
	 */
	@SuppressWarnings("unchecked")
	public List<Item> findAll() {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		Query query = em.createQuery("SELECT i from Item i");
		return query.getResultList();
	}

	/**
	 * Find an item by its ID
	 * 
	 * @param itemID
	 * 
	 * @return The item found
	 */
	public Item findItemByID(int itemID) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		return em.find(Item.class, itemID);
	}

	/**
	 * Edit an item found by its id with the newItem values
	 * 
	 * @param itemID
	 *            id of the item to edit
	 * @param newItem
	 *            the item object representing the attributes to change
	 */
	public void editItem(int itemID, Item newItem) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		Item item = em.find(Item.class, itemID);

		em.getTransaction().begin();

		item.setName(newItem.getName());
		item.setDescription(newItem.getDescription());

		em.getTransaction().commit();
	}

	/**
	 * Freeze any item from the system
	 * 
	 * @param itemID
	 *            id of the item to delete
	 */
	public void freezeItem(int itemID) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		AbstractItem item = em.find(AbstractItem.class, itemID);

		em.getTransaction().begin();
		item.freeze();
		em.getTransaction().commit();
	}

	/**
	 * Create a new feedback session for an item
	 * 
	 * @param itemID
	 *            id of the item
	 * 
	 * @param feedbackSession
	 *            The feedback session to be created
	 */
	public void saveFeedbackSession(int itemID, FeedbackSession feedbackSession) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		Item item = em.find(Item.class, itemID);

		em.getTransaction().begin();
		item.createFeedbackSession(feedbackSession);
		em.getTransaction().commit();
	}

	/**
	 * Find all FeedbackSession from a specific item
	 * 
	 * @param itemID
	 *            id of the item
	 * @return a List of all feedback sessions from this item
	 */
	@SuppressWarnings("unchecked")
	public List<FeedbackSession> findFeedbackSessionsByItem(int itemID) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		Query query = em
				.createQuery("SELECT fbs FROM FeedbackSession fbs JOIN fbs.feedbackData.item i WHERE i.id=:itemID");
		query.setParameter("itemID", itemID);

		return query.getResultList();
	}

	/**
	 * Get the current feedback session of a particular item
	 * 
	 * @param itemID
	 *            id of the item
	 * @return the Feedback session object found
	 */
	public FeedbackSession getCurrentFeedbackSession(int itemID) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		Item item = em.find(Item.class, itemID);

		em.getTransaction().begin();
		FeedbackSession feedbackSession = item.getFeedbackData()
				.getCurrentFeedbackSession();
		em.getTransaction().commit();
		return feedbackSession;
	}

	/**
	 * Create a new feedback unit and add it to the given feedback session
	 * 
	 * @param feedbackSessionID
	 *            id of the feedback session to use
	 * @param feedbackUnit
	 *            feedback unit object to create
	 */
	public void rateItem(int itemID, FeedbackUnit feedbackUnit) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		Item item = em.find(Item.class, itemID);

		em.getTransaction().begin();
		item.addFeedbackUnit(feedbackUnit);
		em.getTransaction().commit();
	}
}
