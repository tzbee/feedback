package com.feedback.item.state;

public abstract class StateFul {
	private State state;

	public StateFul(State active) {
		this.state = active;
	}

	public StateFul() {
		this(State.ACTIVE);
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}
