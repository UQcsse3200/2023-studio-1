package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.utils.Json;
import com.csse3200.game.components.plants.PlantComponent;
import com.csse3200.game.entities.Entity;

/**
 * Custom terrain tile implementation for tiled map terrain that stores
 * additional properties we
 * may want to have in the game, such as audio, walking speed, traversability by
 * AI, etc.
 */
public class TerrainTile implements TiledMapTile {
  private int id;
  private BlendMode blendMode = BlendMode.ALPHA;
  private TextureRegion textureRegion;
  private float offsetX;
  private float offsetY;

  /**
   * Represents the type of terrain that the terrain tile is
   */
  private TerrainCategory terrainCategory;

  /**
   * Stores whether a terrain tile is traversable or not. Is true if it
   * traversable and false if not
   */
  private boolean isTraversable;

  /**
   * Stores whether the tile is occupied or not by a STATIONARY entity - i.e. a
   * cropTile. Is true if terrain tile is occupied and false if not
   */
  private boolean isOccupied;

  /**
   * Stores whether the tile is tillable or not (can be farmed). Is true if the
   * terrain tile is tillable and false if not
   */
  private boolean isTillable;

  /**
   * Stores a crop tile which occupies the terrain tile. Is null if no crop tile
   * occupies the terrain tile.
   */
  private Entity cropTile = null;

  /**
   * Stores a placebale entity which occupies the terrain tile.
   */
  private Entity placeable = null;

  /**
   * Stores the speed modifier of the tile
   */
  private float speedModifier;

  public TerrainTile(TextureRegion textureRegion, TerrainCategory terrainCategory) {
    this.textureRegion = textureRegion;
    this.terrainCategory = terrainCategory;
    this.isOccupied = false;

    // define properties of the TerrainTile based on its terrain category
    switch (terrainCategory) {
      case PATH:
        this.isTraversable = true;
        this.isTillable = true;
        this.speedModifier = 1.2f;
        break;
      case BEACHSAND:
        this.isTraversable = true;
        this.isTillable = false;
        this.speedModifier = 0.9f;
        break;
      case GRASS:
        this.isTraversable = true;
        this.isTillable = true;
        this.speedModifier = 1.1f;
        break;
      case DIRT:
        this.isTraversable = true;
        this.isTillable = true;
        this.speedModifier = 0.7f;
        break;
      case SHALLOWWATER:
        this.isTraversable = true;
        this.isTillable = false;
        this.speedModifier = 0.9f;
        break;
      case DESERT:
        this.isTraversable = true;
        this.isTillable = false;
        this.speedModifier = 0.8f;
        break;
      case SNOW:
        this.isTraversable = true;
        this.isTillable = false;
        this.speedModifier = 0.8f;
        break;
      case ICE:
        this.isTraversable = true;
        this.isTillable = false;
        this.speedModifier = 1.5f;
        break;
      case DEEPWATER:
        this.isTraversable = false;
        this.isTillable = false;
        this.speedModifier = 0.2f; // Not traversable
        break;
      case ROCK:
        this.isTraversable = false;
        this.isTillable = false;
        this.speedModifier = 0.2f; // Not traversable
        break;
      case LAVA:
        this.isTraversable = false;
        this.isTillable = false;
        this.speedModifier = 0.2f; // Not traversable
        break;
      case LAVAGROUND:
        this.isTraversable = true;
        this.isTillable = false;
        this.speedModifier = 0.7f;
        break;
      case GRAVEL:
        this.isTraversable = true;
        this.isTillable = false;
        this.speedModifier = 1f;
        break;
      case FLOWINGWATER:
        this.isTraversable = true;
        this.isTillable = false;
        this.speedModifier = 1.3f;
        break;
    }
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public BlendMode getBlendMode() {
    return blendMode;
  }

  @Override
  public void setBlendMode(BlendMode blendMode) {
    this.blendMode = blendMode;
  }

  @Override
  public TextureRegion getTextureRegion() {
    return textureRegion;
  }

  @Override
  public void setTextureRegion(TextureRegion textureRegion) {
    this.textureRegion = textureRegion;
  }

  @Override
  public float getOffsetX() {
    return offsetX;
  }

  @Override
  public void setOffsetX(float offsetX) {
    this.offsetX = offsetX;
  }

  @Override
  public float getOffsetY() {
    return offsetY;
  }

  @Override
  public void setOffsetY(float offsetY) {
    this.offsetY = offsetY;
  }

  /**
   * Not required for game, unimplemented
   * 
   * @return null
   */
  @Override
  public MapProperties getProperties() {
    return null;
  }

  /**
   * Not required for game, unimplemented
   * 
   * @return null
   */
  @Override
  public MapObjects getObjects() {
    return null;
  }

  /**
   * Returns the terrain tile's terrain category i.e. GRASS, DIRT etc
   * 
   * @return TerrainCategory of the tile
   */
  public TerrainCategory getTerrainCategory() {
    return this.terrainCategory;
  }

  /**
   * Sets the terrain category of the terrain tile to the specified terrain
   * category
   * 
   * @param terrainCategory new terrain category for terrain tile
   */
  public void setTerrainCategory(TerrainCategory terrainCategory) {
    this.terrainCategory = terrainCategory;
  }

  /**
   * returns whether a terrainTile is traversable or not
   * 
   * @return True is traversable and False if not
   */
  public boolean isTraversable() {
    return this.isTraversable;
  }

  /**
   * returns if the tile is occupied by another entity (i.e. player or NPC) or not
   * 
   * @return returns true of the tile is occupied by an entity and false if not
   */
  public boolean isOccupied() {
    return this.isOccupied;
  }

  /**
   * labels the terrain tile as being occupied (terrain tile does not store
   * actually entity occupying it)
   */
  public void setOccupied() {
    this.isOccupied = true;
  }

  /**
   * labels the terrain tile as being unoccupied
   */
  public void setUnOccupied() {
    this.isOccupied = false;
  }

  /**
   * returns if the tile is tillable or not
   * 
   * @return returns true if the tile is tillable and false if not
   */
  public boolean isTillable() {
    return this.isTillable;
  }

  public void write(Json json) {
    getCropTile().getComponent(CropTileComponent.class).write(json);
    if (getCropTile().getComponent(CropTileComponent.class).getPlant() != null) {
      getCropTile().getComponent(CropTileComponent.class).getPlant().getComponent(PlantComponent.class).write(json);
    }
  }

  /**
   * Returns the placeable entity that is on the TerrainTile
   * 
   * @return the placeable entity
   */
  public Entity getPlaceable() {
    return placeable;
  }

  /**
   * Sets the placeable entity and sets the tile to be occupied if not null
   * 
   * @param placeable the entity to be placed on the tile
   */
  public void setPlaceable(Entity placeable) {
    this.placeable = placeable;
    if (placeable != null) {
      setOccupied();
    }
  }

  public enum TerrainCategory { // wanted to name TerrainType but already enum with that name in TerrainFactory
    PATH,
    BEACHSAND,
    GRASS,
    DIRT,
    SHALLOWWATER,
    DESERT,
    SNOW,
    ICE,
    DEEPWATER,
    ROCK,
    LAVA,
    LAVAGROUND,
    GRAVEL,
    FLOWINGWATER
  }

  /**
   * Returns a crop tile entity which occupies the terrain tile
   * 
   * @return cropTile entity or null if there is no cropTile entity
   */
  public Entity getCropTile() {
    return this.cropTile;
  }

  /**
   * Sets the crop tile which occupies the terrain tile. Replaces any existing
   * crop tile that was already occupying the
   * terrain tile. Do not use to set cropTile as null, use removeCropTile instead.
   * 
   * @param cropTile new cropTile entity to occupy the terrainTile
   */
  public void setCropTile(Entity cropTile) {
    this.cropTile = cropTile;
    cropTile.getComponent(CropTileComponent.class).setTerrainTile(this);
    this.setOccupied();
  }

  /**
   * Removes any crop tile which occupies the terrain tile. Does not return the
   * crop tile.
   */
  public void removeCropTile() {
    this.cropTile = null;
    this.setUnOccupied();
  }

  /**
   * Returns the speed modifier of the terrain tile
   * 
   * @return the speed modifier of the terrain tile
   */
  public float getSpeedModifier() {
    return this.speedModifier;
  }
}
