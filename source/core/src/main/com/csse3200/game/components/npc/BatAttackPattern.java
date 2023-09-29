package com.csse3200.game.components.npc;


import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.InteractionDetector;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.ScheduledEvent;
import com.csse3200.game.utils.DirectionUtils;

/**
 * The OxygenEaterAttackPattern class defines the attack behavior of an oxygen eater NPC entity in the game.
 * It allows the oxygen eater to detect nearby entities and initiate attacks by shooting projectiles at them.
 */
public class BatAttackPattern extends Component {
    /** Frequency of attacks in seconds */
    private static final float ATTACK_FREQUENCY = 2f;
    /** Entity's interaction detector */
    private InteractionDetector interactionDetector;
    /** Scheduled attack event */
    private ScheduledEvent currentAttackEvent;

    /**
     * Initializes the BatAttackPattern component when it is created.
     * This method sets up event listeners and interaction detection for the bat.
     */
    @Override
    public void create() {
        interactionDetector = entity.getComponent(InteractionDetector.class);
        interactionDetector.notifyOnDetection(true);

        entity.getEvents().addListener("entityDetected", this::startAttack);
        entity.getEvents().addListener("attack", this::attack);
    }

    /**
     * Initiates an attack when an entity is detected by the oxygen eater.
     *
     * @param target The detected target entity. needed for event listener, unused
     */
    public void startAttack(Entity target) {
        if (currentAttackEvent == null) { // attack loop not started
            attack();
        }
    }

    /**
     * Performs the attack action, which involves determining the nearest entity, changing direction,
     * triggering attack events, and scheduling the next attack.
     */
    private void attack() {
        Entity nearestEntity = interactionDetector.getNearest(interactionDetector.getEntitiesInRange());

        if (nearestEntity == null) { // No entity detected, clear attack loop
            currentAttackEvent = null;
            return;
        }

        Vector2 nearestEntityPosition = nearestEntity.getCenterPosition();
        String attackDirection =
                entity.getCenterPosition().sub(nearestEntityPosition).x < 0 ? DirectionUtils.RIGHT : DirectionUtils.LEFT;

        // Trigger events for direction change and attack animation
        entity.getEvents().trigger("directionChange", attackDirection);
        entity.getEvents().trigger("attackStart");

        // Reduce the player's health by bat's damage
        nearestEntity.getComponent(CombatStatsComponent.class).hit(entity.getComponent(CombatStatsComponent.class));

        // TODO: Remove this degbugging only
        System.out.println(nearestEntity.getComponent(CombatStatsComponent.class).getHealth());

        // Schedule the next attack event
        currentAttackEvent = entity.getEvents().scheduleEvent(ATTACK_FREQUENCY, "attack");
    }

}
