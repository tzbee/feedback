package com.feedback.beans;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.owlike.genson.annotation.JsonIgnore;

/**
 * A wrapper for all feedback related structures
 */
@Entity
@Table(name = "FEEDBACK_WRAPPER")
public class FeedbackWrapper {
	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FBD_ID")
	private int id;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWNER_ITEM_ID")
	private Item item;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private List<FeedbackSession> feedbackSessions;

	@JoinColumn(name = "CURRENT_FBS_ID")
	@OneToOne(fetch = FetchType.LAZY)
	private FeedbackSession currentFeedbackSession;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public List<FeedbackSession> getFeedbackSessions() {
		return feedbackSessions;
	}

	public FeedbackSession getCurrentFeedbackSession() {
		return currentFeedbackSession;
	}

	public void setCurrentFeedbackSession(FeedbackSession currentFeedbackSession) {
		this.currentFeedbackSession = currentFeedbackSession;
	}

	public void addFeedbackSession(FeedbackSession feedbackSession) {
		this.feedbackSessions.add(feedbackSession);
		feedbackSession.setFeedbackData(this);
	}
}
