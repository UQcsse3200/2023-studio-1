package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.placeables.PlaceableCategory;
import com.csse3200.game.components.placeables.SprinklerComponent;
import com.csse3200.game.components.placeables.FenceComponent;
import com.csse3200.game.components.AuraLightComponent;
import com.csse3200.game.components.LightController;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;
import com.csse3200.game.services.FactoryService;
import com.csse3200.game.services.ServiceLocator;

public class PlaceableFactory {

    /**
     * Creates the basic most broad Placeable Entity
     *
     * @param type the type of entity from the EntityType enum
     * @return the entity that was made
     */
    public static Entity createBasePlaceable(EntityType type) {
        Entity placeable = new Entity(type)
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new HitboxComponent())
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
        placeable.getEvents().addListener("destroy", PlaceableFactory::destoryPlaceable);
        return placeable;
    }

    private void destoryPlaceable(TerrainTile tile) {
        Entity placedItem = tile.getPlaceable();
        // Check if the placeable is a chest and if there is items in that chest
        InventoryComponent chestInventory = placedItem.getComponent(InventoryComponent.class);
        if (chestInventory != null){
            if (chestInventory.getInventory().size() >= 1) {
                return;
            }
        }
        // Removes references from the tile to the placeable and set it as unoccupied
        tile.removeCropTile();
        // Spawn in the item that corresponds to the removed placeable
        Entity droppedItem = FactoryService.getItemFactories().get(placedItem.getType().toString()).get();
        ServiceLocator.getGameArea().spawnEntity(droppedItem);
        droppedItem.setPosition(placedItem.getPosition());
        // Remove the placeable from the game area
        ServiceLocator.getGameArea().removeEntity(placedItem);
    }

    /**
     * Creates a fence Entity
     *
     * @return the fence entity that was made
     */
    public static Entity createFence() {
        EntityType type = EntityType.Fence;
        type.setPlaceableCategory(PlaceableCategory.Fences);
        Entity fence = createBasePlaceable(type)
                .addComponent(new DynamicTextureRenderComponent("images/placeable/fences/f.png"))
                .addComponent(new FenceComponent(false));
        // Add components here
        return fence;
    }

    /**
     * Creates a gate Entity
     *
     * @return the gate entity that was made
     */
    public static Entity createGate() {
        EntityType type = EntityType.Gate;
        type.setPlaceableCategory(PlaceableCategory.Fences);
        Entity gate = createBasePlaceable(type)  //used to be .Gate.
                .addComponent(new DynamicTextureRenderComponent("images/placeable/fences/g_r_l.png"))
                .addComponent(new FenceComponent(true));
        return gate;
    }

    /**
     * Creates a sprinkler Entity
     *
     * @return the sprinkler that was made
     */
    public static Entity createSprinkler() {
        EntityType type = EntityType.Sprinkler;
        type.setPlaceableCategory(PlaceableCategory.Sprinklers);
        Entity sprinkler = createBasePlaceable(type)
                .addComponent(new DynamicTextureRenderComponent("images/placeable/sprinkler/pipe_null.png"));
        // stop from blocking player movement
        sprinkler.getComponent(ColliderComponent.class).setLayer(PhysicsLayer.NONE);
        // add sprinkler component
        sprinkler.addComponent(new SprinklerComponent());
        return sprinkler;
    }

    /**
     * Creates a pump Entity - used in conjunction with sprinklers
     * @return the pump that was made
     */
    public static Entity createPump() {
        EntityType type = EntityType.Pump;
        type.setPlaceableCategory(PlaceableCategory.Sprinklers);
        Entity pump = createBasePlaceable(type);
        // set temp texture to differentiate from other entities
        pump.addComponent(new DynamicTextureRenderComponent("images/placeable/sprinkler/pump.png"));
        //pump.getComponent(DynamicTextureRenderComponent.class).setTexture("images/placeable/sprinkler/pump.png");
        // add sprinkler component
        pump.addComponent(new SprinklerComponent());
        // set as a pump
        pump.getComponent(SprinklerComponent.class).setPump();
        return pump;
    }

    /**
     * Creates a chest Entity
     *
     * @return the chest that was made
     */
    public static Entity createChest() {
        Entity chest = createBasePlaceable(EntityType.Chest);
        // Add components here
        chest.addComponent(new InventoryComponent(null))
            .addComponent(new DynamicTextureRenderComponent("images/Temp-Chest.png"));
        return chest;
    }

    public static Entity createLight() {
        Entity light = createBasePlaceable(EntityType.Light);
        light.addComponent(new AuraLightComponent(4f, Color.TAN));
        light.addComponent(new LightController());
        return light;
    }
}
