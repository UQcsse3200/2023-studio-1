package com.csse3200.game.screens;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.controlsmenu.ControlsMenuDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;

/** The game screen for the Controls Screen of the game,
 * which explains which keys are to be used for different actions in the game */

public class ControlsScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(ControlsScreen.class);
  private final GdxGame game;
  private final Renderer renderer;

  /**
   * A count of the frame in the background animation
   */
  public static final int frameCount = 64;

  /**
   * A list of textures that must be loaded for this screen
   */
  private static final String[] mainMenuTextures = {"images/galaxy_home_still.png"};

  /**
   * A list of textures that must be loaded for the animation
   */
  public static String[] transitionTextures = new String[frameCount];

  /**
   * A common name prefix for all the animation textures
   */
  private static final String animationPrefix = "images/menuanimate/";

  private SpriteBatch batch;

  public ControlsScreen(GdxGame game) {
    this.game = game;

    logger.debug("Initialising controls screen services");
    ServiceLocator.registerTimeSource(new GameTime());
    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());
    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());
    ServiceLocator.registerTimeService(new TimeService());
    renderer = RenderFactory.createRenderer();
    renderer.getCamera().getEntity().setPosition(5f, 5f);

    loadAssets();
    createUI();
  }

  @Override
  public void render(float delta) {
    ServiceLocator.getEntityService().update();
    ServiceLocator.getTimeService().update();
    renderer.render();
  }

  @Override
  public void resize(int width, int height) {
    renderer.resize(width, height);
  }

  @Override
  public void dispose() {
    renderer.dispose();
    ServiceLocator.getRenderService().dispose();
    ServiceLocator.getEntityService().dispose();
    ServiceLocator.clear();
  }

  /**
   * Loads the texture assets required for the screen
   */
  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(mainMenuTextures);
    loadFrames();
    ServiceLocator.getResourceService().loadAll();
  }

  /**
   * Loads the texture frames required for the animation on this screen.
   */
  private void loadFrames() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();

    for (int i = 1; i <= frameCount; i++) {
      String frameNumber = String.format("%05d", i);
      transitionTextures[i - 1] = animationPrefix + frameNumber + ".png";
    }
    resourceService.loadTextures(transitionTextures);
    ServiceLocator.getResourceService().loadAll();
  }

  /**
   * Remove the loaded image textures from the ResourceService
   */
  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(mainMenuTextures);
    resourceService.unloadAssets(transitionTextures);
  }

  /**
   * Creates the control screen's ui including components for rendering ui elements to the screen
   * and capturing and handling ui input.
   */
  private void createUI() {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();
    Entity ui = new Entity();
    ui.addComponent(new ControlsMenuDisplay(game))
            .addComponent(new InputDecorator(stage, 10));
    ServiceLocator.getEntityService().register(ui);
  }
}
