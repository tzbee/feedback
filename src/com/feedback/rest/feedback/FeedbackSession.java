package com.feedback.rest.feedback;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import com.feedback.beans.AbstractItem;
import com.feedback.beans.Item;

@Entity
@Table(name = "FEEDBACK_SESSION")
public class FeedbackSession extends AbstractItem {
	@ManyToOne
	@JoinColumn(name = "OWNER_ITEM_ID")
	private Item item;

	@XmlTransient
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
}
