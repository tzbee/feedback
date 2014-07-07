package com.feedback.item.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.feedback.item.Item;

public class ItemDAO {
	public void saveItem(Item item) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		EntityTransaction tx = null;

		try {
			tx = em.getTransaction();
			tx.begin();
			em.persist(item);
			tx.commit();
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception ignore) {
			}
		}
	}
}
