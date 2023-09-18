package com.csse3200.game.components.placeables;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.Arrays;

/*
 * Current functionality:
 * - when a sprinkler is placed next to a power source it will water its AOE and act as a power source for any
 *   other sprinklers that are placed next to it in future.
 * - when a sprinkler is placed it will identify any adjacent sprinklers and obtain a texture to illustrate its
 *    connected configuration.
 * - sprinklers sprinkle every 30 seconds.
 *
 * TODO:
 *  - BUG: place sprinkler, then pump next to, the sprinkler diagonal to fist, then connect in diagonal and game crash.
 *    - this happens because pumps aren't set up to be updated through events, pretty sure.
 *      - temp solution is adding a check for pump in configSprinkler.
 *      - proper solution is to add events for pump.
 *  - !! if u place a pump it wont update the sprinklers around it to be powered, Fix by adding trigger for pump.
 *  - Domino effect power reconfiguration is not fully working, but is almost there!
 *  - Sometimes watering seems funny, prob due to the power problem mentioned above.
 *  - animate watering.
 *  - integrate with save&load.
 *  - move the notifyAdjacent and alike functions to their own component so fences can use them too.
 *  - need finished textures for all sprinkler bits and the pump.
 */

public class SprinklerComponent extends Component {

  /**
   * Texture paths for different sprinkler orientation.
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
  private boolean pump;

  /**
   * The coordinates where this sprinkler expects to find adjacent sprinklers.
   */
  private Vector2[] adjCoords;

  /**
   * A sprinklers area of effect to water, aoe is circular with radius of 2.
   */
  private Vector2[] aoe;

  /**
   * An ordered list of adjacent sprinklers or pumps,
   * if an adjacent tile does not hava a sprinkler/pump, null is placed in that index.
   * The order is: [ Above, Below, Right, Left ].
   */
  private Entity[] adjSprinklers;

  public void create() {
    // If this is a pump we just need to turn its power on:
    if (this.pump) {
      this.isPowered = true;
      return;
    }
    // Sprinklers need configuring:
    setAoe();                        // set area to sprinkle.
    setPlacementListeners();         // set co-ordinates to listen on (for other sprinklers to trigger).
    setAdjSprinklers();              // set adjacent sprinklers.
    configSprinkler();               // set power status and texture orientation

    // set to sprinkle every 30 seconds.
    ServiceLocator.getTimeService().getEvents().addListener("30sec", this::sprinkle);
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
  }

  /**
   * Sets the coordinates for the watering area-of-effect and
   * set the coordinates for expected adjacent sprinklers.
   * Coordinates are this sprinklers position +2 in all directions, +1 in diagonals.
   */
  private void setAoe() {
    // if there is a smarter way to get these co-ords im listening.
    float x = entity.getPosition().x, y = entity.getPosition().y;
    this.aoe = new Vector2[] {
            // 2up, 2down, 2right, 2left.
            new Vector2(x, y+1),
            new Vector2(x, y+2),
            new Vector2(x, y-1),
            new Vector2(x, y-2),
            new Vector2(x+1, y),
            new Vector2(x+2, y),
            new Vector2(x-1, y),
            new Vector2(x-2, y),
            // top right, bottom right, top left, bottom left.
            new Vector2(x+1, y+1),
            new Vector2(x+1, y-1),
            new Vector2(x-1, y+1),
            new Vector2(x-1, y-1)
    };
    this.adjCoords = new Vector2[] {
            new Vector2(x, y+1), // Above
            new Vector2(x, y-1), // Below
            new Vector2(x+1, y), // Right
            new Vector2(x-1, y)  // Left
    };
  }

  /** TODO: improve this, maybe change method of populating this.adjSprinklers.
   * Sets and adds listeners for directly adjacent sprinklers excluding diagonals.
   * After this call, each index in this.adjSprinklers will either be a sprinkler/pump entity or null.
   */
  private void setAdjSprinklers() {
    final int total = 4;
    this.adjSprinklers = new Entity[total];
    for (int i = 0; i < total; i++) {
      int dir = i * 2;  // Map adjSprinkler index to aoe index.
      Entity p = ServiceLocator.getGameArea().getMap().getTile(aoe[dir]).getPlaceable();
      if (p != null) {
        if (p.getComponent(SprinklerComponent.class) == null) {
          p = null;
        }
      }
      this.adjSprinklers[i] = p;
    }
  }

  /**
   * Adds listeners to listen for newly placed sprinklers on adjacent tiles.
   */
  private void setPlacementListeners() {
    // listen for any new sprinklers placed in this spot in-future:
    for (Vector2 pos : this.adjCoords) {
      entity.getEvents().addListener("placed:" + pos, this::reConfigure);
    }
  }

  /**
   * To be called after spawned in the world - Called in ItemActions -> place.
   * Tells all adjacent sprinklers to go reconfigure their power status and orientation.
   */
  public void notifyAdjacent() {
    if (pump) { // need to protect from pumps because setPump is public.
      return;
    }
    // This triggers this sprinkles adjacent sprinklers to call reConfigure().
    for (Entity sprinkler : this.adjSprinklers) {
      if (sprinkler != null) {
        sprinkler.getEvents().trigger("placed:" + entity.getPosition());
      }
    }
  }

  /**
   * Re-configures using configSprinkler and checks if a change of state has occurred.
   * If a change of state has occurred then call notifyAdjacent().
   * TODO: Add more desc, fix bug where chain reconfiguring doesn't reach all necessary sprinklers.
   */
  public void reConfigure() {
    // Save old state
    boolean prevPowerState = this.isPowered;
    // Reconfigure
    setAdjSprinklers();
    configSprinkler();
    // Check for change of state
    if (prevPowerState != this.isPowered) {
      // powered status has changed we should reconfigure adjacent sprinklers too
      for (Entity sprinkler : this.adjSprinklers) { // TODO: temp solution, to be improved.
        if (sprinkler != null) {
          sprinkler.getComponent(SprinklerComponent.class).configSprinkler();
        }
      }
    }
  }


  /**
   * Sets powered status and texture 'orientation' by scanning the adjacent sprinklers/pump.
   *  - A power source is either a pump or a powered sprinkler.
   *  - A texture is selected for this sprinkler based on the surrounding sprinklers,
   *    this illustrates to the player that these sprinklers are connected - like pipes.
   *    - Available textures are (16 in total):
   *        * straight horizontal
   *        * straight vertical
   *        * 90 degree bend in any orientation
   *        * 3 way connection in any orientation
   *        * 4 way connection
   *        * + singular connections in each orientation
   */
  public void configSprinkler() {
    if (this.pump) return; // TODO TEMP FIX - prevents game crash, see line 21.
    int orientation = 0b0000;   // 4 bits representing adjacent sprinklers, used as an index to select a texture.
    for (Entity sprinkler : this.adjSprinklers) {
      orientation <<= 1;
      if (sprinkler != null) {
        // Sets this sprinkler to powered if adj sprinkler is powered, but never sets to this.isPowered to false.
        this.isPowered = sprinkler.getComponent(SprinklerComponent.class).getPowered() || this.isPowered;
        // Set the bit and shift along.
        orientation |= 0b0001;
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