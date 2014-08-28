package com.feedback.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.feedback.beans.DataUnit;
import com.feedback.data.Data;

@Path("test")
public class TestResource {

	/**
	 * Create a randomly generated data set of 100 values between 0 and 10
	 * 
	 * @param dataSetSize
	 *            size of the data set to create
	 * 
	 * @param maxValue
	 *            max value of each data unit created
	 * 
	 * @return the generated data set
	 */
	private List<DataUnit> createRandomDataSet(int dataSetSize, int maxValue) {
		double random;
		List<DataUnit> outputList = new ArrayList<DataUnit>();
		Date now = new Date();
		DataUnit dataUnit;

		for (int i = 0; i < dataSetSize; i++) {
			random = Math.random();
			dataUnit = new DataUnit();
			dataUnit.setCreatedAt(now);
			now = new Date((long) (now.getTime() + random * 100000000));
			dataUnit.setValue(random * maxValue);
			outputList.add(dataUnit);
		}

		return outputList;
	}

	/**
	 * Get a randomly generated data set of 100 values between 0 and 10
	 * 
	 * @return the generated data set
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Data getTest() {
		Data data = new Data();
		data.setDataUnits(createRandomDataSet(100, 10));
		return data;
	}
}
