package com.csse3200.game.physics.components;

import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.components.combat.StunComponent;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.ai.movement.MovementController;
import com.csse3200.game.components.Component;
import com.csse3200.game.utils.math.Vector2Utils;

import java.util.HashSet;
import java.util.Set;

/** Movement controller for a physics-based entity. */
public class PhysicsMovementComponent extends Component implements MovementController {
    private static final Logger logger = LoggerFactory.getLogger(PhysicsMovementComponent.class);
    private Vector2 maxSpeed = Vector2Utils.ONE;
    private PhysicsComponent physicsComponent;
    private Vector2 targetPosition;
    private boolean movementEnabled = true;
    private final GameMap gameMap = ServiceLocator.getGameArea().getMap();

    private static final Set<EntityType> flyingEntitiesHashSet = new HashSet<>(
          Set.of(
                  EntityType.OXYGEN_EATER,
                  EntityType.DRAGONFLY,
                  EntityType.BAT,
                  EntityType.FIRE_FLIES
          )
    );

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
    }

    @Override
    public void update() {
        if (isStunned()) {
          return;
        }   

        if (movementEnabled && targetPosition != null) {
            updateSpeed();
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

    /**
     * @return Target position in the world
     */
    @Override
    public Vector2 getTarget() {
        return targetPosition;
    }

    @Override
    public boolean getMoving() {
        return movementEnabled;
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

    private void updateSpeed() {
        Body body = physicsComponent.getBody();
        Vector2 velocity = body.getLinearVelocity();

        Vector2 desiredVelocity = calculateVelocityVector();
        Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());

        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }

    public Vector2 calculateVelocityVector() {
        Vector2 desiredVelocity = getDirection().scl(maxSpeed);

        if (!flyingEntitiesHashSet.contains(entity.getType())) {
            Vector2 entityCenterPosVector = this.entity.getCenterPosition();
            Vector2 entityBottomLeftPosVector = this.entity.getPosition();
            Vector2 entityCenterBottomPosVector = new Vector2(entityCenterPosVector.x, entityBottomLeftPosVector.y);

          float terrainSpeedModifier = gameMap.getTile(entityCenterBottomPosVector).getSpeedModifier();
          desiredVelocity.scl(terrainSpeedModifier);
        }

        return desiredVelocity;
    }

    private void setToVelocity(Body body, Vector2 desiredVelocity) {
        // impulse force = (desired velocity - current velocity) * mass
        Vector2 velocity = body.getLinearVelocity();
        Vector2 impulse = desiredVelocity.cpy().sub(velocity).scl(body.getMass());
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }

  public void setMaxSpeed(Vector2 newSpeed) {
    //Changes the speed of the current entity
    this.maxSpeed = newSpeed;
  }

  public boolean isStunned() {
    StunComponent stunComponent = entity.getComponent(StunComponent.class);
    if (stunComponent == null) {
      return false;
    }

    return stunComponent.isStunned();
  }

    private Vector2 getDirection() {
        // Move towards targetPosition based on our current position
        return targetPosition.cpy().sub(entity.getPosition()).nor();
    }
}