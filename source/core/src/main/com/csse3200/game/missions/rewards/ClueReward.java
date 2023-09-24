package com.csse3200.game.missions.rewards;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.ship.ClueComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.entities.Entity;

import com.csse3200.game.services.ServiceLocator;

import java.util.List;

import static com.csse3200.game.entities.factories.ShipDebrisFactory.createShipDebris;

public class ClueReward extends ItemReward {

    private final Entity mapItem;
    private final Entity player;
    public ClueReward(Entity mapItem, Entity player) {
        super(List.of(mapItem));
        this.mapItem = mapItem;
        this.player = player;
    }

    @Override
    public void collect() {

        ClueComponent clueComponent = mapItem.getComponent(ClueComponent.class);

        if (clueComponent != null) {
            List<String> possibleLocations = clueComponent.getPossibleLocations();

            if (!possibleLocations.isEmpty()) {
                // Generate debris for the first location in the list
                String location = possibleLocations.get(0);
                generateTileAndDebris(location);
                // Remove the first location from the list
                possibleLocations.remove(0);
            }
        }
    }

    private void generateTileAndDebris(String location) {
        Entity debris = createShipDebris(this.player);
        String[] parts = location.split(",");
        float x = Float.parseFloat(parts[0]);
        float y = Float.parseFloat(parts[1]);
        debris.setPosition(new Vector2(x, y));
    }
}

