package com.csse3200.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.InteractionDetector;
import com.csse3200.game.components.combat.ProjectileComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.events.ScheduledEvent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.DirectionUtils;

import java.util.Random;

/**
 * The HostileAttackPattern class defines the attack behavior of an oxygen eater NPC entity in the game.
 * It allows the oxygen eater to detect nearby entities and initiate attacks by shooting projectiles at them.
 */
public class HostileAttackPattern extends Component {
    /** Frequency of attacks in seconds */
    private final float attackFrequency;
    /** Entity's interaction detector */
    private InteractionDetector interactionDetector;
    /** Scheduled attack event */
    private ScheduledEvent currentAttackEvent;

    public HostileAttackPattern(float attackFrequency) {
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
        entity.getEvents().addListener("shoot", this::shoot);
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

        // Shoot projectile with delay after entity's attack animation
        if (entity.getType() == EntityType.OxygenEater) {
            entity.getEvents().scheduleEvent(0.2f, "shoot", nearestEntityPosition);
        } else if (entity.getType() == EntityType.Dragonfly) {
            entity.getEvents().scheduleEvent(0.2f, "shoot", nearestEntityPosition);
            entity.getEvents().scheduleEvent(0.3f, "shoot", nearestEntityPosition);
            entity.getEvents().scheduleEvent(0.4f, "shoot", nearestEntityPosition);
        }

        // Schedule the next attack event
        currentAttackEvent = entity.getEvents().scheduleEvent(attackFrequency, "attack");
    }

    /**
     * Creates and launches a projectile towards a specified position.
     *
     * @param position The position to which the projectile should be aimed.
     */
    private void shoot(Vector2 position) {
        Entity projectile;
        if (entity.getType() == EntityType.OxygenEater) {
            projectile = ProjectileFactory.createOxygenEaterProjectile();
        } else if (entity.getType() == EntityType.Dragonfly) {
            projectile = ProjectileFactory.createDragonflyProjectile();
            Random random = new Random();
            position.add(random.nextFloat() - 1f, random.nextFloat() -1f);

            float randomSpeed = 6f + 4f * random.nextFloat();
            projectile.getComponent(ProjectileComponent.class).setSpeed(new Vector2(randomSpeed, randomSpeed));
        } else {
            return;
        }

        projectile.setCenterPosition(entity.getCenterPosition());
        ServiceLocator.getGameArea().spawnEntity(projectile);

        ProjectileComponent projectileComponent = projectile.getComponent(ProjectileComponent.class);
        projectileComponent.setTargetDirection(position);
    }
}
