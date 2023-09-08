package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.HashSet;

import java.util.ArrayList;
import java.util.List;
import java.awt.Point; // for positional data
import java.util.HashMap;
import java.util.Map;


import static com.csse3200.game.entities.factories.ItemFactory.createHoe;
import static com.csse3200.game.entities.factories.ItemFactory.createShovel;

/**
 * A component intended to be used by the player to track their inventory.
 *
 * Currently untested, but forms the basis for the UI which will be implemented soon:tm:
 */
public class InventoryComponent extends Component {
  private static final Logger logger = LoggerFactory.getLogger(InventoryComponent.class);
  private final List<Entity> inventory = new ArrayList<>();
  private final Set<Integer> inventoryIds = new HashSet<>();  // To quickly check by ID
  private final Map<Integer, Integer> itemCount = new HashMap<>();
  private final Map<Integer, Point> itemPosition = new HashMap<>();


  public InventoryComponent(List<Entity> items) {
    setInventory(items);
  }

  /**
   * Returns the player's inventory.
   *
   * @return player's inventory
   */
  public List<Entity> getInventory() {
    return this.inventory;
  }

  /**
   * Returns if the player has a certain amount of gold.
   * @param item item to be checked
   * @return boolean representing if the item is on the character
   */


  public Boolean hasItem(Entity item) {
    return inventoryIds.contains(item.getId());
  }

  public void setInventory(List<Entity> items) {
    this.inventory.addAll(items);
    for (Entity item : items) {
      inventoryIds.add(item.getId());
      itemCount.put(item.getId(), 1);
      itemPosition.put(item.getId(), new Point(0, 0));
    }
    logger.debug("Setting inventory to {}", this.inventory.toString());
  }

  public boolean addItem(Entity item) {
    int id = item.getId();
    inventoryIds.add(id);
    itemCount.put(id, itemCount.getOrDefault(id, 0) + 1);
    if (!itemPosition.containsKey(id)) {
      itemPosition.put(id, new Point(0, 0));
    }
    return inventory.add(item);
  }

  public boolean removeItem(Entity item) {
    int id = item.getId();
    int newCount = itemCount.getOrDefault(id, 0) - 1;

    if (newCount <= 0) {
      itemCount.remove(id);
      itemPosition.remove(id);
      inventory.remove(item);
      inventoryIds.remove(id);
    } else {
      itemCount.put(id, newCount);
    }
    return true;
  }

  public Entity getInHand() {
    return createHoe();
  }


  public int getItemCount(Entity item) {
    return itemCount.getOrDefault(item.getId(), 0);
  }

  public Point getItemPosition(Entity item) {
    return itemPosition.get(item.getId());
  }

  public void setItemPosition(Entity item, Point point) {
    itemPosition.put(item.getId(), point);
  }
}