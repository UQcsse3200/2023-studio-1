package com.csse3200.game.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainCropTileFactory;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.components.npc.TamableComponent;
import com.csse3200.game.components.plants.PlantComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.tractor.TractorActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.files.SaveGame;
import com.csse3200.game.files.SaveGame.GameState;
import com.csse3200.game.missions.MissionManager;


/* A note of the registering of this service:
 *  this service is currently only registered at MainMenuScreen,
 *  (source/core/src/main/com/csse3200/game/screens/MainMenuScreen.java)
 *  for the moment I am unsure if this is sufficient.
 *  Some other services are registered in multiple places such as MainGameScreen.
 */

/* Current functionality:
 * - prints save/load to system out depending on method call.
 * - Saves:
 *    - player pos.
 *    - player inventory
 *    - time/day.
 *    - entity (cows and stuff) location & tamed status.
 *    - tiles (cropTileComponent and plants)
 * - Loads:
 *    - player pos.
 *    - time/day.
 *    - entity (cows and stuff) location & tamed status
 *    - tiles (cropTileComponent and plants)
 * After that we need wiki and sprint achievement form.
 */
public class SaveLoadService {
  private static final Logger logger = LoggerFactory.getLogger(SaveLoadService.class);
  /**
   * Saves the current state of the game into a GameState
   */
  public void save() {
    // Make a new GameState
    SaveGame.GameState state = new GameState();

    state.setDay(ServiceLocator.getTimeService().getDay());
    state.setHour(ServiceLocator.getTimeService().getHour());
    state.setClimate(ServiceLocator.getGameArea().getClimateController());

    state.setPlayer(ServiceLocator.getGameArea().getPlayer());
    state.setTractor(ServiceLocator.getGameArea().getTractor());
    state.setEntities(ServiceLocator.getEntityService().getEntities());
    state.setTiles(ServiceLocator.getEntityService().getEntities());

    // Write the state to a file
    SaveGame.set(state);

    logger.debug("The current game state has been saved to the file assets/saves/saveFile.json");
  }

  /**
   * Load function which based on contents in saveFile.json
   * Makes the game state match saveFile.json
   */
  public void load() {
    SaveGame.GameState state = SaveGame.get();
    if (state == null) {
      logger.error("Couldn't read the file assets/saves/saveFile.json");
      return;
    }

    updateGame(state);
    logger.debug("The game state has been loaded from the file assets/saves/saveFile.json");
  }

  /**
   * Check to see if there is a valid save file stored 
   * if not return false
   * 
   * @return true if there exists a valid save file, false otherwise
   */
  public boolean validSaveFile(){
    SaveGame.GameState state = SaveGame.get();
    return state != null;
  }

  /**
   * Update NPCs, player and time of the game based off the gamestate that was
   * saved
   * 
   * @param state state of the game which was saved previously in saveFile.json
   */
  private void updateGame(GameState state) {
    // Get all entities currently in game:
    Array<Entity> currentGameEntities = ServiceLocator.getEntityService().getEntities();
    // Remove entities from game that we will replace on load:
    ServiceLocator.getGameArea().removeLoadableEntities(currentGameEntities);
    // Update playable characters (Tractor and player)
    updatePlayer(state);
    updateTractor(state);
    // Remake and reload anything that could've been destroyed since last save.
    updateNPCs(state);
    updateTiles(state);
    // Update Misc
    updateClimate(state);
    updateTime(state);
    updateMissions(state);
  }

  private void updateClimate(GameState state) {
    ClimateController climate = ServiceLocator.getGameArea().getClimateController();
    climate.setHumidity(state.getClimate().getHumidity());
    climate.setTemperature(state.getClimate().getTemperature());
    if (state.getClimate().getCurrentWeatherEvent() != null) {
      climate.addWeatherEvent(state.getClimate().getCurrentWeatherEvent());
    }
  }

  /**
   * Updates the player entity position based off the saved GameState
   * 
   * @param state gamestate of the entire game based off safeFile.json
   */
  private void updatePlayer(GameState state) {
    Entity currentPlayer = ServiceLocator.getGameArea().getPlayer();
    currentPlayer.setPosition(state.getPlayer().getPosition());
    currentPlayer.getComponent(PlayerActions.class).getCameraVar().setTrackEntity(currentPlayer);
    currentPlayer.getComponent(PlayerActions.class).setMuted(false);
    for (Entity item : state.getPlayer().getComponent(InventoryComponent.class).getInventory()) {
      currentPlayer.getComponent(InventoryComponent.class).addItem(item);
    }
  }

  /**
   * Destroys all NPCS in the map and then recreates them based off the gamestate
   * 
   * @param state gamestate of the entire game based off safeFile.json
   */
  private void updateNPCs(GameState state) {
    Entity player = ServiceLocator.getGameArea().getPlayer();

    for (Entity entity : state.getEntities()) {
      EntityType entityType = entity.getType();
      if (FactoryService.getNpcFactories().containsKey(entityType)) {
        Entity npc = FactoryService.getNpcFactories().get(entityType).apply(player);
        npc.setPosition(entity.getPosition());
        // Non tameable npcs here
        if (entityType != EntityType.OxygenEater) {
          npc.getComponent(TamableComponent.class).setTame(entity.getComponent(TamableComponent.class).isTamed());
        }
        // TODO Team 4 please add in saving health here (feel free to talk to us but please read doc or code first)
        ServiceLocator.getGameArea().spawnEntity(npc);
      }
    }
  }

  /**
   * Update the tractors in-game position and check if the player was in the tractor or not
   *  on saving last
   * @param state GameState holding data to be loaded
   */
  private void updateTractor(GameState state){
    Entity tractor = ServiceLocator.getGameArea().getTractor(); // Get the tractor in the game
    Entity tractorState = state.getTractor();   // Get tractor entity stored within the json file

    //if there isn't a tractor currently in the game, return
    if (tractorState == null || tractor == null) {
      logger.error("Error, No tractor found!");
      return;
    }
    
    boolean inTractor = !tractorState.getComponent(TractorActions.class).isMuted();  // Store the inverse of the muted value from tractor state entity
    tractor.setPosition(tractorState.getPosition());   // Update the tractors position to the values stored in the json file
    
    // Check whether the player was in the tractor when they last saved
    if (inTractor) {
      // Set the player inside the tractor
      Entity player = ServiceLocator.getGameArea().getPlayer();
      player.setPosition(tractor.getPosition());              // Teleport the player to the tractor (Needed so that they are in 5 units of each other)
      player.getEvents().trigger("enterTractor");   // Trigger the enterTractor event
    }
  }


  /**''
   * Updates the time of the game based off the saved values in the gamestate
   * 
   * @param state the state of the saved game
   */
  private void updateTime(GameState state) {
    ServiceLocator.getTimeService().setDay(state.getDay());
    ServiceLocator.getTimeService().setHour(state.getHour());
  }

  /**
   * Recreates / loads tile back into map after a save.
   * @param state gamestate of the entire game based off safeFile.json
   */
  private void updateTiles(GameState state) {
    GameMap map = ServiceLocator.getGameArea().getMap();
    for (Entity savedCropTile : state.getTiles()) {
      CropTileComponent savedComponent = savedCropTile.getComponent(CropTileComponent.class);
      // Makes a new crop tile
      Entity newCropTile = TerrainCropTileFactory.createTerrainEntity(savedCropTile.getPosition());
      ServiceLocator.getGameArea().spawnEntity(newCropTile);
      CropTileComponent newComponent = newCropTile.getComponent(CropTileComponent.class);
      // Sets CropTileComponents values to GameState ones
      newComponent.setFertilised(savedComponent.isFertilised());
      newComponent.setWaterContent(savedComponent.getWaterContent());
      newComponent.setSoilQuality(savedComponent.getSoilQuality());
      if (savedComponent.getPlant() != null) {
        PlantComponent savedPlantComponent = savedComponent.getPlant().getComponent(PlantComponent.class);
        // Makes a new plant
        Entity plant = FactoryService.getPlantFactories().get(savedPlantComponent.getPlantName()).apply(newComponent);
        ServiceLocator.getGameArea().spawnEntity(plant);
        PlantComponent newPlantComponent = plant.getComponent(PlantComponent.class);
        // Sets PlantComponent values to GameState ones
        newPlantComponent.setGrowthStage(savedPlantComponent.getGrowthStage().getValue());
        newPlantComponent.setPlantHealth(savedPlantComponent.getPlantHealth());
        // Plant age does not exist. Plant team will sort this out later.
        //newPlantComponent.setCurrentAge(savedPlantComponent.getCurrentAge());
        // Sets plant to the CropTileComponent
        newComponent.setPlant(plant);
      }
      // Gets the Terrain tile from the map and set the crop tile to it
      TerrainTile tile = map.getTile(newCropTile.getPosition());
      tile.setCropTile(newCropTile);
    }
  }

  /**
   * Updates the missions based off the gamestate
   * @param state gamestate of the entire game based off safeFile.json
   */
  private void updateMissions(GameState state) {
    MissionManager missions = ServiceLocator.getMissionManager();
    // TODO Mission saving
    // Add in setting missions based off the ones that are done
  }
}
