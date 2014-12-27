package com.feedback.data;

import java.util.ArrayList;
import java.util.Collection;

import com.feedback.beans.DataUnit;

/**
 * A collection of static utility methods for the feedback śystem
 *
 */
public class Utils {

	/**
	 * Get a sub-part of a collection from the first element to a specified
	 * index
	 * 
	 * @param collection
	 *            the collection to process
	 * @param index
	 *            the ending index
	 * @return the sub-collection created
	 */
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

	/**
	 * Get an average of the values of a collection of data units
	 * 
	 * @param dataUnitsthe
	 *            collection of data units to process
	 * @return the average value calculated
	 */
	public static double getAverage(Collection<DataUnit> dataUnits) {
		return sum(dataUnits) / dataUnits.size();
	}

	/**
	 * Get the sum of the values a collection of data units
	 * 
	 * @param dataUnits
	 *            the collection of data units to process
	 * 
	 * @return the sum of the data units
	 */
	public static double sum(Collection<DataUnit> dataUnits) {
		double counter = 0;

		for (DataUnit dataUnit : dataUnits) {
			counter += dataUnit.getValue();
		}

		return counter;
	}
}
