package com.csse3200.game.components.placeables;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;


/*
 * TODO:
 *  - get sprinkler powered/un-powered updates working.
 *  - animate watering.
 *  - integrate with save&load.
 *  - need finished textures for all sprinkler bits and the pump.
 */

public class SprinklerComponent extends Component {

  /**
   * Handles updates to the adjacent sprinklers.
   */
  private ConnectedEntityComponent connectedEntityComponent;

  /**
   * Texture paths for different sprinkler orientation. (and powered and non-powered)
   * The order of this array is very important, correct order ensures a sprinkler gets the correct texture.
   */
  private static final String[] textures_on = {
          "images/placeable/sprinkler/pipe_null.png",
          "images/placeable/sprinkler/on/pipe_left.png",
          "images/placeable/sprinkler/on/pipe_right.png",
          "images/placeable/sprinkler/on/pipe_horizontal.png",
          "images/placeable/sprinkler/on/pipe_down.png",
          "images/placeable/sprinkler/on/pipe_down_left.png",
          "images/placeable/sprinkler/on/pipe_down_right.png",
          "images/placeable/sprinkler/on/pipe_down_triple.png",
          "images/placeable/sprinkler/on/pipe_up.png",
          "images/placeable/sprinkler/on/pipe_up_left.png",
          "images/placeable/sprinkler/on/pipe_up_right.png",
          "images/placeable/sprinkler/on/pipe_up_triple.png",
          "images/placeable/sprinkler/on/pipe_vertical.png",
          "images/placeable/sprinkler/on/pipe_left_triple.png",
          "images/placeable/sprinkler/on/pipe_right_triple.png",
          "images/placeable/sprinkler/on/pipe_quad.png"
  };
  private static final String[] textures_off = {
          "images/placeable/sprinkler/pipe_null.png",
          "images/placeable/sprinkler/off/pipe_left.png",
          "images/placeable/sprinkler/off/pipe_right.png",
          "images/placeable/sprinkler/off/pipe_horizontal.png",
          "images/placeable/sprinkler/off/pipe_down.png",
          "images/placeable/sprinkler/off/pipe_down_left.png",
          "images/placeable/sprinkler/off/pipe_down_right.png",
          "images/placeable/sprinkler/off/pipe_down_triple.png",
          "images/placeable/sprinkler/off/pipe_up.png",
          "images/placeable/sprinkler/off/pipe_up_left.png",
          "images/placeable/sprinkler/off/pipe_up_right.png",
          "images/placeable/sprinkler/off/pipe_up_triple.png",
          "images/placeable/sprinkler/off/pipe_vertical.png",
          "images/placeable/sprinkler/off/pipe_left_triple.png",
          "images/placeable/sprinkler/off/pipe_right_triple.png",
          "images/placeable/sprinkler/off/pipe_quad.png"
  };

  /**
   * Powered status of sprinkler.
   */
  private boolean isPowered;

  /**
   * Indicates if the 'sprinkler' is actually just a pump.
   * A pump is a sprinkler that doesn't update or sprinkle but is powered.
   */
  protected boolean pump;

  /**
   * A sprinklers area of effect to water, aoe is circular with radius of 2.
   */
  private Vector2[] aoe;

  /**
   * {@inheritDoc}
   */
  public void create() {
    // Init class vars:
    this.connectedEntityComponent = new ConnectedEntityComponent(entity);

    // A pump doesn't need any more configuring.
    if (!this.pump) {
      configSprinkler();          // set power status and texture orientation.
      setAoe();
      // Listen for reconfigure requests:
      entity.getEvents().addListener("reconfigure", this::reConfigure);
      // set to sprinkle every minute
      ServiceLocator.getTimeService().getEvents().addListener("minuteUpdate", this::sprinkle);
    }
    this.connectedEntityComponent.notifyAdjacent();
  }

  /**
   * Allows other classes to query if this sprinkler has power.
   * @return powered status
   */
  public boolean getPowered() {
    return isPowered;
  }

  /**
   * Sets this sprinkler to be a pump.
   * Should only be called in PlaceableFactory.
   */
  public void setPump() {
    this.pump = true;
    this.isPowered = true;
  }

  /**
   * Sets powered status and texture 'orientation' based off the adjacent sprinklers.
   *  - A power source is either a pump or a powered sprinkler.
   *  - A texture is selected for this sprinkler based on the surrounding sprinklers,
   *    this illustrates to the player that these sprinklers are connected - like pipes.
   *    TODO config power
   */
  public void configSprinkler() {
    // // try to find a path to a pump
    // //this.isPowered = findPump(this.entity);
    // for a placed sprinkler we just need to check if any surrounding sprinklers are powered...
    byte orientation = 0b0000;
    for (Entity s : this.connectedEntityComponent.getAdjacentEntities()) {
      orientation <<= 1;
      if (s != null) {
        orientation |= 0b0001;
        this.isPowered |= s.getComponent(SprinklerComponent.class).getPowered();
      }
    }
    // now set the texture.
    if (this.isPowered) {
      entity.getComponent(DynamicTextureRenderComponent.class).setTexture(textures_on[orientation]);
    } else {
      entity.getComponent(DynamicTextureRenderComponent.class).setTexture(textures_off[orientation]);
    }
  }

  /**
   * Called via ConnectedEntityComponent's "reconfigure" trigger -
   * This trigger is called when a new sprinkler is placed in this sprinklers' vicinity.
   * Re-configures using configSprinkler() and //TODO used to: check if a change of state has occurred.
   * If a change of state has occurred then this sprinkler tells all its adjacent sprinklers to
   * check themselves for a state change via reConfigure().
   */ // TODO: restore functionality
  public void reConfigure() {
    if (this.pump) return;  // A pump shouldn't reconfigure.
    boolean prevPowerState = this.isPowered;
    configSprinkler();
    if (prevPowerState != this.isPowered) {
      // we need to tell the others -- this will even check the one that called us... a single useless iteration
      for (Entity sprinkler : this.connectedEntityComponent.getAdjacentEntities()) {
        if (sprinkler != null) sprinkler.getComponent(SprinklerComponent.class).reConfigure();
          //sprinkler.getEvents().trigger("reconfigure"); // TODO why does this line cause crash?
      }
    }
  }

  /**
   * finds a path to a pump, returns true if path found, false otherwise.
   * @param calling the sprinkler calling (used to we don't loop forever)
   * @return truth value of weather we found a path to a pump or not.
   */
  protected boolean findPump(Entity calling) {
    // could do some recursion to find a pump, but we will run into lots of issues.
    return true;
  }

  /**
   * Sets the coordinates for the watering area-of-effect.
   * The watering AOE is this sprinklers position +2 in all directions, +1 in diagonals,
   * this creates a circular watering effect.
   */
  private void setAoe() {
    this.aoe = new Vector2[12];
    float x = entity.getPosition().x, y = entity.getPosition().y;
    this.aoe = new Vector2[]{
            // 2up, 2down, 2right, 2left.
            new Vector2(x, y + 1),
            new Vector2(x, y + 2),
            new Vector2(x, y - 1),
            new Vector2(x, y - 2),
            new Vector2(x + 1, y),
            new Vector2(x + 2, y),
            new Vector2(x - 1, y),
            new Vector2(x - 2, y),
            // top right, bottom right, top left, bottom left.
            new Vector2(x + 1, y + 1),
            new Vector2(x + 1, y - 1),
            new Vector2(x - 1, y + 1),
            new Vector2(x - 1, y - 1)
    };
  }

  /**
   * Waters the aoe, this is relevant to this sprinklers position and looks like:
   * * 2 tiles: above, below, left, right.
   * * 1 tile in each diagonal.
   * Creating a circular watering effect.
   */
  private void sprinkle() {
    if (!isPowered) return;
    for (Vector2 pos : aoe) {
      TerrainTile tt = ServiceLocator.getGameArea().getMap().getTile(pos);
      if (tt.getCropTile() != null) {
        tt.getCropTile().getEvents().trigger("water", 0.5f);
      }
    }
  }
}