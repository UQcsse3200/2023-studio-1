package com.csse3200.game.entities.factories;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.tools.FarmActionComponent;
import com.csse3200.game.components.items.tools.FarmFunction;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

/**
 * Factory to create an item
 */
public class ItemFactory {
  public static Entity createBaseItem() {
    Entity item = new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent());
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
            .addComponent(new FarmActionComponent());
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
    shovel.getComponent(FarmActionComponent.class).setAction(FarmFunction.DIG);
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
            .addComponent(new ItemComponent("shovel", "Hoe for tilling ground"));
    hoe.getComponent(FarmActionComponent.class).setAction(FarmFunction.TILL);
    //hoe.scaleHeight(1f); // scales the Entity!, this could be scaleWidth too
    return hoe;
  }
}
