package com.feedback.beans;

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

	@Column(name = "LOCAL_INDEX")
	private int localIndex = 0;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private List<FeedbackUnit> feedbackUnits;

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

	public int getLocalIndex() {
		return localIndex;
	}

	public void setLocalIndex(int localIndex) {
		this.localIndex = localIndex;
	}

	public List<FeedbackUnit> getFeedbackUnits() {
		return feedbackUnits;
	}

	public void addFeedbackUnit(FeedbackUnit feedbackUnit)
			throws ConfigurationException, ScaleException {
		if (isFeedbackUnitValid(feedbackUnit)) {
			this.feedbackUnits.add(feedbackUnit);
			feedbackUnit.setFeedbackSession(this);
		} else {
			throw new ConfigurationException(
					"Feedback scale configuration not respected!");
		}
	}

	/**
	 * Checks if configured scale is matching
	 * 
	 * @param feedbackUnit
	 * @return
	 * @throws ScaleException
	 */
	private boolean isFeedbackUnitValid(FeedbackUnit feedbackUnit)
			throws ScaleException {
		FeedbackConfig feedbackConfig = getFeedbackConfig();
		Scale scale = feedbackConfig.getScale();
		return scale.contains(feedbackUnit.getValue());
	}
}
