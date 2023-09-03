package com.csse3200.game.missions;

public abstract class Quest extends Mission {

	/**
	 * Creates a {@link Quest} with the given {@link String} name.
	 *
	 * @param name The {@link String} name of the {@link Quest}, visible to the player in-game.
	 */
	public Quest(String name) {
		super(name);
	}

	/**
	 * Decrements the expiry time of a quest by the given amount
	 * @param amount number of hours to decrement the expiry time by
	 */
	public void updateExpiry(int amount) {
		return;
	}

	/**
	 * determines whether the quest is mandatory or not
	 * @return boolean value representing whether the quest is mandatory
	 */
	public boolean isMandatory() {
		return false;
	}
}
