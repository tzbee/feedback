package com.feedback.beans;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "FEEDBACK_SESSION")
public class FeedbackSession extends AbstractItem {
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "FEEDBACK_CONFIG_ID")
	private FeedbackConfig feedbackConfig;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWNER_FEEDBACK_DATA_ID")
	private FeedbackData feedbackData;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private List<FeedbackUnit> feedbackUnits;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_AT", updatable = false, insertable = false)
	private Date createdAt = new Date();

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CLOSED_AT", updatable = false)
	private Date closedAt = new Date();

	public FeedbackConfig getFeedbackConfig() {
		return feedbackConfig;
	}

	public void setFeedbackConfig(FeedbackConfig feedbackConfig) {
		this.feedbackConfig = feedbackConfig;
		feedbackConfig.setFeedbackSession(this);
	}

	public FeedbackData getFeedbackData() {
		return feedbackData;
	}

	public void setFeedbackData(FeedbackData feedbackData) {
		this.feedbackData = feedbackData;
	}

	public List<FeedbackUnit> getFeedbackUnits() {
		return feedbackUnits;
	}

	public void addFeedbackUnit(FeedbackUnit feedbackUnit) {
		if (isFeedbackUnitValid(feedbackUnit)) {
			this.feedbackUnits.add(feedbackUnit);
			feedbackUnit.setFeedbackSession(this);
		}
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getClosedAt() {
		return closedAt;
	}

	public void setClosedAt(Date closedAt) {
		this.closedAt = closedAt;
	}

	public boolean isFeedbackUnitValid(FeedbackUnit feedbackUnit) {
		// TODO Check if configured scale is matching
		return true;
	}
}
