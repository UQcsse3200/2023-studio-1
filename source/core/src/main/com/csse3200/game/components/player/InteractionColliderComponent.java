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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
     * Updates the interaction range of this component.
     *
     * @param range The new interaction range to set.
     */
    public void updateRange(float range) {
        this.range = range; //TODO: THIS DOES NOTHING. NEED TO CREATE NEW FIXTURE OR SOMETHING
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
        return entitiesInRange;
    }

    /**
     * Gets the list of entities currently in the interaction range in the specified direction.
     *
     * @param direction The direction in which to filter entities (e.g., "UP", "DOWN", "LEFT", "RIGHT").
     * @return A list of entities within the interaction range in the specified direction.
     */
    public List<Entity> getEntitiesTowardsDirection(String direction) {
        List<Entity> filteredEntities = new ArrayList<>();
        for (Entity entity : entitiesInRange) {
            Vector2 targetDirectionVector = entity.getCenterPosition().sub(this.entity.getCenterPosition());
            String targetDirection = DirectionUtils.vectorToDirection(targetDirectionVector);

            if (Objects.equals(targetDirection, direction)) {
                filteredEntities.add(entity);
            }
        }
        return filteredEntities;
    }


    public List<Entity> getEntitiesTowardsPosition(Vector2 position) {
        Vector2 directionVector = position.sub(entity.getCenterPosition());
        String direction = DirectionUtils.vectorToDirection(directionVector);

        return getEntitiesTowardsDirection(direction);
    }

    /**
     * Filters a list of entities by a specified component type.
     *
     * @param entities The list of entities to filter.
     * @param type     The component type by which to filter entities.
     * @return A list of entities containing the specified component type.
     */
    public List<Entity> filterByComponent(List<Entity> entities, Class<? extends Component> type) {
        List<Entity> filteredEntities = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity.getComponent(type) != null) {
                filteredEntities.add(entity);
            }
        }
        return filteredEntities;
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

        Entity nearestEntity = null;
        float nearestEntityDistance = Float.MAX_VALUE;
        Vector2 position = this.entity.getCenterPosition();

        for (Entity entity : entities) {
            Vector2 entityPosition = entity.getCenterPosition();
            float currentEntityDistance = position.dst(entityPosition);

            if (currentEntityDistance < nearestEntityDistance) {
                nearestEntity = entity;
                nearestEntityDistance = currentEntityDistance;
            }
        }

        return nearestEntity;
    }

    public Entity getSuitableEntity(List<Entity> entities, ItemType itemType) {
        switch (itemType){
            case FOOD -> {
                List<Entity> feedableEntities = filterByComponent(entities, TamableComponent.class);
                feedableEntities.removeIf(entity -> entity.getComponent(TamableComponent.class).isTamed());
                return getNearest(feedableEntities);
            }
        }
        return null;
    }
}
