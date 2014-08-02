package com.feedback.data;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Transient;

import com.feedback.beans.DataUnit;

public class SingleAverageDataStrategy implements DataProcessingStrategy {

	@Transient
	@Override
	public Data process(Data data) {
		Data resultData = new Data();
		resultData.setDataUnits(new ArrayList<DataUnit>());

		Collection<DataUnit> dataUnits = data.getDataUnits();
		double averageValue = Utils.getAverage(dataUnits);
		DataUnit resultDataUnit = new DataUnit();

		resultDataUnit.setValue(averageValue);
		resultDataUnit.setTag("Average value");

		resultData.addDataUnit(resultDataUnit);

		return resultData;
	}
}
