package com.csse3200.game.components.combat.attackpatterns;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.combat.ProjectileComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.DirectionUtils;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * The Dragonfly attack pattern class defines the attack behavior of a Dragonfly entity in the game.
 * It allows the dragonfly to detect the player and plants and attack them when in range.
 */
public class DragonflyAttackPattern extends AttackPatternComponent {
    /** Supplies the projectile to be shot by the dragonfly. */
    private final Supplier<Entity> projectileSupplier;

    /**
     * @param attackFrequency How often the dragonfly attacks.
     * @param projectileSupplier The projectile supplier for the Dragonfly's projectiles.
     */
    public DragonflyAttackPattern(float attackFrequency, Supplier<Entity> projectileSupplier) {
        super(attackFrequency);
        this.projectileSupplier = projectileSupplier;
    }

    /**
     * Initialises the DragonFlyAttackPattern component when it is created.
     * This method sets up event listeners and interaction detection for the Dragonfly.
     */
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("shoot", this::shoot);
    }

    /**
     * Performs the attack action, which involves determining the nearest entity, changing direction,
     * triggering attack events, and scheduling the next attack. This will also change the attack
     * performed depending on whether there is a plant or player.
     */
    @Override
    protected void attack() {

       List<Entity> entitiesInRange = interactionDetector.getEntitiesInRange();

       if (entitiesInRange.size() == 0) { // No entity detected, clear attack loop
           currentAttackEvent = null;
           return;
       }

        for (int i = 0; i < entitiesInRange.size(); i++) {
            if (entitiesInRange.get(i).getType() == EntityType.Player) {
                attackPlayer(entitiesInRange.get(i));
                super.attack();
                return;
            }
        }

        Entity nearestEntity = interactionDetector.getNearest(interactionDetector.getEntitiesInRange());

        if (nearestEntity.getType() == EntityType.Plant) {
            // Plant detected, check if close enough to attack
            if (entity.getPosition().dst(nearestEntity.getPosition().x,
                    nearestEntity.getPosition().y) < 1f) {
                    attackPlant(nearestEntity);
                    super.attack();
                    return;
            }
        }
        super.attack();
    }

    /**
     * Creates and launches a projectile towards a specified position.
     *
     * @param position The position to which the projectile should be aimed.
     */
    private void shoot(Vector2 position) {
        Entity projectile = projectileSupplier.get();
        Random random = new Random();
        position.add(random.nextFloat() - 1f, random.nextFloat() -1f);

        float randomSpeed = 6f + 4f * random.nextFloat();
        projectile.getComponent(ProjectileComponent.class).setSpeed(new Vector2(randomSpeed, randomSpeed));

        projectile.setCenterPosition(entity.getCenterPosition());
        ServiceLocator.getGameArea().spawnEntity(projectile);

        ProjectileComponent projectileComponent = projectile.getComponent(ProjectileComponent.class);
        projectileComponent.setTargetDirection(position);
    }

    /**
     * Attacks the player.
     *
     * @param player the player entity to attack.
     */
    private void attackPlayer(Entity player) {
        Vector2 nearestEntityPosition = player.getCenterPosition();
        String attackDirection =
                entity.getCenterPosition().sub(nearestEntityPosition).x < 0 ? DirectionUtils.RIGHT : DirectionUtils.LEFT;

        // Trigger events for direction change and attack animation
        entity.getEvents().trigger("directionChange", attackDirection);
        entity.getEvents().trigger("attackStart");

        // Shoot projectiles with delay after entity's attack animation
        entity.getEvents().scheduleEvent(0.2f, "shoot", nearestEntityPosition);
        entity.getEvents().scheduleEvent(0.3f, "shoot", nearestEntityPosition);
        entity.getEvents().scheduleEvent(0.4f, "shoot", nearestEntityPosition);
    }

    /**
     * Attacks a specific plant.
     *
     * @param plant the plant to attack.
     */
    private void attackPlant(Entity plant) {
        Vector2 nearestEntityPosition = plant.getCenterPosition();
        String attackDirection =
                entity.getCenterPosition().sub(nearestEntityPosition).x < 0 ? DirectionUtils.RIGHT : DirectionUtils.LEFT;

        // Trigger events for direction change and attack animation
        entity.getEvents().trigger("directionChange", attackDirection);
        entity.getEvents().trigger("attackStart");

        // Attack the plant
        plant.getEvents().trigger("attack");

    }
}
