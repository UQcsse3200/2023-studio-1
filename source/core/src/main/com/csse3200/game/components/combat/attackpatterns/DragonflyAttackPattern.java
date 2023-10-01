package com.csse3200.game.components.combat.attackpatterns;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.combat.ProjectileComponent;
import com.csse3200.game.components.plants.PlantComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.DirectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class DragonflyAttackPattern extends AttackPatternComponent {
    private final Supplier<Entity> projectileSupplier;

    public DragonflyAttackPattern(float attackFrequency, Supplier<Entity> projectileSupplier) {
        super(attackFrequency);
        this.projectileSupplier = projectileSupplier;
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

    private void attackPlant(Entity plant) {
        Vector2 nearestEntityPosition = plant.getCenterPosition();
        String attackDirection =
                entity.getCenterPosition().sub(nearestEntityPosition).x < 0 ? DirectionUtils.RIGHT : DirectionUtils.LEFT;

        // Trigger events for direction change and attack animation
        entity.getEvents().trigger("directionChange", attackDirection);
        entity.getEvents().trigger("attackStart");

        // Attack the plant
        plant.getEvents().trigger("attack");

        // Todo: Remove this debugging only
        System.out.println(plant.getComponent(PlantComponent.class).getPlantHealth());
        System.out.println(plant.getComponent(PlantComponent.class).getPlantName());


    }
}
