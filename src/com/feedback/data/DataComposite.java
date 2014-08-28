package com.feedback.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import com.feedback.beans.DataUnit;

/**
 * A composite data block serving as a container for other data blocks
 */
public class DataComposite extends Data {

	@Transient
	private List<Data> dataList = new ArrayList<Data>();

	@Override
	public List<DataUnit> getDataUnits() {
		List<DataUnit> dataUnits = new ArrayList<DataUnit>();

		for (Data data : this.dataList) {
			dataUnits.addAll(data.getDataUnits());
		}

		return dataUnits;
	}

	public void addData(Data data) {
		this.dataList.add(data);
	}
}
