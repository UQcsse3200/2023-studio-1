package com.csse3200.game.areas.weather;

import com.csse3200.game.services.ServiceLocator;

import javax.swing.plaf.SplitPaneUI;

public abstract class WeatherEvent {

	private int numHoursUntil;
	private int duration;
	private int priority;

	public WeatherEvent(int numHoursUntil, int duration, int priority) {
		this.numHoursUntil = numHoursUntil;
		this.duration = duration;
		this.priority = priority;
	}

	//TODO
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

	// TODO
	public boolean isActive() {
		return false;
	}

	public void addEffect(ClimateController climate) {
		return;
	}

	public void triggerWeatherEvents() {
		return;
	}
}
