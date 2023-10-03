package com.csse3200.game.components.gamearea;

import com.csse3200.game.components.inventory.InventoryDisplayManager;
import com.csse3200.game.components.maingame.MainGameActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.components.player.OpenPauseComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays the name of the current game area.
 */
public class GameAreaDisplay extends UIComponent {
  private String gameAreaName = "";
  private Label title;
  private static final Logger logger = LoggerFactory.getLogger(GameAreaDisplay.class);
  private Group pausingGroup = new Group();
  private OpenPauseComponent openPauseComponent;
  private Image backgroundOverlay;
  private InventoryDisplayManager inventoryDisplayManager;

  @Override
  public void create() {
    super.create();
    ServiceLocator.registerCraftArea(this);
    addActors();
    backgroundOverlay = new Image(new Texture(Gdx.files.internal("images/PauseMenu/Pause_Overlay.jpg")));
    backgroundOverlay.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    backgroundOverlay.setColor(0, 0, 0, 0.8f);
    backgroundOverlay.setVisible(false);
    stage.addActor(backgroundOverlay);
    inventoryDisplayManager = new InventoryDisplayManager(stage);
    ServiceLocator.registerInventoryDisplayManager(inventoryDisplayManager);

  }
  public GameAreaDisplay(String gameAreaName) {
    this.gameAreaName = gameAreaName;
    ServiceLocator.registerPauseArea(this);
  }

  public String getGameAreaName() {
    return gameAreaName;
  }

  private void addActors() {
    title = new Label(this.gameAreaName, skin, "large");
    stage.addActor(title);
  }

  public void setPauseMenu() {
    logger.info("Opening Pause Menu");
    openPauseComponent = ServiceLocator.getGameArea().getPlayer().getComponent(OpenPauseComponent.class);

    backgroundOverlay.setVisible(true); // Initially, set it to invisible


    Image pauseMenu = new Image(new Texture(Gdx.files.internal("images/PauseMenu/Pausenew.jpg")));
    pauseMenu.setSize(1300, 700);
    pauseMenu.setPosition((float) (Gdx.graphics.getWidth() / 2.0 - pauseMenu.getWidth() / 2),
            (float) (Gdx.graphics.getHeight() / 2.0 - pauseMenu.getHeight() / 2));
    pausingGroup.addActor(pauseMenu);
    stage.addActor(pausingGroup);
    stage.draw();

    float buttonHeight = 80f;

    TextButton exitBtn = new TextButton("Exit", skin);
    exitBtn.setSize(386f, buttonHeight);
    exitBtn.setPosition(pauseMenu.getX() + 450f, pauseMenu.getY() + 75);
    exitBtn.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        logger.debug("Exit button clicked");
        KeyboardPlayerInputComponent.incrementPauseCounter();
        KeyboardPlayerInputComponent.clearMenuOpening();
        MainGameActions.exitToMainMenu();
      }
    });
    pausingGroup.addActor(exitBtn);

    Label saveMessageLabel = new Label("", skin); // Create an empty label initially
    saveMessageLabel.setPosition(pauseMenu.getX() + 450f, pauseMenu.getY() + 500);
    saveMessageLabel.setVisible(false); // Initially, the label is not visible
    pausingGroup.addActor(saveMessageLabel);
    Label resumeMessageLabel = new Label("", skin); // Create an empty label initially
    resumeMessageLabel.setPosition(pauseMenu.getX() + 450f, pauseMenu.getY() + 500);
    resumeMessageLabel.setVisible(false); // Initially, the label is not visible
    pausingGroup.addActor(resumeMessageLabel);
    TextButton loadBtn = new TextButton("Load Previous", skin);
    loadBtn.setSize(386f, buttonHeight);
    loadBtn.setPosition(pauseMenu.getX() + 450f, pauseMenu.getY() + 175);
    loadBtn.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        saveMessageLabel.setVisible(false);
        resumeMessageLabel.setText("                    Previous Game Loaded!");
        resumeMessageLabel.setVisible(true);
        ServiceLocator.getSaveLoadService().load();
      }
    });
    pausingGroup.addActor(loadBtn);

    TextButton saveBtn = new TextButton("Save", skin);
    saveBtn.setSize(386f, buttonHeight);
    saveBtn.setPosition(pauseMenu.getX() + 450f, pauseMenu.getY() + 275);
    saveBtn.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        saveMessageLabel.setText("                      Current Game Saved!");
        saveMessageLabel.setVisible(true);
        resumeMessageLabel.setVisible(false);
        ServiceLocator.getSaveLoadService().save();
      }
    });
    pausingGroup.addActor(saveBtn);


    TextButton resumeBtn = new TextButton("Resume", skin);
    resumeBtn.setSize(386f, buttonHeight);
    resumeBtn.setPosition(pauseMenu.getX() + 450f, pauseMenu.getY() + 375);
    resumeBtn.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        logger.debug("Resume button clicked");
        KeyboardPlayerInputComponent.incrementPauseCounter();
        KeyboardPlayerInputComponent.clearMenuOpening();
        openPauseComponent.closePauseMenu();
        saveMessageLabel.setVisible(false);
      }
    });
    pausingGroup.addActor(resumeBtn);

    stage.draw();
  }

  /**
   * Gets the current Display Manager
   * @return the current Display Manager
   */
  public InventoryDisplayManager getInventoryManager() {
    return inventoryDisplayManager;
  }


  public void disposePauseMenu() {
    backgroundOverlay.setVisible(false);
    pausingGroup.clear();
  }

  @Override
  public void draw(SpriteBatch batch)  {
    int screenHeight = Gdx.graphics.getHeight();
    float offsetX = 10f;
    float offsetY = 30f;

    title.setPosition(offsetX, screenHeight - offsetY);
  }

  @Override
  public void dispose() {
    super.dispose();
    title.remove();
  }
}
