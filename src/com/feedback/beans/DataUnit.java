package com.feedback.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.feedback.data.Data;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class DataUnit extends AbstractItem {

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWNER_DATA_ID")
	private Data ownerData;

	@Column(name = "VALUE")
	private int value;

	public DataUnit() {
		freeze();
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Data getOwnerData() {
		return ownerData;
	}

	public void setOwnerData(Data ownerData) {
		this.ownerData = ownerData;
	}
}
