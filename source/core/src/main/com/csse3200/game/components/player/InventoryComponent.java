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

    private final Set<Integer> inventoryIds = new HashSet<>();
    private final List<Entity> inventory = new ArrayList<>();
    private final Map<Integer, Integer> itemCount = new HashMap<>();
    private final Map<Integer, Point> itemPosition = new HashMap<>();
    private Entity heldItem = null;

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

  /**
   * Sets the player's inventory.
   *
   * @param items items to be added to inventory
   */

  public void setInventory(List<Entity> items) {
    for (Entity item : items) {
      // Check if the inventory already contains an item with the same ID
      if (inventoryIds.contains(item.getId())) {
        // If yes, just increase the count
        itemCount.put(item.getId(), itemCount.get(item.getId()) + 1);
      } else {
        // If no, add the item to the inventory and set its count to 1
        this.inventory.add(item);
        inventoryIds.add(item.getId());
        itemCount.put(item.getId(), 1);
        itemPosition.put(item.getId(), new Point(0, 0)); // Default position
      }
    }
    logger.debug("Setting inventory to {}", this.inventory.toString());
  }

  /**
   * Adds an item to the Player's inventory
   * @param item item to add
   * @return boolean representing if the item was added successfully
   */
  public boolean addItem(Entity item) {
    if (inventoryIds.contains(item.getId())) {
      // If the inventory already contains an item with the same ID, just increase the count
      itemCount.put(item.getId(), itemCount.get(item.getId()) + 1);
    } else {
      // If not, add the item to the inventory and set its count to 1
      this.inventory.add(item);
      inventoryIds.add(item.getId());
      itemCount.put(item.getId(), 1);
      itemPosition.put(item.getId(), new Point(0, 0)); // Default position
    }
    return true;
  }

  /**
   * Removes an item from the Player's Inventory
   * @param item item to be removed
   * @return boolean representing if the item was removed successfully
   */
  public boolean removeItem(Entity item) {
    if (itemCount.getOrDefault(item.getId(), 0) > 1) {
      // If there are more than one items with the same ID, decrease the count
      itemCount.put(item.getId(), itemCount.get(item.getId()) - 1);
      return false; // Item not fully removed, just decreased the count
    } else {
      // If there's only one item left with the ID, remove it from the inventory
      itemCount.remove(item.getId());
      itemPosition.remove(item.getId());
      inventoryIds.remove(item.getId());
      return this.inventory.remove(item);
    }
  }

  public void setHeldItem(int index) {
    if (index >= 0 && index < inventory.size()) {
      this.heldItem = inventory.get(index);
    }
  }

  /**
   * Retrieves the held item of the Player.
   *
   * @return The Entity representing the held item.
   * @throws IllegalStateException If the player is not holding an item.
   */
  public Entity getHeldItem() {
    if (this.heldItem != null) {
      return this.heldItem;
    }
    return null;
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
