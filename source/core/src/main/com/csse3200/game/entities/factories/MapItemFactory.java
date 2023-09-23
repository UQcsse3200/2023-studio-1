package com.csse3200.game.entities.factories;

import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.rendering.TextureRenderComponent;
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

public class MapItemFactory {

    public static Entity createMapItem() {
        Entity mapItem = new Entity(EntityType.Item)
                .addComponent(new TextureRenderComponent("images/ship/ship_clue.png"))
                .addComponent(new ItemComponent("map", ItemType.CLUE_ITEM, new Texture("images/ship/ship_clue.png")));
//                .addComponent(new MapComponent());

        return mapItem;
    }
}
