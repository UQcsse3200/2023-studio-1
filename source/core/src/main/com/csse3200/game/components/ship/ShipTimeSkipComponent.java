package com.csse3200.game.components.ship;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.ship.ShipProgressComponent.Feature;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Handles allowing the player to sleep through the night by interacting
 * with the ship once the repair threshold is reached.
 */
public class ShipTimeSkipComponent extends Component {
	private static final Logger logger = LoggerFactory.getLogger(ShipTimeSkipComponent.class);
	private boolean unlocked;
	private boolean timeSkipInProgress;

	@Override
	public void create() {
		super.create();
		unlocked = false;
		timeSkipInProgress = false;

		entity.getEvents().addListener("progressUpdated", this::progressUpdated);
		entity.getEvents().addListener("timeSkip", this::triggerTimeSkip);

		ServiceLocator.getTimeService().getEvents().addListener("morningTime", this::stopTimeSkip);
	}

	/**
	 * If the BED feature has been unlocked on the Ship Entity, set own state to unlocked
	 *
	 * @param repairsMade      number of repairs on the Ship Entity
	 * @param unlockedFeatures features that have been unlocked on the Ship Entity
	 */
	private void progressUpdated(int repairsMade, Set<Feature> unlockedFeatures) {
		if (unlockedFeatures.contains(Feature.BED)) {
			logger.debug("Ship TimeSkip unlocked");
			unlocked = true;
		}
	}

	/**
	 * If time skip is unlocked, trigger the sleep function on the time service.
	 */
	private void triggerTimeSkip() {
		if (unlocked) {
			logger.debug("Skipping time to next MORNING_HOUR");
			ServiceLocator.getTimeSource().setTimeScale(200f);
			timeSkipInProgress = true;
		}
	}

	private void stopTimeSkip() {
		// Don't do anything if the morningTime event was triggered, but we're not sleeping
		if (timeSkipInProgress) {
			ServiceLocator.getTimeSource().setTimeScale(1f);
			timeSkipInProgress = false;
		}
	}

	@Override
	public void write(Json json) {
		json.writeObjectStart(this.getClass().getSimpleName());
		json.writeValue("unlocked", unlocked);
		json.writeValue("timeSkipInProgress", timeSkipInProgress);
		json.writeObjectEnd();
	}

	@Override
	public void read(Json json, JsonValue jsonMap) {
		unlocked = jsonMap.getBoolean("unlocked");
		timeSkipInProgress = jsonMap.getBoolean("timeSkipInProgress");
	}
}
