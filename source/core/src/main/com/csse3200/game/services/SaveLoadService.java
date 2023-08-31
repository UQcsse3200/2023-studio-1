package com.csse3200.game.services;

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
    System.out.println("save!");
  }
  public void load() {
    System.out.println("load!");
  }
}
