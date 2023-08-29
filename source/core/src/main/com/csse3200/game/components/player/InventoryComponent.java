package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.awt.Point; // for positional data
import java.util.HashMap;
import java.util.Map;

/**
 * A component intended to be used by the player to track their inventory.
 * Currently untested, but forms the basis for the UI which will be implemented soon:tm:
 */
public class InventoryComponent extends Component {
  private static final Logger logger = LoggerFactory.getLogger(InventoryComponent.class);
  private final List<Entity> inventory = new ArrayList<Entity>();
  private final HashMap<Entity, Integer> itemCount = new HashMap<>();
  private final HashMap<Entity, Point> itemPosition = new HashMap<>();

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
    return this.inventory.contains(item);
  }

  /**
   * Sets the player's inventory.
   *
   * @param items items to be added to inventory
   */
  public void setInventory(List<Entity> items) {
    this.inventory.addAll(items);
    logger.debug("Setting inventory to {}", this.inventory.toString());
    for (Entity item : items) {
      if (itemCount.containsKey(item)) {
        itemCount.put(item, itemCount.get(item) + 1);
      }

      itemCount.put(item, 1); // Setting initial count as 1
      itemPosition.put(item, new Point(0, 0)); // Setting a default position (0,0) for now.
    }
    logger.debug("Setting inventory to {}", this.inventory.toString());

  }

  /**
   * Adds an item to the Player's inventory
   * @param item item to add
   * @return boolean representing if the item was added successfully
   */
  public boolean addItem(Entity item) {
    itemCount.put(item, itemCount.getOrDefault(item, 0) + 1);
    if (!itemPosition.containsKey(item)) {
      itemPosition.put(item, new Point(0, 0)); // Default position. You can change this as needed.
    }
    return this.inventory.add(item);
  }

  /**
   * Removes an item from the Player's Inventory
   * @param item item to be removed
   * @return boolean representing if the item was removed successfully
   */
  public boolean removeItem(Entity item) {
    itemCount.put(item, this.getItemCount(item) - 1);
    if (itemCount.get(item) == 0) {
      itemCount.remove(item);
      itemPosition.remove(item);
    }
    return this.inventory.remove(item);
  }

  /**
   * Sets the held item for the Player.
   *
   * @param index The index of the item in the inventory to be set as the held item.
   */
  public void setHeldItem(int index) {
    if (index >= 0 && index < inventory.size()) {
      this.heldItem = inventory.get(index);
    } else {
      throw new IndexOutOfBoundsException("Invalid index");
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

  /**
   * Returns the count of an item in the inventory
   * @param item Entity that is an item to find the counted of
   * @return integer representation of count
   */
  public int getItemCount(Entity item) {
    return itemCount.getOrDefault(item, 0);
  }

  /**
   * Returns the position of an item
   * @param item entity that is the item we want to find
   * @return Point of the positional representation of the inventory
   */
  public Point getItemPosition(Entity item) {
    return itemPosition.get(item);
  }

  /**
   * Returns a boolean value representing whether or not an item is at a point representation of the inventory
   * @param point Point to check whether an item is at that position
   * @return Boolean representing whether or not item is at point
   */
  public Boolean getItemAtPoint( Point point) {
    for (Map.Entry<Entity, Point> entry : itemPosition.entrySet()) {
      if (entry.getValue().equals(point)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Get item at a point
   * @param point Point representation of inventory position
   * @return Item at that position or null
   */
  public Entity getItem(Point point) {
    for (Map.Entry<Entity, Point> entry : itemPosition.entrySet()) {
      if (entry.getValue().equals(point)) {
        return entry.getKey();
      }
    }
    return null;
  }


  /**
   * Set point position for item
   * @param item item to set position of
   * @param point position to set
   */
  public void setItemPosition(Entity item, Point point) {
    itemPosition.put(item, point);
  }
}
