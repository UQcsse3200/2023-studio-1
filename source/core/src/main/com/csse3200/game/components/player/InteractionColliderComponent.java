package com.csse3200.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.Component;
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
public class InteractionColliderComponent extends HitboxComponent {
    /** List of entities currently in the interaction range. */
    private final List<Entity> entitiesInRange = new ArrayList<>();
    /** The interaction range within which entities are detected. */
    private float range;

    /**
     * Constructs an InteractionColliderComponent with the specified interaction range.
     *
     * @param range The interaction range within which entities are detected.
     */
    public InteractionColliderComponent(float range) {
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

    public List<Entity> getEntitiesTowardsPosition(Vector2 position) {
        Vector2 directionVector = position.sub(entity.getCenterPosition());
        String direction = DirectionUtils.vectorToDirection(directionVector);

        return getEntitiesTowardsDirection(direction);
    }

    /**
     * Gets the nearest entity from a list of entities based on their distances.
     *
     * @param entities The list of entities to search for the nearest entity.
     * @return The nearest entity in the list, or null if the list is empty.
     */
    public Entity getNearest(List<Entity> entities) {
        if (entities.isEmpty()) {
            return null;
        }

        Vector2 position = this.entity.getCenterPosition();
        Comparator<Entity> distanceComparator = (entity1, entity2) -> {
            float distance1 = position.dst(entity1.getCenterPosition());
            float distance2 = position.dst(entity2.getCenterPosition());
            return Float.compare(distance1, distance2);
        };

        return Collections.max(entities, distanceComparator);
    }

    public Entity getSuitableEntity(List<Entity> entities, ItemType itemType) {
        switch (itemType){
            case FOOD -> {
                entities.removeIf(entity -> entity.getComponent(TamableComponent.class) == null);
                entities.removeIf(entity -> entity.getComponent(TamableComponent.class).isTamed());
                return getNearest(entities);
            }
        }
        return null;
    }
}
