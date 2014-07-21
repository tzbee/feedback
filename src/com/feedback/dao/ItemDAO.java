package com.feedback.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.feedback.beans.AbstractItem;
import com.feedback.beans.ConfigurationException;
import com.feedback.beans.FeedbackSession;
import com.feedback.beans.FeedbackUnit;
import com.feedback.beans.Item;
import com.feedback.beans.ScaleException;
import com.feedback.beans.State;
import com.feedback.rest.RatingDisabledException;

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
	 * Find all items by their state
	 * 
	 * @param itemState
	 *            state of the items to look for
	 * @return a collection of all active registered items
	 */
	@SuppressWarnings("unchecked")
	public List<Item> findItemsByState(State itemState) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		Query query = em.createQuery(
				"SELECT i FROM Item i WHERE i.state = :itemState")
				.setParameter("itemState", itemState);
		return query.getResultList();
	}

	/**
	 * Find an item by its ID
	 * 
	 * @param itemID
	 * 
	 * @return The item found
	 */
	public Item findItemByID(int itemID) throws NoResourceFoundException {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		Item item = em.find(Item.class, itemID);

		if (item == null) {
			throw new NoResourceFoundException("No item found for id: "
					+ itemID);
		}

		return item;
	}

	/**
	 * Edit an item found by its id with the newItem values
	 * 
	 * @param itemID
	 *            id of the item to edit
	 * @param newItem
	 *            the item object representing the attributes to change
	 */
	public void editItem(int itemID, Item newItem)
			throws NoResourceFoundException {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		Item item = em.find(Item.class, itemID);

		if (null == item) {
			throw new NoResourceFoundException();
		}

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
	public void freezeItem(int itemID) throws NoResourceFoundException {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		AbstractItem item = em.find(AbstractItem.class, itemID);

		if (null == item) {
			throw new NoResourceFoundException("No item found of id " + itemID);
		}

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
	 * 
	 * @throws NoResourceFoundException
	 *             if no item was found
	 * 
	 */
	public void saveFeedbackSession(int itemID, FeedbackSession feedbackSession)
			throws NoResourceFoundException {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		Item item = em.find(Item.class, itemID);

		if (item == null) {
			throw new NoResourceFoundException("No item found of id: " + itemID);
		}

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
	public List<FeedbackSession> findFeedbackSessionsByItem(int itemID)
			throws NoResourceFoundException {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		if (null == em.find(Item.class, itemID)) {
			throw new NoResourceFoundException();
		}

		return em
				.createQuery(
						"SELECT fbs FROM FeedbackSession fbs JOIN fbs.feedbackData.item i WHERE i.id=:itemID")
				.setParameter("itemID", itemID).getResultList();
	}

	/**
	 * Get the current feedback session of a particular item
	 * 
	 * @param itemID
	 *            id of the item
	 * @return the Feedback session object found
	 */

	public FeedbackSession getCurrentFeedbackSession(int itemID)
			throws NoResourceFoundException {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		Item item = null;

		item = em.find(Item.class, itemID);

		if (item == null) {
			throw new NoResourceFoundException("No item found of id " + itemID);
		}

		em.getTransaction().begin();
		FeedbackSession feedbackSession = item.getFeedbackData()
				.getCurrentFeedbackSession();

		if (null == feedbackSession) {
			throw new NoResourceFoundException(
					"No current session found of id " + itemID);
		}

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
	 * @throws ConfigurationException
	 *             if the feedbackUnit does not respect the internal
	 *             configuration
	 * @throws ScaleException
	 *             The scale is malformed
	 */
	public void rateItem(int itemID, FeedbackUnit feedbackUnit)
			throws ConfigurationException, ScaleException,
			NoResourceFoundException, RatingDisabledException {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		Item item = em.find(Item.class, itemID);

		if (null == item) {
			throw new NoResourceFoundException("No item found of id " + itemID);
		}
		if (!item.isRatingEnabled()) {
			throw new RatingDisabledException(itemID);
		}

		em.getTransaction().begin();
		item.addFeedbackUnit(feedbackUnit);
		em.getTransaction().commit();
	}

	/**
	 * Get a feedback session given it index relative to the item it belongs to
	 * 
	 * @param itemID
	 *            id of the item the feedback session belongs to
	 * @param localSessionIndex
	 *            local index of the session
	 * @return the session object found
	 * @throws NoResourceFoundException
	 */
	public FeedbackSession getFeedbackSessionByLocalIndex(int itemID,
			int localSessionIndex) throws NoResourceFoundException {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		try {
			return (FeedbackSession) em
					.createQuery(
							"SELECT fbs FROM FeedbackSession fbs JOIN fbs.feedbackData.item AS i WHERE i.id=:itemID AND fbs.localIndex=:localSessionIndex")
					.setParameter("itemID", itemID)
					.setParameter("localSessionIndex", localSessionIndex)
					.getSingleResult();
		} catch (NoResultException e) {
			throw new NoResourceFoundException("No session found of index "
					+ localSessionIndex + " for item " + itemID);
		}
	}
}
