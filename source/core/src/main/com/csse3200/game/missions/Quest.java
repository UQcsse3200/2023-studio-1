package com.csse3200.game.missions;

public class Quest extends Mission {
	public Quest(String name) {
		super(name);
	}

	/**
	 *
	 */
	@Override
	public void registerMission() {

	}

	/**
	 * @return
	 */
	@Override
	public boolean isCompleted() {
		return false;
	}

	/**
	 * @return
	 */
	@Override
	public String getDescription() {
		return null;
	}
}
