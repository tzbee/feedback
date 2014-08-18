package com.feedback.beans;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.feedback.data.Data;

@Entity
@Table(name = "FEEDBACK_SESSION")
public class FeedbackSession extends AbstractItem implements DataSource {

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "FEEDBACK_CONFIG_ID")
	private FeedbackConfig feedbackConfig;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWNER_FEEDBACK_DATA_ID")
	private FeedbackWrapper feedbackWrapper;

	@Column(name = "LOCAL_INDEX")
	private int localIndex = 0;

	@Column(name = "GRAPH_PERIOD")
	private int period = 0;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "DATA_ID")
	private Data data;

	public FeedbackSession() {
		setData(new Data());
	}

	public Data getData() {
		return this.data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public FeedbackConfig getFeedbackConfig() {
		return feedbackConfig;
	}

	public void setFeedbackConfig(FeedbackConfig feedbackConfig) {
		this.feedbackConfig = feedbackConfig;
		feedbackConfig.setFeedbackSession(this);
	}

	public FeedbackWrapper getFeedbackWrapper() {
		return feedbackWrapper;
	}

	public void setFeedbackData(FeedbackWrapper feedbackData) {
		this.feedbackWrapper = feedbackData;
	}

	public int getLocalIndex() {
		return localIndex;
	}

	public void setLocalIndex(int localIndex) {
		this.localIndex = localIndex;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public int getPeriod() {
		return period;
	}

	public void addDataUnit(DataUnit dataUnit) throws ConfigurationException,
			ScaleException {
		if (isDataUnitValid(dataUnit)) {
			getData().addDataUnit(dataUnit);
			dataUnit.setOwnerData(getData());
		} else {
			throw new ConfigurationException(
					"Feedback scale configuration not respected!");
		}
	}

	/**
	 * Checks if configured scale is matching
	 * 
	 * @param dataUnit
	 * @return
	 * @throws ScaleException
	 */
	private boolean isDataUnitValid(DataUnit dataUnit) throws ScaleException {
		FeedbackConfig feedbackConfig = getFeedbackConfig();
		Scale scale = feedbackConfig.getScale();
		return scale.contains(dataUnit.getValue());
	}
}
