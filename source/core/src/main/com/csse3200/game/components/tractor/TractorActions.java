package com.csse3200.game.components.tractor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;

import static com.csse3200.game.areas.terrain.TerrainCropTileFactory.createTerrainEntity;

public class TractorActions extends Component {
  private static final Vector2 MAX_SPEED = new Vector2(5f, 5f); // Metres per second
  private Entity player;

  private PhysicsComponent physicsComponent;
  private Vector2 walkDirection = Vector2.Zero.cpy();
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
    if (this.moving) {
      updateSpeed();
      use();
    }
  }

  private void use() {
    switch (getMode()) {
      case normal -> {
      }
      case tilling -> {
        TerrainTile tile = map.getTile(entity.getPosition());
        TerrainTile tile2 = map.getTile(new Vector2(entity.getPosition().x, entity.getPosition().y - 1));
        hoe(tile, entity.getPosition());
        hoe(tile2, new Vector2(entity.getPosition().x, entity.getPosition().y - 1));
      }
      case harvesting -> {
        TerrainTile tile = map.getTile(entity.getPosition());
        TerrainTile tile2 = map.getTile(new Vector2(entity.getPosition().x, entity.getPosition().y - 1));
        harvest(tile);
        harvest(tile2);
      }
    }
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
    if (tile.getCropTile() != null || !tile.isTillable()) {
      return;
    }
    // Make a new tile
    Entity cropTile = createTerrainEntity(new Vector2((int)Math.ceil(pos.x), (int)Math.ceil(pos.y)));
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
   * Gets the location of the tractor
   * 
   * @return the location of the tractor as Vector2
   */
  public Vector2 getTractorLocation() {
    return entity.getPosition();
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