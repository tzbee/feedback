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

/**
 * Class representing a basic numerical scale
 */
@Entity
@Table(name = "SCALE")
public class Scale {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SCALE_ID")
	private int id;

	@Column(name = "START_VALUE")
	private double startValue;

	@Column(name = "END_VALUE")
	private double endValue;

	@Column(name = "SCALE_INTERVAL")
	private double interval;

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

	public double getStartValue() {
		return startValue;
	}

	public void setStartValue(double startValue) {
		this.startValue = startValue;
	}

	public double getEndValue() {
		return endValue;
	}

	public void setEndValue(double endValue) {
		this.endValue = endValue;
	}

	public double getInterval() {
		return interval;
	}

	public void setInterval(double interval) {
		this.interval = interval;
	}

	public FeedbackConfig getFeedbackConfig() {
		return feedbackConfig;
	}

	public void setFeedbackConfig(FeedbackConfig feedbackConfig) {
		this.feedbackConfig = feedbackConfig;
	}

	@Transient
	public List<Double> getScaleValues() throws ScaleException {
		List<Double> scaleValues = new ArrayList<Double>();
		double startValue = getStartValue(), endValue = getEndValue();

		if (startValue > endValue) {
			throw new ScaleException();
		}

		for (double value = startValue; value <= endValue; value += getInterval()) {
			scaleValues.add(value);
		}

		return scaleValues;
	}

	@JsonIgnore
	public boolean contains(double number) throws ScaleException {
		return getScaleValues().contains(number);
	}

	@Override
	public String toString() {
		return getStartValue() + " " + getEndValue() + " " + getInterval();
	}
}
