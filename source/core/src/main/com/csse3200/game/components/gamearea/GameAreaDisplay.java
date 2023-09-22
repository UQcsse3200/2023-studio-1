package com.csse3200.game.components.gamearea;

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
import com.csse3200.game.components.maingame.PauseMenuActions;
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
  private Group pausingGroup = new Group();
  private OpenPauseComponent openPauseComponent;
  private Image popUp;


  @Override
  public void create() {
    super.create();
    ServiceLocator.registerCraftArea(this);
    addActors();
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
    Image pauseMenu = new Image(new Texture(Gdx.files.internal("images/PauseMenu/Pausenew.jpg")));
    pauseMenu.setSize(1300, 700);
    pauseMenu.setPosition((float) (Gdx.graphics.getWidth() / 2.0 - pauseMenu.getWidth() / 2),
            (float) (Gdx.graphics.getHeight() / 2.0 - pauseMenu.getHeight() / 2));
    pausingGroup.addActor(pauseMenu);
    stage.addActor(pausingGroup);
    stage.draw();

    float buttonHeight = 80f;

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
      }
    });
    pausingGroup.addActor(resumeBtn);

    TextButton exitBtn = new TextButton("Exit", skin);
    exitBtn.setSize(386f, buttonHeight);
    exitBtn.setPosition(pauseMenu.getX() + 450f, pauseMenu.getY() + 75);
    exitBtn.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        logger.debug("Exit button clicked");
        KeyboardPlayerInputComponent.incrementPauseCounter();
        KeyboardPlayerInputComponent.clearMenuOpening();
        PauseMenuActions.setQuitGameStatus();
      }
    });
    pausingGroup.addActor(exitBtn);

    TextButton loadBtn = new TextButton("Load Previous", skin);
    loadBtn.setSize(386f, buttonHeight);
    loadBtn.setPosition(pauseMenu.getX() + 450f, pauseMenu.getY() + 175);
    loadBtn.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
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
        ServiceLocator.getSaveLoadService().save();
      }
    });
    pausingGroup.addActor(saveBtn);

    stage.draw();
  }



  public void disposePauseMenu() {
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
