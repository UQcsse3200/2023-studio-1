package com.csse3200.game.missions.achievements;

import com.badlogic.gdx.utils.Json;
import com.csse3200.game.missions.Mission;

public abstract class Achievement extends Mission {

	/**
	 * Creates an {@link Achievement} with the given {@link String} name.
	 *
	 * @param name The {@link String} name of the {@link Achievement}, visible to the player
	 *             in-game.
	 */
	protected Achievement(String name) {
		super(name);
	}

	public void write(Json json) {
		json.writeObjectStart("Quest");
		json.writeValue("name", getName());
		json.writeValue("progress", new int[]{0, 1, 2});
		json.writeObjectEnd();
	}
}
