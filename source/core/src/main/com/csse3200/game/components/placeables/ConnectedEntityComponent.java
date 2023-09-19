package com.csse3200.game.components.placeables;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

import java.util.LinkedHashMap;

/**
 * Used by placeable components to handle connected textures and other functionality that requires a Placeable to
 * interact dynamically with other Placeable entities.
 */
public class ConnectedEntityComponent extends Component {

  private final Entity target;

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

    // Init the adjacent map:
    this.adjPlaceable = new LinkedHashMap<Vector2, Boolean>(4);
    // TODO Add desc & clean up this ugly code
    for (Vector2 pos : adjPositions) {
      // Add listener for new Placeable in this position:
      target.getEvents().addListener("placed:"+pos, this::updateAdjPlaceable);
      // Get an initial mapping of the adjacent Placeable(s).
      Entity p = ServiceLocator.getGameArea().getMap().getTile(pos).getPlaceable();
      if (p != null) {
        if (p.getType() == target.getType()) {
          System.out.println("p:"+p.getType()+", target:"+target.getType());
          this.adjPlaceable.put(pos, true); //p.getType() == target.getType());
          p.getEvents().trigger("placed:"+target.getPosition(), target.getPosition());
        } else this.adjPlaceable.put(pos, false);
        // Trigger the adjacent Placeable to update its map with this Placeable.
      } else this.adjPlaceable.put(pos, false);
    }
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
   * TODO add option to set true of false, (handy for on destroy).
   * @param pos A key into the adjacency map
   */
  public void updateAdjPlaceable(Vector2 pos) {
    this.adjPlaceable.replace(pos, true);   // TODO should I check return values...?
    target.getEvents().trigger("reconfigure");
  }
}
