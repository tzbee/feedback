package com.feedback.rest;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.feedback.beans.DataUnit;
import com.feedback.data.Data;

@Path("test")
public class TestResource {

	private static final double[] VALUES = { 12, 4, 5, 5, 2 };

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Data getTest() {
		Data data = new Data();
		data.setDataUnits(new ArrayList<DataUnit>());
		DataUnit dataUnit;

		for (Double value : VALUES) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			dataUnit = new DataUnit();
			dataUnit.setValue(value);
			data.addDataUnit(dataUnit);
		}

		return data;
	}
}
