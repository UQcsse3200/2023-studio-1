package com.csse3200.game.missions;

public abstract class Achievement extends Mission {

	/**
	 * Creates an {@link Achievement} with the given {@link String} name.
	 *
	 * @param name The {@link String} name of the {@link Achievement}, visible to the player
	 *             in-game.
	 */
	public Achievement(String name) {
		super(name);
	}

}
