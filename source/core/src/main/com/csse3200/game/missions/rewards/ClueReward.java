package com.csse3200.game.missions.rewards;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.ShipPartTileComponent;
import com.csse3200.game.areas.terrain.ShipPartTileFactory;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.items.ClueComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ShipDebrisFactory;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.RandomUtils;

import java.util.List;

/**
 * A reward that spawns a ShipPartTile and ShipDebris on collection.
 */
public class ClueReward extends ItemReward {

	private final Entity clueItem;

	public ClueReward(Entity clueItem) {
		super(List.of(clueItem));
		this.clueItem = clueItem;
	}

	/**
	 * When called, changes the isCollected variable to true and adds the reward items to the player's inventory.
	 * Also spawns a ShipPartTile and ShipDebris around the base location contained in the Map Item.
	 */
	@Override
	public void collect() {
		ServiceLocator.getEntityService().register(clueItem);
		ClueComponent clueComponent = clueItem.getComponent(ClueComponent.class);

		if (clueComponent != null) {
			Vector2 location = clueComponent.getCurrentBaseLocation();
			// Generate tile & debris for the location
			generateTileAndDebris(location);
		}
		super.collect();
	}

	/**
	 * Spawns a ShipPartTile & ShipDebris around the given location. Checks whether a tile is
	 * occupied before proceeding with the spawn.
	 *
	 * @param location to base the random generation around
	 */
	private void generateTileAndDebris(Vector2 location) {
		GridPoint2 minPos = new GridPoint2((int) (location.x - 5), (int) (location.y - 5));
		GridPoint2 maxPos = new GridPoint2((int) (location.x + 5), (int) (location.y + 5));

		GameMap gameMap = ServiceLocator.getGameArea().getMap();

		GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
		TerrainTile tile = gameMap.getTile(randomPos);

		while (!tile.isTraversable() || tile.isOccupied()) {
			randomPos = RandomUtils.random(minPos, maxPos);
			tile = gameMap.getTile(randomPos);
		}

		Entity partTile = ShipPartTileFactory.createShipPartTile(
				gameMap.getTerrainComponent().tileToWorldPosition(randomPos)
		);

		ServiceLocator.getEntityService().register(partTile);

		tile.setOccupant(partTile);
		tile.setOccupied();

		Entity shipDebris = ShipDebrisFactory.createShipDebris();

		partTile.getComponent(ShipPartTileComponent.class).addShipDebris(shipDebris);
		partTile.getComponent(ShipPartTileComponent.class).addClueItem(clueItem);
	}
}

