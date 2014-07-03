package com.feedback.item;

import java.io.Serializable;

public class Item implements Serializable {

	private String itemName;
	private String itemDescription;

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
}
