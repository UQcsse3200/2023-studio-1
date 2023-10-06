package com.csse3200.game.components.tractor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.AuraLightComponent;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.ConeLightComponent;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static com.csse3200.game.areas.terrain.TerrainCropTileFactory.createTerrainEntity;

public class TractorActions extends Component {
  /**
   * The maximum speed for the player
   */
  private static final Vector2 MAX_SPEED = new Vector2(5f, 5f); // Metres per second

  /**
   * The reference to the player entity
   */
  private Entity player;

  /**
   * The reference to the PhysicsComponent
   */
  private PhysicsComponent physicsComponent;

  /**
   * The move direction of the tractor
   */
  private Vector2 walkDirection = Vector2.Zero.cpy();

  /**
   * The direction the tractor was/is moving in, previous meaning before it stopped moving;
   * prevDirection == direction.angleDeg() when moving == true.
   */
  private float prevDirection = 300;

  /**
   * If the tractor is moving or not
   */
  private boolean moving = false;

  /**
   * If the tractor's inputs should be muted or not
   */
  private boolean muted = true;

  /**
   * A reference to the camera, used to track the player / tractor on exit / enter
   */
  private CameraComponent camera;

  /**
   * The mode the tractor is in, used to interact with tiles
   */
  private TractorMode mode = TractorMode.NORMAL;

  /**
   * The map of the tiles, used to aid in getting / setting tiles
   */
  private GameMap map;

  private static final String RIGHT_STRING = "right";

  private static final Logger logger = LoggerFactory.getLogger(TractorActions.class);


  @Override
  /**
   * Creates the tractor actions component.
   */
  public void create() {
    this.map = ServiceLocator.getGameArea().getMap();
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
    if (!muted) {
      if (this.moving) {
        updateSpeed();
        use();
      }
      updateAnimation();
    }
    float lightDirection = getDirectionAdjusted(prevDirection);
    entity.getComponent(ConeLightComponent.class).setDirection(lightDirection);
  }

  /**
   * Updates the animation of the tractor based on direction, mode and whether it is moving or not.
   * This method of updating animation was adjusted to fit tractor from the code written by Team 2,
   * in PlayerActions and PlayerAnimationController.
   */
  private void updateAnimation() {
    if (walkDirection.epsilonEquals(Vector2.Zero)) {
      entity.getEvents().trigger("stopMoving", getDirection(prevDirection), getMode().toString());
    } else {
      entity.getEvents().trigger("startMoving", getDirection(walkDirection.angleDeg()), getMode().toString());
    }
  }

  /**
   * This method of getting direction was adjusted to fit tractor (changed return values)
   * from the code written by Team 2, in PlayerActions and PlayerAnimationController.
   * @param direction The direction the tractor is facing
   * @return a String that matches with where the tractor is looking, values can be "right", "left", "up" or "down"
   *          defaults to "right" in an error situation to avoid crashes.
   */
  private String getDirection(float direction) {
    if (direction < 45) {
      return RIGHT_STRING;
    } else if (direction < 135) {
      return "up";
    } else if (direction < 225) {
      return "left";
    } else if (direction < 315) {
      return "down";
    }
    logger.error("Direction was not in range of 0-360, was {}", direction);

    return RIGHT_STRING;
  }

  /**
   * This method of getting direction was adjusted to fit tractor (changed return values)
   * from the code written by Team 2, in PlayerActions and PlayerAnimationController.
   * @param direction The direction the tractor is facing
   * @return a String that matches with where the tractor is looking, values can be "right", "left", "up" or "down"
   *          defaults to "right" in an error situation to avoid crashes.
   */
  private float getDirectionAdjusted(float direction) {
    if (direction < 45) {
      return 0;
    } else if (direction < 135) {
      return 90;
    } else if (direction < 225) {
      return 180;
    } else if (direction < 315) {
      return 270;
    }
    logger.error("Direction was not in range of 0-360, was {}", direction);
    return 0;
  }

  /**
   * Interacts with the TerrainTiles based on a given TractorMode
   * Note: should not be used outside of update() as this was not intended to be how it works
   * and may provide unexpected results.
   */
  private void use() {
    switch (getMode()) {
      case TILLING -> {
        Array<Object> tiles = getTiles(TractorMode.TILLING, getDirection(walkDirection.angleDeg()));
        if (tiles == null) {
          return;
        }
        if (tiles.size != 4) {
          return;
        }
        hoe((TerrainTile) tiles.get(0), (Vector2) tiles.get(2));
        hoe((TerrainTile) tiles.get(1), (Vector2) tiles.get(3));
      }
      case HARVESTING -> {
        Array<Object> tiles = getTiles(TractorMode.TILLING, getDirection(walkDirection.angleDeg()));
        if (tiles == null) {
          return;
        }
        if (tiles.size != 4) {
          return;
        }
        harvest((TerrainTile) tiles.get(0));
        harvest((TerrainTile) tiles.get(1));
      }
      default -> {
        // Nothing
      }
    }
  }

  /**
   * Returns the tiles that the tractor should interact with based on the mode (TractorMode) and the direction
   * of the tractor.
   * @param mode The current mode of the tractor, either normal, tilling or harvesting, though normal will always
   *             result in an empty Array to be returned
   * @param dir The direction of the tractor as a String, accepts values from getDirection()
   * @return an Array consisting of two tiles in slots 0 and 1, followed by the Vector2 positions of the tiles
   *          in the same order as the tiles in slots 2 and 3.
   */
  private Array<Object> getTiles(TractorMode mode, String dir) {
    if (mode == TractorMode.TILLING) {
      return getTilesTilling(dir);
    } else if (mode == TractorMode.HARVESTING) {
      return getTilesHarvest(dir);
    }
    return null;
  }

  private Array<Object> getTilesTilling(String dir) {
    Array<Object> tiles = new Array<>(4);
    Vector2 pos1 = new Vector2();
    Vector2 pos2 = new Vector2();

    if (Objects.equals(dir, RIGHT_STRING)) {
      pos1.set(entity.getPosition().x, entity.getPosition().y + 1);
      pos2.set(entity.getPosition().x, entity.getPosition().y + 2);
      tiles.add(map.getTile(pos1), map.getTile(pos2), pos1, pos2);
      return tiles;
    } else if (Objects.equals(dir, "left")) {
      pos1.set(entity.getPosition().x + 5, entity.getPosition().y + 1);
      pos2.set(entity.getPosition().x + 5, entity.getPosition().y + 2);
      tiles.add(map.getTile(pos1), map.getTile(pos2), pos1, pos2);
      return tiles;
    } else if (Objects.equals(dir, "up")) {
      pos1.set(entity.getPosition().x + 2, entity.getPosition().y + 1);
      pos2.set(entity.getPosition().x + 3, entity.getPosition().y + 1);
      tiles.add(map.getTile(pos1), map.getTile(pos2), pos1, pos2);
      return tiles;
    } else if (Objects.equals(dir, "down")) {
      pos1.set(entity.getPosition().x + 2, entity.getPosition().y + 3);
      pos2.set(entity.getPosition().x + 3, entity.getPosition().y + 3);
      tiles.add(map.getTile(pos1), map.getTile(pos2), pos1, pos2);
      return tiles;
    }
    return null;
  }

  private Array<Object> getTilesHarvest(String dir) {
    Array<Object> tiles = new Array<>(4);
    Vector2 pos1 = new Vector2();
    Vector2 pos2 = new Vector2();

    if (Objects.equals(dir, "left")) {
      pos1.set(entity.getPosition().x, entity.getPosition().y + 1);
      pos2.set(entity.getPosition().x, entity.getPosition().y + 2);
      tiles.add(map.getTile(pos1), map.getTile(pos2), pos1, pos2);
      return tiles;
    } else if (Objects.equals(dir, RIGHT_STRING)) {
      pos1.set(entity.getPosition().x + 5, entity.getPosition().y + 1);
      pos2.set(entity.getPosition().x + 5, entity.getPosition().y + 2);
      tiles.add(map.getTile(pos1), map.getTile(pos2), pos1, pos2);
      return tiles;
    } else if (Objects.equals(dir, "down")) {
      pos1.set(entity.getPosition().x + 2, entity.getPosition().y + 1);
      pos2.set(entity.getPosition().x + 3, entity.getPosition().y + 1);
      tiles.add(map.getTile(pos1), map.getTile(pos2), pos1, pos2);
      return tiles;
    } else if (Objects.equals(dir, "up")) {
      pos1.set(entity.getPosition().x + 2, entity.getPosition().y + 3);
      pos2.set(entity.getPosition().x + 3, entity.getPosition().y + 3);
      tiles.add(map.getTile(pos1), map.getTile(pos2), pos1, pos2);
      return tiles;
    }
    return null;
  }

  /**
   * The method used when Harvesting mode is on, triggers the harvest method on a CropTileComponent
   * for a given TerrainTile.
   * @param tile The TerrainTile that should be interacted with
   */
  private void harvest(TerrainTile tile) {
    if (tile == null) {
      // Leave
    } else if (tile.getOccupant() == null) {
      // Leave
    } else if (isCropTile(tile.getOccupant())) {
      tile.getOccupant().getEvents().trigger("harvest");
    }
  }

  private boolean isCropTile(Entity tile) {
    return (tile != null) && (tile.getComponent(CropTileComponent.class) != null);
  }

  /**
   * The method used when Tilling mode is on, creates a new TerrainEntity and registers it with the
   * Entity service if there was not a CropTile already on the tile.
   * @param tile The TerrainTile that will be given a CropTile
   * @param pos The position to set the location of the CropTile to if there was no CropTile already
   *
   */
  private void hoe(TerrainTile tile, Vector2 pos) {
    if (tile == null) {
      return;
    }
    if (tile.isOccupied() || !tile.isTillable()) {
      return;
    }
    // Make a new tile
    Entity cropTile = createTerrainEntity(map.tileCoordinatesToVector(map.vectorToTileCoordinates(pos)));
    tile.setOccupant(cropTile);
    tile.setOccupied();
    ServiceLocator.getEntityService().register(cropTile);
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
    this.prevDirection = walkDirection.angleDeg();
    this.moving = true;
  }

  /**
   * Stops the player from walking.
   */
  public void stopMoving() {
    this.walkDirection = Vector2.Zero.cpy();
    updateSpeed();
    this.moving = false;
  }

  /**
   * Checks if the player/tractor is moving.
   * 
   * @return true if moving else false
   */
  public boolean isMoving() {
    return this.moving;
  }

  /**
   * Sets the player reference to the tractor, used to enter and exit the tractor
   * Will most likely be removed in Sprint 4 as this was code before there was a way to
   * easily get a reference to the player
   * @param player the player Entity
   */
  public void setPlayer(Entity player) {
    this.player = player;
    player.getComponent(PlayerActions.class).setTractor(this.entity);
  }

  /**
   * Makes the player get into tractor.
   */
  void exitTractor() {
    this.stopMoving();
    this.mode = TractorMode.NORMAL;
    player.getComponent(PlayerActions.class).setMuted(false);
    muted = true;
    entity.getComponent(AuraLightComponent.class).toggleLight();
    entity.getEvents().trigger("idle", getDirection(prevDirection));
    player.getComponent(KeyboardPlayerInputComponent.class)
        .setWalkDirection(entity.getComponent(KeyboardTractorInputComponent.class).getWalkDirection());
    player.setPosition(this.entity.getPosition());
    camera.setTrackEntity(player);
  }

  /**
   * When in the tractor inputs should be muted, this handles that.
   * 
   * @return if the players inputs should be muted
   */
  public boolean isMuted() {
    return muted;
  }

  /**
   * Sets the tractor's actions to be muted, This should be done when exiting the tractor.
   * @param muted
   */
  public void setMuted(boolean muted) {
    this.muted = muted;
  }

  /**
   * ONLY USE FOR TESTING PURPOSES, USED TO TEST MOVEMENT
   * 
   * @param comp a physics component
   */
  public void setPhysicsComponent(PhysicsComponent comp) {
    this.physicsComponent = comp;
  }

  /**
   * Sets the camera reference to the tractor, used to enter and exit the tractor
   * Will most likely be removed in Sprint 4 as this was code before there was a way to
   * easily get a reference to the camera
   * @param cam The game camera
   */
  public void setCameraVar(CameraComponent cam) {
    this.camera = cam;
  }

  /**
   * Returns tractor mode the tractor is currently using
   * @return the TractorMode value for the mode of the Tractor entity
   */
  public TractorMode getMode() {
    return mode;
  }

  /**
   * Sets the mode of the tractor entity to a given mode
   * @param mode The TractorMode to set the mode to
   */
  public void setMode(TractorMode mode) {
    this.mode = mode;
  }

  /**
   * Writes to the json in order to store the tractor's state, used to save the game
   * @param json The json that will be converted to a file
   */
  @Override
  public void write(Json json){
    json.writeObjectStart(this.getClass().getSimpleName());
    //Save the muted value to the json file
    json.writeValue("isMuted", this.isMuted());
    json.writeObjectEnd();
  }
}