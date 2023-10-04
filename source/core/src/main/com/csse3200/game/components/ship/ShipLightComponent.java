package com.csse3200.game.components.ship;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.components.AuraLightComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Adds a controllable light source to the player ship.
 */
public class ShipLightComponent extends AuraLightComponent {
	private static final Logger logger = LoggerFactory.getLogger(ShipLightComponent.class);
	private boolean unlocked;

	public ShipLightComponent() {
		super(30f, Color.WHITE);
	}

	@Override
	public void create() {
		unlocked = false;

		entity.getEvents().addListener("progressUpdated", this::progressUpdated);
		entity.getEvents().addListener("interact", this::toggleLight);
	}

	/**
	 * If the LIGHT feature has been unlocked on the Ship Entity, set own state to unlocked
	 *
	 * @param repairsMade      number of repairs on the Ship Entity
	 * @param unlockedFeatures features that have been unlocked on the Ship Entity
	 */
	private void progressUpdated(int repairsMade, Set<ShipProgressComponent.Feature> unlockedFeatures) {
		if (unlockedFeatures.contains(ShipProgressComponent.Feature.LIGHT)) {
			logger.debug("Ship TimeSkip unlocked");
			unlocked = true;
		}
	}

	/**
	 * Toggles the light between an on and off state, if the feature is unlocked.
	 */
	@Override
	public void toggleLight() {
		if (unlocked) {
			super.toggleLight();
		}
	}

	@Override
	public void write(Json json) {
		json.writeObjectStart(this.getClass().getSimpleName());
		json.writeValue("unlocked", unlocked);
		json.writeValue("active", getActive());
		json.writeObjectEnd();
	}

	@Override
	public void read(Json json, JsonValue jsonMap) {
		unlocked = jsonMap.getBoolean("unlocked");
		boolean active = jsonMap.getBoolean("active");
		if (getActive() != active) super.toggleLight();
	}
}
