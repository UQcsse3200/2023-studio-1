package com.csse3200.game.components.ship;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.services.ServiceLocator;

import java.security.SecureRandom;

/**
 * Handles Ship Debris events.
 * <p>
 * Currently, triggers a DEBRIS_CLEARED event on the mission manager when destroyed.
 */
public class ShipDebrisComponent extends Component {
	SecureRandom random = new SecureRandom();

	@Override
	public void create() {
		super.create();
		entity.getEvents().addListener("destroy", this::destroy);
	}

	/**
	 * Trigger the mission manager's DEBRIS_CLEARED event then self destruct.
	 */
	void destroy(TerrainTile tile) {
		if (random.nextInt(2) == 0) {
			// unlucky, shipeater will spawn!
			GameArea gameArea = ServiceLocator.getGameArea();
			gameArea.spawnEntityAt(NPCFactory.createShipEater(), gameArea.getMap().vectorToTileCoordinates(entity.getCenterPosition()), true, true);
		}
		ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.DEBRIS_CLEARED.name());
		if (tile != null) tile.removeOccupant();
		entity.dispose();
	}
}