package com.csse3200.game.components.ship;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.ShipPartTileComponent;
import com.csse3200.game.areas.terrain.ShipPartTileFactory;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ShipDebrisFactory;
import com.csse3200.game.missions.rewards.ItemReward;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

public class ClueReward extends ItemReward {

    private final Entity mapItem;
    public ClueReward(Entity mapItem) {
        super(List.of(mapItem));
        this.mapItem = mapItem;
    }

    @Override
    public void collect() {
        ServiceLocator.getEntityService().register(mapItem);
        ClueComponent clueComponent = mapItem.getComponent(ClueComponent.class);

        if (clueComponent != null) {
            Vector2 location = clueComponent.getCurrentLocation();
            // Generate debris for the location
            generateTileAndDebris(location);
        }
        super.collect();
    }

    private void generateTileAndDebris(Vector2 location) {
        GameMap gameMap = ServiceLocator.getGameArea().getMap();

        TerrainTile tile = gameMap.getTile(location);

        Entity partTile = ShipPartTileFactory.createShipPartTile(location);
        ServiceLocator.getEntityService().register(partTile);
        tile.setOccupant(partTile);
        tile.setOccupied();

        Entity shipDebris = ShipDebrisFactory.createShipDebris(null);

        partTile.getComponent(ShipPartTileComponent.class).addShipDebris(shipDebris);
        partTile.getComponent(ShipPartTileComponent.class).addClueItem(mapItem);
    }
}

