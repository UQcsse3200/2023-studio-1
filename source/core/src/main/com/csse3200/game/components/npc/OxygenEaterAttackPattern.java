package com.csse3200.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.AuraLightComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.InteractionDetector;
import com.csse3200.game.components.combat.ProjectileComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.events.ScheduledEvent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.DirectionUtils;
import net.dermetfan.gdx.physics.box2d.PositionController;

import java.awt.*;
import java.util.List;
import java.util.Vector;

public class OxygenEaterAttackPattern extends Component {
    private static final float ATTACK_FREQUENCY = 1.5f;
    private InteractionDetector interactionDetector;
    private ScheduledEvent currentAttackEvent;

    @Override
    public void create() {
        interactionDetector = entity.getComponent(InteractionDetector.class);
        interactionDetector.notifyOnDetection(true);

        entity.getEvents().addListener("entityDetected", this::startAttack);
        entity.getEvents().addListener("attack", this::attack);
        entity.getEvents().addListener("shoot", this::shoot);
    }

    public void startAttack(Entity target) {
        if (currentAttackEvent == null) { // attack loop not started
            attack();
        }
    }

    private void attack() {
        Entity nearestEntity = interactionDetector.getNearest(interactionDetector.getEntitiesInRange());

        if (nearestEntity == null) { // No entity detected, clear attack loop
            currentAttackEvent = null;
            return;
        }

        Vector2 nearestEntityPosition = nearestEntity.getCenterPosition();
        String attackDirection =
                entity.getCenterPosition().sub(nearestEntityPosition).x < 0 ? DirectionUtils.RIGHT : DirectionUtils.LEFT;

        entity.getEvents().trigger("directionChange", attackDirection);
        entity.getEvents().trigger("attackStart");
        entity.getEvents().scheduleEvent(0.2f, "shoot", nearestEntityPosition);

        currentAttackEvent = entity.getEvents().scheduleEvent(ATTACK_FREQUENCY, "attack");
    }

    private void shoot(Vector2 position) {
        Entity projectile = ProjectileFactory.createOxygenEaterProjectile();
        projectile.setCenterPosition(entity.getCenterPosition());

        ServiceLocator.getGameArea().spawnEntity(projectile);

        ProjectileComponent projectileComponent = projectile.getComponent(ProjectileComponent.class);
        projectileComponent.setSpeed(new Vector2(3f, 3f));
        projectileComponent.setTargetDirection(position);
    }
}
