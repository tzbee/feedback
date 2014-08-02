package com.feedback.data;

import java.util.ArrayList;
import java.util.Collection;

import com.feedback.beans.DataUnit;

public class Utils {

	public static <E> Collection<E> getSubCollection(Collection<E> collection,
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

	public static double getAverage(Collection<DataUnit> dataUnits) {
		return sum(dataUnits) / dataUnits.size();
	}

	public static double sum(Collection<DataUnit> dataUnits) {
		double counter = 0;

		for (DataUnit dataUnit : dataUnits) {
			counter += dataUnit.getValue();
		}

		return counter;
	}
}
