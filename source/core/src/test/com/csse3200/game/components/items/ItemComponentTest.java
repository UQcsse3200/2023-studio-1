package com.csse3200.game.components.items;

import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.extensions.GameExtension;

import java.io.Serial;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class ItemComponentTest {

  @BeforeEach
  void setup() {
    ServiceLocator.registerResourceService(new ResourceService());
    ServiceLocator.getResourceService().loadTextures(new String[]{"images/tool_shovel.png", "images/tool_hoe.png"});
    ServiceLocator.getResourceService().loadAll();
  }

  @AfterEach
  void clear() {
    ServiceLocator.clear();
  }


  @Test
  void createItemComponentConstructorBase() {
    ItemComponent item = new ItemComponent("test", ItemType.SHOVEL, "images/tool_shovel.png");
    assertEquals("test", item.getItemName());
    assertEquals(0, item.getPrice());
    assertEquals("", item.getItemDescription());
    assertEquals(false, item.isSellable());
    assertEquals(false, item.isPerishable());
    assertEquals(ItemType.SHOVEL, item.getItemType());
  }

  @Test
  void createItemGetPrice() {
    ItemComponent item = new ItemComponent("hoe", ItemType.HOE, 3, "images/tool_hoe.png");
    assertEquals("hoe", item.getItemName());
    assertEquals(3, item.getPrice());
    assertEquals(true, item.isSellable());
    assertEquals(ItemType.HOE, item.getItemType());
    assertEquals(ServiceLocator.getResourceService().getAsset("images/tool_hoe.png", Texture.class), item.getItemTexture());
    item.setItemTexture("images/tool_shovel.png");
    assertEquals(ServiceLocator.getResourceService().getAsset("images/tool_shovel.png", Texture.class), item.getItemTexture());
  }

  @Test
  void createItemComponentConstructorDescription() {
    ItemComponent item = new ItemComponent("test", ItemType.HOE, "test description", "images/tool_shovel.png");
    ItemComponent item2 = new ItemComponent("test", ItemType.HOE, "test description", "images/tool_shovel.png");
    ItemComponent item3 = new ItemComponent("test", ItemType.HOE, "images/tool_shovel.png");
    assertEquals("test", item.getItemName());
    assertEquals(0, item.getPrice());
    assertEquals("test description", item.getItemDescription());
    assertFalse(item.isSellable());
    assertEquals(ItemType.HOE, item.getItemType());
  }

  @Test
  void createItemComponentConstructorDescriptionPrice() {
    ItemComponent item = new ItemComponent("test", ItemType.SCYTHE, "test description", 10, "images/tool_shovel.png");
    assertEquals("test", item.getItemName());
    assertEquals(10, item.getPrice());
    assertEquals("test description", item.getItemDescription());
    assertEquals(true, item.isSellable());
    assertEquals(ItemType.SCYTHE, item.getItemType());
  }

  @Test
  void setItemName() {
    ItemComponent item = new ItemComponent("test", ItemType.WATERING_CAN, "images/tool_shovel.png");
    item.setItemName("test2");
    assertEquals("test2", item.getItemName());
  }

  @Test 
  void setPrice() {
    ItemComponent item = new ItemComponent("test", ItemType.SCYTHE, "images/tool_shovel.png");
    item.setPrice(10);
    assertEquals(10, item.getPrice());
  }

  @Test 
  void setDescription() {
    ItemComponent item = new ItemComponent("test", ItemType.HOE, "images/tool_shovel.png");
    item.setItemDescription("test description");
    assertEquals("test description", item.getItemDescription());
  }

  @Test
  void idUnique() {
    ItemComponent item = new ItemComponent("test", ItemType.HOE, "images/tool_shovel.png");
    ItemComponent item2 = new ItemComponent("test", ItemType.HOE, "images/tool_shovel.png");
    assertNotEquals(item.getItemId(), item2.getItemId());
  }
}
