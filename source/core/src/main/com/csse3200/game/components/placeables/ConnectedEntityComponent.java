package com.csse3200.game.components.placeables;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

import java.util.*;

/**
 * Used by placeable components to handle connected textures and other functionality that requires a Placeable to
 * interact dynamically with other Placeable entities.
 * This component should be a composite component of the target entity.
 * For example an entity adds a SprinklerComponent OR a FenceComponent which creates a new instance of this component,
 * and passes the entity to its constructor.
 */
public class ConnectedEntityComponent extends Component {

  /**
   * The Placeable entity that this instance belongs to.
   */
  private final Entity target;

  /**
   * A mapping of coordinates to indexes into the 'adjacentEntities' array.
   */
  private final Map<Vector2, Integer> indexMap;

  /**
   * Each directly adjacent (Placeable) entity, stored in-order.
   * This array's order represents the relevant orientation of an entity to the target.
   * Orientation order: [ Above, Below, Right, Left ]
   */
  private final Entity[] adjacentEntities;

  public ConnectedEntityComponent(Entity target) {
    // Set target entity and the coordinates where it expects to find adjacent Placeable(s).
    // TODO Add explanatory comments
    this.target = target;
    Vector2 targetPos = target.getPosition();
    this.indexMap = Map.of(
            new Vector2(target.getPosition().x, target.getPosition().y + 1), 0,
            new Vector2(target.getPosition().x, target.getPosition().y - 1), 1,
            new Vector2(target.getPosition().x + 1, target.getPosition().y), 2,
            new Vector2(target.getPosition().x - 1, target.getPosition().y), 3
    );

    // TODO Add explanatory comments
    this.adjacentEntities = new Entity[4];
    for (Vector2 pos : indexMap.keySet()) {
      Entity p = ServiceLocator.getGameArea().getMap().getTile(pos).getPlaceable();
      if (p != null) {
        if (p.getType().getPlaceableCategory() != target.getType().getPlaceableCategory()) {
          p = null;
        } else p.getEvents().trigger("placed:"+targetPos, targetPos, this.target); // notify of this placement
      }
      // set entity at that location
      adjacentEntities[indexMap.get(pos)] = p;
      // add listeners
      target.getEvents().addListener("placed:"+pos, this::updateAdjEntity);
      target.getEvents().addListener("destroyed:"+pos, this::updateAdjEntity);
    }
    target.getEvents().addListener("destroy", this::destroy);
  }

  /**
   * Destroys the target entity (along with this component if used correctly),
   * and notifies its adjacent Placeable entities.
   */
  private void destroy() {
    for (Entity p : this.adjacentEntities) {
      if (p != null) {
        if (p.getType().getPlaceableCategory() == this.target.getType().getPlaceableCategory()) {
          p.getEvents().trigger("destroyed:"+this.target.getPosition(),
                  this.target.getPosition(), null);
        }
      }
    }
    target.dispose(); // is this the best place to call .dispose? seems fine.
  }

  /**
   * Gives a 4-bit representation of the adjacent Placeable entities.
   * This is very useful for texture selection since the 4-bit number acts as an index into an ordered texture array.
   * See usages in SprinklerComponent.java.
   * @return 4-bit number representation of the target entities adjacent Placeable entities.
   */
  public byte getAdjacentBitmap() {
    byte mapping = 0b0000;
    for (Entity p : this.adjacentEntities) {
      mapping <<= 1;
      if (p != null) mapping |= 0b0001;
    }
    return mapping;
  }

  /**
   * Uses the given position as an index into the array of adjacent entities, and sets the entity
   * at that index to the given entity (placeable).
   * // TODO redo docstring
   * @param pos The location (key) of the Placeable entity to be updated.
   * @param placeable The entity to update the adjacent position with.
   */
  public void updateAdjEntity(Vector2 pos, Entity placeable) {  // TODO make this private
    this.adjacentEntities[this.indexMap.get(pos)] = placeable;
    target.getEvents().trigger("reconfigure");
  }

  /**
   * Getter for this adjacent entity array.
   * @return this array of adjacent entities.
   */
  public Entity[] getAdjacentEntities() {
    return this.adjacentEntities;
  }
}
