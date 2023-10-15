package com.csse3200.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Null;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.*;
import com.csse3200.game.components.combat.CombatStatsComponent;
import com.csse3200.game.components.combat.ProjectileComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.InteractionDetector;
import com.csse3200.game.components.items.ItemActions;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.components.tractor.KeyboardTractorInputComponent;
import com.csse3200.game.components.combat.StunComponent;
import com.csse3200.game.components.tractor.TractorActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ParticleService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.sound.EffectSoundFile;
import com.csse3200.game.services.sound.InvalidSoundFileException;
import com.csse3200.game.utils.math.Vector2Utils;

import java.security.SecureRandom;
import java.util.List;

/**
 * Action component for interacting with the player. Player events should be
 * initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {
  private static final Vector2 MAX_WALK_SPEED = new Vector2(3f, 3f); // Metres per second
  private static final Vector2 MAX_RUN_SPEED = new Vector2(5f, 5f); // Metres per second
  private float prevMoveDirection = 300; // Initialize it with a default value

  private PhysicsComponent physicsComponent;
  private Vector2 moveDirection = Vector2.Zero.cpy();
  private boolean moving = false;
  private boolean running = false;
  private boolean muted = false;
  private boolean frozen = false;
  private GameMap gameMap = ServiceLocator.getGameArea().getMap();
  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PlayerActions.class);
  private SecureRandom random = new SecureRandom();
  private float speedMultiplier = 1f;
  private float damageMultiplier = 1f;
  int swordDamage = 5;

  private float weatherSpeedModifier = 1.0f;
  private boolean isWeatherAffectingSpeed = false;

  private static final String RIGHT_STRING = "right";
  enum Direction {
    RIGHT("right"),
    LEFT("left"),
    UP("up"),
    DOWN("down");

    final String representation;
    Direction(String representation) {this.representation = representation;}
  }

  public enum events {
    FREEZE,
    UNFREEZE,
    MOVE,
    MOVE_STOP,
    RUN,
    RUN_STOP,
    ATTACK,
    SHOOT,
    ENTER_TRACTOR,
    EXIT_TRACTOR,
    USE,
    EAT,
    FISH_CAUGHT,
    ESC_INPUT,
    CAST_FISHING_RODS
  }

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    entity.getEvents().addListener(events.MOVE.name(), this::move);
    entity.getEvents().addListener(events.MOVE_STOP.name(), this::stopMoving);
    entity.getEvents().addListener(events.RUN.name(), this::run);
    entity.getEvents().addListener(events.RUN_STOP.name(), this::stopRunning);
    entity.getEvents().addListener("interact", this::interact);
    entity.getEvents().addListener(events.ATTACK.name(), this::attack);
    entity.getEvents().addListener(events.SHOOT.name(), this::shoot);
    entity.getEvents().addListener(events.ENTER_TRACTOR.name(), this::enterTractor);
    entity.getEvents().addListener(events.USE.name(), this::use);
    entity.getEvents().addListener("hotkeySelection", this::hotkeySelection);
	entity.getEvents().addListener(events.EAT.name(), this::eat);
	entity.getEvents().addListener(events.FREEZE.name(), this::freeze);
	entity.getEvents().addListener(events.UNFREEZE.name(), this::unfreeze);
    entity.getEvents().addListener("setSpeedMultiplier", this::setSpeedMultiplier);
    entity.getEvents().addListener("setDamageMultiplier", this::setDamageMultiplier);
    ServiceLocator.getGameArea().getClimateController().getEvents().addListener(
            "startPlayerMovementSpeedEffect", this::startPlayerMovementSpeedEffect);
    ServiceLocator.getGameArea().getClimateController().getEvents().addListener(
            "stopPlayerMovementSpeedEffect", this::stopPlayerMovementSpeedEffect);
  }

  @Override
  public void update() {
    if (entity.getComponent(PlayerAnimationController.class).readyToPlay()) {
      if (moving && !isStunned() && !frozen) {
        updateSpeed();
      }
      updateAnimation();
    }
  }

  /**
   * Freeze the player (i.e. render them unable to move)
   */
  private void freeze() {
    frozen = true;
  }

  /**
   * Unfreeze the player (allow them to move)
   */
  private void unfreeze() {
    frozen = false;
  }

  /**
   * Plays the correct animation for the type of player movement.
   */
  private void updateAnimation() {

    int max = 300;
    int min = 1;

    int animationRandomizer = min + this.random.nextInt(max);

    if (moveDirection.epsilonEquals(Vector2.Zero)) {
      // player is not moving

      String animationName = PlayerAnimationController.events.ANIMATION_WALK_STOP.name();
      float direction = getPrevMoveDirection();
      if (direction < 45) {
        entity.getEvents().trigger(animationName, Direction.RIGHT.representation, animationRandomizer, false);
      } else if (direction < 135) {
        entity.getEvents().trigger(animationName, Direction.UP.representation, animationRandomizer, false);
      } else if (direction < 225) {
        entity.getEvents().trigger(animationName, Direction.LEFT.representation, animationRandomizer, false);
      } else if (direction < 315) {
        entity.getEvents().trigger(animationName, Direction.LEFT.representation, animationRandomizer, false);
      }
      return;
    }

    // player is moving
    String animationName = running ?
            PlayerAnimationController.events.ANIMATION_RUN_START.name() :
            PlayerAnimationController.events.ANIMATION_WALK_START.name();
    float direction = moveDirection.angleDeg();
    if (direction < 45) {
      entity.getEvents().trigger(animationName, Direction.RIGHT.representation);
    } else if (direction < 135) {
      entity.getEvents().trigger(animationName, Direction.UP.representation);
    } else if (direction < 225) {
      entity.getEvents().trigger(animationName, Direction.LEFT.representation);
    } else if (direction < 315) {
      entity.getEvents().trigger(animationName, Direction.DOWN.representation);
    }
  }

  private void updateSpeed() {
    Body body = physicsComponent.getBody();
    Vector2 velocity = body.getLinearVelocity();
    Vector2 velocityScale = this.running ? MAX_RUN_SPEED.cpy() : MAX_WALK_SPEED.cpy();

    // Used to apply the terrainSpeedModifier
    Vector2 playerVector = this.entity.getCenterPosition(); // Centre position is better indicator of player location
    playerVector.add(0, -1.0f); // Player entity sprite's feet are located -1.0f below the centre of the entity
    TerrainTile terrainTile = gameMap.getTile(playerVector);
    if (terrainTile != null) {
      // Null check implemented for when the player Entity is moved out of bounds (Tractor spawning with terminal)
      float terrainSpeedModifier = gameMap.getTile(playerVector).getSpeedModifier();
      velocityScale.scl(terrainSpeedModifier);
    }

    if (isWeatherAffectingSpeed) {
      velocityScale.scl(weatherSpeedModifier);
    }

    velocityScale.scl(speedMultiplier);

    Vector2 desiredVelocity = moveDirection.cpy().scl(velocityScale);
    // impulse = (desiredVel - currentVel) * mass
    Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());

    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);

    if (impulse.len() > 4) {
      entity.getEvents().trigger("startEffect", ParticleService.ParticleEffectType.DIRT_EFFECT);
    }
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
  public void stopMoving() {
    this.moveDirection = Vector2.Zero.cpy();
    updateSpeed();
    moving = false;
  }

  void pauseMoving() {
    Vector2 tmp = this.moveDirection;
    this.moveDirection = Vector2.Zero.cpy();
    updateSpeed();
    moveDirection.add(tmp);
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
  public void stopRunning() {
    this.running = false;
  }

  void interact() {
    String animationInteract = PlayerAnimationController.events.ANIMATION_INTERACT.name();
    float direction = getPrevMoveDirection();
    if (direction < 45) {
      entity.getEvents().trigger(animationInteract, Direction.RIGHT.representation);
    } else if (direction < 135) {
      entity.getEvents().trigger(animationInteract, Direction.UP.representation);
    } else if (direction < 225) {
      entity.getEvents().trigger(animationInteract, Direction.LEFT.representation);
    } else if (direction < 315) {
      entity.getEvents().trigger(animationInteract, Direction.DOWN.representation);
    }

    /*
     * Find the closest entity we can interact with. To register a new entity:
     * 1. Go to InteractionDetector.java
     * 2. Add the entity to the interactableEntities array
     */
    InteractionDetector interactionDetector = entity.getComponent(InteractionDetector.class);
    List<Entity> entitiesInRange = interactionDetector.getEntitiesInRange();
    Entity closestEntity = interactionDetector.getNearest(entitiesInRange);

    if (closestEntity != null) {
      closestEntity.getEvents().trigger("interact");
    }
  }

  /**
   * Makes the player attack any animal within given arc and distance
   *
   * @param mousePos Determine direction of mouse
   */
  void attack(Vector2 mousePos) {
    Sound attackSound = ServiceLocator.getResourceService().getAsset("sounds/Impact4.ogg", Sound.class);
    attackSound.play();
    mousePos = ServiceLocator.getCameraComponent().screenPositionToWorldPosition(mousePos);
    List<Entity> areaEntities = ServiceLocator.getGameArea().getAreaEntities();
    for(Entity animal : areaEntities) {
      CombatStatsComponent combat = animal.getComponent(CombatStatsComponent.class);
      if(combat != null && animal != entity && entity.getPosition().dst(animal.getPosition()) < 3) {
          Vector2 result = new Vector2(0, 0);
          result.x = animal.getCenterPosition().x - entity.getCenterPosition().x;
          result.y = animal.getCenterPosition().y - entity.getCenterPosition().y;
          Vector2 mouseResult = new Vector2(0, 0);
          mouseResult.x = mousePos.x - entity.getCenterPosition().x;
          mouseResult.y = mousePos.y - entity.getCenterPosition().y;
          float resAngle = result.angleDeg();
          float mouseResAngle = mouseResult.angleDeg();
          float difference = Math.abs(resAngle - mouseResAngle);
          difference = difference > 180 ? 360 - difference : difference;
          if(difference <= 45) {
            combat.addHealth((int) -(swordDamage * damageMultiplier));
            animal.getEvents().trigger("hit", entity);
            ServiceLocator.getParticleService().startEffectAtPosition(ParticleService.ParticleEffectType.ATTACK_EFFECT, animal.getCenterPosition());
            animal.getEvents().trigger("panicStart");
          }
      }
    }
  }

  /**
   * Makes the player shoot a bullet entity at given mouse position
   *
   * @param mousePos Determine direction of mouse
   */
  void shoot(Vector2 mousePos) {
    Sound attackSound = ServiceLocator.getResourceService().getAsset("sounds/Impact4.ogg", Sound.class);
    attackSound.play();
    mousePos = ServiceLocator.getCameraComponent().screenPositionToWorldPosition(mousePos);
    Entity projectile = ProjectileFactory.createPlayerProjectile();

    CombatStatsComponent combatStatsComponent = projectile.getComponent(CombatStatsComponent.class);
    combatStatsComponent.setBaseAttack((int) (combatStatsComponent.getBaseAttack() * damageMultiplier));
    projectile.setCenterPosition(entity.getCenterPosition());
    ServiceLocator.getGameArea().spawnEntity(projectile);
    ProjectileComponent projectileComponent = projectile.getComponent(ProjectileComponent.class);
    projectileComponent.setSpeed(new Vector2(3f, 3f));
    projectileComponent.setTargetDirection(mousePos);
  }

  /**
   * Makes the player get into tractor.
   */
  void enterTractor() {
    // check within 4 units of tractor
    if (ServiceLocator.getGameArea().getTractor() == null || this.entity.getPosition().dst(ServiceLocator.getGameArea().getTractor().getPosition()) > 4) {
      return;
    }
    try {
      ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.TRACTOR_START_UP);
    } catch (InvalidSoundFileException e) {
      logger.error("Failed to play tractor start up sound", e);
    }
    this.stopMoving();
    muted = true;
    ServiceLocator.getGameArea().getTractor().getEvents().trigger("toggleAuraLight");
    ServiceLocator.getGameArea().getTractor().getComponent(TractorActions.class).setMuted(false);
    ServiceLocator.getGameArea().getTractor().getComponent(KeyboardTractorInputComponent.class)
        .setWalkDirection(entity.getComponent(KeyboardPlayerInputComponent.class).getWalkDirection());
    this.entity.setPosition(new Vector2(-10, -10));
    ServiceLocator.getCameraComponent().setTrackEntity(ServiceLocator.getGameArea().getTractor());
  }

  void use(Vector2 mousePos, Entity itemInHand) {
    if (itemInHand != null && itemInHand.getComponent(ItemActions.class) != null) {
      pauseMoving();
      itemInHand.getComponent(ItemActions.class).use(entity, mousePos);
    }
  }

  void eat(Entity itemInHand) {
    if (itemInHand != null) {
      pauseMoving();
      ItemType itemType = itemInHand.getComponent(ItemComponent.class).getItemType();

      if (itemType == ItemType.FOOD || itemType == ItemType.EGG || itemType == ItemType.MILK) {
        itemInHand.getComponent(ItemActions.class).eat(entity);
        entity.getComponent(InventoryComponent.class).removeItem(itemInHand);
      }
    }
  }

  void hotkeySelection(int index) {
    InventoryComponent inventoryComponent = entity.getComponent(InventoryComponent.class);
    // Make sure its initialised
    if (inventoryComponent != null) {
      inventoryComponent.setHeldItem(index);
    }
  }

  /**
   * When in the tractor inputs should be muted, this handles that.
   * 
   * @return if the players inputs should be muted
   */
  public boolean isMuted() {
    return muted;
  }

  public boolean isStunned() {
    StunComponent stunComponent = entity.getComponent(StunComponent.class);
    if (stunComponent == null) {
      return false;
    }

    return stunComponent.isStunned();
  }

  public void setSpeedMultiplier(float multiplier) {
    this.speedMultiplier = multiplier;
  }

  public void setDamageMultiplier(float multiplier) {
    this.damageMultiplier = multiplier;
  }

  public void setMuted(boolean muted) {
    this.muted = muted;
  }

  private void startPlayerMovementSpeedEffect(float movementMultiplier) {
    this.weatherSpeedModifier = movementMultiplier;
    this.isWeatherAffectingSpeed = true;
  }

  private void stopPlayerMovementSpeedEffect() {
    this.weatherSpeedModifier = 1.0f;
    this.isWeatherAffectingSpeed = false;
  }

}
