package com.feedback.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "FEEDBACK_CONFIG")
public class FeedbackConfig {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "CONFIG_ID")
	private int id;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONFIGURED_SESSION_ID")
	private FeedbackSession feedbackSession;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public FeedbackSession getFeedbackSession() {
		return feedbackSession;
	}

	public void setFeedbackSession(FeedbackSession feedbackSession) {
		this.feedbackSession = feedbackSession;
	}
}
