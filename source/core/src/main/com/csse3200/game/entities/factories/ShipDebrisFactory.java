package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.ship.ShipDebrisComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class ShipDebrisFactory {

    public static Entity createShipDebris(Entity player) {
        Entity shipDebris = new Entity(EntityType.ShipDebris)
                .addComponent(new TextureRenderComponent("images/ship/ship_debris.png"))
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new ColliderComponent())
                .addComponent(new ShipDebrisComponent());

        shipDebris.setScale(1f, 1f);

        return shipDebris;
    }
}
