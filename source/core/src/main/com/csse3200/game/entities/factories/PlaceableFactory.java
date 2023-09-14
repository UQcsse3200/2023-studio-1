package com.csse3200.game.entities.factories;

import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;

public class PlaceableFactory {

    /**
     * Creates the basic most broad Placeable Entity
     * @param type the type of entity from the EntityType enum
     * @return the entity that was made
     */
    public static Entity createBasePlaceable(EntityType type) {
        Entity placeable = new Entity(type)
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE));
        return placeable;
    }

    /**
     * Creates a fence Entity
     * @return the fence entity that was made
     */
    public static Entity createFence() {
        Entity fence = createBasePlaceable(EntityType.Fence);
        // Add components here
        return fence;
    }

    /**
     * Creates a gate Entity
     * @return the gate entity that was made
     */
    public static Entity createGate() {
        Entity gate = createBasePlaceable(EntityType.Gate);
        // Add components here
        return gate;
    }

    /**
     * Creates a sprinkler Entity
     * @return the sprinkler that was made
     */
    public static Entity createSprinkler() {
        Entity sprinkler = createBasePlaceable(EntityType.Sprinkler);
        // Add components here
        return sprinkler;
    }

    /**
     * Creates a chest Entity
     * @return the chest that was made
     */
    public static Entity createChest() {
        Entity chest = createBasePlaceable(EntityType.Chest);
        // Add components here
        chest.addComponent(new InventoryComponent(null));
        return chest;
    }
}
