package com.csse3200.game.entities.factories;
import com.csse3200.game.components.items.ItemActions;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.components.items.WateringCanLevelComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

/**
 * Factory to create an item
 */
public class ItemFactory {
  public static Entity createBaseItem() {
    Entity item = new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ITEM))
            .addComponent(new ItemActions());
    return item;
  }

  /**
   * Creates a shovel item
   * @return shovel
   */
  public static Entity createShovel() {
    Entity shovel = createBaseItem()
            .addComponent(new TextureRenderComponent("images/tool_shovel.png"))
            .addComponent(new ItemComponent("shovel", ItemType.SHOVEL, "Shovel for removing items"));
    return shovel;
  }

  /**
   * Creates a hoe item
   * @return hoe
   */
  public static Entity createHoe() {
    Entity hoe = createBaseItem()
            .addComponent(new TextureRenderComponent("images/tool_hoe.png"))
            .addComponent(new ItemComponent("hoe", ItemType.HOE));
    return hoe;
  }

  /**
   * Creates a watering-can item
   * @return watering can
   */
  public static Entity createWateringcan() {
    Entity watering_can = createBaseItem()
            .addComponent(new TextureRenderComponent("images/tool_watering_can.png"))
            .addComponent(new ItemComponent("watering can", ItemType.WATERING_CAN))
            .addComponent(new WateringCanLevelComponent(150));
    return watering_can;
  }

  /**
   * Creates a scythe item
   * @return scythe
   */
  public static Entity createScythe() {
    Entity scythe = createBaseItem()
            .addComponent(new TextureRenderComponent("images/tool_scythe.png"))
            .addComponent(new ItemComponent("watering can", ItemType.SCYTHE));
    return scythe;
  }
}
