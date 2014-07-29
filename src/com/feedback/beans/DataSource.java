package com.feedback.beans;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.feedback.data.Data;

/**
 * An item holding data
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class DataSource extends AbstractItem {

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "DATA_ID")
	private Data data;

	public Data getData() {
		return this.data;
	}

	public void setData(Data data) {
		this.data = data;
	}
}
