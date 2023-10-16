package com.csse3200.game.files;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.files.FileLoader.Location;
import com.csse3200.game.missions.MissionManager;

/**
 * Reading, Writing, and applying user settings in the game.
 */
public class SaveGame {

  private SaveGame() {
    throw new IllegalStateException("Util class");
  }

  private static final String ROOT_DIR = "saves";
  private static final String SAVE_FILE = "saveFile.json";

  /**
   * Get the stored save file
   * 
   * @return Copy of the saved game state
   */
  public static GameState get(String path) {
    return FileLoader.readClass(GameState.class, path, Location.LOCAL);
  }

  /**
   * Set the current game state
   * 
   * @param gameState The gameState to store
   */
  public static void set(GameState gameState, String path) {
    FileLoader.writeClass(gameState, path, Location.LOCAL);
  }

  /**
   * Stores game state. Can be serialised/deserialised.
   */
  public static class GameState {
    private int day;
    private int hour;
    private int minute;

    private ClimateController climate;

    private MissionManager missions;

    private Entity player;
    private Entity tractor;
    private Array<Entity> entities;

    private Array<Entity> tiles;
    private Array<Entity> placeables;

    public GameState() {
      // No constructor code needed
    }

    /**
     * Gets the day value stored in the GameState
     * @return the in game day as an int
     */
    public int getDay() {
      return day;
    }

    /**
     * Sets the day value stored in the GameState
     * @param day the day to set GameState to store
     */
    public void setDay(int day) {
      this.day = day;
    }

    /**
     * Gets the hour value stored in the GameState
     * @return the in game hour as an int
     */
    public int getHour() {
      return hour;
    }

    /**
     * Sets the hour value stored in the GameState
     * @param hour the hour to set GameState to store
     */
    public void setHour(int hour) {
      this.hour = hour;
    }

    /**
     * Gets the player entity stored in the GameState
     * @return the player entity stored in the GameState
     */
    public Entity getPlayer() {
      return player;
    }

    /**
     * Sets the player entity stored in the GameState
     * @param player the player entity to store in the GameState
     */
    public void setPlayer(Entity player) {
      this.player = player;
    }

    /**
     * Gets the entities that are used in save/load
     * @return All entities that are stored in the GameState that were not filtered out from filterEntities()
     */
    public Array<Entity> getEntities() {
      return entities;
    }

    /**
     * Sets the entities that are used in save/load (filtered by filterEntities() first)
     * @param entities All entities in the game or at least a relevant subset of them
     */
    public void setEntities(Array<Entity> entities) {
      this.entities = filterEntities(entities);
    }

    /**
     * Filter out all entities that are not in the NPC Factory. This
     * function is mainly for SaveLoadService.java. Produces an Array of entities
     * from the NPC factory that needs to be remade
     * 
     * @param entities the entities to filter in Array<Entity>
     * @return the entities filtered in Array<Entity> for use in SaveLoadService.
     */
    private Array<Entity> filterEntities(Array<Entity> entities) {
      // If you edit this original array you edit what is in the ResourceService
      Array<Entity> tmp = new Array<>();
      for (Entity e : entities) {
        if (e.getType() == EntityType.ASTROLOTL || e.getType() == EntityType.CHICKEN ||
                e.getType() == EntityType.COW || e.getType() == EntityType.OXYGEN_EATER ||
                e.getType() == EntityType.SHIP_DEBRIS || e.getType() == EntityType.FIRE_FLIES ||
                e.getType() == EntityType.SHIP) {
          tmp.add(e);
        }
      }
      return tmp;
    }

    /**
     * Gets the entities that are used in save/load
     * @return All entities that are stored in the GameState that were not filtered out from filterTiles()
     */
    public Array<Entity> getTiles() {
      return tiles;
    }

    /**
     * Gets the Tractor entity stored within the GameState
     * @return the tractor entity within the GameState, or null if none
     */
    public Entity getTractor() {
      return tractor;
    }

    /**
     * Sets the entities that are used in save/load (filtered by filterTiles() first)
     * @param tiles All entities in the game or at least a relevant subset of them
     */
    public void setTiles(Array<Entity> tiles) {
      this.tiles = filterTiles(tiles);
    }

    /**
     * Filter out all entities that are not tiles. This
     * function is mainly for SaveLoadService.java. Produces an Array of entities
     * from factories that needs to be remade
     *
     * @param entities the entities to filter in Array<Entity>
     * @return the entities filtered in Array<Entity> for use in SaveLoadService.
     */
    private Array<Entity> filterTiles(Array<Entity> entities) {
      // If you edit this original array you edit what is in the ResourceService
      Array<Entity> tiles = new Array<>(entities);
      for (int i = 0; i < tiles.size; i++) {
        if (tiles.get(i).getType() != EntityType.TILE && tiles.get(i).getType() != EntityType.SHIP_PART_TILE) {
          tiles.removeIndex(i);
          // Moves the indexing down when removed so keep index same
          i--;

        }
      }
      return tiles;
    }

    /**
     * Gets the Tractor entity stored within the GameState
     * @param tractor the tractor entity within the GameState, or null if none
     */
    public void setTractor(Entity tractor) {
      this.tractor = tractor;
    }

    /**
     * Gets the ClimateController that is stored within the GameState
     * @return the ClimateController that is stored within the GameState
     */
    public ClimateController getClimate() {
      return climate;
    }

    /**
     * Gets the ClimateController that is stored within the GameState
     * @param climate  the ClimateController that is stored within the GameState
     */
    public void setClimate(ClimateController climate) {
      this.climate = climate;
    }

    /**
     * Gets the MissionManager that is stored within the GameState
     * @return the MissionManager that is stored within the GameState
     */
    public MissionManager getMissions() {
      return missions;
    }

    /**
     * Gets the MissionManager that is stored within the GameState
     * @param missions  the MissionManager that is stored within the GameState
     */
    public void setMissions(MissionManager missions) {
      this.missions = missions;
    }

    /**
     * Gets the in game minute that is stored within the GameState
     * @return the in game minute that is stored within the GameState
     */
    public int getMinute() {
      return minute;
    }

    /**
     * Gets the in game minute that is stored within the GameState
     * @param minute the in game minute that is stored within the GameState
     */
    public void setMinute(int minute) {
      this.minute = minute;
    }

    /**
     * Gets the entities that are used in save/load
     * @return All entities that are stored in the GameState that were not filtered out from filterPlaceables()
     */
    public Array<Entity> getPlaceables() {
      return placeables;
    }

    /**
     * Sets the entities that are used in save/load (filtered by filterPlaceables() first)
     * @param placeables All entities in the game or at least a relevant subset of them
     */
    public void setPlaceables(Array<Entity> placeables) {
      this.placeables = filterPlaceables(placeables);
    }

    /**
     * Filter out all entities that are not placeables. This
     * function is mainly for SaveLoadService.java. Produces an Array of entities
     * from the Placeable factory that needs to be remade
     *
     * @param entities the entities to filter in Array<Entity>
     * @return the entities filtered in Array<Entity> for use in SaveLoadService.
     */
    private Array<Entity> filterPlaceables(Array<Entity> entities) {
      Array<Entity> returnValue = new Array<>(entities);
      ArrayList<EntityType> placeableTypes = new ArrayList<>(Arrays.asList(EntityType.CHEST, EntityType.LIGHT,
              EntityType.FENCE, EntityType.GATE, EntityType.SPRINKLER, EntityType.PUMP, EntityType.GOLDEN_STATUE));
      for (int i = 0; i < returnValue.size; i++) {
        if (!placeableTypes.contains(returnValue.get(i).getType())) {
          returnValue.removeIndex(i);
          // Moves the indexing down when removed so keep index same
          i--;
        }
      }
      return returnValue;
    }
  }
}