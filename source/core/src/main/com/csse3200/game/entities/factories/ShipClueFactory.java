package com.csse3200.game.entities.factories;

import com.csse3200.game.components.ClueComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

import java.util.List;

public class ShipClueFactory {

    public static Entity createShipClue(Entity player, List<String> possibleLocations) {
        Entity shipClue = new Entity(EntityType.ShipDebris)
                .addComponent(new ClueComponent(possibleLocations));

        return shipClue;
    }
}
