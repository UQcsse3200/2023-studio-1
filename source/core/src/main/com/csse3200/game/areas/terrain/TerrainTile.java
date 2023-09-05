package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.csse3200.game.entities.Entity;

/**
 * Custom terrain tile implementation for tiled map terrain that stores additional properties we
 * may want to have in the game, such as audio, walking speed, traversability by AI, etc.
 */
public class TerrainTile implements TiledMapTile {
  private int id;
  private BlendMode blendMode = BlendMode.ALPHA;
  private TextureRegion textureRegion;
  private float offsetX;
  private float offsetY;
  private TerrainCategory terrainCategory;
  private boolean isTraversable;
  private boolean isOccupied;
  private boolean isTillable;
  private Entity cropTile = null;
  private double speed;

  public TerrainTile(TextureRegion textureRegion, TerrainCategory terrainCategory) {
    this.textureRegion = textureRegion;
    this.terrainCategory = terrainCategory;
    this.isOccupied = false;

    // define properties of the TerrainTile based on its terrain category
    switch (terrainCategory) {
      case PATH:
        this.isTraversable = true;
        this.isTillable = true;
        this.speed = 1.2;
        break;
      case BEACHSAND:
        this.isTraversable = true;
        this.isTillable = false;
        this.speed = 0.9;
        break;
      case GRASS:
        this.isTraversable = true;
        this.isTillable = true;
        this.speed = 1.1;
        break;
      case DIRT:
        this.isTraversable = true;
        this.isTillable = true;
        this.speed = 0.7;
        break;
      case SHALLOWWATER:
        this.isTraversable = true;
        this.isTillable = false;
        this.speed = 0.9;
        break;
      case DESERT:
        this.isTraversable = true;
        this.isTillable = false;
        this.speed = 0.8;
        break;
      case SNOW:
        this.isTraversable = true;
        this.isTillable = false;
        this.speed = 0.8;
        break;
      case ICE:
        this.isTraversable = true;
        this.isTillable = false;
        this.speed = 1.5;
        break;
      case DEEPWATER:
        this.isTraversable = false;
        this.isTillable = false;
        break;
      case ROCK:
        this.isTraversable = false;
        this.isTillable = false;
        break;
      case LAVA:
        this.isTraversable = false;
        this.isTillable = false;
        break;
      case LAVAGROUND:
        this.isTraversable = true;
        this.isTillable = false;
        this.speed = 0.7;
        break;
      case GRAVEL:
        this.isTraversable = true;
        this.isTillable = false;
        this.speed = 1;
        break;
      case FLOWINGWATER:
        this.isTraversable = true;
        this.isTillable = false;
        this.speed = 1.3;
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
   * @return null
   */
  @Override
  public MapProperties getProperties() {
    return null;
  }

  /**
   * Not required for game, unimplemented
   * @return null
   */
  @Override
  public MapObjects getObjects() {
    return null;
  }

  /**
   * Returns the terrain tile's terrain category i.e. GRASS, DIRT etc
   * @return TerrainCategory of the tile
   */
  public TerrainCategory getTerrainCategory() {
    return this.terrainCategory;
  }

  /**
   * Sets the terrain category of the terrain tile to the specified terrain category
   * @param terrainCategory new terrain category for terrain tile
   */
  public void setTerrainCategory(TerrainCategory terrainCategory) {
    this.terrainCategory = terrainCategory;
  }

  /**
   * returns whether a terrainTile is traversable or not
   * @return True is traversable and False if not
   */
  public boolean isTraversable() {
    return this.isTraversable;
  }

  /**
   * returns if the tile is occupied by another entity (i.e. player or NPC) or not
   * @return returns true of the tile is occupied by an entity and false if not
   */
  public boolean isOccupied() {
    return this.isOccupied;
  }

  /**
   * labels the terrain tile as being occupied (terrain tile does not store actually entity occupying it)
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
   * @return returns true if the tile is tillable and false if not
   */
  public boolean isTillable(){
    return this.isTillable;
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

  public Entity getCropTile() {
    return cropTile;
  }

  public void setCropTile(Entity cropTile) {
    this.cropTile = cropTile;
  }
}
