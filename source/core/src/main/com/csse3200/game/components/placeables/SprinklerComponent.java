package com.csse3200.game.components.placeables;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.plants.PlantComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


public class SprinklerComponent extends Component {

  /**
   * Handles updates to the adjacent sprinklers.
   */
  private ConnectedEntityUtility connectedEntityUtility;

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
  protected Vector2[] aoe;

  /**
   * {@inheritDoc}
   */
  @Override
  public void create() {
    // Create a list of the adjacent sprinklers:
    this.connectedEntityUtility = new ConnectedEntityUtility(entity);
    if (!this.pump) {
      // Configure the sprinklers dynamic components:
      configSprinkler();
      setAoe();
      // Add listener for reconfigure requests:
      entity.getEvents().addListener("reconfigure", this::reConfigure);
      // set to sprinkle every minute:
      ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::sprinkle);
    }
    // Update adjacent sprinklers:
    this.connectedEntityUtility.notifyAdjacent();
  }

  /**
   * Allows other sprinklers to query if this sprinkler has power.
   * @return powered status
   */
  protected boolean getPowered() {
    return isPowered;
  }

  /**
   * Allows other sprinklers to set the power of this sprinkler.
   */
  protected void setPower(boolean state) {
    this.isPowered = state;
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
   * Returns true if this sprinkler is a pump.
   * @return pump truth value
   */
  public boolean getPump() {
    return this.pump;
  }

  /**
   * Getter for adj entity list
   * @return array of adj entities
   */
  protected Entity[] getAdjList() {
    return this.connectedEntityUtility.getAdjacentEntities();
  }

  /**
   * Sets powered status and texture 'orientation' based off the adjacent sprinklers.
   *  - A power source is either a pump or a powered sprinkler.
   *  - A texture is selected for this sprinkler based on the surrounding sprinklers,
   *    this illustrates to the player that these sprinklers are connected - like pipes.
   */
  public void configSprinkler() {
    // Find suitable texture and configure power status based off of the adjacent:
    byte orientation = 0b0000;
    for (Entity s : this.connectedEntityUtility.getAdjacentEntities()) {
      orientation <<= 1;
      if (s != null) {
        orientation |= 0b0001;
        this.isPowered |= s.getComponent(SprinklerComponent.class).getPowered();
      }
    }
    // Now set the texture:
    setTexture(this.isPowered, orientation);
  }

  /**
   * Helper method to set the texture of this sprinkler, using a given power status and orientation
   * @param powerStatus powered status of sprinkler, used for texture selection
   * @param orientation orientation of sprinkler based off of adjacent sprinklers.
   */
  protected void setTexture(boolean powerStatus, byte orientation) {
    if (powerStatus) {
      entity.getComponent(DynamicTextureRenderComponent.class).setTexture(textures_on[orientation]);
    } else {
      entity.getComponent(DynamicTextureRenderComponent.class).setTexture(textures_off[orientation]);
    }
  }

  /**
   * Called via ConnectedEntityComponent's "reconfigure" trigger -
   * This trigger is called when a new sprinkler is placed in this sprinklers' vicinity.
   * Re-configures using pathfinding (see findPump() and notifyConnected() methods).
   */
  public void reConfigure() {
    // A pump doesn't need to reconfigure, it's power and texture are constant, and cannot be effected.
    if (this.pump) return;
    // Identify if this sprinkler is connected to a pump,
    // Then configure every sprinkler in range with that result:
    notifyConnected(findPump(this.entity));
    // Update this sprinklers texture:
    setTexture(this.isPowered, this.connectedEntityUtility.getAdjacentBitmap());
  }

  /**
   * finds a path to a pump, returns true if path found, false otherwise.
   * @param calling the sprinkler calling.
   * @return truth value of weather we found a path to a pump or not.
   */
  protected boolean findPump(Entity calling) {
    // BFS with a queue
    Queue<Entity> queue = new LinkedList<>();
    ArrayList<Entity> visited = new ArrayList<>();
    queue.offer(calling);

    while (!queue.isEmpty()) {
      calling = queue.poll();
      for (Entity s : calling.getComponent(SprinklerComponent.class).getAdjList()) {
        if (s != null && !visited.contains(s)) {
          // If we find a pump we return here:
          if (s.getComponent(SprinklerComponent.class).getPump()) return true;
          queue.offer(s);
          visited.add(s);
        }
      }
    }
    return false;
  }

  /**
   * Uses pathfinding to set all connected sprinkles (from this.entity) to the given powerStatus &
   * sets their texture appropriately.
   * @param powerStatus the truth value used to set all sprinklers found via search.
   */
  private void notifyConnected(boolean powerStatus) {
    // Set the calling sprinkler to match powerStatus
    this.isPowered = powerStatus;
    // BFS to tell everyone to update:
    Entity calling = this.entity;
    Queue<Entity> queue = new LinkedList<>();
    ArrayList<Entity> visited = new ArrayList<>();
    queue.offer(calling);

    while (!queue.isEmpty()) {
      calling = queue.poll();
      for (Entity s : calling.getComponent(SprinklerComponent.class).getAdjList()) {
        if (s != null && !visited.contains(s)) {
          s.getComponent(SprinklerComponent.class).setPower(powerStatus);
          // Set the powered status and texture of everything we find that's not a pump:
          if (!s.getComponent(SprinklerComponent.class).getPump()) {
            s.getComponent(SprinklerComponent.class)
                    .setTexture(powerStatus, s.getComponent(SprinklerComponent.class)
                            .connectedEntityUtility.getAdjacentBitmap());
          }
          queue.offer(s);
          visited.add(s);
        }
      }
    }
  }

  /**
   * Sets the coordinates for the watering area-of-effect.
   * The watering AOE is this sprinklers position +2 in all directions, +1 in diagonals,
   * this creates a circular watering effect.
   */
  private void setAoe() {
    this.aoe = new Vector2[12];
    float x = entity.getPosition().x;
    float y = entity.getPosition().y;
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
    };
  }

  /**
   * Waters plants to the ideal amount within the aoe.
   * The aoe is relevant to this sprinklers position and looks like:
   * 2 tiles: above, below, left, right.
   */
  private void sprinkle() {
    if (!isPowered) return;
    for (Vector2 pos : aoe) {
      TerrainTile tt = ServiceLocator.getGameArea().getMap().getTile(pos);
      Entity occupant = tt.getOccupant();
      if (occupant != null) {
        CropTileComponent cropTile = occupant.getComponent(CropTileComponent.class);
        if (cropTile != null) {
          Entity plant = cropTile.getPlant();
          if (plant != null) {
            // water a plant to its ideal water amount
            float currWater = cropTile.getWaterContent();
            float ideaWater = plant.getComponent(PlantComponent.class).getIdealWaterLevel();
            if (currWater < ideaWater) {
              // Watering plant!
              occupant.getEvents().trigger("water", ideaWater - currWater);
            }
          // no plant but we'll water the cropTile just for visuals
          } else occupant.getEvents().trigger("water", 0.25f);
        }
      }
    }
  }
}