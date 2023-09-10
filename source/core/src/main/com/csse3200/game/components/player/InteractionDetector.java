package com.csse3200.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.components.npc.TamableComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.utils.DirectionUtils;

import java.util.*;

/**
 * Represents a component that handles interaction detection with entities within a specified range.
 */
public class InteractionDetector extends HitboxComponent {
    /** List of entities currently in the interaction range. */
    private final List<Entity> entitiesInRange = new ArrayList<>();
    /** The interaction range within which entities are detected. */
    private final float range;

    /**
     * Constructs an InteractionDetector with the specified interaction range.
     *
     * @param range The interaction range within which entities are detected.
     */
    public InteractionDetector(float range) {
        this.range = range;
    }


    /**
     * When entity is created, sets up circular collider with radius as range used for detecting entities.
     * Also attaches collision event listeners.
     */
    @Override
    public void create() {
        CircleShape shape = new CircleShape();
        shape.setRadius(range);
        shape.setPosition(entity.getScale().scl(0.5f));

        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
        setShape(shape);
        super.create();
    }

    /**
     * Adds entity to entitiesInRange on collision start.
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
     *
     * @param me     The fixture of this component.
     * @param other  The fixture of the colliding entity.
     */
    private void onCollisionEnd(Fixture me, Fixture other) {
        if (getFixture() != me) {
            return;
        }

        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        entitiesInRange.remove(target);
    }

    /**
     * Gets the list of entities currently in the interaction range.
     *
     * @return A list of entities within the interaction range.
     */
    public List<Entity> getEntitiesInRange() {
        return new ArrayList<>(entitiesInRange);
    }

    /**
     * Gets the list of entities currently in the interaction range in the specified direction.
     *
     * @param direction The direction in which to filter entities (e.g., "UP", "DOWN", "LEFT", "RIGHT").
     * @return A list of entities within the interaction range in the specified direction.
     */
    public List<Entity> getEntitiesTowardsDirection(String direction) {
        List<Entity> entities = getEntitiesInRange();
        entities.removeIf(entity -> {
            Vector2 targetDirectionVector = entity.getCenterPosition().sub(this.entity.getCenterPosition());
            String targetDirection = DirectionUtils.vectorToDirection(targetDirectionVector);

            return !Objects.equals(targetDirection, direction);
        });
        return entities;
    }

    /**
     * Gets the list of entities currently in the interaction range towards a position.
     *
     * @param position The vector position in which to filter entities.
     * @return A list of entities within the interaction range towards the position.
     */
    public List<Entity> getEntitiesTowardsPosition(Vector2 position) {
        Vector2 directionVector = position.sub(entity.getCenterPosition());
        String direction = DirectionUtils.vectorToDirection(directionVector);

        return getEntitiesTowardsDirection(direction);
    }

    /**
     * Gets a list containing the nearest entity from a list of entities.
     *
     * @param entities The list of entities to search for the nearest entity.
     * @return A list containing the nearest entity in the list, or an empty list if the input list is empty.
     */
    public List<Entity> getNearest(List<Entity> entities) {
        if (entities.isEmpty()) {
            return Collections.emptyList(); // Return an empty list if there are no entities.
        }

        Vector2 position = this.entity.getCenterPosition();
        Comparator<Entity> distanceComparator = (entity1, entity2) ->
                Float.compare(position.dst(entity1.getCenterPosition()), position.dst(entity2.getCenterPosition()));

        Entity nearestEntity = Collections.min(entities, distanceComparator);
        return Collections.singletonList(nearestEntity);
    }

    /**
     * Retrieves a list of suitable entities of the specified item type based on their proximity to a given position.
     *
     * @param itemType The type of item to determine suitability criteria (e.g., FOOD).
     * @param position The position to evaluate proximity to.
     * @return A list of suitable entities based on the provided criteria, or an empty list if no suitable entities are found.
     */
    public List<Entity> getSuitableEntities(ItemType itemType, Vector2 position) {
        List<Entity> entities = getEntitiesTowardsPosition(position);
        switch (itemType){
            case FOOD -> {
                entities.removeIf(entity -> entity.getComponent(TamableComponent.class) == null);
                entities.removeIf(entity -> entity.getComponent(TamableComponent.class).isTamed()); //TODO: axolotl? handle that
                return getNearest(entities);
            }
        }
        return Collections.emptyList();
    }
}
