package com.csse3200.game.components.gamearea;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.inventory.InventoryDisplayManager;
import com.csse3200.game.components.maingame.MainGameActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.components.player.OpenPauseComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

/**
 * Displays the name of the current game area.
 */
public class GameAreaDisplay extends UIComponent {
  private String gameAreaName = "";
  private Label title;
  private static final Logger logger = LoggerFactory.getLogger(GameAreaDisplay.class);
  private Window pausingGroup = new Window("Pause", skin, "wooden");
  private OpenPauseComponent openPauseComponent;
  private Image backgroundOverlay;
  private InventoryDisplayManager inventoryDisplayManager;

  @Override
  public void create() {
    super.create();
    ServiceLocator.registerCraftArea(this);
    addActors();
    backgroundOverlay = new Image(ServiceLocator.getResourceService().getAsset("images/PauseMenu/Pause_Overlay.jpg", Texture.class));
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
  }

  public void setPauseMenu() {
    pausingGroup.setVisible(true);
    pausingGroup.toFront();
    logger.info("Opening Pause Menu");
    openPauseComponent = ServiceLocator.getGameArea().getPlayer().getComponent(OpenPauseComponent.class);

    backgroundOverlay.setVisible(true); // Initially, set it to invisible

    //Image pauseMenu = new Image(ServiceLocator.getResourceService().getAsset("images/PauseMenu/Pausenew.jpg", Texture.class));
    //pauseMenu.setSize(1300, 700);
    //pauseMenu.setPosition((float) (Gdx.graphics.getWidth() / 2.0 - pauseMenu.getWidth() / 2),
     //       (float) (Gdx.graphics.getHeight() / 2.0 - pauseMenu.getHeight() / 2));
    //pausingGroup.addActor(pauseMenu);

    //pausingGroup.setSize(1300, 700);
    pausingGroup.setPosition(Gdx.graphics.getWidth() / 2f,
                 Gdx.graphics.getHeight() / 2f, Align.center);
    stage.addActor(pausingGroup);
    //stage.draw();

    Table buttons = new Table();
    pausingGroup.add(buttons);

    float buttonHeight = 80f;

    TextButton exitBtn = new TextButton("Exit", skin,"orange");
    exitBtn.setSize(386f, buttonHeight);
    //exitBtn.setPosition(pauseMenu.getX() + 450f, pauseMenu.getY() + 75);
    exitBtn.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        logger.debug("Exit button clicked");
        KeyboardPlayerInputComponent.incrementPauseCounter();
        KeyboardPlayerInputComponent.clearMenuOpening();
        MainGameActions.exitToMainMenu();
      }
    });
    buttons.add(exitBtn);

    buttons.row().padTop(15f);

    Label messageLabel = new Label("", skin); // Create an empty label initially
    //messageLabel.setPosition(pauseMenu.getX() + 450f, pauseMenu.getY() + 500);
    messageLabel.setVisible(false); // Initially, the label is not visible

    TextButton loadBtn = new TextButton("Load Previous", skin,"orange");
    loadBtn.setSize(386f, buttonHeight);
    //loadBtn.setPosition(pauseMenu.getX() + 450f, pauseMenu.getY() + 175);
    loadBtn.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        messageLabel.setVisible(false);
        messageLabel.setText("                    Previous Game Loaded!");
        messageLabel.setVisible(true);
        ServiceLocator.getSaveLoadService().load();
      }
    });
    buttons.add(loadBtn);

    buttons.row().padTop(15f);

    TextButton saveBtn = new TextButton("Save", skin,"orange");
    saveBtn.setSize(386f, buttonHeight);
    //saveBtn.setPosition(pauseMenu.getX() + 450f, pauseMenu.getY() + 275);
    saveBtn.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        messageLabel.setVisible(false);
        messageLabel.setText("                      Current Game Saved!");
        messageLabel.setVisible(true);
        ServiceLocator.getSaveLoadService().save();
      }
    });
    buttons.add(saveBtn);

    buttons.row().padTop(15f);

    TextButton resumeBtn = new TextButton("Resume", skin,"orange");
    resumeBtn.setSize(386f, buttonHeight);
    //resumeBtn.setPosition(pauseMenu.getX() + 450f, pauseMenu.getY() + 375);
    resumeBtn.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        logger.debug("Resume button clicked");
        KeyboardPlayerInputComponent.incrementPauseCounter();
        KeyboardPlayerInputComponent.clearMenuOpening();
        openPauseComponent.closePauseMenu();
        messageLabel.setVisible(false);
      }
    });
    buttons.add(resumeBtn);

    buttons.row().padTop(15f);

    buttons.add(messageLabel);

    pausingGroup.setSize(400, 400);

    //stage.draw();
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
    pausingGroup.setVisible(false);
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

  @Override
  public void update() {
    logger.info("blah");
    pausingGroup.setPosition(Gdx.graphics.getWidth() / 2f,
            Gdx.graphics.getHeight() / 2f, Align.center);
    backgroundOverlay.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    backgroundOverlay.toFront();

  }
}
