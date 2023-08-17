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
                .addComponent(new TextureRenderComponent("images/box_boy_leaf.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.DEFAULT))
                .addComponent(new ItemComponent());
        return item;
    }
}
