package com.csse3200.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.services.ServiceLocator;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Handles the dropping one item for one entity on a specified trigger and
 * at a specified rate.
 */
public class SingleDropHandler {
    /**
     * Method to create item to be dropped.
     */
    private final Supplier<Entity> createItem;
    /**
     * Method to add listener to
     */
    private final BiConsumer<String, EventListener0> listener;
    /**
     * Trigger that will prompt item drop
     */
    private final String trigger;
    /**
     * Number of triggers until an item is dropped
     */
    private int triggersToNextDrop;
    /**
     * Number of triggers since last item drop
     */
    private int triggerCount;
    /**
     * Entity that is dropping the item
     */
    private Entity entity;
    /**
     * GameArea that the entity is on, and where the item
     * will spawn
     */
    private final GameArea gameArea;
    /**
     * Whether animal needs to be tamed before the item is dropped
     */
    private final boolean requiresTamed;

    /**
     * Constructor for SingleDropHandler
     *
     * @param createItem Method that creates item to be dropped
     * @param triggersToNextDrop Number of triggers until item is dropped
     * @param listener Method to add new listener
     * @param trigger Trigger that will prompt item drop
     */
    public SingleDropHandler(Supplier<Entity> createItem, int triggersToNextDrop,
                             BiConsumer<String, EventListener0> listener,
                             String trigger, boolean requiresTamed) {
        this.createItem = createItem;
        this.triggersToNextDrop = triggersToNextDrop;
        this.listener = listener;
        this.trigger = trigger;
        this.requiresTamed = requiresTamed;
        this.gameArea = ServiceLocator.getGameArea();
        triggerCount = 0;
    }

    /**
     * Set the entity that is dropping the item.
     * This method has to be separate from the constructor as the entity
     * may not be instantiated before this class is instantiated.
     *
     * @param entity The entity that is dropping the item
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    /**
     * Drops item at entity's location. Checks tamed status (if necessary)
     * and maintains drop rate.
     */
    public void dropItem() {
        if (requiresTamed) {
            TamableComponent tamableComponent = entity.getComponent(TamableComponent.class);

            if (tamableComponent == null || !tamableComponent.isTamed()) {
                return;
            }
        }

        triggerCount++;

        if (triggersToNextDrop == triggerCount) {
            triggerCount = 0;
            Entity item = createItem.get();
            Vector2 position = entity.getPosition();
            //Spawn different items in different locations around entity
            position.add((float) ((Math.random() - 0.5) * 1.5),
                    (float) ((Math.random() - 0.5) * 1.5));
            item.setPosition(position);
            gameArea.spawnEntity(item);
        }
    }

    /**
     * Getter for listener
     *
     * @return listener for the trigger
     */
    public BiConsumer<String, EventListener0> getListener() {
        return listener;
    }

    /**
     * Getter for trigger
     *
     * @return trigger that prompts item drop
     */
    public String getTrigger() {
        return trigger;
    }
}
