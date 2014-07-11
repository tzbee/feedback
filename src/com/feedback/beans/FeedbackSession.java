package com.feedback.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "FEEDBACK_SESSION")
public class FeedbackSession extends AbstractItem {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWNER_ITEM_ID")
	private Item item;

	@Column(name = "LOCAL_INDEX")
	private int localIndex = -1;

	@XmlTransient
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public int getLocalIndex() {
		return localIndex;
	}

	public void setLocalIndex(int localIndex) {
		this.localIndex = localIndex;
	}
}
