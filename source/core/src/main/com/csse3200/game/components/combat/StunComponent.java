package com.csse3200.game.components.combat;

import com.csse3200.game.components.Component;
import com.csse3200.game.events.ScheduledEvent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;

/**
 * A component that manages the stun status of an entity in a game.
 */
public class StunComponent extends Component {
    /** Flag to indicate whether the entity is currently stunned. */
    private boolean stunned = false;
    /** Scheduled event for managing the stun duration. */
    private ScheduledEvent stunEvent;

    /**
     * Initializes the StunComponent and sets up event listeners for stun-related events.
     */
    @Override
    public void create() {
        entity.getEvents().addListener("setStun", this::setStun);
        entity.getEvents().addListener("triggerStunDuration", this::setStunDuration);
    }

    /**
     * Sets the stun status of the entity.
     *
     * @param stunned {@code true} if the entity should be stunned; {@code false} to remove stun.
     */
    public void setStun(boolean stunned) {
        if (!stunned) {
           entity.getEvents().cancelEvent(stunEvent);
           stunEvent = null;
        }

        this.stunned = stunned;

        // Pauses npc movement on stun
        PhysicsMovementComponent movementComponent = entity.getComponent(PhysicsMovementComponent.class);
        if (movementComponent != null) {
            movementComponent.setEnabled(stunned);
        }
    }

    /**
     * Sets the stun duration for the entity.
     *
     * @param duration The duration (in seconds) for which the entity should be stunned.
     */
    public void setStunDuration(float duration) {
        stunned = true;
        entity.getEvents().cancelEvent(stunEvent); // override existing stun event if exists
        stunEvent = entity.getEvents().scheduleEvent(duration, "setStun", false);
    }

    /**
     * Checks whether the entity is currently stunned.
     *
     * @return {@code true} if the entity is stunned; {@code false} if not stunned.
     */
    public boolean isStunned() {
        return stunned;
    }
}
