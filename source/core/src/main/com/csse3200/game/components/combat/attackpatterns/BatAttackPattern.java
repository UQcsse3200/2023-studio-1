package com.csse3200.game.components.combat.attackpatterns;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.combat.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.sound.EffectSoundFile;
import com.csse3200.game.utils.DirectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The BatAttackPattern class defines the attack behavior of a Bat entity in the game.
 * It allows the bat to detect the player and attack them when in range.
 */
public class BatAttackPattern extends AttackPatternComponent {
    private final Logger logger = LoggerFactory.getLogger(BatAttackPattern.class);

    public BatAttackPattern(float attackFrequency) {
        super(attackFrequency);
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

        try {
            ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.BAT_ATTACK);
        } catch (Exception e) {
            logger.error("Failed to play bat attack player sound", e);
        }

        super.attack();
    }

}
