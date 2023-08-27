package com.csse3200.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.items.ItemActions;
import com.csse3200.game.components.tractor.KeyboardTractorInputComponent;
import com.csse3200.game.components.tractor.TractorActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {
  private static final Vector2 MAX_WALK_SPEED = new Vector2(3f, 3f); // Metres per second
  private static final Vector2 MAX_RUN_SPEED = new Vector2(5f, 5f); // Metres per second
  private float prevMoveDirection = 300; // Initialize it with a default value
  private Entity tractor;

  private PhysicsComponent physicsComponent;
  private Vector2 moveDirection = Vector2.Zero.cpy();
  private boolean moving = false;
  private boolean running = false;
  private boolean muted = false;
  private CameraComponent camera;
  private GameMap map;

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    entity.getEvents().addListener("move", this::move);
    entity.getEvents().addListener("moveStop", this::stopMoving);
    entity.getEvents().addListener("run", this::run);
    entity.getEvents().addListener("runStop", this::stopRunning);
    entity.getEvents().addListener("attack", this::attack);
    entity.getEvents().addListener("enterTractor", this::enterTractor);
    entity.getEvents().addListener("use", this::use);
  }

  @Override
  public void update() {
    if (moving) {
      updateSpeed();

    }
    updateAnimation();
  }

  /**
   * Plays the correct animation for the type of player movement.
   */
  private void updateAnimation() {

    if (moveDirection.epsilonEquals(Vector2.Zero)) {
      // player is not moving

      String animationName = "animationWalkStop";
      float direction = getPrevMoveDirection();
      if (direction < 45) {
        entity.getEvents().trigger(animationName, "right");
      } else if (direction < 135) {
        entity.getEvents().trigger(animationName, "up");
      } else if (direction < 225) {
        entity.getEvents().trigger(animationName, "left");
      } else if (direction < 315) {
        entity.getEvents().trigger(animationName, "down");
      }
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

  public float getPrevMoveDirection() {
    return prevMoveDirection;
  }
  /**
   * Moves the player towards a given direction.
   *
   * @param direction direction to move in
   */
  void move(Vector2 direction) {
    // Store the previous move direction
    this.moveDirection = direction;
    this.prevMoveDirection = moveDirection.angleDeg();
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
<<<<<<< HEAD
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

  /**
   * Sets tractor to the tractor entity, can be used to calculate distances and mute inputs
   * @param tractor
   */
  public void setTractor(Entity tractor) {
    this.tractor = tractor;
  }

  /**
   * Makes the player get into tractor.
   */
  void enterTractor() {
    //check within 4 units of tractor
    if (this.entity.getPosition().dst(tractor.getPosition()) > 4) {
      return;
    }
    this.stopMoving();
    muted = true;
    tractor.getComponent(TractorActions.class).setMuted(false);
    tractor.getComponent(KeyboardTractorInputComponent.class).setWalkDirection(entity.getComponent(KeyboardPlayerInputComponent.class).getWalkDirection());
    this.entity.setPosition(new Vector2(-10,-10));
    camera.setTrackEntity(tractor);
  }

  void use(Vector2 playerPos, Vector2 mousePos, Entity itemInHand) {
    itemInHand.getComponent(ItemActions.class).use(playerPos, mousePos, itemInHand, map);
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

  public void setCameraVar (CameraComponent cam) {
    this.camera = cam;
  }

  public void setGameMap(GameMap map) {
    this.map = map;
  }
}
