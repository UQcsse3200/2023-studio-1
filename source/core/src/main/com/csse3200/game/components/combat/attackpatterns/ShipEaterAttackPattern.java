package com.csse3200.game.components.combat.attackpatterns;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.npc.ShipEaterScareComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.utils.DirectionUtils;

/**
 * The ShipEaterAttackPattern class defines the attack behavior of a ShipEater entity in the game.
 * It allows the ShipEater to detect the ship and reduce repair state when in range.
 *
 * Adapted from Team 4's BatAttackPattern.
 */
public class ShipEaterAttackPattern extends AttackPatternComponent {

    public ShipEaterAttackPattern(float attackFrequency) {
        super(attackFrequency);
    }

    /**
     * Initiates an attack loop.
     *
     * @param target The detected target entity. needed for event listener, unused
     */
    @Override
    protected void startAttack(Entity target) {
        if (currentAttackEvent == null) { // attack loop not started
            attack();
        }
    }

    /**
     * Performs the attack action, which involves determining the nearest entity, changing direction,
     * triggering attack events, and scheduling the next attack.
     */
    @Override
    protected void attack() {
        Entity shipEntity = interactionDetector.getEntitiesInRange()
                .stream().filter(entity -> entity.getType() == EntityType.SHIP)
                .findAny().orElse(null);

        if (shipEntity == null || entity.getComponent(ShipEaterScareComponent.class).getIsHiding()) { // No ship detected or player is near, clear attack loop
            currentAttackEvent = null;
            return;
        }

        Vector2 nearestEntityPosition = shipEntity.getCenterPosition();
        String attackDirection =
                entity.getCenterPosition().sub(nearestEntityPosition).x < 0 ? DirectionUtils.RIGHT : DirectionUtils.LEFT;

        // Trigger events for direction change and attack animation
        entity.getEvents().trigger("directionChange", attackDirection);
        entity.getEvents().trigger("attackStart");

        // Reduce the ship's repair state by 1
        shipEntity.getEvents().trigger("removePart", 1);

        super.attack();
    }

}
