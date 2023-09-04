package com.csse3200.game.areas.weather;

import javax.swing.plaf.SplitPaneUI;

public abstract class WeatherEvent {

	private int numHoursUntil;
	private int duration;
	private int priority;

	//TODO
	public void updateTime() {
		return;
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
