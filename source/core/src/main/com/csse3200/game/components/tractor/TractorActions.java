package com.csse3200.game.components.tractor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.Component;
import com.csse3200.game.physics.components.PhysicsComponent;

public class TractorActions extends Component {
    private static final Vector2 MAX_SPEED = new Vector2(5f, 5f); // Metres per second

    private PhysicsComponent physicsComponent;
    private Vector2 walkDirection = Vector2.Zero.cpy();
    private boolean moving = false;

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        entity.getEvents().addListener("move", this::move);
        entity.getEvents().addListener("moveStop", this::stopMoving);
    }

    @Override
    public void update() {
        if (moving) {
            updateSpeed();
        }
    }

    private void updateSpeed() {
        Body body = physicsComponent.getBody();
        Vector2 velocity = body.getLinearVelocity();
        Vector2 desiredVelocity = walkDirection.cpy().scl(MAX_SPEED);
        // impulse = (desiredVel - currentVel) * mass
        Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }

    /**
     * Moves the player towards a given direction.
     *
     * @param direction direction to move in
     */
    void move(Vector2 direction) {
        this.walkDirection = direction;
        moving = true;
    }

    /**
     * Stops the player from walking.
     */
    void stopMoving() {
        this.walkDirection = Vector2.Zero.cpy();
        updateSpeed();
        moving = false;
    }
}
