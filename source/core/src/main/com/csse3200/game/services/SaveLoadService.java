package com.csse3200.game.services;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.tractor.TractorActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.files.SaveGame;
import com.csse3200.game.files.SaveGame.GameState;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.TractorFactory;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;
import com.csse3200.game.rendering.RenderComponent;

import static com.csse3200.game.areas.terrain.TerrainCropTileFactory.createTerrainEntity;



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
 *    - player inventory (partially)
 *    - time/day.
 *    - entity (cows and stuff) location & tamed status.
 * - Loads:
 *    - player pos.
 *    - time/day.
 */
public class SaveLoadService {
  /**
   * Saves the current state of the game into a GameState
   */
  public void save() {
    // Make a new GameState
    SaveGame.GameState state = new GameState();

    state.setDay(ServiceLocator.getTimeService().getDay());
    state.setHour(ServiceLocator.getTimeService().getHour());

    state.setPlayer(ServiceLocator.getGameArea().getPlayer());
    state.setEntities(ServiceLocator.getEntityService().getEntities());
    state.setTiles(ServiceLocator.getEntityService().getEntities());

    // Write the state to a file
    SaveGame.set(state);

    System.out.println("save!");
  }

  /**
   * Load function which based on conents in saveFile.json
   * Makes the game state match saveFile.json
   */
  public void load() {
    SaveGame.GameState state = SaveGame.get();
    if (state == null) {
      // TODO: Dialogue box or something?
      System.out.println("Couldn't read saveFile");
      return;
    }

    updateGame(state);
    System.out.println("load!");
  }

  /**
   * Check to see if there is a valid save file stored 
   * if not return false
   * 
   * @return
   */
  public boolean validSaveFile(){
    SaveGame.GameState state = SaveGame.get();
    if (state == null){
      return false;
    }
    return true;
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
    // Replace current entities with loaded ones:
    updateNPCs(state);
    updateTiles(state);
    updatePlayer(state);
    updateTime(state);
  }

  /**
   * Updates the player entity position based off the saved gamestate
   * 
   * @param state gamestate of the entire game based off safeFile.json
   */
  private void updatePlayer(GameState state) {
    Entity currentPlayer = ServiceLocator.getGameArea().getPlayer();
    currentPlayer.setPosition(state.getPlayer().getPosition());
    // TODO
    // These on load will take the player out of tractor so temp if we can figure out how to keep inside
    // (we would need to store a bool saying if player is in tractor
    // I know how to figure that out but don't know how to use it in the json or to store it
    currentPlayer.getComponent(PlayerActions.class).getCameraVar().setTrackEntity(currentPlayer);
    currentPlayer.getComponent(PlayerActions.class).setMuted(false);
    //currentPlayer.getComponent(PlayerActions.class).stopMoving();
  }

  /**
   * Destroys all NPCS in the map and then recreates them based off the gamestate
   * 
   * @param state gamestate of the entire game based off safeFile.json
   */
  private void updateNPCs(GameState state) {
    Entity player = ServiceLocator.getGameArea().getPlayer();
    // Create a map to associate entity types with NPC factory methods
    Map<EntityType, Function<Entity, Entity>> npcFactories = new HashMap<>();
    npcFactories.put(EntityType.Cow, NPCFactory::createCow);
    npcFactories.put(EntityType.Chicken, NPCFactory::createChicken);
    npcFactories.put(EntityType.Astrolotl, NPCFactory::createAstrolotl);
    npcFactories.put(EntityType.Tractor, TractorFactory::createTractor);

    for (Entity entity : state.getEntities()) {
      EntityType entityType = entity.getType();
      if (npcFactories.containsKey(entityType)) {
        Entity npc = npcFactories.get(entityType).apply(player);
        npc.setPosition(entity.getPosition());
        ServiceLocator.getGameArea().spawnEntity(npc);
        // TODO takes the player out of the tractor on load
        if (entityType == EntityType.Tractor) {
          npc.getComponent(TractorActions.class).setMuted(true);    //disable the tractor
        }
      }
    }
  }

  /**
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
    for (Entity cropTile : state.getTiles()) {
      // add render-component back & re-register:
      DynamicTextureRenderComponent renderComponent = new DynamicTextureRenderComponent("images/cropTile.png");
      renderComponent.setLayer(1);
      cropTile.addComponent(renderComponent);
      ServiceLocator.getEntityService().register(cropTile);
      // Stick cropTile onto terrainTile
      TerrainTile tile = map.getTile(cropTile.getPosition());
      tile.setCropTile(cropTile);
      tile.setOccupied();
    }
  }
}
