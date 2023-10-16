package com.csse3200.game.components.plants;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.components.combat.CombatStatsComponent;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.missions.MissionManager;
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
    private final List<Entity> entitiesInRange = new ArrayList<>();

    /**
     * Circle shape of the area.
     */
    private final CircleShape shape = new CircleShape();

    private Vector2[] aoeVectors;

    /**
     * Constructor for the Area of Effect class.
     * @param radius - The initial radius of the area.
     * @param effectType - The type of effect.
     */
    public PlantAreaOfEffectComponent(float radius, String effectType) {
        this.radius = radius;
        this.effectType = effectType;
    }

    public String getEffectType() {
        return this.effectType;
    }

    /**
     * Update the current effect being executed.
     * @param effectType - The effect to be implemented.
     */
    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }

    /**
     * Returns the radius of the area of effect for the plant.
     * @return The radius of the area of effect.
     */
    public float getRadius() {
        return this.radius;
    }

    /**
     * Set the radius of the area.
     * @param radius - the new radius of the area.
     */
    public void setRadius(float radius) {
        // Currently not in use because changing the radius is causing issues.
        this.radius = radius;
    }

    /**
     * Returns a list of entities within the range.
     * @return A list of entities within the range.
     */
    public List<Entity> getEntitiesInRange() {
        return new ArrayList<>(entitiesInRange);
    }

    /**
     * Sets up a radius for the collider and listens to relevant services.
     */
    @Override
    public void create() {
        //shape.setRadius(getRadius());
        shape.setPosition(entity.getComponent(PlantComponent.class).getCropTile().getEntity().getScale().scl(0.5f).add(0, -0.5f));
        setShape(shape);

        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
        ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::hourlyEffect);
        ServiceLocator.getTimeService().getEvents().addListener("minuteUpdate", this::minuteUpdate);

        super.create();
    }

    /**
     * Function is triggered every hour of in game time and checks which effects should be executed.
     */
    private void hourlyEffect() {
        if (getEffectType().equals("Sound")) {
            soundEffect();
        }
    }

    /**
     * Function is triggered every minute of in game time and checks which effects should be executed.
     */
    private void minuteUpdate() {
        int min = ServiceLocator.getTimeService().getMinute();

        if (min % 5 == 0) {
            switch (getEffectType()) {
                case "Decay" -> decayAndDeadEffect();
                case "Health" -> healthEffect();
                case "Poison" -> poisonEffect();
                default -> { // Cry or something
                }
            }
        }

        if (getEffectType().equals("Eat")) {
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
    public void onCollisionStart(Fixture me, Fixture other) {
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
    public void onCollisionEnd(Fixture me, Fixture other) {
        if (getFixture() != me) {
            return;
        }

        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        entitiesInRange.remove(target);
    }

    /**
     * Effect that is triggered when the plant is decaying or dead. This effect decreases the health of all plants
     * near the decaying or dead plant. This encourages the player to remove dead and decaying plants.
     * This effects all other plants within a 1 tile radius of the decaying plant.
     * Thanks to the person that implemented sprinklers for coming up with this
     * approach for aoe.
     */
    private void decayAndDeadEffect() {
        // Position of this decaying plant
        Vector2 plantPos = entity.getPosition();

        // List of all coordinates in a 1 tile radius of the plant.
        this.aoeVectors = new Vector2[]{
                new Vector2(plantPos.x - 1, plantPos.y + 1),
                new Vector2(plantPos.x, plantPos.y + 1),
                new Vector2(plantPos.x + 1, plantPos.y + 1),
                new Vector2(plantPos.x - 1, plantPos.y),
                new Vector2(plantPos.x + 1, plantPos.y),
                new Vector2(plantPos.x - 1, plantPos.y - 1),
                new Vector2(plantPos.x, plantPos.y - 1),
                new Vector2(plantPos.x + 1, plantPos.y - 1),
        };

        // Decay effect for adjacent plants
        for (Vector2 otherPos : aoeVectors) {
            TerrainTile tile = ServiceLocator.getGameArea().getMap().getTile(otherPos);
            Entity occupant = tile.getOccupant();
            if (occupant != null) {
                CropTileComponent cropTile = occupant.getComponent(CropTileComponent.class);
                if (cropTile != null) {
                    Entity otherPlant = cropTile.getPlant();
                    if (otherPlant != null) {
                        otherPlant.getComponent(PlantComponent.class).increasePlantHealth(-4);
                    }
                }
            }
        }
    }

    /**
     * Effect that increases the health of plants, animals, and the player.
     * This increases the health of all plants within a 1 tile radius and increases
     * the health of the player and all animals in the radius of the collider.
     * Thanks to the person that implemented sprinklers for coming up with this
     * approach for aoe.
     */
    private void healthEffect() {
        // Position of this health plant.
        Vector2 plantPos = entity.getPosition();

        // List of all coordinates in a 1 tile radius of the plant.
        this.aoeVectors = new Vector2[]{
                new Vector2(plantPos.x - 1, plantPos.y + 1),
                new Vector2(plantPos.x, plantPos.y + 1),
                new Vector2(plantPos.x + 1, plantPos.y + 1),
                new Vector2(plantPos.x - 1, plantPos.y),
                new Vector2(plantPos.x + 1, plantPos.y),
                new Vector2(plantPos.x - 1, plantPos.y - 1),
                new Vector2(plantPos.x, plantPos.y - 1),
                new Vector2(plantPos.x + 1, plantPos.y - 1),
        };

        // Health effect for adjacent plants.
        for (Vector2 otherPos : aoeVectors) {
            TerrainTile tile = ServiceLocator.getGameArea().getMap().getTile(otherPos);
            Entity occupant = tile.getOccupant();
            if (occupant != null) {
                CropTileComponent cropTile = occupant.getComponent(CropTileComponent.class);
                if (cropTile != null) {
                    Entity otherPlant = cropTile.getPlant();
                    if (otherPlant != null) {
                        otherPlant.getComponent(PlantComponent.class).increasePlantHealth(4);
                    }
                }
            }
        }

        // Health effect for player and animals.
        for (Entity entityInRange : getEntitiesInRange()) {

            if (    entityInRange.getType() == EntityType.PLAYER ||
                    entityInRange.getType() == EntityType.BAT ||
                    entityInRange.getType() == EntityType.ASTROLOTL ||
                    entityInRange.getType() == EntityType.DRAGONFLY ||
                    entityInRange.getType() == EntityType.CHICKEN ||
                    entityInRange.getType() == EntityType.OXYGEN_EATER ||
                    entityInRange.getType() == EntityType.COW
            ) {
                entityInRange.getComponent(CombatStatsComponent.class).addHealth(4);
            }
        }
    }

    /**
     * Effect that poisons the player and any animals in the area.
     */
    private void poisonEffect() {
        for (Entity entityInRange : getEntitiesInRange()) {
            if (    entityInRange.getType() == EntityType.PLAYER ||
                    entityInRange.getType() == EntityType.BAT ||
                    entityInRange.getType() == EntityType.ASTROLOTL ||
                    entityInRange.getType() == EntityType.DRAGONFLY ||
                    entityInRange.getType() == EntityType.CHICKEN ||
                    entityInRange.getType() == EntityType.OXYGEN_EATER ||
                    entityInRange.getType() == EntityType.COW
            ) {
                entityInRange.getComponent(CombatStatsComponent.class).addHealth(-1);
            }
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

                if (    entityInRange.getType() == EntityType.BAT ||
                        entityInRange.getType() == EntityType.ASTROLOTL ||
                        entityInRange.getType() == EntityType.DRAGONFLY ||
                        entityInRange.getType() == EntityType.CHICKEN ||
                        entityInRange.getType() == EntityType.OXYGEN_EATER ||
                        entityInRange.getType() == EntityType.COW ||
                        entityInRange.getType() == EntityType.SHIP_EATER) {

                    // If a valid entity is in the area, tell the plant it is eating.
                    entity.getComponent(PlantComponent.class).setIsEating();
                    entity.getComponent(AnimationRenderComponent.class).startAnimation("digesting");
                    // just dispose of the entity being eaten. might want to implement a count of
                    // eaten entities in plant component.
                    entityInRange.dispose();

                    ServiceLocator.getMissionManager().getEvents().trigger(
                            MissionManager.MissionEvent.ANIMAL_EATEN.name(),
                            entityInRange.getType());

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

            if (entityInRange.getType() == EntityType.PLAYER) {
                entity.getComponent(PlantComponent.class).playSound("nearby");
            }
        }
    }
}
