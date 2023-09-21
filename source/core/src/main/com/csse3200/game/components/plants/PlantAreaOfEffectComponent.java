package com.csse3200.game.components.plants;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to add an Area of Effect to all plants.
 */
public class PlantAreaOfEffectComponent extends HitboxComponent {
    /**
     * The radius of the area of effect.
     */
    private float radius;

    /**
     * The type of effect to be implemented.
     */
    private String effectType;

    /**
     * List of entities within the area
     */
    private List<Entity> entitiesInRange = new ArrayList<>();

    /**
     * Circle shape of the area.
     */
    private CircleShape shape = new CircleShape();

    /**
     * Constructor for the Area of Effect class.
     * @param radius - The initial radius of the area.
     * @param effectType - The type of effect.
     */
    public PlantAreaOfEffectComponent(float radius, String effectType) {
        this.radius = radius;
        this.effectType = effectType;
    }

    /**
     * Sets up a radius for the collider and listens to relevant services.
     */
    @Override
    public void create() {
        shape.setRadius(radius);
        shape.setPosition(entity.getComponent(PlantComponent.class).getCropTile().getEntity().getScale().scl(0.5f).add(0, -0.5f));
        setShape(shape);

        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
        ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::hourlyEffect);
        ServiceLocator.getTimeService().getEvents().addListener("minuteUpdate", this::minuteUpdate);

        super.create();
    }

    /**
     * Effect that takes place every hour.
     */
    private void hourlyEffect() {
        if (this.effectType.equals("Sound")) {
            soundEffect();
        }
    }

    private void minuteUpdate() {
        int min = ServiceLocator.getTimeService().getMinute();

        if (min % 5 == 0) {
            switch (this.effectType) {
                case "Decay" -> decayAndDeadEffect();
                case "Health" -> healthEffect();
                case "Poison" -> poisonEffect();
            }
        }

        if (this.effectType.equals("Eat")) {
            eatEffect();
        }

    }

    /**
     * Adds entity to entitiesInRange on collision start.
     * NOTE: This function is copied from the InteractionDetector class, written by
     * someone else.
     *
     * @param me     The fixture of this component.
     * @param other  The fixture of the colliding entity.
     */
    private void onCollisionStart(Fixture me, Fixture other) {
        if (getFixture() != me) {
            return;
        }

        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        entitiesInRange.add(target);
    }

    /**
     * Removes entity from entitiesInRange on collision end.
     * NOTE: This function is copied from the InteractionDetector class, written by
     * someone else.
     *
     * @param me    The fixture of this component.
     * @param other  The fixture of the colliding entity.
     */
    private void onCollisionEnd(Fixture me, Fixture other) {
        if (getFixture() != me) {
            return;
        }

        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        entitiesInRange.remove(target);
    }

    public List<Entity> getEntitiesInRange() {
        return new ArrayList<>(entitiesInRange);
    }

    /**
     * Effect that is triggered when the plant is decaying or dead. This effect decreases the health of all plants
     * near the decaying or dead plant. This encourages the player to remove dead and decaying plants.
     */
    private void decayAndDeadEffect() {

        for (Entity entityInRange : getEntitiesInRange()) {

            // Check for any crop tiles near the plant.
            if (entityInRange.getType() == EntityType.Tile) {
                Entity plant = entityInRange.getComponent(CropTileComponent.class).getPlant();
                if (plant != null) {

                    // Make sure the plant does not harm itself.
                    if (entity.getId() != plant.getId()) {

                        // Decrease the health of all plants in the effect area.
                        plant.getComponent(PlantComponent.class).increasePlantHealth(-1);
                    }
                }
            }

        }
    }

    /**
     * Effect that increases the health of plants, animals, and the player.
     */
    private void healthEffect() {
        for (Entity entityInRange : getEntitiesInRange()) {
            if (entityInRange.getType() == EntityType.Tile) {

                // First check for other plants in the area.
                Entity plant = entityInRange.getComponent(CropTileComponent.class).getPlant();
                if (plant != null) {
                    if (entity.getId() != plant.getId()) {
                        plant.getComponent(PlantComponent.class).increasePlantHealth(1);
                    }
                }

            // Now check if the player.
            } else if (entityInRange.getType() == EntityType.Player) {
                entityInRange.getComponent(CombatStatsComponent.class).addHealth(10);
            }
            // add animals to this.
        }
    }

    /**
     * Effect that poisons the player and any animals in the area.
     */
    private void poisonEffect() {
        for (Entity entityInRange : getEntitiesInRange()) {

            if (entityInRange.getType() == EntityType.Player) {
                entityInRange.getComponent(CombatStatsComponent.class).addHealth(-20);
            }
            // add animals to this.
        }
    }

    /**
     * Effect that allows the space snapper to eat any animals in its area. The space snapper has a
     * cool down period after eating an animal.
     */
    private void eatEffect() {
        // Check that the space snapper is not already eating.
        if (!entity.getComponent(PlantComponent.class).getIsEating()) {

            for (Entity entityInRange : getEntitiesInRange()) {

                if (entityInRange.getType() == EntityType.Cow
                        || entityInRange.getType() == EntityType.Chicken
                        || entityInRange.getType() == EntityType.Astrolotl
                        || entityInRange.getType() == EntityType.OxygenEater) {

                    // If a valid entity is in the area, tell the plant it is eating.
                    entity.getComponent(PlantComponent.class).setIsEating();
                    entity.getComponent(AnimationRenderComponent.class).startAnimation("digesting");
                    // just dispose of the entity being eaten. might want to implement a count of
                    // eaten entities in plant component.
                    entityInRange.dispose();

                    // break once a valid entity has been eaten because space snapper can only eat one entity at a time.
                    break;
                }
            }
        }
    }

    /**
     * Plays a Nearby sound if the player comes near the plant.
     */
    private void soundEffect() {
        for (Entity entityInRange : getEntitiesInRange()) {

            if (entityInRange.getType() == EntityType.Player) {
                entity.getComponent(PlantComponent.class).playSound("nearby");
            }
        }
    }

    /**
     * Update the current effect being executed.
     * @param effectType - The effect to be implemented.
     */
    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }

    /**
     * Set the radius of the area. This will dispose of the old hitbox component
     * and create a new one with the desired radius.
     * @param radius - the new radius of the area.
     */
    public void setRadius(float radius) {
        /*
        this.radius = radius;
        shape.setRadius(radius);
        // Dispose the old HitboxComponent and create a new one with the new radius.
        super.dispose();
        super.create();

         */
    }
}
