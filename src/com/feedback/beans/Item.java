package com.feedback.beans;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * POJO class describing an Item to evaluate
 */
@Entity
@Table(name = "RATABLE_ITEM")
public class Item extends AbstractItem {
	@Column(name = "ITEM_NAME")
	private String name;

	@Column(name = "ITEM_DESCRIPTION")
	private String description;

	@Column(name = "RATING_ENABLED")
	private boolean ratingEnabled;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "FEEDBACK_DATA_ID")
	private FeedbackData feedbackData;

	public Item() {
		setFeedbackData(new FeedbackData());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isRatingEnabled() {
		return ratingEnabled;
	}

	public void setRatingEnabled(boolean ratingEnabled) {
		this.ratingEnabled = ratingEnabled;
	}

	public FeedbackData getFeedbackData() {
		return feedbackData;
	}

	public void setFeedbackData(FeedbackData feedbackData) {
		this.feedbackData = feedbackData;
		feedbackData.setItem(this);
	}

	public void addFeedbackSession(FeedbackSession feedbackSession) {
		getFeedbackData().addFeedbackSession(feedbackSession);
		FeedbackSession currentFeedbackSession = getFeedbackData()
				.getCurrentFeedbackSession();

		if (currentFeedbackSession != null) {
			currentFeedbackSession.freeze();
		}

		getFeedbackData().setCurrentFeedbackSession(feedbackSession);
	}
}
