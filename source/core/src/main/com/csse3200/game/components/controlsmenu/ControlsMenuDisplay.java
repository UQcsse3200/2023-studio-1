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
 * Settings menu display and logic. If you bork the settings, they can be changed manually in
 * CSSE3200Game/settings.json under your home directory (This is C:/users/[username] on Windows).
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
    Label title = new Label("How to Play", skin, "title");
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
    rootTable.setFillParent(true);

    rootTable.add(title).expandX().top().padTop(20f);

    rootTable.row();

    rootTable.add(controlsTbl).padTop(10f);

    rootTable.row();

    rootTable.add(returnBtn).padTop(20f);

    stage.addActor(rootTable);
  }

  private Table makeControlsTable() {
    Table controlsTbl = new Table();
    controlsTbl.setFillParent(false);

    // Set column default heights and widths
    controlsTbl.defaults().height(50f).padTop(20f);
    controlsTbl.columnDefaults(0).width(50f);
    controlsTbl.columnDefaults(1).padLeft(30f);

    // Add the header row
    Label controlLabel = new Label("Key", skin);
    Label useLabel = new Label("Usage", skin);

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
      controlsTbl.row();
      TextButton keyButton = new TextButton(key, skin);
      Label descriptionLabel = new Label(controls.get(key), skin);

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
