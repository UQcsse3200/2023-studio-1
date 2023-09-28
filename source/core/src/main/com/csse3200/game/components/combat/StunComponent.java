package com.csse3200.game.components.combat;

import com.csse3200.game.components.Component;
import com.csse3200.game.events.ScheduledEvent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;

public class StunComponent extends Component {
    private boolean stunned = false;
    private ScheduledEvent stunEvent;

    @Override
    public void create() {
        entity.getEvents().addListener("setStun", this::setStun);
        entity.getEvents().addListener("triggerStunDuration", this::setStunDuration);
    }

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

    public void setStunDuration(float duration) {
        stunned = true;
        entity.getEvents().cancelEvent(stunEvent); // override existing stun event if exists
        stunEvent = entity.getEvents().scheduleEvent(duration, "setStun", false);
    }

    public boolean isStunned() {
        return stunned;
    }
}
