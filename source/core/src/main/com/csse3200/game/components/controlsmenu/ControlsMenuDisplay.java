package com.csse3200.game.components.controlsmenu;

import java.util.LinkedHashMap;

import com.csse3200.game.screens.MainMenuScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.GdxGame;
import com.csse3200.game.GdxGame.ScreenType;
import com.csse3200.game.screens.ControlsScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

/**
 * Control menu display and settings.
 */
public class ControlsMenuDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(ControlsMenuDisplay.class);
  private final GdxGame game;

  /**
   * The base Table on which the layout of the screen is built
   */
  private Table rootTable;

  /**
   * The Image that represents the background of the page
   */
  private Image background;

  /**
   * An Image that stores the current frame of the menu screen animation
   */
  private Image transitionFrames;

  /**
   * The current frame of the animation
   */
  private int frame;

  /**
   * The time at which the last frame was updated
   */
  private long lastFrameTime;

  /**
   * The target fps at which the frames should be updated
   */
  private int fps = 15;

  /**
   * The duration for which each frame should be displayed
   */
  private final long frameDuration = (long) (400 / fps);

  public ControlsMenuDisplay(GdxGame game) {
    super();
    // Initialise the animation with a blank image
    transitionFrames = new Image();
    this.game = game;
  }

  @Override
  public void create() {
    super.create();
    // initialise to the first frame
    frame = 1;
    addActors();
  }

  /**
   * Add the UI widgets that form the menu to the stage
   */
  private void addActors() {
    TextButton returnBtn = new TextButton("Return", skin); // Returns the user to the MainMenu Screen
    returnBtn.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent changeEvent, Actor actor) {
        logger.debug("Exit button clicked");
        exitMenu();
      }
    });

    Table controlsTbl = makeControlsTable(); // generate the table that represents the controls of the game
    rootTable = new Table();
    rootTable.setFillParent(true); // Make the root table fill the screen
    rootTable.row().padTop(30f); // Padding ensures that there is always space between table and title

    rootTable.add(controlsTbl).expandX().expandY(); // Add the controls table and let it take as much space as needed

    rootTable.row().padBottom(30f); // Padding ensures that there is always space between table and exit button

    rootTable.add(returnBtn).padBottom(30f); // The return button is anchored to the bottom of the page

    // Trigger the first frame of the animation to be loaded
    updateAnimation();
    stage.addActor(transitionFrames);

    stage.addActor(rootTable); // Add the root table to the stage
  }

  /**
   * Update the frame of the background animation
   */
  private void updateAnimation() {
    if (frame < MainMenuScreen.frameCount) {
      transitionFrames.setDrawable(new TextureRegionDrawable(new TextureRegion(ServiceLocator.getResourceService()
              .getAsset(MainMenuScreen.transitionTextures[frame], Texture.class))));
    } else {
      int descendingFrame = MainMenuScreen.frameCount * 2 - 1 - frame;
      transitionFrames.setDrawable(new TextureRegionDrawable(new TextureRegion(ServiceLocator.getResourceService()
              .getAsset(MainMenuScreen.transitionTextures[descendingFrame], Texture.class))));
    }
    transitionFrames.setWidth(Gdx.graphics.getWidth());
    transitionFrames.setHeight(Gdx.graphics.getHeight());
    frame++;
    if (frame >= MainMenuScreen.frameCount * 2) {
      frame = 0;
    }
    lastFrameTime = System.currentTimeMillis();
  }

  /**
   * Helper method to construct the controls table and return it to be added to the root table
   * @return controlsTable - A Table containing the game's controls and their descriptions
   */
  private Table makeControlsTable() {
    Table controlsTbl = new Table();

    // Set column default heights and widths
    controlsTbl.defaults().height(50f).padTop(20f);
    controlsTbl.columnDefaults(0).width(50f);
    controlsTbl.columnDefaults(1).padLeft(50f);

    // Add the header row
    Label controlLabel = new Label("Key", skin, "large");
    Label useLabel = new Label("Usage", skin, "large");

    controlsTbl.add(controlLabel);
    controlsTbl.add(useLabel).center();

    // Create a dictionary to store all the controls
    // LinkedHashMap is the only map the preserves order of insertion.
    // Performance is a non-issue in a static table, so using a linked list structure doesn't matter
    LinkedHashMap<String, String> controls = new LinkedHashMap<>();

    // To add another control, simply put it in the map below.
    controls.put("W", "Moves the character upwards");
    controls.put("A", "Moves the character to the left");
    controls.put("S", "Moves the character to the right");
    controls.put("D", "Moves the character downwards");
    controls.put("T", "Toggles the player light on and off");
    controls.put("Esc", "Toggles the pause game function");

    for (String key : controls.keySet()) {
      // Start a new row for each control
      controlsTbl.row();
      // Create a button to represent the key press required
      TextButton keyButton = new TextButton(key, skin);
      // Create a button to represent the control's description
      Label descriptionLabel = new Label(controls.get(key), skin);

      // Add the buttons to the table
      controlsTbl.add(keyButton).center();
      controlsTbl.add(descriptionLabel).center();
    }

    return controlsTbl;
  }

  /**
   * Returns the user to the Main Menu screen
   */
  private void exitMenu() {
    game.setScreen(ScreenType.MAIN_MENU);
  }

  @Override
  protected void draw(SpriteBatch batch) {
    // draw is handled by the stage
  }

  @Override
  public void update() {
    // Update the animation frame if the time threshold has been hit
    if (System.currentTimeMillis() - lastFrameTime > frameDuration) {
      updateAnimation();
    }
    stage.act(ServiceLocator.getTimeSource().getDeltaTime());
  }

  @Override
  public void dispose() {
    rootTable.clear();
    super.dispose();
  }
}
