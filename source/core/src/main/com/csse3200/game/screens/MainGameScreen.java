package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.tractor.TractorActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.*;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import com.csse3200.game.components.maingame.MainGameExitDisplay;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The game screen containing the main game.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class MainGameScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);
  private static final String[] mainGameTextures = {
          "images/heart.png",
          "images/time_system_ui/clock_frame.png",
          "images/time_system_ui/indicator_0.png",
          "images/time_system_ui/indicator_1.png",
          "images/time_system_ui/indicator_2.png",
          "images/time_system_ui/indicator_3.png",
          "images/time_system_ui/indicator_4.png",
          "images/time_system_ui/indicator_5.png",
          "images/time_system_ui/indicator_6.png",
          "images/time_system_ui/indicator_7.png",
          "images/time_system_ui/indicator_8.png",
          "images/time_system_ui/indicator_9.png",
          "images/time_system_ui/indicator_10.png",
          "images/time_system_ui/indicator_11.png",
          "images/time_system_ui/indicator_12.png",
          "images/time_system_ui/indicator_13.png",
          "images/time_system_ui/indicator_14.png",
          "images/time_system_ui/indicator_15.png",
          "images/time_system_ui/indicator_16.png",
          "images/time_system_ui/indicator_17.png",
          "images/time_system_ui/indicator_18.png",
          "images/time_system_ui/indicator_19.png",
          "images/time_system_ui/indicator_20.png",
          "images/time_system_ui/indicator_21.png",
          "images/time_system_ui/indicator_22.png",
          "images/time_system_ui/indicator_23.png",
  };
  private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);

  private final GdxGame game;
  private final Renderer renderer;
  private final PhysicsEngine physicsEngine;

  private static Boolean lose;

  public MainGameScreen(GdxGame game) {
    this.game = game;

    logger.debug("Initialising main game screen services");
    ServiceLocator.registerTimeSource(new GameTime());

    PhysicsService physicsService = new PhysicsService();
    ServiceLocator.registerPhysicsService(physicsService);
    physicsEngine = physicsService.getPhysics();

    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());

    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());
    ServiceLocator.registerTimeService(new TimeService());

    renderer = RenderFactory.createRenderer();
    renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

    ServiceLocator.registerLightService(new LightService((OrthographicCamera) renderer.getCamera().getCamera()));

    loadAssets();
    createUI();

    logger.debug("Initialising main game screen entities");
    TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());

    SpaceGameArea spaceGameArea = new SpaceGameArea(terrainFactory);
    spaceGameArea.create();
    renderer.getCamera().setTrackEntity(spaceGameArea.getPlayer());

    // Switched to spaceGameArea
    //ForestGameArea forestGameArea = new ForestGameArea(terrainFactory);
    //forestGameArea.create();
    //renderer.getCamera().setTrackEntity(forestGameArea.getPlayer());
    spaceGameArea.getPlayer().getComponent(PlayerActions.class).setCameraVar(renderer.getCamera());
    spaceGameArea.getTractor().getComponent(TractorActions.class).setCameraVar(renderer.getCamera());

    lose = false;
    spaceGameArea.getPlayer().getEvents().addListener("loseScreen", this::loseScreenStart);
  }

  public void loseScreenStart() {
    lose = true;
  }

  @Override
  public void render(float delta) {
    if (!ServiceLocator.getTimeService().isPaused()) {
      physicsEngine.update();
      ServiceLocator.getEntityService().update();
    }
      ServiceLocator.getTimeService().update();
      renderer.render();
      ServiceLocator.getLightService().renderLight();
    if (lose) {
      game.setScreen(GdxGame.ScreenType.LOSESCREEN);
    }
  }

  @Override
  public void resize(int width, int height) {
    renderer.resize(width, height);
    logger.trace("Resized renderer: ({} x {})", width, height);
  }

  @Override
  public void pause() {
    logger.info("Game paused");
  }

  @Override
  public void resume() {
    logger.info("Game resumed");
  }

  @Override
  public void dispose() {
    logger.debug("Disposing main game screen");

    renderer.dispose();
    unloadAssets();

    ServiceLocator.getEntityService().dispose();
    ServiceLocator.getRenderService().dispose();
    ServiceLocator.getResourceService().dispose();

    ServiceLocator.clear();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(mainGameTextures);
    ServiceLocator.getResourceService().loadAll();
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(mainGameTextures);
  }

  /**
   * Creates the main game's ui including components for rendering ui elements to the screen and
   * capturing and handling ui input.
   */
  private void createUI() {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();
    InputComponent inputComponent =
        ServiceLocator.getInputService().getInputFactory().createForTerminal();

    Entity ui = new Entity();
    ui.addComponent(new InputDecorator(stage, 10))
        .addComponent(new PerformanceDisplay())
        .addComponent(new MainGameActions(this.game))
        .addComponent(new MainGameExitDisplay())
        .addComponent(new Terminal())
        .addComponent(inputComponent)
        .addComponent(new TerminalDisplay())
        .addComponent(new GameTimeDisplay());

    ServiceLocator.getEntityService().register(ui);
  }
}
