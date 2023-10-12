package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.inventory.InventoryDisplay;
import com.csse3200.game.components.AuraLightComponent;
import com.csse3200.game.components.placeables.*;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class PlaceableFactory {

    /**
     * Creates the basic most broad Placeable Entity
     *
     * @param type the type of entity from the EntityType enum
     * @return the entity that was made
     */
    public static Entity createBasePlaceable(EntityType type) {
        return new Entity(type)
                .addComponent(new PlaceableEvents())
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new HitboxComponent())
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    }

    /**
     * Creates a fence Entity
     *
     * @return the fence entity that was made
     */
    public static Entity createFence() {
        EntityType type = EntityType.FENCE;
        type.setPlaceableCategory(PlaceableCategory.FENCES);
        return createBasePlaceable(type)
                .addComponent(new DynamicTextureRenderComponent("images/placeable/fences/f.png"))
                .addComponent(new FenceComponent(false));
        // Add components here
    }

    /**
     * Creates a gate Entity
     *
     * @return the gate entity that was made
     */
    public static Entity createGate() {
        EntityType type = EntityType.GATE;
        type.setPlaceableCategory(PlaceableCategory.FENCES);
        return createBasePlaceable(type)  //used to be .Gate.
                .addComponent(new DynamicTextureRenderComponent("images/placeable/fences/g_r_l.png"))
                .addComponent(new FenceComponent(true));
    }

    /**
     * Creates a sprinkler Entity
     *
     * @return the sprinkler that was made
     */
    public static Entity createSprinkler() {
        EntityType type = EntityType.SPRINKLER;
        type.setPlaceableCategory(PlaceableCategory.SPRINKLERS);
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
        EntityType type = EntityType.PUMP;
        type.setPlaceableCategory(PlaceableCategory.SPRINKLERS);
        Entity pump = createBasePlaceable(type);
        // set temp texture to differentiate from other entities
        pump.addComponent(new DynamicTextureRenderComponent("images/placeable/sprinkler/pump.png"));
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
        Entity chest = createBasePlaceable(EntityType.CHEST);
        // Add components here
        chest.addComponent(new InventoryComponent())
            .addComponent(new DynamicTextureRenderComponent("images/Temp-Chest.png"))
            .addComponent(new ChestComponent())
            .addComponent(new InventoryDisplay("refreshChest", "toggleChest", 30, 10, false));
        return chest;
    }

    public static Entity createLight() {
        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/light.atlas", TextureAtlas.class),
                16f
        );

        animator.addAnimation("light_off", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("light_on", 0.1f, Animation.PlayMode.LOOP);

        animator.startAnimation("light_off");

        return createBasePlaceable(EntityType.LIGHT)
                .addComponent(new AuraLightComponent(4f, Color.TAN))
                .addComponent(new LightController())
                .addComponent(animator);

    }

    public static Entity createGoldenTrophy() {
        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/golden_trophy.atlas", TextureAtlas.class),
                16f
        );

        animator.addAnimation("default", 0.5f, Animation.PlayMode.LOOP);
        animator.startAnimation("default");

        AuraLightComponent lightComponent = new AuraLightComponent();
        lightComponent.toggleLight();
        DialogueComponent dialogue = new DialogueComponent();
        dialogue.addCutsceneAnimation(makeCutsceneEntity());
        return createBasePlaceable(EntityType.GOLDEN_STATUE)
                .addComponent(lightComponent)
                .addComponent(dialogue)
                .addComponent(animator);
    }

    private static Entity makeCutsceneEntity() {
        AnimationRenderComponent animation = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/player_fishing.atlas", TextureAtlas.class));
        animation.addAnimation("cast_left", 0.1f, Animation.PlayMode.NORMAL);
        return new Entity().addComponent(animation);
    }
}
