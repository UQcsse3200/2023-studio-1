package com.csse3200.game.components.placeables;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * Current functionality:
 * - when a sprinkler is placed next to a power source it will water its AOE and act as a power source for any
 *   other sprinklers that are placed next to it in future.
 * - when a sprinkler is placed it will identify any adjacent sprinklers and obtain a texture to illustrate its
 *    connected configuration.
 *    It will also set its powered status based off the sprinklers around it.
 * - sprinklers sprinkle every 'minute'. (however long a minute in TimeService is).
 *
 * TODO:
 *  - when a pump is placed next to an unpowered sprinkler, that sprinkler doesn't update its power status like it should.
 *    - most likely easy fix but not sure the cause.
 *  - animate watering.
 *  - integrate with save&load.
 *  - move the notifyAdjacent and alike functions to their own component so fences can use them too.
 *  - need finished textures for all sprinkler bits and the pump.
 */

public class SprinklerComponent extends Component {

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
  private boolean pump;

  /**
   * The coordinates where this sprinkler expects to find adjacent sprinklers.
   */
  private Vector2[] adjCoords;

  /**
   * An ordered list of adjacent sprinklers or pumps,
   * if an adjacent tile does not hava a sprinkler/pump, null is placed in that index.
   * The order is: [ Above, Below, Right, Left ].
   */
  private Entity[] adjSprinklers;

  /**
   * A sprinklers area of effect to water, aoe is circular with radius of 2.
   */
  private Vector2[] aoe;

  /**
   * {@inheritDoc}
   */
  public void create() {
    // Init class vars:
    this.adjCoords        = new Vector2[4];
    this.adjSprinklers    = new Entity[4];
    this.aoe              = new Vector2[12];
    // Set class vars:
    setAdjCoords();
    setAdjSprinklers(); // TODO: i could potentially scrap this for just coords, this might be good for fence compatibility.
    // A pump doesn't need any more configuring.
    if (!this.pump) {
      setPlacementListeners();    // set co-ordinates to listen on (for other sprinklers to trigger).
      configSprinkler();          // set power status and texture orientation.
      setAoe();
      // set to sprinkle every minute
      ServiceLocator.getTimeService().getEvents().addListener("minuteUpdate", this::sprinkle);
    }
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
   * Sets the coordinates that this sprinkler/pump expects to find adjacent sprinkler/pumps in.
   */
  private void setAdjCoords() {
    float x = entity.getPosition().x, y = entity.getPosition().y;
    this.adjCoords[0] = new Vector2(x, y+1); // Above
    this.adjCoords[1] = new Vector2(x, y-1); // Below
    this.adjCoords[2] = new Vector2(x+1, y); // Right
    this.adjCoords[3] = new Vector2(x-1, y); // Left
  }

  /**
   * set the coordinates for expected adjacent sprinklers.
   * Sets the coordinates for the watering area-of-effect and
   * The watering AOE is this sprinklers position +2 in all directions, +1 in diagonals,
   * this creates a circular watering effect.
   */
  private void setAoe() {
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
   * Adds listeners to listen for newly placed sprinklers on adjacent tiles.
   * When a new sprinkler is placed in this sprinklers vicinity reConfigure() is called.
   */
  private void setPlacementListeners() {
    // listen for any new sprinklers placed in vicinity.
    for (Vector2 pos : this.adjCoords) {
      entity.getEvents().addListener("placed:" + pos, this::reConfigure);
    }
  }

  /** TODO: maybe change method of populating this.adjSprinklers. so we dont have ItemActions calling triggers.
   * Adds directly adjacent sprinklers excluding diagonals to a class array to aid configuration.
   * After this call, each index in this.adjSprinklers will either be a sprinkler/pump entity or null.
   */
  private void setAdjSprinklers() {
    for (int i = 0; i < 4; i++) {
      Entity s = ServiceLocator.getGameArea().getMap().getTile(this.adjCoords[i]).getPlaceable();
      if (s != null) {
        if (s.getComponent(SprinklerComponent.class) == null) {
          s = null;
        }
      }
      this.adjSprinklers[i] = s;
    }
  }

  /**
   * To be called after spawned in the world - Called in ItemActions -> place.
   * Tells all adjacent sprinklers to go reconfigure their power status and orientation.
   * (causes sprinklers in this.adjSprinklers to call reConfigure()).
   */
  public void notifyAdjacent() {
    // TODO: we could just use the coordinates from this.adjCoords here - more code but more compatible.
    // i will consider this
    for (Entity sprinkler : this.adjSprinklers) {
      if (sprinkler != null) {
        sprinkler.getEvents().trigger("placed:" + entity.getPosition());
      }
    }
  }

  /**
   * Called via the "placed:(x,y)" trigger - when a new sprinkler is placed in this sprinklers' vicinity.
   * Re-configures using configSprinkler() and checks if a change of state has occurred.
   * If a change of state has occurred then this sprinkler tells all its adjacent sprinklers to
   * check themselves for a state change via reConfigure().
   */
  public void reConfigure() {
    if (this.pump) return;  // A pump shouldn't reconfigure.
    // Save old state
    boolean prevPowerState = this.isPowered;
    // Reconfigure
    setAdjSprinklers();
    configSprinkler();
    // Check for change of state
    if (prevPowerState != this.isPowered) {
      // powered status has changed we should reconfigure adjacent sprinklers too
      for (Entity sprinkler : this.adjSprinklers) {
        if (sprinkler != null) {
          sprinkler.getComponent(SprinklerComponent.class).reConfigure();
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