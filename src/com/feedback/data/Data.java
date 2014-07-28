package com.feedback.data;

import java.util.List;

import com.feedback.beans.DataUnit;

public interface Data {
	public List<? extends DataUnit> getData();
}
