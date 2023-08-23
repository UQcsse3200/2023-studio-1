package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A component intended to be used by the player to track their inventory.
 *
 * Currently untested, but forms the basis for the UI which will be implemented soon:tm:
 */
public class InventoryComponent extends Component {
  private static final Logger logger = LoggerFactory.getLogger(InventoryComponent.class);
  private final List<Entity> inventory = new ArrayList<Entity>();

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
  }

  /**
   * Adds an item to the Player's inventory
   * @param item item to add
   * @return boolean representing if the item was added successfully
   */
  public boolean addItem(Entity item) {
      return this.inventory.add(item);
  }

  /**
   * Removes an item from the Player's Inventory
   * @param item item to be removed
   * @return boolean representing if the item was removed successfully
   */
  public boolean removeItem(Entity item) {
    return this.inventory.remove(item);
  }
}
