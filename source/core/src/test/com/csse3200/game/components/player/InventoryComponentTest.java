package com.csse3200.game.components.player;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    List<Entity> items = new ArrayList<>();
    item1 = new Entity();
    item2 = new Entity();
    items.add(item1);
    items.add(item2);
    inventoryComponent = new InventoryComponent(items);
  }

  @Test
  public void testGetInventory() {
    List<Entity> inventory = inventoryComponent.getInventory();
    assertEquals(2, inventory.size());
    assertTrue(inventory.contains(item1));
    assertTrue(inventory.contains(item2));
  }

  @Test
  public void testHasItem() {
    assertTrue(inventoryComponent.hasItem(item1));
    assertFalse(inventoryComponent.hasItem(new Entity()));
  }

  @Test
  public void testAddItem() {
    Entity newItem = new Entity();
    assertTrue(inventoryComponent.addItem(newItem));
    assertTrue(inventoryComponent.hasItem(newItem));
  }

  @Test
  public void testRemoveItem() {
    assertTrue(inventoryComponent.removeItem(item1));
    assertFalse(inventoryComponent.hasItem(item1));
    assertFalse(inventoryComponent.removeItem(new Entity()));
  }
}