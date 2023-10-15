package com.csse3200.game.components.combat.attackpatterns;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.InteractionDetector;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.events.ScheduledEvent;

/**
 * The ShipEaterAttackPattern class defines the attack behavior of a ShipEater entity in the game.
 * It allows the ShipEater to detect the ship and reduce repair state when in range.
 *
 * Adapted from Team 4's AttackPatternComponent.
 */
public class ShipEaterAttackPattern extends Component {
    private InteractionDetector interactionDetector;
    private ScheduledEvent currentAttackEvent;
    private final float attackFrequency;
    private boolean isHiding;
    private boolean isAttacking;

    public ShipEaterAttackPattern(float attackFrequency) {
        this.attackFrequency = attackFrequency;
    }

    @Override
    public void create() {
        interactionDetector = entity.getComponent(InteractionDetector.class);
        interactionDetector.notifyOnDetection(true);

        entity.getEvents().addListener("entityDetected", (Entity e) -> setAttacking(e, true));
        entity.getEvents().addListener("entityExitDetected", (Entity e) -> setAttacking(e, false));
        entity.getEvents().addListener("hidingUpdated", this::setHiding);
    }

    private void setAttacking(Entity entity, boolean isAttacking) {
        if (entity.getType() != EntityType.SHIP) return;

        this.isAttacking = isAttacking;
        if (isAttacking) {
            startAttack();
        }
    }
    private void setHiding(boolean isHiding) {
        this.isHiding = isHiding;
    }

    /**
     * Initiates an attack loop.
     */
    protected void startAttack() {
        if (currentAttackEvent == null) { // attack loop not started
            attack();
        }
    }

    /**
     * Performs the attack action, which involves determining the nearest entity, changing direction,
     * triggering attack events, and scheduling the next attack.
     */
    protected void attack() {
        Entity shipEntity = interactionDetector.getEntitiesInRange()
                .stream().filter(entity -> entity.getType() == EntityType.SHIP)
                .findAny().orElse(null);

        if (shipEntity == null || isHiding) {
            // no ship detected or player is near, clear attack loop
            currentAttackEvent = null;
            entity.getEvents().trigger("eatingUpdated", false);
            return;
        }

        float distanceToShip = entity.getCenterPosition().dst(shipEntity.getCenterPosition());
        if (distanceToShip >= 2f) {
            // too far away, don't start eating
            currentAttackEvent = null;
            return;
        }

        entity.getEvents().trigger("eatingUpdated", true);

        // reduce the ship's repair state by 1
        shipEntity.getEvents().trigger("removePart", 1);

        currentAttackEvent = entity.getEvents().scheduleEvent(attackFrequency, "attack");
    }

    @Override
    public void update() {
        if (currentAttackEvent == null && isAttacking && !isHiding) {
            // stopped hiding, go back to attacking
            attack();
        }
    }

}
