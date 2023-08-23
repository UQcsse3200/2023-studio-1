package com.csse3200.game.components.items;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class ItemComponentTest {
  @Test
  void createItemComponentConstructorBase() {
    ItemComponent item = new ItemComponent("test");
    assertEquals("test", item.getItemName());
    assertEquals(0, item.getPrice());
    assertEquals("", item.getItemDescription());
    assertEquals(false, item.isSellable());
  }

  @Test
  void createItemComponentConstructorDescription() {
    ItemComponent item = new ItemComponent("test", "test description");
    assertEquals("test", item.getItemName());
    assertEquals(0, item.getPrice());
    assertEquals("test description", item.getItemDescription());
    assertEquals(false, item.isSellable());
  }

  @Test
  void createItemComponentConstructorDescriptionPrice() {
    ItemComponent item = new ItemComponent("test", "test description", 10);
    assertEquals("test", item.getItemName());
    assertEquals(10, item.getPrice());
    assertEquals("test description", item.getItemDescription());
    assertEquals(true, item.isSellable());
  }

  @Test
  void setItemName() {
    ItemComponent item = new ItemComponent("test");
    item.setItemName("test2");
    assertEquals("test2", item.getItemName());
  }

  @Test 
  void setPrice() {
    ItemComponent item = new ItemComponent("test");
    item.setPrice(10);
    assertEquals(10, item.getPrice());
  }

  @Test 
  void setDescription() {
    ItemComponent item = new ItemComponent("test");
    item.setItemDescription("test description");
    assertEquals("test description", item.getItemDescription());
  }
}
