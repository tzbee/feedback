package com.feedback.item.dao;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import com.feedback.item.Item;

public class ItemDAO {
	@PersistenceContext(name = "Feedback")
	private EntityManager em;

	@Resource
	private UserTransaction utx;

	public void saveItem(Item item) {
		System.out.println("Saving item " + item + "..");
		try {
			this.utx.begin();
			this.em.persist(item);
			this.utx.commit();
		} catch (Exception e) {
			try {
				this.utx.rollback();
			} catch (Exception ignore) {
			}
		}
	}
}
