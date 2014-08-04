package com.feedback.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import com.feedback.beans.DataUnit;

/**
 * Creates a data set containing the average of all previous data units at one
 * particular time stamp
 */
public class AverageDataProcessingStrategy implements DataProcessingStrategy {

	@Transient
	@Override
	public Data process(Data data) {
		Data resultData = new Data();
		resultData.setDataUnits(new ArrayList<DataUnit>());
		DataUnit resultDataUnit;

		List<DataUnit> dataUnits = data.getDataUnits();

		for (DataUnit dataUnit : dataUnits) {
			resultDataUnit = new DataUnit();
			resultDataUnit.setCreatedAt(dataUnit.getCreatedAt());
			resultDataUnit.setValue(Utils.getAverage(Utils.getSubCollection(
					dataUnits, dataUnits.indexOf(dataUnit))));
			resultData.addDataUnit(resultDataUnit);
		}

		return resultData;
	}
}
