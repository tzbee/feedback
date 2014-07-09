package com.feedback.item.feedback;

import java.util.ArrayList;
import java.util.List;

import com.feedback.item.AbstractItem;

public class FeedbackSession extends AbstractItem {
	private List<FeedbackUnit> feedbackUnits = new ArrayList<FeedbackUnit>();

	public void addFeedbackUnit(FeedbackUnit feedbackUnit) {
		this.feedbackUnits.add(feedbackUnit);
	}
}
