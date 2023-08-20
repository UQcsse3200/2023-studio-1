package com.csse3200.game.components.player;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(GameExtension.class)
class InventoryComponentTest {

  private InventoryComponent inventory;

  /**
   * Sets the player's inventory. Sets up an example inventory
   */
  @BeforeEach
  void setUp() {
    List<Item> items = new ArrayList<>();
    items.add(new Item("Sword", 1, 1));
    items.add(new Item("Shield", 2, 1));
    inventory = new InventoryComponent(items);
  }

  /**
   * Tests the inventory size method
   */
  @Test
  void testGetInventory() {
    assertEquals(2, inventory.getInventory().size());
  }

  /**
   * Tests the hasItem method
   */
  @Test
  void testHasItem() {
    assertTrue(inventory.hasItem(new Item("Sword", 1, 1)));
    assertFalse(inventory.hasItem(new Item("Ring", 3, 1)));
  }

  /**
   * Tests the addItem method
   */
  @Test
  void testAddItem() {
    assertTrue(inventory.addItem(new Item("Ring", 3, 1)));
    assertTrue(inventory.hasItem(new Item("Ring", 3, 1)));
  }

  /**
   * Tests the remove item method
   */
  @Test
  void testRemoveItem() {
    assertTrue(inventory.removeItem(new Item("Sword", 1, 1)));
    assertFalse(inventory.hasItem(new Item("Sword", 1, 1)));
  }

  /**
   * Tests the updateItemCount method
   */
  @Test
  void testUpdateItemCount() {
    assertTrue(inventory.updateItemCount(new Item("Sword", 1, 1), 2));
    assertEquals(2, inventory.getInventory().get(0).getItemCount());
  }

  /**
   * Tests the getInventory method
   */
  @Test
  void testGetInventory() {
    InventoryComponent inventory = new InventoryComponent(new ArrayList<Item>());
    assertEquals(new ArrayList<Item>(), inventory.getInventory());
  }
}