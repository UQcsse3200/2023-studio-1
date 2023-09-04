package com.csse3200.game.services;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.files.SaveGame;
import com.csse3200.game.files.SaveGame.GameState;

/* A note of the registering of this service:
 *  this service is currently only registered at MainMenuScreen,
 *  (source/core/src/main/com/csse3200/game/screens/MainMenuScreen.java)
 *  for the moment I am unsure if this is sufficient.
 *  Some other services are registered in multiple places such as MainGameScreen.
 */

/* Current functionality:
 * - prints save/load to system out depending on method call.
 */
public class SaveLoadService {

  public void save() {
    // Make a new GameState
    SaveGame.GameState state = new GameState();

    state.setDay(ServiceLocator.getTimeService().getDay());
    state.setHour(ServiceLocator.getTimeService().getHour());

    state.setPlayer(ServiceLocator.getGameArea().getPlayer());
    state.setEntities(ServiceLocator.getEntityService().getEntities());

    // Write the state to a file
    SaveGame.set(state);

    System.out.println("save!");
  }

  public void load() {
    SaveGame.GameState state = SaveGame.get();

    if (state == null) {
      // TODO: Dialogue box or something?
      System.out.println("NO FILE TO LOAD!");
      return;
    }

    updateGame(state);
    System.out.println("load!");
  }

  private void updateGame(GameState state) {
    updateNPCs(state);
    updatePlayer(state);
    updateTime(state);
  }

  private void updatePlayer(GameState state) {
    Entity currentPlayer = ServiceLocator.getGameArea().getPlayer();
    currentPlayer.setPosition(state.getPlayer().getPosition());
    System.out.println(currentPlayer.getPosition());
    System.out.println(state.getPlayer().getPosition());
  }

  private void updateNPCs(GameState state) {
    EntityService entityService = ServiceLocator.getEntityService();
    entityService.disposeNPCs();
    for (Entity entity : state.getEntities()) {
      if (entity.getType() == EntityType.Cow) {
        //TODO get either spawnentity at working or set pos
        ServiceLocator.getGameArea().spawnEntity(entity);
        System.out.println("cow spawned");

      }
    }
  }



  private void updateTime(GameState state) {
    ServiceLocator.getTimeService().setDay(state.getDay());
    ServiceLocator.getTimeService().setHour(state.getHour());
  }
}
