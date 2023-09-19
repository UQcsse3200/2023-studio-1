package com.csse3200.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.InteractionDetector;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.ScheduledEvent;
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

        currentAttackEvent = entity.getEvents().scheduleEvent(ATTACK_FREQUENCY, "attack");
    }
}
