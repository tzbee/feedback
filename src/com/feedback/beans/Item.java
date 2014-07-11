package com.feedback.beans;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * POJO class describing an Item to evaluate
 */
@Entity
@Table(name = "RATABLE_ITEM")
public class Item extends AbstractItem {
	@Column(name = "RATING_ENABLED")
	private boolean ratingEnabled;

	@OneToMany(cascade = CascadeType.PERSIST)
	private List<FeedbackSession> feedbackSessions;

	@Column(name = "CURRENT_FEEDBACK_SESSION_INDEX")
	private int currentFeedbackSessionIndex = -1;

	public boolean isRatingEnabled() {
		return ratingEnabled;
	}

	public void setRatingEnabled(boolean ratingEnabled) {
		this.ratingEnabled = ratingEnabled;
	}

	public void addFeedbackSession(FeedbackSession feedbackSession) {
		this.feedbackSessions.add(feedbackSession);
		feedbackSession.setItem(this);
	}

	public int getCurrentFeedbackSessionIndex() {
		return currentFeedbackSessionIndex;
	}

	public void setCurrentFeedbackSessionIndex(int currentFeedbackSessionIndex) {
		this.currentFeedbackSessionIndex = currentFeedbackSessionIndex;
	}
}
