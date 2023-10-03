package com.csse3200.game.components.controlsmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.GdxGame;
import com.csse3200.game.GdxGame.ScreenType;
import com.csse3200.game.screens.ControlsScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import net.dermetfan.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

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
  private static int fps = 15;

  /**
   * The duration for which each frame should be displayed
   */
  private final static long FRAME_DURATION = 800 / fps;

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

    // Listen for a button press
    returnBtn.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent changeEvent, Actor actor) {
        logger.debug("Exit button clicked");
        exitMenu();
      }
    });

    Actor controlsTbl = makeControlsTable(); // generate the table that represents the controls of the game

    // Set the background image
    // TODO: Make the background images smaller and use inbuilt animation and styling for the title so that spacing is easier to handle
    background = new Image(
            ServiceLocator.getResourceService().getAsset("images/galaxy_home_still.png", Texture.class));
    background.setWidth(Gdx.graphics.getWidth());
    background.setHeight(Gdx.graphics.getHeight());
    background.setPosition(0, 0);
    stage.addActor(background);

    rootTable = new Table();
    rootTable.setFillParent(true); // Make the root table fill the screen
    rootTable.debug();

    rootTable.add(transitionFrames).padBottom(-50f);

    rootTable.row(); // Padding ensures that there is always space between table and title

    rootTable.add(controlsTbl).expandX().padBottom(30f); // Add the controls table and let it take as much space as needed
      rootTable.row().padBottom(50f); // Padding ensures that there is always space between table and exit button

    rootTable.add(returnBtn).padBottom(30f); // The return button is anchored to the bottom of the page

    stage.addActor(rootTable); // Add the root table to the stage
  }

  /**
   * Update the frame of the background animation
   */
  private void updateAnimation() {
    if (frame < ControlsScreen.frameCount) {
      // set the next frame of the animation
      transitionFrames.setDrawable(new TextureRegionDrawable(new TextureRegion(ServiceLocator.getResourceService()
              .getAsset(ControlsScreen.transitionTextures[frame], Texture.class))));
      transitionFrames.setWidth(Gdx.graphics.getWidth());
      transitionFrames.setHeight((float)Gdx.graphics.getHeight() / 2);
      transitionFrames.setPosition(0, (float)Gdx.graphics.getHeight() / 2 + 15);
      frame++;
      lastFrameTime = System.currentTimeMillis();
    } else {
      frame = 1;
    }
  }

  /**
   * Helper method to construct the controls table and return it to be added to the root table
   * @return controlsTable - A Table containing the game's controls and their descriptions
   */
  private Actor makeControlsTable() {
    Table controlsTbl = new Table();

    // Make a scroll pane
      ScrollPane scrollPane = new ScrollPane(controlsTbl);
      // TODO: Update scrollpane style to have black labels

    // Set column default heights and widths
    controlsTbl.defaults().minHeight(50f).padTop(20f);
    controlsTbl.columnDefaults(0).minWidth(50f);
    controlsTbl.columnDefaults(1).padLeft(50f);

    // Add the header row
    Label controlLabel = new Label("Key", skin, "large");
    Label useLabel = new Label("Usage", skin, "large");

    controlsTbl.add(controlLabel);
    controlsTbl.add(useLabel).center();

    // Create a dictionary to store all the controls
    // LinkedHashMap is the only map the preserves order of insertion.
    // Performance is a non-issue in a static table, so using a linked list structure doesn't matter
    ArrayList<Pair<String, String>> controls = new ArrayList<>();

    // To add another control, simply put it in the map below.
    controls.add(new Pair<>("W", "Moves the character upwards"));
    controls.add(new Pair<>("A", "Moves the character to the left"));
    controls.add(new Pair<>("S", "Moves the character to the right"));
    controls.add(new Pair<>("D", "Moves the character downwards"));
    controls.add(new Pair<>("T", "Toggles the player light on and off"));
    controls.add(new Pair<>("Esc", "Toggles the pause game function"));

    for (Pair<String, String> control : controls) {
      // Start a new row for each control
      controlsTbl.row();
      // Create a button to represent the key press required
      TextButton keyButton = new TextButton(control.getKey(), skin);
      // Create a button to represent the control's description
      Label descriptionLabel = new Label(control.getValue(), skin);

      // Add the buttons to the table
      controlsTbl.add(keyButton).center();
      controlsTbl.add(descriptionLabel).center();
    }

    return scrollPane;
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
    if (System.currentTimeMillis() - lastFrameTime > FRAME_DURATION) {
      updateAnimation();
    }

    // Make sure the background image stays full screen even after screen resizes
    if (background != null &&
            (Gdx.graphics.getHeight() != background.getHeight()
            || Gdx.graphics.getWidth() != background.getWidth())) {
      background.setWidth(Gdx.graphics.getWidth());
      background.setHeight(Gdx.graphics.getHeight());
      background.setPosition(0, 0);
      logger.debug("Background resized");
    }

    stage.act(ServiceLocator.getTimeSource().getDeltaTime());
  }

  @Override
  public void dispose() {
    rootTable.clear();
    super.dispose();
  }
}
