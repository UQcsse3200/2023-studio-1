package com.csse3200.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.Component;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {
  private static final Vector2 MAX_WALK_SPEED = new Vector2(3f, 3f); // Metres per second
  private static final Vector2 MAX_RUN_SPEED = new Vector2(5f, 5f); // Metres per second

  private PhysicsComponent physicsComponent;
  private Vector2 moveDirection = Vector2.Zero.cpy();
  private boolean moving = false;
  private boolean running = false;

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    entity.getEvents().addListener("move", this::move);
    entity.getEvents().addListener("moveStop", this::stopMoving);
    entity.getEvents().addListener("run", this::run);
    entity.getEvents().addListener("runStop", this::stopRunning);
    entity.getEvents().addListener("attack", this::attack);
  }

  @Override
  public void update() {
    if (moving) {
      updateSpeed();
      updateAnimation();
    }
  }

  /**
   * Plays the correct animation for the type of player movement.
   */
  private void updateAnimation() {
    if (moveDirection.epsilonEquals(Vector2.Zero)) {
      // player is not moving
      entity.getEvents().trigger("animationWalkStop");
      return;
    }

    // player is moving
    String animationName = String.format("animation%sStart", running ? "Run" : "Walk");
    float direction = moveDirection.angleDeg();

    if (direction < 45) {
      entity.getEvents().trigger(animationName, "right");
    } else if (direction < 135) {
      entity.getEvents().trigger(animationName, "up");
    } else if (direction < 225) {
      entity.getEvents().trigger(animationName, "left");
    } else if (direction < 315) {
      entity.getEvents().trigger(animationName, "down");
    }
  }

  private void updateSpeed() {
    Body body = physicsComponent.getBody();
    Vector2 velocity = body.getLinearVelocity();
    Vector2 velocityScale = this.running ? MAX_RUN_SPEED : MAX_WALK_SPEED;
    Vector2 desiredVelocity = moveDirection.cpy().scl(velocityScale);
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
    this.moveDirection = direction;
    moving = true;
  }

  /**
   * Stops the player from moving.
   */
  void stopMoving() {
    this.moveDirection = Vector2.Zero.cpy();
    updateSpeed();
    moving = false;
  }

  /**
   * Increases the velocity of the player when they move.
   */
  void run() {
    this.running = true;
  }

  /**
   * Removes the velocity increase of the player.
   */
  void stopRunning() {
    this.running = false;
  }

  /**
   * Makes the player attack.
   */
  void attack() {
    Sound attackSound = ServiceLocator.getResourceService().getAsset("sounds/Impact4.ogg", Sound.class);
    attackSound.play();
  }
}
