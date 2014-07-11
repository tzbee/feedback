package com.feedback.beans;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "FEEDBACK_SESSION")
public class FeedbackSession extends AbstractItem {
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "FEEDBACK_CONFIG_ID")
	private FeedbackConfig feedbackConfig;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWNER_FEEDBACK_DATA_ID")
	@XmlTransient
	private FeedbackData feedbackData;

	@OneToMany(fetch = FetchType.LAZY)
	private List<FeedbackUnit> feedbackUnits;

	public FeedbackConfig getFeedbackConfig() {
		return feedbackConfig;
	}

	public void setFeedbackConfig(FeedbackConfig feedbackConfig) {
		this.feedbackConfig = feedbackConfig;
		feedbackConfig.setFeedbackSession(this);
	}

	@JsonIgnore
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
		this.feedbackUnits.add(feedbackUnit);
		feedbackUnit.setFeedbackSession(this);
	}
}
