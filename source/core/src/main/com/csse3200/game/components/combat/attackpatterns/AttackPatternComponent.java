package com.csse3200.game.components.combat.attackpatterns;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.InteractionDetector;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.events.ScheduledEvent;
import com.csse3200.game.services.sound.EffectSoundFile;

/**
 * The AttackPatternComponent is responsible for managing the attack patterns
 * of entities within the game. It allows entities to initiate attacks with a specified
 * frequency and provides methods for starting and scheduling attacks.
 */
public class AttackPatternComponent extends Component {
    /** Frequency of attacks in seconds */
    protected final float attackFrequency;
    /** Entity's interaction detector */
    protected InteractionDetector interactionDetector;
    /** Scheduled attack event */
    protected ScheduledEvent currentAttackEvent;

    public AttackPatternComponent(float attackFrequency) {
        this.attackFrequency = attackFrequency;
    }

    /**
     * Initializes the HostileAttackPattern component when it is created.
     * This method sets up event listeners and interaction detection for the oxygen eater.
     */
    @Override
    public void create() {
        interactionDetector = entity.getComponent(InteractionDetector.class);
        interactionDetector.notifyOnDetection(true);

        entity.getEvents().addListener("entityDetected", this::startAttack);
        entity.getEvents().addListener("attack", this::attack);
    }

    /**
     * Initiates an attack loop.
     *
     * @param target The detected target entity. needed for event listener, unused
     */
    protected void startAttack(Entity target) {
        if (currentAttackEvent == null) { // attack loop not started
            attack();
        }
    }

    /**
     * Attacks and schedules next attack.
     */
    protected void attack() {
        // No action by default.

        currentAttackEvent = entity.getEvents().scheduleEvent(attackFrequency, "attack");
    }
}
