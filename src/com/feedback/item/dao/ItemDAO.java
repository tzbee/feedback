package com.feedback.item.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.feedback.item.Item;

public class ItemDAO {
	/**
	 * Save an item in the database
	 * 
	 * @param item
	 *            item to save
	 */
	public void saveItem(Item item) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		EntityTransaction tx = null;

		tx = em.getTransaction();
		tx.begin();
		em.persist(item);
		tx.commit();
	}

	/**
	 * Find all items registered in the database
	 * 
	 * @return a collection of all registered items
	 */
	@SuppressWarnings("unchecked")
	public List<Item> findAll() {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		Query query = em.createQuery("SELECT i from Item i");
		return query.getResultList();
	}
}
