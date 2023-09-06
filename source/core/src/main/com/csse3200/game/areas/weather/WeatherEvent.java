package com.csse3200.game.areas.weather;

import com.csse3200.game.services.ServiceLocator;

import javax.swing.plaf.SplitPaneUI;

public abstract class WeatherEvent {

	private int numHoursUntil;
	private int duration;
	private int priority;
	private float humidity_modifier;
	private static final float MIN_HUMIDITY_MODIFIER = 0.8f;
	private static final float MAX_HUMIDITY_MODIFIER = 1.2f;

	public WeatherEvent(int numHoursUntil, int duration, int priority) {
		this.numHoursUntil = numHoursUntil;
		this.duration = duration;
		this.priority = priority;

	}

	public void updateTime() {
		if (numHoursUntil > 0) {
			numHoursUntil--;
		} else if (duration > 0) {
			duration--;
		}
	}

	/**
	 * Returns the priority of the weather event
	 * @return weather event priority
	 */
	public int getPriority() {
		return priority;
	}

	public boolean isActive() {
		return numHoursUntil == 0 && duration > 0;
	}

	// TODO Figure out how to render screen effects and therefore fix method siganture
	public abstract void getEffect();

}
