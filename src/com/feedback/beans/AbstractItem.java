package com.feedback.beans;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractItem {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "ITEM_ID")
	private int id;

	@Column(name = "STATE")
	@Enumerated(EnumType.STRING)
	private State state;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_AT", updatable = false, insertable = false)
	private Date createdAt = new Date();

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CLOSED_AT", updatable = false)
	private Date closedAt;

	public AbstractItem() {
		setState(State.ACTIVE);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void freeze() {
		setState(State.FROZEN);
		this.closedAt = new Date();
	}

	public boolean isFrozen() {
		return getState().equals(State.FROZEN);
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getClosedAt() {
		return closedAt;
	}

	public String getFormattedClosedAt() {
		return getClosedAt() != null ? getClosedAt().toString() : "";
	}

	public String getFormattedCreatedAt() {
		return getCreatedAt() != null ? getCreatedAt().toString() : "";
	}
}
