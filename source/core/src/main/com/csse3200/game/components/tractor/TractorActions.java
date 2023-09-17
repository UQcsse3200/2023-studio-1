package com.csse3200.game.components.tractor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.Objects;

import static com.csse3200.game.areas.terrain.TerrainCropTileFactory.createTerrainEntity;

public class TractorActions extends Component {
  private static final Vector2 MAX_SPEED = new Vector2(5f, 5f); // Metres per second
  private Entity player;

  private PhysicsComponent physicsComponent;
  private Vector2 walkDirection = Vector2.Zero.cpy();
  private float prevDirection = 300;
  private boolean moving = false;
  private boolean muted = true;
  private CameraComponent camera;
  private TractorMode mode = TractorMode.normal;
  private GameMap map;

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
  }

  /**
   *
   */
  private void updateAnimation() {
    if (walkDirection.epsilonEquals(Vector2.Zero)) {
      entity.getEvents().trigger("stopMoving", getDirection(prevDirection), getMode().toString());
    } else {
      entity.getEvents().trigger("startMoving", getDirection(walkDirection.angleDeg()), getMode().toString());
    }
  }

  private String getDirection(float direction) {
    if (direction < 45) {
      return "right";
    } else if (direction < 135) {
      return "up";
    } else if (direction < 225) {
      return "left";
    } else if (direction < 315) {
      return "down";
    }
    // TODO add logger to provide error here?
    return "right";
  }

  /**
   *
   */
  private void use() {
    switch (getMode()) {
      case tilling -> {
        Array<Object> tiles = getTiles(TractorMode.tilling, getDirection(walkDirection.angleDeg()));
        hoe((TerrainTile) tiles.get(0), (Vector2) tiles.get(2));
        hoe((TerrainTile) tiles.get(1), (Vector2) tiles.get(3));
      }
      case harvesting -> {
        Array<Object> tiles = getTiles(TractorMode.tilling, getDirection(walkDirection.angleDeg()));
        harvest((TerrainTile) tiles.get(0));
        harvest((TerrainTile) tiles.get(1));
      }
    }
  }

  /**
   *
   * @param mode
   * @param dir
   * @return
   */
  private Array<Object> getTiles(TractorMode mode, String dir) {
    Array<Object> tiles = new Array<>(2);
    Vector2 pos1 = new Vector2();
    Vector2 pos2 = new Vector2();
    if ((Objects.equals(dir, "right") && mode == TractorMode.tilling) || (Objects.equals(dir, "left") && mode == TractorMode.harvesting)) {
      pos1 = entity.getPosition();
      pos2.set(entity.getPosition().x, entity.getPosition().y + 1);
      tiles.add(map.getTile(pos1), map.getTile(pos2), pos1, pos2);
      return tiles;
    } else if ((Objects.equals(dir, "left") && mode == TractorMode.tilling) || (Objects.equals(dir, "right") && mode == TractorMode.harvesting)) {
      pos1.set(entity.getPosition().x + 5, entity.getPosition().y);
      pos2.set(entity.getPosition().x + 5, entity.getPosition().y + 1);
      tiles.add(map.getTile(pos1), map.getTile(pos2), pos1, pos2);
      return tiles;
    } else if ((Objects.equals(dir, "up") && mode == TractorMode.tilling) || (Objects.equals(dir, "down") && mode == TractorMode.harvesting)) {
      pos1.set(entity.getPosition().x + 2, entity.getPosition().y + 1);
      pos2.set(entity.getPosition().x + 3, entity.getPosition().y + 1);
      tiles.add(map.getTile(pos1), map.getTile(pos2), pos1, pos2);
      return tiles;
    } else if ((Objects.equals(dir, "down") && mode == TractorMode.tilling) || (Objects.equals(dir, "up") && mode == TractorMode.harvesting)) {
      pos1.set(entity.getPosition().x + 2, entity.getPosition().y + 3);
      pos2.set(entity.getPosition().x + 3, entity.getPosition().y + 3);
      tiles.add(map.getTile(pos1), map.getTile(pos2), pos1, pos2);
      return tiles;
    }
    return null;
  }

  private void harvest(TerrainTile tile) {
    if (tile == null) {
      return;
    } else if (tile.getCropTile() == null) {
      return;
    }
    tile.getCropTile().getEvents().trigger("harvest");
  }

  private void hoe(TerrainTile tile, Vector2 pos) {
    if (tile == null) {
      return;
    }
    if (tile.isOccupied() || !tile.isTillable()) {
      return;
    }
    // Make a new tile
    Entity cropTile = createTerrainEntity(map.tileCoordinatesToVector(map.vectorToTileCoordinates(pos)));
    tile.setCropTile(cropTile);
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

  public void setPlayer(Entity player) {
    this.player = player;
    player.getComponent(PlayerActions.class).setTractor(this.entity);
  }

  /**
   * Makes the player get into tractor.
   */
  void exitTractor() {
    this.stopMoving();
    this.mode = TractorMode.normal;
    player.getComponent(PlayerActions.class).setMuted(false);
    muted = true;
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

  public void setMuted(boolean muted) {
    this.muted = muted;
  }

  /**
   * ONLY USE FOR TESTING PURPOSES
   * 
   * @param comp
   */
  public void setPhysicsComponent(PhysicsComponent comp) {
    this.physicsComponent = comp;
  }

  public void setCameraVar(CameraComponent cam) {
    this.camera = cam;
  }

  public TractorMode getMode() {
    return mode;
  }

  public void setMode(TractorMode mode) {
    this.mode = mode;
  }

  public void write(Json json){
    json.writeObjectStart(this.getClass().getSimpleName());
    //Save the muted value to the json file
    json.writeValue("isMuted", this.isMuted());
    json.writeObjectEnd();
  }
}