package com.csse3200.game.components.combat.attackpatterns;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.combat.ProjectileComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.sound.EffectSoundFile;
import com.csse3200.game.utils.DirectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

/**
 * The OxygenEater attack pattern class defines the attack behaviour of an OxygenEater entity. It
 * allows the OxygenEater to detect the player and attack them when in range.
 */
public class OxygenEaterAttackPattern extends AttackPatternComponent {
    private final Logger logger = LoggerFactory.getLogger(OxygenEaterAttackPattern.class);

    /** Supplies the projectile to be shot by the oxygen eater. */
    private final Supplier<Entity> projectileSupplier;

    /**
     * @param attackFrequency How often the OxygenEater attacks.
     * @param projectileSupplier The projectile supplier for the OxygenEater's projectile.
     */
    public OxygenEaterAttackPattern(float attackFrequency, Supplier<Entity> projectileSupplier) {
        super(attackFrequency);
        this.projectileSupplier = projectileSupplier;
    }

    /**
     * Initialises the OxygenEaterAttackPattern component when it is created.
     * This method sets up event listeners and interaction detection for the OxygenEater.
     */
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("shoot", this::shoot);
    }

    /**
     * Performs the attack action which involves detecting the player entity if one exists and
     * shooting projectiles at them.
     */
    @Override
    protected void attack() {
        Entity nearestEntity = interactionDetector.getNearest(interactionDetector.getEntitiesInRange());
        entity.getEvents().trigger("startEffect", "attack");

        if (nearestEntity == null) { // No entity detected, clear attack loop
            currentAttackEvent = null;
            entity.getEvents().trigger("stopEffect", "attack");
            return;
        }

        Vector2 nearestEntityPosition = nearestEntity.getCenterPosition();
        String attackDirection =
                entity.getCenterPosition().sub(nearestEntityPosition).x < 0 ? DirectionUtils.RIGHT : DirectionUtils.LEFT;

        // Trigger events for direction change and attack animation
        entity.getEvents().trigger("directionChange", attackDirection);
        entity.getEvents().trigger("attackStart");

        // Shoot projectile with delay after entity's attack animation
        entity.getEvents().scheduleEvent(0.2f, "shoot", nearestEntityPosition);

        super.attack();
    }

    /**
     * Creates and launches a projectile towards a specified position.
     *
     * @param position The position to which the projectile should be aimed.
     */
    private void shoot(Vector2 position) {
        try {
            ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.OXYGEN_ATTACK);
        } catch (Exception e) {
            logger.error("Failed to play Oxygen Eater attack player sound", e);
        }
        Entity projectile = projectileSupplier.get();

        projectile.setCenterPosition(entity.getCenterPosition());
        ServiceLocator.getGameArea().spawnEntity(projectile);

        ProjectileComponent projectileComponent = projectile.getComponent(ProjectileComponent.class);
        projectileComponent.setTargetDirection(position);
    }
}
