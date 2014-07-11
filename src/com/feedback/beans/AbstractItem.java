package com.feedback.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

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
	}

	public boolean isFrozen() {
		return getState().equals(State.FROZEN);
	}
}
