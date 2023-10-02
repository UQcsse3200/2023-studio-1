package com.csse3200.game.components.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.entities.EntityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;

@ExtendWith(GameExtension.class)
class InventoryComponentTest {


  private InventoryComponent inventoryComponent;
  private Entity item1;
  private Entity item2;
  private Entity player;

  @BeforeEach
  public void setUp() {
    // Set up the inventory with two initial items
    List<Entity> items = new ArrayList<>();
    inventoryComponent = spy(new InventoryComponent(new ArrayList<>()));
    player = new Entity().addComponent(inventoryComponent);
    player.create();
    item1 = new Entity(EntityType.Item);
    item2 = new Entity(EntityType.Item);
//    items.put(item1.getComponent(ItemComponent.class).getItemName(),item1);
//    items.put(item2.getComponent(ItemComponent.class).getItemName(),item2);
    ItemComponent itemComponent1 = new ItemComponent("itemTest1", ItemType.HOE,
            new Texture("images/tool_shovel.png")); // Texture is not used...
    ItemComponent itemComponent2 = new ItemComponent("itemTest2", ItemType.SCYTHE,
            new Texture("images/tool_shovel.png")); // Texture is not used...
    item1.addComponent(itemComponent1);
    item2.addComponent(itemComponent2);
    inventoryComponent.addItem(item1);
    inventoryComponent.addItem(item2);
  }

  /**
   * Test case for the getInventory() method.
   */
  /*
  @Test
  public void testGetInventory() {
    // Retrieve the inventory from the component
    List<Entity> inventory = inventoryComponent.getInventory();
    // Ensure that the inventory contains both items
    assertEquals(2, inventory.size());
    assertTrue(inventory.contains(item1));
    assertTrue(inventory.contains(item2));
  }
  */
  /**
   * Test case for the hasItem() method.
   */
  @Test
  public void testHasItem() {
    // Check if an item is present in the inventory

    assertTrue(player.getComponent(InventoryComponent.class).hasItem(item1));
    // Check if a non-existent item is not in the inventory
    assertFalse(player.getComponent(InventoryComponent.class).hasItem(new Entity()));
  }

  /**
   * Test case for the addItem() method.
   */

  @Test
  public void testAddItem() {
    // Create a new item
    Entity newItem = new Entity(EntityType.Item);
    ItemComponent itemComponent3 = new ItemComponent("itemTest3", ItemType.SCYTHE,
            new Texture("images/tool_shovel.png")); // Texture is not used...
    // Add the new item to the inventory
    newItem.addComponent(itemComponent3);
    assertTrue(inventoryComponent.addItem(newItem));
    // Check if the new item is now in the inventory
    assertTrue(inventoryComponent.hasItem(newItem));
  }

  /**
   * Test case for the removeItem() method.
   */
  @Test
  public void testRemoveItem() {
    // Remove an item from the inventory
    Entity newItem = new Entity(EntityType.Item);
    ItemComponent itemComponent3 = new ItemComponent("itemTest3", ItemType.SCYTHE,
            new Texture("images/tool_shovel.png")); // Texture is not used...
    // Add the new item to the inventory
    newItem.addComponent(itemComponent3);
    if(inventoryComponent.getItemCount(item1) == 0){
        inventoryComponent.addItem(item1);
    }
    assertTrue(inventoryComponent.removeItem(item1));
    // Check that the removed item is no longer in the inventory
    assertFalse(inventoryComponent.hasItem(item1));
    // Check that removing a non-existent item does not affect the inventory
    assertFalse(inventoryComponent.removeItem(new Entity()));
  }

  @Test
  void testGetItemCount() {
    assertEquals(inventoryComponent.getItemCount(item1), 1);
    inventoryComponent.addItem(item1);
    assertEquals(inventoryComponent.getItemCount(item1), 2);
    inventoryComponent.removeItem(item1);
    inventoryComponent.removeItem(item1);
    assertEquals(inventoryComponent.getItemCount(item1), 0);

  }
  /*
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

