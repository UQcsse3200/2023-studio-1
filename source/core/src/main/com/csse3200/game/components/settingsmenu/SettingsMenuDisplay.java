package com.csse3200.game.components.settingsmenu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.GdxGame;
import com.csse3200.game.GdxGame.ScreenType;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.files.UserSettings.DisplaySettings;
import com.csse3200.game.screens.SettingsScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.utils.StringDecorator;

/**
 * Settings menu display and logic. If you bork the settings, they can be changed manually in
 * CSSE3200Game/settings.json under your home directory (This is C:/users/[username] on Windows).
 */
public class SettingsMenuDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(SettingsMenuDisplay.class);
  private final GdxGame game;
  private TextField fpsText;
  private CheckBox fullScreenCheck;
  private CheckBox vsyncCheck;
  private Slider uiScaleSlider;
  private SelectBox<StringDecorator<DisplayMode>> displayModeSelect;

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
  private final long frameDuration = (long) (800 / fps);


  public SettingsMenuDisplay(GdxGame game) {
    super();
    transitionFrames = new Image();
    this.game = game;
  }

  @Override
  public void create() {
    super.create();
    frame = 1;
    addActors();
  }

  private void addActors() {
    Label titleback = new Label("Settings", skin, "title");
    titleback.setPosition(0, 0);

    Table titleContainer = new Table();
    titleContainer.add(titleback).padTop(290f);
    titleContainer.setPosition(0, Gdx.graphics.getHeight() * 0.75f);

    Table settingsTable = makeSettingsTable();
    Table menuBtns = makeMenuBtns();

    Image title = new Image(
            ServiceLocator.getResourceService()
                    .getAsset("images/galaxy_home_still.png", Texture.class));
    title.setWidth(Gdx.graphics.getWidth());
    title.setHeight(Gdx.graphics.getHeight());
    title.setPosition(0, 0);

    rootTable = new Table();
    rootTable.setFillParent(true);
    rootTable.add(titleContainer).expandX().top();
    rootTable.add(title);
    stage.addActor(title);

    // Create a container for the settingsTable to prevent shifting
    Table settingsContainer = new Table();
    settingsContainer.add(settingsTable).expandX().expandY();

    rootTable.row().padTop(10f);
    rootTable.add(settingsContainer).expandX().expandY(); // Add the settingsContainer instead of settingsTable
    rootTable.row();
    rootTable.add(menuBtns).fillX();

    updateAnimation();
    stage.addActor(transitionFrames);
    stage.addActor(rootTable);
  }

  private void updateAnimation() {
    if (frame < SettingsScreen.frameCount) {
      transitionFrames.setDrawable(new TextureRegionDrawable(new TextureRegion(ServiceLocator.getResourceService()
              .getAsset(SettingsScreen.transitionTextures[frame], Texture.class))));
      transitionFrames.setWidth(Gdx.graphics.getWidth());
      transitionFrames.setHeight(Gdx.graphics.getHeight() / (float)2); //https://rules.sonarsource.com/java/RSPEC-2184/
      transitionFrames.setPosition(0, Gdx.graphics.getHeight() / (float)2 + 15); //https://rules.sonarsource.com/java/RSPEC-2184/
      frame++;
      lastFrameTime = System.currentTimeMillis();
    } else {
      frame = 1;
    }
  }

  private Table makeSettingsTable() {
    // Get current values
    UserSettings.Settings settings = UserSettings.get();

    // Create components
    Label fpsLabel = new Label("FPS Cap:", skin);
    fpsText = new TextField(Integer.toString(settings.fps), skin);

    Label fullScreenLabel = new Label("Fullscreen:", skin);
    fullScreenCheck = new CheckBox("", skin);
    fullScreenCheck.setChecked(settings.fullscreen);

    Label vsyncLabel = new Label("VSync:", skin);
    vsyncCheck = new CheckBox("", skin);
    vsyncCheck.setChecked(settings.vsync);

    Label uiScaleLabel = new Label("ui Scale (Unused):", skin);
    uiScaleSlider = new Slider(0.2f, 2f, 0.1f, false, skin);
    uiScaleSlider.setValue(settings.uiScale);
    Label uiScaleValue = new Label(String.format("%.2fx", settings.uiScale), skin);

    Label displayModeLabel = new Label("Resolution:", skin);
    displayModeSelect = new SelectBox<>(skin);
    Monitor selectedMonitor = Gdx.graphics.getMonitor();
    displayModeSelect.setItems(getDisplayModes(selectedMonitor));
    displayModeSelect.setSelected(getActiveMode(displayModeSelect.getItems()));

    // Position Components on table
    Table table = new Table();

    table.add(fpsLabel).right().padRight(15f);
    table.add(fpsText).width(100).left();

    table.row().padTop(10f);
    table.add(fullScreenLabel).right().padRight(15f);
    table.add(fullScreenCheck).left();

    table.row().padTop(10f);
    table.add(vsyncLabel).right().padRight(15f);
    table.add(vsyncCheck).left();

    table.row().padTop(10f);
    Table uiScaleTable = new Table();
    uiScaleTable.add(uiScaleSlider).width(100).left();
    uiScaleTable.add(uiScaleValue).left().padLeft(5f).expandX();

    table.add(uiScaleLabel).right().padRight(15f);
    table.add(uiScaleTable).left();

    table.row().padTop(10f);
    table.add(displayModeLabel).right().padRight(15f);
    table.add(displayModeSelect).left();

    // Events on inputs
    uiScaleSlider.addListener(
        (Event event) -> {
          float value = uiScaleSlider.getValue();
          uiScaleValue.setText(String.format("%.2fx", value));
          return true;
        });

    return table;
  }

  private StringDecorator<DisplayMode> getActiveMode(Array<StringDecorator<DisplayMode>> modes) {
    DisplayMode active = Gdx.graphics.getDisplayMode();

    for (StringDecorator<DisplayMode> stringMode : modes) {
      DisplayMode mode = stringMode.object;
      if (active.width == mode.width
          && active.height == mode.height
          && active.refreshRate == mode.refreshRate) {
        return stringMode;
      }
    }
    return null;
  }

  private Array<StringDecorator<DisplayMode>> getDisplayModes(Monitor monitor) {
    DisplayMode[] displayModes = Gdx.graphics.getDisplayModes(monitor);
    Array<StringDecorator<DisplayMode>> arr = new Array<>();

    for (DisplayMode displayMode : displayModes) {
      arr.add(new StringDecorator<>(displayMode, this::prettyPrint));
    }

    return arr;
  }

  private String prettyPrint(DisplayMode displayMode) {
    return displayMode.width + "x" + displayMode.height + ", " + displayMode.refreshRate + "hz";
  }

  private Table makeMenuBtns() {
    TextButton exitBtn = new TextButton("Exit", skin);
    TextButton applyBtn = new TextButton("Apply", skin);

    exitBtn.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Exit button clicked");
            exitMenu();
          }
        });

    applyBtn.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Apply button clicked");
            applyChanges();
          }
        });

    Table table = new Table();
    table.add(exitBtn).expandX().left().pad(200f, 15f, 15f, 0f);
    table.add(applyBtn).expandX().right().pad(200f, 0f, 15f, 15f);
    return table;

  }

  private void applyChanges() {
    UserSettings.Settings settings = UserSettings.get();

    Integer fpsVal = parseOrNull(fpsText.getText());
    if (fpsVal != null) {
      settings.fps = fpsVal;
    }
    settings.fullscreen = fullScreenCheck.isChecked();
    settings.uiScale = uiScaleSlider.getValue();
    settings.displayMode = new DisplaySettings(displayModeSelect.getSelected().object);
    settings.vsync = vsyncCheck.isChecked();

    UserSettings.set(settings, true);
  }

  private void exitMenu() {
    game.setScreen(ScreenType.MAIN_MENU);
  }

  private Integer parseOrNull(String num) {
    try {
      return Integer.parseInt(num, 10);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  @Override
  protected void draw(SpriteBatch batch) {
    // draw is handled by the stage

  }

  @Override
  public void update() {
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
