package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.items.ItemActions;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.components.items.WateringCanLevelComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory to create an item
 */
public class ItemFactory {
  Map<String, Method> factoryMap = new HashMap<>();

  public Map<String, Method> getFactoryMap() throws NoSuchMethodException {
    Class<ItemFactory> c = ItemFactory.class;
    factoryMap.put("base", c.getDeclaredMethod("createBaseItem"));
    factoryMap.put("shovel", c.getDeclaredMethod("createShovel"));
    factoryMap.put("hoe", c.getDeclaredMethod("createHoe"));
    factoryMap.put("watering can", c.getDeclaredMethod("createWateringcan"));
    factoryMap.put("scythe", c.getDeclaredMethod("createScythe"));
    return factoryMap;
  }

  public static Entity createBaseItem() {
    Entity item = new Entity(EntityType.Item)
        .addComponent(new PhysicsComponent())
        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ITEM))
        .addComponent(new ItemActions());
    return item;
  }

  /**
   * Creates a shovel item
   * 
   * @return shovel
   */
  public static Entity createShovel() {
    Entity shovel = createBaseItem()
        .addComponent(new TextureRenderComponent("images/tool_shovel.png"))
        .addComponent(new ItemComponent("shovel", ItemType.SHOVEL, "Shovel for removing items",
            new Texture("images/tool_shovel.png")));
    return shovel;
  }

  /**
   * Creates a hoe item
   * 
   * @return hoe
   */
  public static Entity createHoe() {
    Entity hoe = createBaseItem()
        .addComponent(new TextureRenderComponent("images/tool_hoe.png"))
        .addComponent(new ItemComponent("hoe", ItemType.HOE, new Texture("images/tool_hoe.png")));
    return hoe;
  }

  /**
   * Creates a watering-can item
   * 
   * @return watering can
   */
  public static Entity createWateringcan() {
    Entity watering_can = createBaseItem()
        .addComponent(new TextureRenderComponent("images/tool_watering_can.png"))
        .addComponent(
            new ItemComponent("watering can", ItemType.WATERING_CAN, new Texture("images/tool_watering_can.png")))
        .addComponent(new WateringCanLevelComponent(150));
    return watering_can;
  }

  /**
   * Creates a scythe item
   * 
   * @return scythe
   */
  public static Entity createScythe() {
    Entity scythe = createBaseItem()
        .addComponent(new TextureRenderComponent("images/tool_scythe.png"))
        .addComponent(new ItemComponent("watering can", ItemType.SCYTHE, new Texture("images/tool_scythe.png")));
    return scythe;
  }
}
