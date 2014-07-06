package com.feedback.item;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * POJO class describing an Item to evaluate
 */
@Entity
@Table(name = "ITEM")
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ITEM_ID")
	private int itemID;

	@Column(name = "ITEM_NAME")
	private String itemName;

	@Column(name = "ITEM_DESCRIPTION")
	private String itemDescription;

	@Column(name = "RATING_ENABLED")
	private boolean ratingEnabled;

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public boolean isRatingEnabled() {
		return ratingEnabled;
	}

	public void setRatingEnabled(boolean ratingEnabled) {
		this.ratingEnabled = ratingEnabled;
	}

	@Override
	public String toString() {
		return "[ " + getItemName() + " ," + getItemDescription() + ", "
				+ isRatingEnabled() + "]";
	}
}
