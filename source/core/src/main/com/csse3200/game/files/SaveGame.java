package com.csse3200.game.files;

import java.io.File;

import com.csse3200.game.entities.Entity;
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

    return saveFile != null ? saveFile : new GameState();
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
  }
}