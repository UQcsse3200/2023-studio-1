package com.csse3200.game.components.combat;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.Component;
import com.csse3200.game.physics.components.PhysicsComponent;

public class ProjectileComponent extends Component {
    private Vector2 speed;
    private float duration;
    private PhysicsComponent physicsComponent;
    public ProjectileComponent(float duration) {
        this.duration = duration;
        speed = new Vector2(1f, 1f);
    }

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        entity.getEvents().addListener("durationFinished", this::stopProjectile);
        entity.getEvents().scheduleEvent(duration, "durationFinished");
    }

    public void setTargetDirection(Vector2 target) {
        Vector2 directionVector = target.cpy().sub(entity.getCenterPosition()).nor();
        Vector2 velocity = directionVector.scl(speed);
        Body body = physicsComponent.getBody();
        body.setAngularDamping(0f);
        body.setLinearDamping(0f);
        body.applyLinearImpulse(velocity, body.getWorldCenter(), true);
    }

    public void setSpeed(Vector2 speed) {
        this.speed = speed;
    }

    private void stopProjectile() {
        entity.dispose();
    }
}
