package com.csse3200.game.components.tractor;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;

public class TractorActions extends Component {
    private static final Vector2 MAX_SPEED = new Vector2(5f, 5f); // Metres per second
    private Entity player;

    private PhysicsComponent physicsComponent;
    private Vector2 walkDirection = Vector2.Zero.cpy();
    private boolean moving = false;
    private boolean muted = true;
    private CameraComponent camera;

    @Override
    /**
     * Creates the tractor actions component.
     */
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        entity.getEvents().addListener("move", this::move);
        entity.getEvents().addListener("moveStop", this::stopMoving);
        entity.getEvents().addListener("exitTractor", this::exitTractor);
    }

    @Override
    /**
     * Updates the tractor actions component.
     */
    public void update() {
        if (this.moving) {
            updateSpeed();
        }
    }

    /**
     * Updates the speed of the tractor.
     */
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
        this.moving = true;
    }

    /**
     * Stops the player from walking.
     */
    void stopMoving() {
        this.walkDirection = Vector2.Zero.cpy();
        updateSpeed();
        this.moving = false;
    }

    /**
     * Checks if the player/tractor is moving.
     * @return true if moving else false
     */
    public boolean isMoving() {
        return this.moving;
    }

    public void setPlayer(Entity player) {
        this.player = player;
        player.getComponent(PlayerActions.class).setTractor(this.entity);
    }

    /**
     * Makes the player get into tractor.
     */
    void exitTractor() {
      this.stopMoving();
      player.getComponent(PlayerActions.class).setMuted(false);
      muted = true;
      player.getComponent(KeyboardPlayerInputComponent.class).setWalkDirection(entity.getComponent(KeyboardTractorInputComponent.class).getWalkDirection());
      player.setPosition(this.entity.getPosition());
      camera.setTrackEntity(player);
    }

    /**
     * When in the tractor inputs should be muted, this handles that.
     * @return if the players inputs should be muted
     */
    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    /**
     * Gets the location of the tractor
     * @return the location of the tractor as Vector2
     */
    public Vector2 getTractorLocation() {
        return entity.getPosition();
    }

    /**
     * ONLY USE FOR TESTING PURPOSES
     * @param comp
     */
    public void setPhysicsComponent(PhysicsComponent comp) {
        this.physicsComponent = comp;
    }

    public void setCameraVar (CameraComponent cam) {
        this.camera = cam;
    }
}
