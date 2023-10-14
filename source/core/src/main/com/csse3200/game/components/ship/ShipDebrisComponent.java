package com.csse3200.game.components.ship;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.services.ServiceLocator;

import java.security.SecureRandom;
import java.util.Objects;

import static com.csse3200.game.missions.quests.QuestFactory.ALIENS_ATTACK_QUEST_NAME;

/**
 * Handles Ship Debris events.
 * <p>
 * Currently, triggers a DEBRIS_CLEARED event on the mission manager when destroyed.
 */
public class ShipDebrisComponent extends Component {
	SecureRandom random = new SecureRandom();
	static boolean canSpawnShipEater = false;

	@Override
	public void create() {
		super.create();

		entity.getEvents().addListener("destroy", this::destroy);
		ServiceLocator.getMissionManager().getEvents().addListener(MissionManager.MissionEvent.MISSION_COMPLETE.name(), this::checkCanSpawnShipEater);
	}

	/**
	 * Check if the player has completed the ALIENS_ATTACK_QUEST_NAME quest which gives
	 * the player ranged weapons.
	 *
	 * @param missionCompleteName name of the mission that triggered the event
	 */
	private void checkCanSpawnShipEater(String missionCompleteName) {
		if (Objects.equals(missionCompleteName, ALIENS_ATTACK_QUEST_NAME)) {
			canSpawnShipEater = true;
		}
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
			GameArea gameArea = ServiceLocator.getGameArea();
			gameArea.spawnEntityAt(NPCFactory.createShipEater(), gameArea.getMap().vectorToTileCoordinates(entity.getCenterPosition()), true, true);
		}
		ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.DEBRIS_CLEARED.name());
		if (tile != null) tile.removeOccupant();
		entity.dispose();
	}
}