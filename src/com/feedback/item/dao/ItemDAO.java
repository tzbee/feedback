package com.feedback.item.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.feedback.item.Item;

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
	public void deleteItem(int itemID) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		Item item = em.find(Item.class, itemID);

		em.getTransaction().begin();
		item.freeze();
		em.getTransaction().commit();
	}
}
