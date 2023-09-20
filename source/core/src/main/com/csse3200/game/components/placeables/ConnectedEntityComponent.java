package com.csse3200.game.components.placeables;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

import java.util.LinkedHashMap;

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
   * An adjacency map, mapping (ordered) adjacent coordinates to a truth value representing whether of not a
   * Placeable entity occupies that position.
   * The order of this map is: [ Above, Below, Right, Left ]
   */
  private final LinkedHashMap<Vector2, Boolean> adjPlaceable;

  public ConnectedEntityComponent(Entity target) {
    // Set target entity and the coordinates where it expects to find adjacent Placeable(s).
    this.target = target;
    Vector2[] adjPositions = new Vector2[]{
            new Vector2(target.getPosition().x, target.getPosition().y + 1), // Above
            new Vector2(target.getPosition().x, target.getPosition().y - 1), // Below
            new Vector2(target.getPosition().x + 1, target.getPosition().y), // Right
            new Vector2(target.getPosition().x - 1, target.getPosition().y)  // Left
    };
    target.getEvents().addListener("destroy", this::destroy);

    // Init the adjacent map, add listeners, and trigger adjacent Placeable entities to update:
    this.adjPlaceable = new LinkedHashMap<Vector2, Boolean>(4);
    for (Vector2 pos : adjPositions) {
      boolean occupied = false;
      target.getEvents().addListener("placed:"+pos, this::updateAdjPlaceable);
      target.getEvents().addListener("destroyed:"+pos, this::updateAdjPlaceable);
      Entity p = ServiceLocator.getGameArea().getMap().getTile(pos).getPlaceable();
      if (p != null) {
        if (p.getType().getPlaceableCategory() == target.getType().getPlaceableCategory()) {
          occupied = true;
          p.getEvents().trigger("placed:"+target.getPosition(), target.getPosition(), occupied);
        }
      }
      this.adjPlaceable.put(pos, occupied);
    }
  }

  /**
   * Destroys the target entity (along with this component if used correctly),
   * and notifies its adjacent Placeable entities.
   */
  private void destroy() {
    for (Vector2 pos : this.adjPlaceable.keySet()) {
      Entity p = ServiceLocator.getGameArea().getMap().getTile(pos).getPlaceable();
      if (p != null) {
        if (p.getType().getPlaceableCategory() == this.target.getType().getPlaceableCategory()) {
          p.getEvents().trigger("destroyed:"+this.target.getPosition(),
                  this.target.getPosition(), false);
        }
      }
    }
    target.dispose(); // is this the best place to call .dispose? seems fine.
  }

  /**
   * Gives a 4-bit representation of the adjacent placeable(s).
   * This is very useful for texture selection since the 4-bit number acts as an index into an ordered texture array.
   * See usages in SprinklerComponent.java.
   * @return 4-bit number representation of the target entities adjacent Placeable(s).
   */
  public byte getAdjacentBitmap() {
    byte mapping = 0b0000;
    for (Boolean bit : this.adjPlaceable.values()) {
      mapping <<= 1;
      if (bit) mapping |= 0b0001;
    }
    return mapping;
  }

  /**
   * Uses the given position as a key in the adjacency map to set the corresponding value to true.
   * @param pos The location (key) of the Placeable to be updated
   * @param vale The value to update the Placeable entity's presence with.
   */
  public void updateAdjPlaceable(Vector2 pos, boolean vale) {
    this.adjPlaceable.replace(pos, vale);   // TODO should I check return values...?
    target.getEvents().trigger("reconfigure");
  }
}
