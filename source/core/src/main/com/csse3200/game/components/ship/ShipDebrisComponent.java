package com.csse3200.game.components.ship;

import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.services.ServiceLocator;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

import static com.csse3200.game.missions.quests.QuestFactory.ALIENS_ATTACK_QUEST_NAME;

/**
 * Handles Ship Debris events.
 * <p>
 * Currently, triggers a DEBRIS_CLEARED event on the mission manager when destroyed.
 */
public class ShipDebrisComponent extends Component {
	Random random = new SecureRandom();
	static boolean canSpawnShipEater = false;

	@Override
	public void create() {
		super.create();

		entity.getEvents().addListener("destroy", this::destroy);
		ServiceLocator.getMissionManager().getEvents().addListener(MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), ShipDebrisComponent::checkCanSpawnShipEater);
	}

	/**
	 * Check if the player has completed the ALIENS_ATTACK_QUEST_NAME quest which gives
	 * the player ranged weapons.
	 *
	 * @param missionCompleteName name of the mission that triggered the event
	 */
	private static void checkCanSpawnShipEater(String missionCompleteName) {
		if (Objects.equals(missionCompleteName, ALIENS_ATTACK_QUEST_NAME)) {
			canSpawnShipEater = true;
		}
	}

	/**
	 * Sets the random instance to use.
	 *
	 * @param randomInstance to use
	 */
	public void setRandomInstance(Random randomInstance) {
		random = randomInstance;
	}

	/**
	 * Trigger the mission manager's DEBRIS_CLEARED event then self destruct.
	 */
	void destroy(TerrainTile tile) {
		if (    // player must have gotten ranged weapons to defeat a ship eater!
				canSpawnShipEater
						&& random.nextInt(2) == 0
		) {
			// unlucky, shipeater will spawn!
			Entity shipEater = NPCFactory.createShipEater();
			shipEater.setCenterPosition(entity.getCenterPosition());
			ServiceLocator.getGameArea().spawnEntity(shipEater);
		}
		ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.DEBRIS_CLEARED.name());
		if (tile != null) tile.removeOccupant();
		entity.dispose();
	}
}