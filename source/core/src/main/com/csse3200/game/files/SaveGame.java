package com.csse3200.game.files;

import java.io.File;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.files.FileLoader.Location;


/**
 * Reading, Writing, and applying user settings in the game.
 */
public class SaveGame {
  private static final String ROOT_DIR = "saves";
  private static final String SAVE_FILE = "saveFile.json";

  /**
   * Get the stored save file
   * @return Copy of the saved game state
   */
  public static GameState get() {
    String path = ROOT_DIR + File.separator + SAVE_FILE;
    GameState saveFile = FileLoader.readClass(GameState.class, path, Location.LOCAL);

    return saveFile; // we do a null check in calling function anyway
    // TODO: remove old code
    //return saveFile != null ? saveFile : new GameState();
  }

  /**
   * Set the current game state
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

    private Entity player;
    private Array<Entity> entities;

    private GameMap map;

    public GameState() {};

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

    private Array<Entity> filterEntities(Array<Entity> entities) {
      // If you edit this original array you edit what is in the ResourceService
      Array<Entity> tmp = new Array<>(entities);
      for (int i = 0; i < tmp.size; i++) {
        if (tmp.get(i).getType() == EntityType.Item || tmp.get(i).getType() == EntityType.Player || tmp.get(i).getType() == null || tmp.get(i).getType() == EntityType.Tile) {
          tmp.removeIndex(i);
          // Moves the indexing down when removed so keep index same
          i--;

        }
      }
      return tmp;
    }

    public GameMap getMap() {
      return map;
    }

    public void setMap(GameMap map) {
      this.map = map;
    }
  }
}