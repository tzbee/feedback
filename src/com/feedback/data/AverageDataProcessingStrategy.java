package com.feedback.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Transient;

import com.feedback.beans.DataUnit;

public class AverageDataProcessingStrategy implements DataProcessingStrategy {

	@Transient
	@Override
	public Data process(Data data) {
		// TODO AverageDataProcessingStrategy process
		Data resultData = new Data();
		resultData.setDataUnits(new ArrayList<DataUnit>());
		DataUnit resultDataUnit;

		List<DataUnit> dataUnits = data.getDataUnits();

		for (DataUnit dataUnit : dataUnits) {
			resultDataUnit = new DataUnit();
			resultDataUnit.setCreatedAt(dataUnit.getCreatedAt());
			resultDataUnit.setValue(getAverage(getSubCollection(dataUnits,
					dataUnits.indexOf(dataUnit))));
			resultData.addDataUnit(resultDataUnit);
		}

		return resultData;
	}

	private <E> Collection<E> getSubCollection(Collection<E> collection,
			int index) {
		Collection<E> subCollection = new ArrayList<E>();

		int counter = 0;
		for (E object : collection) {
			if (counter <= index) {
				subCollection.add(object);
			}

			counter++;
		}

		return subCollection;
	}

	private double getAverage(Collection<DataUnit> dataUnits) {
		return sum(dataUnits) / dataUnits.size();
	}

	private double sum(Collection<DataUnit> dataUnits) {
		double counter = 0;

		for (DataUnit dataUnit : dataUnits) {
			counter += dataUnit.getValue();
		}

		return counter;
	}
}
