package com.csse3200.game.components.npc;


import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.InteractionDetector;
import com.csse3200.game.components.combat.attackpatterns.AttackPatternComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.ScheduledEvent;
import com.csse3200.game.utils.DirectionUtils;

/**
 * The OxygenEaterAttackPattern class defines the attack behavior of an oxygen eater NPC entity in the game.
 * It allows the oxygen eater to detect nearby entities and initiate attacks by shooting projectiles at them.
 */
public class BatAttackPattern extends AttackPatternComponent {

    public BatAttackPattern(float attackFrequency) {
        super(attackFrequency);
    }

    /**
     * Initializes the BatAttackPattern component when it is created.
     * This method sets up event listeners and interaction detection for the bat.
     */
    @Override
    public void create() {
        super.create();
    }


    /**
     * Performs the attack action, which involves determining the nearest entity, changing direction,
     * triggering attack events, and scheduling the next attack.
     */
    @Override
    protected void attack() {
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

        super.attack();
    }

}
