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
 *  - !! if u place a pump it wont update the sprinklers around it to be powered, Fix by adding trigger for pump.
 *  - Domino effect power reconfiguration isn't implemented yet, shouldn't be hard.
 *  - Sometimes watering seems funny, prob due to the power problem mentioned above.
 *  - animate watering.
 *  - integrate with save&load.
 *  - move the notifyAdjacent and alike functions to their own component so fences can use them too.
 *  - need finished textures for all sprinkler bits and the pump. an led type indicator would be cool.
 */

public class SprinklerComponent extends Component {

  /**
   * Texture paths for different sprinkler orientation.
   * The order of this array is very important, correct order ensures a sprinkler gets the correct texture.
   */
  private static final String[] textures = {
          "images/placeable/pipe_null.png",
          "images/placeable/pipe_left.png",
          "images/placeable/pipe_right.png",
          "images/placeable/pipe_horizontal.png",
          "images/placeable/pipe_down.png",
          "images/placeable/pipe_down_left.png",
          "images/placeable/pipe_down_right.png",
          "images/placeable/pipe_down_triple.png",
          "images/placeable/pipe_up.png",
          "images/placeable/pipe_up_left.png",
          "images/placeable/pipe_up_right.png",
          "images/placeable/pipe_up_triple.png",
          "images/placeable/pipe_vertical.png",
          "images/placeable/pipe_left_triple.png",
          "images/placeable/pipe_right_triple.png",
          "images/placeable/pipe_quad.png"
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
    setAoe();                     // set area to sprinkle.
    setListeners();               // set co-ordinates to listen on (for other sprinklers to trigger).
    setAdjSprinklers();           // set adjacent sprinklers.
    configSprinkler();            // set power status and texture orientation

    // set to sprinkle every 30 seconds.
    ServiceLocator.getTimeService().getEvents().addListener("30sec", this::sprinkle);

    // TODO: [DEBUGGING]
    System.out.println(this.entity+" power: "+isPowered+", adjSprinklers: "+Arrays.toString(this.adjSprinklers));
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
   * Sets the coordinates for the watering area-of-effect.
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
  }

  /**
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

  //TODO make this more efficient and remove magic number 4.
  private void setListeners() {
    // listen for any new sprinklers placed in this spot in-future:
    for (int i = 0; i < 4; i++) {
      int dir = i * 2;  // Map adjSprinkler index to aoe index.
      System.out.println("listening for: " + "placed:" + aoe[dir]);
      addPlacementListener(aoe[dir]);
    }
  }

  /**
   * Adds listeners for newly placed sprinklers on adjacent tiles
   */
  private void addPlacementListener(Vector2 pos) {
    entity.getEvents().addListener("placed:" + pos, this::reConfigure);
  }

  /**
   * To be called after spawned in the word
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
   * TODO: Add more desc.
   */
  public void reConfigure() {
    boolean prevPowerState = this.isPowered;
    setAdjSprinklers();
    configSprinkler();
    if (prevPowerState != this.isPowered) {
      // powered status has changed we should reconfigure adjacent sprinklers too
      for (Entity sprinkler : this.adjSprinklers) { //TODO un-tested and not efficient
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
    int orientation = 0b0000;   // 4 bits representing adjacent sprinklers, used as an index to select a texture.
    for (Entity sprinkler : this.adjSprinklers) {
      if (sprinkler != null) {
        // Sets this sprinkler to powered if adj sprinkler is powered, but never sets to this.isPowered to false.
        this.isPowered = sprinkler.getComponent(SprinklerComponent.class).getPowered() || this.isPowered;
        // Set the bit and shift along.
        orientation |= 0b0001;
      }
      orientation <<= 1;
    }
    // now set the texture.
    orientation >>= 1; // little ugly but I've shifted 1 too many times and need to shift back, or I go out-of-bounds.
    entity.getComponent(DynamicTextureRenderComponent.class).setTexture(textures[orientation]);
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