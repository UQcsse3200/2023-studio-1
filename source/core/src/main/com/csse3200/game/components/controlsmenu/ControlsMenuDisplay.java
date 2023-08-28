package com.csse3200.game.components.controlsmenu;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.GdxGame.ScreenType;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;

/**
 * Control menu display and settings.
 */
public class ControlsMenuDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(ControlsMenuDisplay.class);
  private final GdxGame game;

  private Table rootTable;

  public ControlsMenuDisplay(GdxGame game) {
    super();
    this.game = game;
  }

  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    Label title = new Label("Controls", skin, "title");
    TextButton returnBtn = new TextButton("Return", skin);

    returnBtn.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent changeEvent, Actor actor) {
        logger.debug("Exit button clicked");
        exitMenu();
      }
    });

    Table controlsTbl = makeControlsTable();

    rootTable = new Table();
    rootTable.setFillParent(true); // Make the table fill the screen

    rootTable.add(title).expandX().top().padTop(30f); // The title is anchored to the top of the page

    rootTable.row().padTop(30f); // Padding ensures that there is always space between table and title

    rootTable.add(controlsTbl).expandX().expandY(); // Add the controls table and let it take as much space as needed

    rootTable.row().padBottom(30f); // Padding ensures that there is always space between table and exit button

    rootTable.add(returnBtn).padBottom(30f); // The return button is anchored to the bottom of the page

    stage.addActor(rootTable);
  }

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

  private void exitMenu() {
    game.setScreen(ScreenType.MAIN_MENU);
  }

  @Override
  protected void draw(SpriteBatch batch) {
    // draw is handled by the stage
  }

  @Override
  public void update() {
    stage.act(ServiceLocator.getTimeSource().getDeltaTime());
  }

  @Override
  public void dispose() {
    rootTable.clear();
    super.dispose();
  }
}
