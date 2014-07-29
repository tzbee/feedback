package com.feedback.data;

import java.util.ArrayList;

import javax.persistence.Transient;

import com.feedback.beans.DataUnit;

public class StubDataStrategy implements DataStrategy {

	@Transient
	@Override
	public Data process(Data data) {
		Data data2 = new Data();
		data2.setDataUnits(new ArrayList<DataUnit>());
		DataUnit dataUnit = new DataUnit();

		dataUnit.setValue(1);
		data2.addDataUnit(dataUnit);

		dataUnit = new DataUnit();
		dataUnit.setValue(2);
		data2.addDataUnit(dataUnit);

		dataUnit = new DataUnit();
		dataUnit.setValue(3);
		data2.addDataUnit(dataUnit);

		dataUnit = new DataUnit();
		dataUnit.setValue(4);
		data2.addDataUnit(dataUnit);

		dataUnit = new DataUnit();
		dataUnit.setValue(5);
		data2.addDataUnit(dataUnit);

		dataUnit = new DataUnit();
		dataUnit.setValue(6);
		data2.addDataUnit(dataUnit);

		return data2;
	}
}
