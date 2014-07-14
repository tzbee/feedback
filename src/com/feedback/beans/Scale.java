package com.feedback.beans;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "SCALE")
public class Scale {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "SCALE_ID")
	private int id;

	@Column(name = "START_VALUE")
	private int startValue;

	@Column(name = "END_VALUE")
	private int endValue;

	@Column(name = "SCALE_INTERVAL")
	private int interval;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONFIG_ID")
	private FeedbackConfig feedbackConfig;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStartValue() {
		return startValue;
	}

	public void setStartValue(int startValue) {
		this.startValue = startValue;
	}

	public int getEndValue() {
		return endValue;
	}

	public void setEndValue(int endValue) {
		this.endValue = endValue;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public FeedbackConfig getFeedbackConfig() {
		return feedbackConfig;
	}

	public void setFeedbackConfig(FeedbackConfig feedbackConfig) {
		this.feedbackConfig = feedbackConfig;
	}

	@Transient
	public List<Integer> getScaleValues() {
		List<Integer> scaleValues = new ArrayList<Integer>();

		for (int value = getStartValue(); value <= getEndValue(); value += getInterval()) {
			scaleValues.add(value);
		}

		return scaleValues;
	}

	@Override
	public String toString() {
		return getStartValue() + " " + getEndValue() + " " + getInterval();
	}
}