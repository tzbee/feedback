package com.feedback.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "FEEDBACK_UNIT")
public class FeedbackUnit extends AbstractItem {

	@Column(name = "VALUE")
	private int value;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWNER_SESSION_ID")
	private FeedbackSession feedbackSession;

	public FeedbackUnit() {
		freeze();
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public FeedbackSession getFeedbackSession() {
		return feedbackSession;
	}

	public void setFeedbackSession(FeedbackSession feedbackSession) {
		this.feedbackSession = feedbackSession;
	}
}
