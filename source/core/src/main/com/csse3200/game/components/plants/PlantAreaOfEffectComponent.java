package com.csse3200.game.components.plants;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.InteractionDetector;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.services.ServiceLocator;

/**
 * Class to add an Area of Effect to all plants.
 */
public class PlantAreaOfEffectComponent  extends InteractionDetector {
    /**
     * The radius of the area of effect.
     */
    private float radius;

    /**
     * The type of effect to be implemented.
     */
    private String effectType;

    /**
     * Constructor for the Area of Effect class.
     * @param radius - The radius of the area.
     * @param effectType - The type of effect.
     */
    public PlantAreaOfEffectComponent(float radius, String effectType) {
        super(1f);
        this.radius = radius;
        this.effectType = effectType;
    }

    /**
     * Sets up a radius for the collider and listens to relevant services.
     */
    @Override
    public void create() {
        super.create();
        ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::hourlyEffect);
    }

    /**
     * Override the function from the parent class. This allows the area to be centered on the crop tile
     * where the plant is located.
     */
    @Override
    public void setShape() {
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        shape.setPosition(entity.getComponent(PlantComponent.class).getCropTile().getEntity().getScale().scl(0.5f).add(0, -0.5f));
        setShape(shape);
    }

    /**
     * Effect that takes place every hour.
     */
    private void hourlyEffect() {
        switch (this.effectType) {
            case "Decay" -> decayAndDeadEffect();
            case "Health" -> healthEffect();
            case "Poison" -> poisonEffect();
            case "Eat" -> eatEffect();
        }
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
                        // Decrease the health of plants in the effect area.
                        plant.getComponent(PlantComponent.class).increasePlantHealth(-10);
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

            // First check for other plants in the area
            if (entityInRange.getType() == EntityType.Tile) {
                Entity plant = entityInRange.getComponent(CropTileComponent.class).getPlant();
                if (plant != null) {
                    if (entity.getId() != plant.getId()) {
                        plant.getComponent(PlantComponent.class).increasePlantHealth(10);
                    }
                }

            // Now check for the player
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
                entityInRange.getComponent(CombatStatsComponent.class).addHealth(-10);
            }
            // add animals to this.
        }
    }

    /**
     * Effect that allows the space snapper to eat any animals in its area. The space snapper has a
     * cool down period after eating an animal.
     */
    private void eatEffect() {

        // Check that the space snapper is not already eating
        if (!entity.getComponent(PlantComponent.class).getIsEating()) {

            for (Entity entityInRange : getEntitiesInRange()) {

                if (entityInRange.getType() == EntityType.Cow
                        || entityInRange.getType() == EntityType.Chicken
                        || entityInRange.getType() == EntityType.Astrolotl
                        || entityInRange.getType() == EntityType.OxygenEater) {

                    // If a valid entity is in the area, tell the plant it is eating.
                    entity.getComponent(PlantComponent.class).setIsEating();

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
     * Update the current effect being executed.
     * @param effectType - The effect to be implemented.
     */
    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }

    /**
     * Set the radius of the area.
     * @param radius - the new radius of the area.
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }
}
