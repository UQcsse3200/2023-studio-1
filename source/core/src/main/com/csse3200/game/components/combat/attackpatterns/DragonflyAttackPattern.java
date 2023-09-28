package com.csse3200.game.components.combat.attackpatterns;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.combat.ProjectileComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.DirectionUtils;

import java.util.Random;

public class DragonflyAttackPattern extends AttackPatternComponent {
    public DragonflyAttackPattern(float attackFrequency) {
        super(attackFrequency);
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("shoot", this::shoot);
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

        // Shoot projectiles with delay after entity's attack animation
        entity.getEvents().scheduleEvent(0.2f, "shoot", nearestEntityPosition);
        entity.getEvents().scheduleEvent(0.3f, "shoot", nearestEntityPosition);
        entity.getEvents().scheduleEvent(0.4f, "shoot", nearestEntityPosition);

        super.attack();
    }

    /**
     * Creates and launches a projectile towards a specified position.
     *
     * @param position The position to which the projectile should be aimed.
     */
    private void shoot(Vector2 position) {
        Entity projectile = ProjectileFactory.createDragonflyProjectile();
        Random random = new Random();
        position.add(random.nextFloat() - 1f, random.nextFloat() -1f);

        float randomSpeed = 6f + 4f * random.nextFloat();
        projectile.getComponent(ProjectileComponent.class).setSpeed(new Vector2(randomSpeed, randomSpeed));

        projectile.setCenterPosition(entity.getCenterPosition());
        ServiceLocator.getGameArea().spawnEntity(projectile);

        ProjectileComponent projectileComponent = projectile.getComponent(ProjectileComponent.class);
        projectileComponent.setTargetDirection(position);
    }
}
