package com.csse3200.game.entities.factories;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.tools.FarmAbilityComponent;
import com.csse3200.game.components.items.tools.FarmAbilities;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
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
            //.addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ITEM));
    item.scaleHeight(1f); // scales the Entity!, this could be scaleWidth too
    return item;
  }

  /**
   * A starting point for all farm tools to be built upon.
   *
   * @return Basic tool entity with bare-bones tool properties.
   */
  public static Entity createBaseTool() {
    Entity baseTool = createBaseItem()
            .addComponent(new FarmAbilityComponent(FarmAbilities.NONE));
    // will add basic tool properties here
    return baseTool;
  }

  /**
   * Creates a shovel item
   *
   * @return shovel
   */
  public static Entity createShovel() {
    Entity shovel = createBaseTool()
            .addComponent(new TextureRenderComponent("images/tool_shovel.png"))
            .addComponent(new ItemComponent("shovel", "Shovel for removing items"));
    shovel.getComponent(FarmAbilityComponent.class).setAbility(FarmAbilities.DIG);
    //shovel.scaleHeight(1f); // scales the Entity!, this could be scaleWidth too
    return shovel;
  }

  /**
   * Creates a hoe item
   *
   * @return hoe
   */
  public static Entity createHoe() {
    Entity hoe = createBaseTool()
            .addComponent(new TextureRenderComponent("images/tool_hoe.png"))
            .addComponent(new ItemComponent("hoe", "Hoe for tilling ground"));
    hoe.getComponent(FarmAbilityComponent.class).setAbility(FarmAbilities.TILL);
    //hoe.scaleHeight(1f); // scales the Entity!, this could be scaleWidth too
    return hoe;
  }

  /**
   * Creates a watering-can item
   *
   * @return watering can
   */
  public static Entity createWateringcan() {
    Entity watering_can = createBaseTool()
            .addComponent(new TextureRenderComponent("images/watering_can.png"))
            .addComponent(new ItemComponent("watering can", "Watering can for watering things"));
    watering_can.getComponent(FarmAbilityComponent.class).setAbility(FarmAbilities.WATER);
    //hoe.scaleHeight(1f); // scales the Entity!, this could be scaleWidth too
    return watering_can;
  }
}
