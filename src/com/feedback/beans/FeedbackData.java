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

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "FEEDBACK_DATA")
public class FeedbackData {
	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "FBD_ID")
	private int id;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWNER_ITEM_ID")
	private Item item;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private List<FeedbackSession> feedbackSessions;

	@JoinColumn(name = "CURRENT_FBS_ID")
	private FeedbackSession currentFeedbackSession;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@JsonIgnore
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
