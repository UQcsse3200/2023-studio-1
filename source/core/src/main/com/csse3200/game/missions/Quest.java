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

}
