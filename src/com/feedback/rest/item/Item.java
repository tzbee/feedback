package com.feedback.rest.item;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.feedback.rest.feedback.FeedbackSession;

/**
 * POJO class describing an Item to evaluate
 */
@Entity
@Table(name = "RATABLE_ITEM")
public class Item extends AbstractItem {
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<FeedbackSession> feedbackSessions;

	@Column(name = "RATING_ENABLED")
	private boolean ratingEnabled;

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
}
