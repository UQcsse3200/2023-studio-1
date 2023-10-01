package com.csse3200.game.services;

import com.csse3200.game.services.plants.PlantCommandService;
import com.csse3200.game.services.plants.PlantInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.input.InputService;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.cutscenes.Cutscene;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;

/**
 * A simplified implementation of the Service Locator pattern:
 * https://martinfowler.com/articles/injection.html#UsingAServiceLocator
 *
 * <p>Allows global access to a few core game services.
 * Warning: global access is a trap and should be used <i>extremely</i> sparingly.
 * Read the wiki for details (https://github.com/UQcsse3200/game-engine/wiki/Service-Locator).
 */
public class ServiceLocator {
  private static final Logger logger = LoggerFactory.getLogger(ServiceLocator.class);
  private static EntityService entityService;
  private static RenderService renderService;
  private static PhysicsService physicsService;
  private static InputService inputService;
  private static ResourceService resourceService;
  private static TimeService timeService;
  private static GameTime timeSource;
  private static GameArea gameArea;
  private static LightService lightService;
  private static GameAreaDisplay pauseMenuArea;
  private static GameAreaDisplay craftArea;
  private static CameraComponent cameraComponent;
  private static SaveLoadService saveLoadService;
  private static MissionManager missions;
  private static PlanetOxygenService planetOxygenService;
  private static PlantCommandService plantCommandService;
  private static PlantInfoService plantInfoService;
  private static boolean cutSceneRunning; // true for running and false otherwise

  public static PlantCommandService getPlantCommandService() {
    return plantCommandService;
  }
  public static PlantInfoService getPlantInfoService() {
    return plantInfoService;
  }
  public static GameArea getGameArea() {
    return gameArea;
  }

  public static CameraComponent getCameraComponent() { return cameraComponent; }

  public static EntityService getEntityService() {
    return entityService;
  }

  public static RenderService getRenderService() {
    return renderService;
  }

  public static PhysicsService getPhysicsService() {
    return physicsService;
  }

  public static InputService getInputService() {
    return inputService;
  }

  public static ResourceService getResourceService() {
    return resourceService;
  }
  public static GameTime getTimeSource() {
    return timeSource;
  }
  public static TimeService getTimeService() {
    return timeService;
  }
  public static LightService getLightService() {
    return lightService;
  }

  public static MissionManager getMissionManager() {
    return missions;
  }
  public static PlanetOxygenService getPlanetOxygenService() {
    return planetOxygenService;
  }

  public static SaveLoadService getSaveLoadService() {
    return saveLoadService;
  }

  public static void registerGameArea(GameArea area) {
    logger.debug("Registering game area {}", area);
    gameArea = area;
  }

  public static void registerCameraComponent(CameraComponent camera) {
    logger.debug("Registering game area {}", camera);
    cameraComponent = camera;
  }

  public static void registerEntityService(EntityService service) {
    logger.debug("Registering entity service {}", service);
    entityService = service;
  }

  public static void registerRenderService(RenderService service) {
    logger.debug("Registering render service {}", service);
    renderService = service;
  }

  public static void registerPhysicsService(PhysicsService service) {
    logger.debug("Registering physics service {}", service);
    physicsService = service;
  }

  public static void registerTimeService(TimeService service) {
    logger.debug("Registering time service {}", service);
    timeService = service;
  }

  public static void registerInputService(InputService source) {
    logger.debug("Registering input service {}", source);
    inputService = source;
  }

  public static void registerResourceService(ResourceService source) {
    logger.debug("Registering resource service {}", source);
    resourceService = source;
  }

  public static void registerTimeSource(GameTime source) {
    logger.debug("Registering time source {}", source);
    timeSource  = source;
  }

  public static void setCutSceneRunning(boolean isRunning) {
    cutSceneRunning = isRunning;
  }

  public static boolean getCutSceneStatus() {
    return cutSceneRunning;
  }

  public static void registerMissionManager(MissionManager source) {
    logger.debug("Registering mission manager {}", source);
    missions = source;
  }

  public static void registerPlanetOxygenService(PlanetOxygenService source) {
    logger.debug("Registering planet oxygen service {}", source);
    planetOxygenService = source;
  }

  public static void registerPlantCommandService(PlantCommandService source) {
    logger.debug("Registering plant command service {}", source);
    plantCommandService = source;
  }

  public static void registerPlantInfoService(PlantInfoService source) {
    logger.debug("Registering plant command service {}",source);
    plantInfoService = source;
  }

  public static void registerLightService(LightService source) {
    logger.debug("Registering light service {}", source);
    lightService = source;
  }

  /**
   * Registers the save/load service.
   * @param source the service to register
   */
  public static void registerSaveLoadService(SaveLoadService source) {
    logger.debug("Registering Save/Load service {}", source);
    saveLoadService = source;
  }

  /**
   * Clears all registered services.
   * Do not clear saveLoadService
   */
  public static void clear() {
    entityService = null;
    renderService = null;
    physicsService = null;
    timeSource = null;
    inputService = null;
    resourceService = null;
    gameArea = null;
  }

  private ServiceLocator() {
    throw new IllegalStateException("Instantiating static util class");
  }

  public static void registerPauseArea(GameAreaDisplay area) {pauseMenuArea = area;}

  public static GameAreaDisplay getPauseMenuArea() {
    return pauseMenuArea;
  }

  public static void registerCraftArea(GameAreaDisplay area){
    craftArea = area;
  }


}
