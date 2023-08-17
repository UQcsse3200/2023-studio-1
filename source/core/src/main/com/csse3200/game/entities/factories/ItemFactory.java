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
//TODO change to hoe and shovel. fix base item to not take a texture path
public class ItemFactory {
    public static Entity createBaseItem() {
        Entity item = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.DEFAULT))
                .addComponent(new ItemComponent("hoe", "images/tool_hoe.png"));
        return item;
    }

    public static Entity createShovel() {
      Entity testTool = createBaseItem()
              .addComponent(new TextureRenderComponent("images/tool_shovel.png"));
      //testTool.getComponent(TextureRenderComponent.class).scaleEntity();  // dont know what this does
      testTool.scaleHeight(1f); // scales the Entity!, this could be scaleWidth too
      return testTool;
  }

      // Hoe tool
      public static Entity createHoe() {
        Entity hoe = createBaseItem()
                .addComponent(new TextureRenderComponent("images/tool_hoe.png"));
        // do some more stuff here...
        /* add more components to build on baseTool:

         */
        return hoe;
    }


}
