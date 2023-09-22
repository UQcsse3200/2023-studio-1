package com.csse3200.game.components.combat;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class ProjectileComponent extends Component {
    private Vector2 speed;
    private float duration;
    private boolean hasImpact;
    private PhysicsComponent physicsComponent;
    public ProjectileComponent(float duration) {
        this.duration = duration;
        speed = new Vector2(1f, 1f);
    }

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        physicsComponent.getBody().setBullet(true);
        entity.getEvents().addListener("destroyProjectile", this::destroyProjectile);
        entity.getEvents().scheduleEvent(duration, "destroyProjectile");
        entity.getEvents().addListener("impactStart", this::impact);
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

    private void destroyProjectile() {
//        entity.getEvents().trigger("toggleLight");
        entity.dispose();
    }

    private void impact() {
        physicsComponent.getBody().setLinearVelocity(0,  0);
        entity.getComponent(TouchAttackComponent.class).setEnabled(false);
        hasImpact = true;
//        physicsComponent.setEnabled(false); // i need a way to run this on next tick
//        entity.getEvents().scheduleEvent(0.3f, "stopProjectile");// TODO: this is so dodgy
    }

    @Override
    public void update() {
        if (hasImpact) {
            if (entity.getComponent(AnimationRenderComponent.class).isFinished()) {
                entity.getEvents().scheduleEvent(0f, "destroyProjectile");
            }
        }
    }
}
