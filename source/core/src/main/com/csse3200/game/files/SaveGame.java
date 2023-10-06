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
  private static final String ROOT_DIR = "saves";
  private static final String SAVE_FILE = "saveFile.json";

  /**
   * Get the stored save file
   * 
   * @return Copy of the saved game state
   */
  public static GameState get(String path) {
    GameState saveFile = FileLoader.readClass(GameState.class, path, Location.LOCAL);
    return saveFile;
  }

  /**
   * Set the current game state
   * 
   * @param gameState The gameState to store
   */
  public static void set(GameState gameState) {
    String path = ROOT_DIR + File.separator + SAVE_FILE;
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

    public int getDay() {
      return day;
    }

    public void setDay(int day) {
      this.day = day;
    }

    public int getHour() {
      return hour;
    }

    public void setHour(int hour) {
      this.hour = hour;
    }

    public Entity getPlayer() {
      return player;
    }

    public void setPlayer(Entity player) {
      this.player = player;
    }

    public Array<Entity> getEntities() {
      return entities;
    }

    public void setEntities(Array<Entity> entities) {
      this.entities = filterEntities(entities);
    }

    /**
     * Filter out all entities that are in the NPC Factory, player or tractor. This
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

    public Array<Entity> getTiles() {
      return tiles;
    }

    /**
     * Loop through all entities in the save file and if the tractor is there return
     * the tractor
     * if not return null
     * 
     * @return
     */
    public Entity getTractor() {
      return tractor;
    }

    public void setTiles(Array<Entity> tiles) {
      this.tiles = filterTiles(tiles);
    }

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

    public void setTractor(Entity tractor) {
      this.tractor = tractor;
    }

    public ClimateController getClimate() {
      return climate;
    }

    public void setClimate(ClimateController climate) {
      this.climate = climate;
    }

    public MissionManager getMissions() {
      return missions;
    }

    public void setMissions(MissionManager missions) {
      this.missions = missions;
    }

    public int getMinute() {
      return minute;
    }

    public void setMinute(int minute) {
      this.minute = minute;
    }

    public Array<Entity> getPlaceables() {
      return placeables;
    }

    public void setPlaceables(Array<Entity> placeables) {
      this.placeables = filterPlaceables(placeables);
    }

    private Array<Entity> filterPlaceables(Array<Entity> entities) {
      Array<Entity> returnValue = new Array<>(entities);
      ArrayList<EntityType> placeableTypes = new ArrayList<>(Arrays.asList(EntityType.CHEST, EntityType.LIGHT,
              EntityType.FENCE, EntityType.GATE, EntityType.SPRINKLER, EntityType.PUMP));
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