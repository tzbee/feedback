package com.feedback.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "FEEDBACK_UNIT")
public class FeedbackUnit {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "FEEDBACK_UNIT_ID")
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWNER_SESSION_ID")
	@XmlTransient
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
