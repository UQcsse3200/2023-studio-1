package com.csse3200.game.screens;

import com.csse3200.game.services.*;
import com.csse3200.game.components.plants.PlantInfoDisplayComponent;
import com.csse3200.game.entities.FireflySpawner;
import com.csse3200.game.components.losescreen.LoseScreenDisplay;
import com.csse3200.game.ui.UIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.weather.WeatherEventDisplay;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.components.maingame.MainGameExitDisplay;
import com.csse3200.game.components.maingame.PauseMenuActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.sound.SoundService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.GameTimeDisplay;
import com.csse3200.game.services.LightService;
import com.csse3200.game.services.OxygenDisplay;
import com.csse3200.game.services.ParticleService;
import com.csse3200.game.services.PlanetOxygenService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import com.csse3200.game.services.plants.PlantCommandService;
import com.csse3200.game.services.plants.PlantInfoService;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;



/**
 * The game screen containing the main game.
 *
 * <p>Details on libGDX screens: <a href="https://happycoding.io/tutorials/libgdx/game-screens">...</a>
 */

public class MainGameScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);
    private static final String[] mainGameTextures = {
            //"images/heart.png",
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
            "images/status_ui/status_frame.png",
            "images/status_ui/health_fill.png",
            "images/status_ui/health_icon.png",
            "images/status_ui/hunger_fill.png",
            "images/status_ui/hunger_icon.png",
            "images/status_ui/oxygen_fill.png",
            "images/status_ui/oxygen_icon.png",
            "images/weather_event/weather-border.png",
            "images/weather_event/acid-rain.png",
            "images/weather_event/solar-flare.png"

    };
    private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);

    private final GdxGame game;
    private Entity entity;
    private final Renderer renderer;
    private final PhysicsEngine physicsEngine;
    public enum ScreenType {
        MAIN_GAME,
        WIN,
        LOSE
    }
    private ScreenType currentScreenType = ScreenType.MAIN_GAME;

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
        ServiceLocator.registerPlanetOxygenService(new PlanetOxygenService());

        ServiceLocator.registerPlantCommandService(new PlantCommandService());
        ServiceLocator.registerPlayerHungerService(new PlayerHungerService());
        ServiceLocator.registerPlayerMapService(new PlayerMapService());
        ServiceLocator.registerPlantInfoService(new PlantInfoService());

        ServiceLocator.registerUIService(new UIService());
        ServiceLocator.registerSoundService(new SoundService());


        ServiceLocator.registerMissionManager(new MissionManager());
        // Why does this not exist??
        //ServiceLocator.registerSoundService(new SoundService());

        ServiceLocator.registerMissionManager(new MissionManager());
        renderer = RenderFactory.createRenderer();
        renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
        renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());
        ServiceLocator.registerCameraComponent(renderer.getCamera());

        ServiceLocator.registerLightService(new LightService());
        ServiceLocator.registerParticleService(new ParticleService());

        loadAssets();

        logger.debug("Initialising main game screen entities");
        TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());
        SpaceGameArea spaceGameArea = new SpaceGameArea(terrainFactory);
        spaceGameArea.create();
        renderer.getCamera().setTrackEntity(spaceGameArea.getPlayer());

        createUI();

        ServiceLocator.getMissionManager().getEvents().addListener("loseScreen", this::playLoseScreen);

        ServiceLocator.getMissionManager().getEvents().addListener("winScreen", this::playWinScreen);

        new FireflySpawner();

        // if the LoadSaveOnStart value is set true then load entities saved from file
        if (game.isLoadOnStart()){
            ServiceLocator.getSaveLoadService().load();
        }
    }

    /**
     * Switch to the losing screen in case of player loss
     */
    public void playLoseScreen(String causeOfDeath) {
        LoseScreenDisplay.setLoseReason(causeOfDeath);
        currentScreenType = ScreenType.LOSE;
    }

    /**
     * Switch to winning screen in case of player win
     */
    public void playWinScreen() {
        currentScreenType = ScreenType.WIN;
    }

    @Override
    public void render(float delta) {
        switch (currentScreenType) {
            case MAIN_GAME -> {
                if (!ServiceLocator.getTimeService().isPaused()) {
                    physicsEngine.update();
                    ServiceLocator.getEntityService().update();
                }
                ServiceLocator.getTimeService().update();
                renderer.render();
                if (Boolean.TRUE.equals(PauseMenuActions.getQuitGameStatus())) {
                    entity.getEvents().trigger("exit");
                    PauseMenuActions.setQuitGameStatus();
                }
            }
            case LOSE -> game.setScreen(GdxGame.ScreenType.LOSESCREEN);
            case WIN -> game.setScreen(GdxGame.ScreenType.WINSCREEN);
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
        ServiceLocator.getSoundService().getEffectsMusicService().dispose();
        ServiceLocator.getSoundService().getBackgroundMusicService().dispose();

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
                .addComponent(new PlantInfoDisplayComponent())
                .addComponent(new WeatherEventDisplay())
            // NOTE: VERY IMPORTANT
            // UI components that require transitions must be added AFTER those that don't, otherwise screen
            // entities added after them will transition even if you don't want them to. Add components with
            // transitions underneath this comment. Hopefully it should work, it fixed my bug at least.
                .addComponent(new GameTimeDisplay())
                .addComponent(new ProgressBar())
                .addComponent(new OxygenDisplay())
                .addComponent(new HungerBar())
                .addComponent(new PlantInfoDisplayComponent())

                .addComponent(new WeatherEventDisplay())
                .addComponent(new HealthDisplay())
                .addComponent(new ToggleableMap());

        ServiceLocator.getEntityService().register(ui);
    }
}
