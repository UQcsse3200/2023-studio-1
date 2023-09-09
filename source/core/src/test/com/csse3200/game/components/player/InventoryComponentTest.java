package com.csse3200.game.components.player;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import com.csse3200.game.entities.Entity;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(GameExtension.class)
class InventoryComponentTest {


  private InventoryComponent inventoryComponent;
  private Entity item1;
  private Entity item2;

  @BeforeEach
  public void setUp() {
    // Set up the inventory with two initial items
    List<Entity> items = new ArrayList<>();
    item1 = new Entity();
    item2 = new Entity();
    items.add(item1);
    items.add(item2);
    inventoryComponent = new InventoryComponent(items);
  }

  /**
   * Test case for the getInventory() method.
   */
  @Test
  public void testGetInventory() {
    // Retrieve the inventory from the component
    List<Entity> inventory = inventoryComponent.getInventory();
    // Ensure that the inventory contains both items
    assertEquals(2, inventory.size());
    assertTrue(inventory.contains(item1));
    assertTrue(inventory.contains(item2));
  }

  /**
   * Test case for the hasItem() method.
   */
  @Test
  public void testHasItem() {
    // Check if an item is present in the inventory
    assertTrue(inventoryComponent.hasItem(item1));
    // Check if a non-existent item is not in the inventory
    assertFalse(inventoryComponent.hasItem(new Entity()));
  }

  /**
   * Test case for the addItem() method.
   */
  /*
  @Test
  public void testAddItem() {
    // Create a new item
    Entity newItem = new Entity();
    // Add the new item to the inventory
    assertTrue(inventoryComponent.addItem(newItem));
    // Check if the new item is now in the inventory
    assertTrue(inventoryComponent.hasItem(newItem));
  }
  */
  /**
   * Test case for the removeItem() method.
   */
  /*
  @Test
  public void testRemoveItem() {
    // Remove an item from the inventory
    assertTrue(inventoryComponent.removeItem(item1));
    // Check that the removed item is no longer in the inventory
    assertFalse(inventoryComponent.hasItem(item1));
    // Check that removing a non-existent item does not affect the inventory
    assertFalse(inventoryComponent.removeItem(new Entity()));
  }
   */
  /*
  @Test
  void testGetItemCount() {
    assertEquals(inventoryComponent.getItemCount(item1), 1);
    inventoryComponent.addItem(item1);
    assertEquals(inventoryComponent.getItemCount(item1), 2);
    inventoryComponent.removeItem(item1);
    inventoryComponent.removeItem(item1);
    assertEquals(inventoryComponent.getItemCount(item1), 0);

  }
  @Test
  void testGetItemPosition() {
    assertEquals(inventoryComponent.getItemPosition(item1), new Point(0, 0));
  }

  @Test
  void testSetItemPosition() {
    assertEquals(inventoryComponent.getItemPosition(item1), new Point(0, 0));
    inventoryComponent.setItemPosition(item1,new Point(2,2));
    assertEquals(inventoryComponent.getItemPosition(item1), new Point(2,2));
  }

   */
}

