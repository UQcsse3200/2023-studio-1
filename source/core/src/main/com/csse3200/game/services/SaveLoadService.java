package com.csse3200.game.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.AuraLightComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.tractor.TractorActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.files.SaveGame;
import com.csse3200.game.files.SaveGame.GameState;

import java.io.File;
import java.util.HashMap;


/* A note of the registering of this service:
 *  this service is currently only registered at MainMenuScreen,
 *  (source/core/src/main/com/csse3200/game/screens/MainMenuScreen.java)
 *  for the moment I am unsure if this is sufficient.
 *  Some other services are registered in multiple places such as MainGameScreen.
 */
public class SaveLoadService {
    private static final Logger logger = LoggerFactory.getLogger(SaveLoadService.class);
    private static final String ROOT_DIR = "saves";
    private static final String SAVE_FILE = "saveFile.json";

    /**
     * Saves the current state of the game into a GameState
     */
    public void save() {
        // Make a new GameState
        SaveGame.GameState state = new GameState();

        state.setDay(ServiceLocator.getTimeService().getDay());
        state.setHour(ServiceLocator.getTimeService().getHour());
        state.setMinute(ServiceLocator.getTimeService().getMinute());

        state.setClimate(ServiceLocator.getGameArea().getClimateController());
        state.setMissions(ServiceLocator.getMissionManager());

        state.setPlayer(ServiceLocator.getGameArea().getPlayer());
        state.setTractor(ServiceLocator.getGameArea().getTractor());

        state.setEntities(ServiceLocator.getEntityService().getEntities());
        state.setTiles(ServiceLocator.getEntityService().getEntities());
        state.setPlaceables(ServiceLocator.getEntityService().getEntities());

        // Write the state to a file
        SaveGame.set(state);

        logger.debug("The current game state has been saved to the file assets/saves/saveFile.json");
    }

    /**
     * Load function which based on contents in saveFile.json
     * Makes the game state match saveFile.json
     */
    public void load(String path) {
        // Get all entities currently in game:
        Array<Entity> currentGameEntities = ServiceLocator.getEntityService().getEntities();
        // Remove them
        ServiceLocator.getGameArea().setTractor(null);
        ServiceLocator.getGameArea().removeLoadableEntities(currentGameEntities);

        SaveGame.GameState state = SaveGame.get(path);
        if (state == null) {
            logger.error("Couldn't read the file assets/saves/saveFile.json");
            return;
        }

        updateGame(state);
        logger.debug("The game state has been loaded from the file assets/saves/saveFile.json");
    }

    public void load() {
        load(ROOT_DIR + File.separator + SAVE_FILE);
    }

    /**
     * Check to see if there is a valid save file stored
     * if not return false
     *
     * @return true if there exists a valid save file, false otherwise
     */
    public boolean validSaveFile(){
        return validSaveFile(ROOT_DIR + File.separator + SAVE_FILE);
    }

    public boolean validSaveFile(String path) {
        SaveGame.GameState state = SaveGame.get(path);
        return state != null;
    }

    /**
     * Update NPCs, player and time of the game based off the gamestate that was
     * saved
     *
     * @param state state of the game which was saved previously in saveFile.json
     */
    private void updateGame(GameState state) {
        // Update playable characters (Tractor and player)
        updatePlayer(state);
        updateTractor(state);
        // Update Misc
        updateTime(state);
        updateMissions(state);
    }

    /**
     * Updates the player entity position based off the saved GameState
     *
     * @param state gamestate of the entire game based off safeFile.json
     */
    private void updatePlayer(GameState state) {
        Entity currentPlayer = ServiceLocator.getGameArea().getPlayer();
        currentPlayer.setPosition(state.getPlayer().getPosition());
        ServiceLocator.getCameraComponent().setTrackEntity(currentPlayer);
        currentPlayer.getComponent(PlayerActions.class).setMuted(false);
        InventoryComponent stateInventory = state.getPlayer().getComponent(InventoryComponent.class);
        HashMap<String, Integer> itemCount = stateInventory.getItemCount();
        HashMap<String, Entity> heldItemsEntity = stateInventory.getHeldItemsEntity();
        HashMap<Integer, String> itemPlace = stateInventory.getItemPlace();
        currentPlayer.getComponent(InventoryComponent.class).loadInventory(itemCount, heldItemsEntity, itemPlace);
    }

    /**
     * Update the tractors in-game position and check if the player was in the tractor or not
     *  on saving last
     * @param state GameState holding data to be loaded
     */
    private void updateTractor(GameState state){
        Entity tractor = ServiceLocator.getGameArea().getTractor(); // Get the tractor in the game

        //if there isn't a tractor currently in the game, return
        if (tractor == null) {
            logger.info("No tractor found!");
            return;
        }

        boolean inTractor = !tractor.getComponent(TractorActions.class).isMuted();  // Store the inverse of the muted value from tractor state entity
        // Check whether the player was in the tractor when they last saved
        if (inTractor) {
            // Set the player inside the tractor
            Entity player = ServiceLocator.getGameArea().getPlayer();
            player.setPosition(tractor.getPosition());              // Teleport the player to the tractor (Needed so that they are in 5 units of each other)
            player.getEvents().trigger(PlayerActions.events.ENTER_TRACTOR.name());   // Trigger the enterTractor event
            tractor.getComponent(AuraLightComponent.class).toggleLight();
        }
    }

    /**''
     * Updates the time of the game based off the saved values in the gamestate
     *
     * @param state the state of the saved game
     */
    private void updateTime(GameState state) {
        ServiceLocator.getTimeService().loadTime(state.getDay(), state.getHour(), state.getDay());
    }

    /**
     * Updates the missions based off the gamestate
     * @param state gamestate of the entire game based off safeFile.json
     */
    private void updateMissions(GameState state) {
        ServiceLocator.registerMissionManager(state.getMissions());
    }
}
