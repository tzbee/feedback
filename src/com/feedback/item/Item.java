package com.feedback.item;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * POJO class describing an Item to evaluate
 */
@Entity
@Table(name = "RATABLE_ITEM")
public class Item extends AbstractItem {
	@Column(name = "RATING_ENABLED")
	private boolean ratingEnabled;

	public Item() {
		super();
	}

	public boolean isRatingEnabled() {
		return ratingEnabled;
	}

	public void setRatingEnabled(boolean ratingEnabled) {
		this.ratingEnabled = ratingEnabled;
	}
}
