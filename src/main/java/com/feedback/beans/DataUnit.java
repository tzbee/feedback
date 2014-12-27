package com.feedback.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.feedback.data.Data;
import com.owlike.genson.annotation.JsonIgnore;

/**
 * Single unit of data
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class DataUnit extends AbstractItem {

	@Column(name = "VALUE")
	private double value;

	@Column(name = "TAG")
	private String tag = "";

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWNER_DATA_ID")
	private Data ownerData;

	public DataUnit() {
		freeze();
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Data getOwnerData() {
		return ownerData;
	}

	public void setOwnerData(Data ownerData) {
		this.ownerData = ownerData;
	}
}
