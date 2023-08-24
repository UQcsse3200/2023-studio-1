package com.csse3200.game.components.items;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class ItemComponentTest {
  @Test
  void createItemComponentConstructorBase() {
    ItemComponent item = new ItemComponent("test", ItemType.SHOVEL);
    assertEquals("test", item.getItemName());
    assertEquals(0, item.getPrice());
    assertEquals("", item.getItemDescription());
    assertEquals(false, item.isSellable());
    assertEquals(ItemType.SHOVEL, item.getItemType());
  }

  @Test
  void createItemComponentConstructorDescription() {
    ItemComponent item = new ItemComponent("test", ItemType.HOE, "test description");
    assertEquals("test", item.getItemName());
    assertEquals(0, item.getPrice());
    assertEquals("test description", item.getItemDescription());
    assertEquals(false, item.isSellable());
    assertEquals(ItemType.HOE, item.getItemType());
  }

  @Test
  void createItemComponentConstructorDescriptionPrice() {
    ItemComponent item = new ItemComponent("test", ItemType.SICKLE, "test description", 10);
    assertEquals("test", item.getItemName());
    assertEquals(10, item.getPrice());
    assertEquals("test description", item.getItemDescription());
    assertEquals(true, item.isSellable());
    assertEquals(ItemType.SICKLE, item.getItemType());
  }

  @Test
  void setItemName() {
    ItemComponent item = new ItemComponent("test", ItemType.WATERING_CAN);
    item.setItemName("test2");
    assertEquals("test2", item.getItemName());
  }

  @Test 
  void setPrice() {
    ItemComponent item = new ItemComponent("test", ItemType.SICKLE);
    item.setPrice(10);
    assertEquals(10, item.getPrice());
  }

  @Test 
  void setDescription() {
    ItemComponent item = new ItemComponent("test", ItemType.HOE);
    item.setItemDescription("test description");
    assertEquals("test description", item.getItemDescription());
  }
}
