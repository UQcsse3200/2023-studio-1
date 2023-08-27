package com.csse3200.game.entities.factories;
import com.csse3200.game.components.items.ItemComponent;
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
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.DEFAULT));
        return item;
    }

    /**
     * Creates a shovel item 
     * @return shovel
     */
    public static Entity createShovel() {
      Entity shovel = createBaseItem()
              .addComponent(new TextureRenderComponent("images/tool_shovel.png"))
              .addComponent(new ItemComponent("shovel", "Shovel for removing items", "images/tool_shovel.png"));
      //shovel.scaleHeight(1f); // scales the Entity!, this could be scaleWidth too
      return shovel;
  }

      /**
       * Creates a hoe item
       * @return hoe
       */
      public static Entity createHoe() {
        Entity hoe = createBaseItem()
                .addComponent(new TextureRenderComponent("images/tool_hoe.png"))
                .addComponent(new ItemComponent("hoe", "A hoe for cultivating soil", "images/tool_hoe.png"));
        return hoe;
    }
}
