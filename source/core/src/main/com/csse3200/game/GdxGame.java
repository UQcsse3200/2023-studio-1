package com.csse3200.game;

import static com.badlogic.gdx.Gdx.app;

import com.csse3200.game.screens.*;
import com.csse3200.game.utils.DiscordActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.csse3200.game.files.UserSettings;

/**
 * Entry point of the non-platform-specific game logic. Controls which screen is currently running.
 * The current screen triggers transitions to other screens. This works similarly to a finite state
 * machine (See the State Pattern).
 */
public class GdxGame extends Game {
  private static final Logger logger = LoggerFactory.getLogger(GdxGame.class);
  private boolean loadSaveOnStart = false;
  private DiscordActivity discordActivity;
  @Override
  public void create() {
    logger.info("Creating game");
    loadSettings();

    // Sets background to light yellow
    Gdx.gl.glClearColor(0.0f, 0.098f, 0.309f, 1.0f);
    setScreen(ScreenType.MAIN_MENU);
    discordActivity = new DiscordActivity();
  }

  /**
   * Loads the game's settings.
   */
  private void loadSettings() {
    logger.debug("Loading game settings");
    UserSettings.Settings settings = UserSettings.get();
    UserSettings.applySettings(settings);
  }

   /**
   * isLoadOnStart lets the game know if the player wants to load up a saved game
   *    if false the game will load up as default
   * @return
   */
  public boolean isLoadOnStart(){
    return loadSaveOnStart;
  }

  public void setLoadOnStart(boolean value){
    loadSaveOnStart = value;
  }

  /**
   * Sets the game's screen to a new screen of the provided type.
   * @param screenType screen type
   */
  public void setScreen(ScreenType screenType) {
    logger.info("Setting game screen to {}", screenType);
    Screen currentScreen = getScreen();
    if (currentScreen != null) {
      currentScreen.dispose();
    }
    setScreen(newScreen(screenType));
  }

  @Override
  public void dispose() {
    logger.debug("Disposing of current screen");
    getScreen().dispose();
  }

  /**
   * Create a new screen of the provided type.
   * @param screenType screen type
   * @return new screen
   */
  private Screen newScreen(ScreenType screenType) {
    switch (screenType) {
      case MAIN_MENU:
        updateDiscordStatus("Perusing the Main Menu");
        return new MainMenuScreen(this);
      case LOAD_GAME:
        updateDiscordStatus("Loading and ready to go!");
        setLoadOnStart(true);
        return new MainGameScreen(this);
      case MAIN_GAME:
        updateDiscordStatus("Watering Crops");
        return new MainGameScreen(this);
      case SETTINGS:
        updateDiscordStatus("Changing Settings");
        return new SettingsScreen(this);
      case CONTROLS:
        return new ControlsScreen(this);
      case INTRO:
        setLoadOnStart(false);
        return new IntroScreen(this);
      case LOSESCREEN:
        return new LoseScreen(this);
      case ENDCREDITS:
        return new EndCreditsScreen(this);
      case WINSCREEN:
        return new WinScreen(this);
      default:
        return null;
    }
  }

  public enum ScreenType {
    MAIN_MENU, LOAD_GAME, MAIN_GAME, SETTINGS, CONTROLS, INTRO, LOSESCREEN, WINSCREEN, ENDCREDITS
  }



  /**
   * Exit the game.
   */
  public void exit() {
    app.exit();
  }
}
