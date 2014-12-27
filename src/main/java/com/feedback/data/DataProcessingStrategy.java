package com.feedback.data;

/**
 * Strategy interface used to process data
 */
public interface DataProcessingStrategy {

	/**
	 * Transforms original data by applying calculations to it
	 * 
	 * @param data
	 *            the original data to process
	 * @return the processed data
	 */
	Data process(Data data);
}
