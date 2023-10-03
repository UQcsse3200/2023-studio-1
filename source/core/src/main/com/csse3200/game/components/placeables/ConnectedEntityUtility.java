package com.csse3200.game.components.placeables;

import com.badlogic.gdx.math.Vector2;
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
public class ConnectedEntityUtility {

  private static final String update = "update";

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

  public ConnectedEntityUtility(Entity target) {
    // Set target entity and the coordinates where it expects to find adjacent Placeable(s).
    this.target = target;
    this.indexMap = Map.of(
            new Vector2(target.getPosition().x, target.getPosition().y + 1), 0,
            new Vector2(target.getPosition().x, target.getPosition().y - 1), 1,
            new Vector2(target.getPosition().x + 1, target.getPosition().y), 2,
            new Vector2(target.getPosition().x - 1, target.getPosition().y), 3
    );

    // Discover and add adjacent placeable entities to a list.
    this.adjacentEntities = new Entity[4];
    for (Map.Entry<Vector2, Integer> entry : indexMap.entrySet()) {
      Vector2 pos = entry.getKey();
      Integer index = entry.getValue();
      Entity p = ServiceLocator.getGameArea().getMap().getTile(pos).getOccupant();
      if (p != null && p.getType().getPlaceableCategory() != target.getType().getPlaceableCategory()) p = null;
      // Set entity at that location:
      adjacentEntities[index] = p;
    }
    // Add listeners for update and destroy:
    target.getEvents().addListener(update, this::updateAdjEntity);
    target.getEvents().addListener("destroyConnections", this::destroy);
  }

  /**
   * Getter for this adjacent entity array.
   * @return this array of adjacent entities.
   */
  protected Entity[] getAdjacentEntities() {
    return this.adjacentEntities;
  }

  /**
   * Used by a Placeable entity after some update has occurred.
   * Notifies the calling's adjacent entities via a trigger.
   */
  public void notifyAdjacent() {
    for (Entity p : this.adjacentEntities) {
      if (p != null) {
        p.getEvents().trigger(update, target.getPosition(), this.target);
      }
    }
  }

  /**
   * Uses the given position as an index into the array of adjacent entities, and sets the entity
   * at that index to the given entity (placeable).
   * @param pos The location (key) of the Placeable entity to be updated.
   * @param placeable The entity to update the adjacent position with.
   */
  private void updateAdjEntity(Vector2 pos, Entity placeable) {
    this.adjacentEntities[this.indexMap.get(pos)] = placeable;
    target.getEvents().trigger("reconfigure");
  }

  /**
   * Destroys the target entity (along with this component if used correctly),
   * and notifies its adjacent Placeable entities.
   */
  private void destroy() {
    for (Entity p : this.adjacentEntities) {
      if (p != null) {
        p.getEvents().trigger(update, this.target.getPosition(), null);
      }
    }
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
}
