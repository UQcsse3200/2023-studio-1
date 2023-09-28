package com.csse3200.game.components.combat.attackpatterns;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.combat.ProjectileComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.DirectionUtils;

import java.util.Random;

public class OxygenEaterAttackPattern extends AttackPatternComponent {
    public OxygenEaterAttackPattern(float attackFrequency) {
        super(attackFrequency);
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("shoot", this::shoot);
    }

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
        Entity projectile = ProjectileFactory.createOxygenEaterProjectile();

        projectile.setCenterPosition(entity.getCenterPosition());
        ServiceLocator.getGameArea().spawnEntity(projectile);

        ProjectileComponent projectileComponent = projectile.getComponent(ProjectileComponent.class);
        projectileComponent.setTargetDirection(position);
    }
}
