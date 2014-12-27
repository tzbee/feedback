package com.feedback.beans;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.feedback.data.Data;
import com.feedback.data.DataComposite;

/**
 * POJO class describing an Item holding data
 */
@Entity
@Table(name = "ITEM")
public class Item extends AbstractItem implements DataSource {
	@Column(name = "ITEM_NAME")
	private String name;

	@Column(name = "ITEM_DESCRIPTION")
	private String description;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "FEEDBACK_DATA_ID")
	private FeedbackWrapper feedbackWrapper;

	public Item() {
		setFeedbackWrapper(new FeedbackWrapper());
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

	@Override
	public void freeze() {
		super.freeze();

		for (FeedbackSession feedbackSession : getFeedbackWrapper()
				.getFeedbackSessions()) {
			feedbackSession.freeze();
		}
	}

	public FeedbackWrapper getFeedbackWrapper() {
		return feedbackWrapper;
	}

	public void setFeedbackWrapper(FeedbackWrapper feedbackWrapper) {
		this.feedbackWrapper = feedbackWrapper;
		feedbackWrapper.setItem(this);
	}

	public void createFeedbackSession(FeedbackSession feedbackSession) {
		getFeedbackWrapper().addFeedbackSession(feedbackSession);
		FeedbackSession currentFeedbackSession = getCurrentFeedbackSession();

		if (currentFeedbackSession != null) {
			currentFeedbackSession.freeze();
		}

		getFeedbackWrapper().setCurrentFeedbackSession(feedbackSession);
	}

	public void freezeCurrentFeedbackSession() {
		FeedbackSession currentFeedbackSession = getCurrentFeedbackSession();

		if (currentFeedbackSession != null) {
			currentFeedbackSession.freeze();
		}
	}

	@JsonIgnore
	public FeedbackSession getCurrentFeedbackSession() {
		return getFeedbackWrapper().getCurrentFeedbackSession();
	}

	public void addDataUnit(DataUnit dataUnit) throws ConfigurationException,
			ScaleException {
		getCurrentFeedbackSession().addDataUnit(dataUnit);
	}

	public boolean isRatingEnabled() {
		FeedbackSession feedbackSession = getCurrentFeedbackSession();
		return feedbackSession != null && !feedbackSession.isFrozen();
	}

	/**
	 * DATA
	 */

	@Override
	public Data getData() {
		// Create data object
		DataComposite dataComposite = new DataComposite();

		for (FeedbackSession feedbackSession : getFeedbackWrapper()
				.getFeedbackSessions()) {
			dataComposite.addData(feedbackSession.getData());
		}

		return dataComposite;
	}
}
