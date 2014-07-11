package com.feedback.dao.item;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.feedback.beans.item.FeedbackSession;
import com.feedback.beans.item.Item;
import com.feedback.dao.LocalEntityManagerFactory;

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
		item.setRatingEnabled(newItem.isRatingEnabled());

		em.getTransaction().commit();
	}

	/**
	 * Freeze an item from the system
	 * 
	 * @param itemID
	 *            id of the item to delete
	 */
	public void freezeItem(int itemID) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		Item item = em.find(Item.class, itemID);

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
	public void createFeedbackSession(int itemID,
			FeedbackSession feedbackSession) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		Item item = em.find(Item.class, itemID);

		em.getTransaction().begin();
		item.addFeedbackSession(feedbackSession);
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
				.createQuery("SELECT fbs FROM FeedbackSession fbs JOIN fbs.item i WHERE i.id=:itemID");
		query.setParameter("itemID", itemID);

		return query.getResultList();
	}
}
