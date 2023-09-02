package com.csse3200.game.files;

import java.io.File;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.files.FileLoader.Location;


/**
 * Reading, Writing, and applying user settings in the game.
 */
public class SaveGame {
  private static final String ROOT_DIR = "CSSE3200Game";
  private static final String SAVE_FILE = "saveFile.json";

  /**
   * Get the stored save file
   * @return Copy of the saved game state
   */
  public static GameState get() {
    String path = ROOT_DIR + File.separator + SAVE_FILE;
    GameState saveFile = FileLoader.readClass(GameState.class, path, Location.EXTERNAL);

    return saveFile != null ? saveFile : new GameState();
  }

  /**
   * Set the current game state
   * @param gameState The gameState to store
   */
  public static void set(GameState gameState) {
    String path = ROOT_DIR + File.separator + SAVE_FILE;
    FileLoader.writeClass(gameState, path, Location.EXTERNAL);
  }

  /**
   * Stores game state. Can be serialised/deserialised.
   */
  public static class GameState {
    public Entity player;

    public GameState() {};

  }
}