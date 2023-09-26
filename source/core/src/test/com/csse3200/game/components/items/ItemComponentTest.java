package com.csse3200.game.components.items;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.extensions.GameExtension;

@ExtendWith(GameExtension.class)
class ItemComponentTest {
  @Test
  void createItemComponentConstructorBase() {
    ItemComponent item = new ItemComponent("test", ItemType.SHOVEL, new Texture("images/tool_shovel.png"));
    assertEquals("test", item.getItemName());
    assertEquals(0, item.getPrice());
    assertEquals("", item.getItemDescription());
    assertEquals(false, item.isSellable());
    assertEquals(ItemType.SHOVEL, item.getItemType());
  }

  @Test
  void createItemGetPrice() {
    Texture texture = new Texture("images/tool_hoe.png");
    ItemComponent item = new ItemComponent("hoe", ItemType.HOE, 3, texture);
    assertEquals("hoe", item.getItemName());
    assertEquals(3, item.getPrice());
    assertEquals(true, item.isSellable());
    assertEquals(ItemType.HOE, item.getItemType());
    assertEquals(texture, item.getItemTexture());
    Texture texture2 = new Texture("images/tool_shovel.png");
    item.setItemTexture(texture2);
    assertEquals(texture2, item.getItemTexture());
  }

  @Test
  void createItemComponentConstructorDescription() {
    ItemComponent item = new ItemComponent("test", ItemType.HOE, "test description", new Texture("images/tool_shovel.png"));
    assertEquals("test", item.getItemName());
    assertEquals(0, item.getPrice());
    assertEquals("test description", item.getItemDescription());
    assertEquals(false, item.isSellable());
    assertEquals(ItemType.HOE, item.getItemType());
  }

  @Test
  void createItemComponentConstructorDescriptionPrice() {
    ItemComponent item = new ItemComponent("test", ItemType.SCYTHE, "test description", 10, new Texture("images/tool_shovel.png"));
    assertEquals("test", item.getItemName());
    assertEquals(10, item.getPrice());
    assertEquals("test description", item.getItemDescription());
    assertEquals(true, item.isSellable());
    assertEquals(ItemType.SCYTHE, item.getItemType());
  }

  @Test
  void setItemName() {
    ItemComponent item = new ItemComponent("test", ItemType.WATERING_CAN, new Texture("images/tool_shovel.png"));
    item.setItemName("test2");
    assertEquals("test2", item.getItemName());
  }

  @Test 
  void setPrice() {
    ItemComponent item = new ItemComponent("test", ItemType.SCYTHE, new Texture("images/tool_shovel.png"));
    item.setPrice(10);
    assertEquals(10, item.getPrice());
  }

  @Test 
  void setDescription() {
    ItemComponent item = new ItemComponent("test", ItemType.HOE, new Texture("images/tool_shovel.png"));
    item.setItemDescription("test description");
    assertEquals("test description", item.getItemDescription());
  }

  @Test
  void idUnique() {
    ItemComponent item = new ItemComponent("test", ItemType.HOE, new Texture("images/tool_shovel.png"));
    ItemComponent item2 = new ItemComponent("test", ItemType.HOE, new Texture("images/tool_shovel.png"));
    assertNotEquals(item.getItemId(), item2.getItemId());
  }
}
