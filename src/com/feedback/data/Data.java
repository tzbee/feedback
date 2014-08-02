package com.feedback.data;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.feedback.beans.AbstractItem;
import com.feedback.beans.DataUnit;

@Entity
@Table(name = "DATA")
public class Data extends AbstractItem {
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private List<DataUnit> dataUnits;

	public List<DataUnit> getDataUnits() {
		return this.dataUnits;
	}

	public void setDataUnits(List<DataUnit> dataUnits) {
		this.dataUnits = dataUnits;
	}

	public void addDataUnit(DataUnit dataUnit) {
		this.dataUnits.add(dataUnit);
	}
}
