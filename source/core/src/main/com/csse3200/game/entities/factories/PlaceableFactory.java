package com.csse3200.game.entities.factories;

import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;

public class PlaceableFactory {

    public static Entity createBasePlaceable(EntityType type) {
        Entity placeable = new Entity(type)
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE));
        return placeable;
    }

    public static Entity createFence() {
        Entity fence = createBasePlaceable(EntityType.Fence);
        // Add components here
        return fence;
    }

    public static Entity createGate() {
        Entity gate = createBasePlaceable(EntityType.Gate);
        // Add components here
        return gate;
    }

    public static Entity createSprinkler() {
        Entity sprinkler = createBasePlaceable(EntityType.Sprinkler);
        // Add components here
        return sprinkler;
    }

    public static Entity createChest() {
        Entity chest = createBasePlaceable(EntityType.Chest);
        // Add components here
        chest.addComponent(new InventoryComponent(null));
        return chest;
    }
}
