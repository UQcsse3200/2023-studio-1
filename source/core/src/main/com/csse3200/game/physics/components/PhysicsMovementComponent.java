package com.csse3200.game.physics.components;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.ai.movement.MovementController;
import com.csse3200.game.components.Component;
import com.csse3200.game.utils.math.Vector2Utils;

/** Movement controller for a physics-based entity. */
public class PhysicsMovementComponent extends Component implements MovementController {
  private static final Logger logger = LoggerFactory.getLogger(PhysicsMovementComponent.class);
  private Vector2 maxSpeed = Vector2Utils.ONE;
  private PhysicsComponent physicsComponent;
  private Vector2 targetPosition;
  private boolean movementEnabled = true;
  private final GameMap gameMap = ServiceLocator.getGameArea().getMap();

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
  }

  @Override
  public void update() {
    if (movementEnabled && targetPosition != null) {
      Body body = physicsComponent.getBody();
      updateDirection(body);
    }
  }

  /**
   * Enable/disable movement for the controller. Disabling will immediately set velocity to 0.
   *
   * @param movementEnabled true to enable movement, false otherwise
   */
  @Override
  public void setMoving(boolean movementEnabled) {
    this.movementEnabled = movementEnabled;
    if (!movementEnabled) {
      Body body = physicsComponent.getBody();
      setToVelocity(body, Vector2.Zero);
    }
  }

  @Override
  public boolean getMoving() {
    return movementEnabled;
  }

  /** @return Target position in the world */
  @Override
  public Vector2 getTarget() {
    return targetPosition;
  }

  /**
   * Set a target to move towards. The entity will be steered towards it in a straight line, not
   * using pathfinding or avoiding other entities.
   *
   * @param target target position
   */
  @Override
  public void setTarget(Vector2 target) {
    logger.trace("Setting target to {}", target);
    this.targetPosition = target;
  }

  private void updateDirection(Body body) {
    Vector2 desiredVelocity = getDirection().scl(maxSpeed);

//    try {
//      Vector2 entityCenterPosVector = this.entity.getCenterPosition();
//      Vector2 entityBottomLeftPosVector = this.entity.getPosition();
//      Vector2 entityCentreBottomPosVector = new Vector2(entityCenterPosVector.x, entityBottomLeftPosVector.y);
//      float terrainSpeedModifier = gameMap.getTile(entityCentreBottomPosVector).getSpeedModifier();
//      desiredVelocity.scl(terrainSpeedModifier);
//    } catch (Exception e) {
//      logger.info("{entity} yadda yadda"); // Put entity in here
//    }

    Vector2 entityCenterPosVector = this.entity.getCenterPosition();
    Vector2 entityBottomLeftPosVector = this.entity.getPosition();
    Vector2 entityCentreBottomPosVector = new Vector2(entityCenterPosVector.x, entityBottomLeftPosVector.y);

    float terrainSpeedModifier = gameMap.getTile(entityCentreBottomPosVector).getSpeedModifier();
    desiredVelocity.scl(terrainSpeedModifier);

    setToVelocity(body, desiredVelocity);
  }

  private void setToVelocity(Body body, Vector2 desiredVelocity) {
    // impulse force = (desired velocity - current velocity) * mass
    Vector2 velocity = body.getLinearVelocity();
    Vector2 impulse = desiredVelocity.cpy().sub(velocity).scl(body.getMass());
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }

  private Vector2 getDirection() {
    // Move towards targetPosition based on our current position
    return targetPosition.cpy().sub(entity.getPosition()).nor();
  }

  public void setMaxSpeed(Vector2 newSpeed){
    //Changes the speed of the current entity
    this.maxSpeed = newSpeed;
  }
}
