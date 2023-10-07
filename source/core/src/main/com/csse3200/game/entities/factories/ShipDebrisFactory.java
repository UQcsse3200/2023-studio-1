package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.ship.ShipDebrisComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class ShipDebrisFactory {

    /**
     * Creates a Ship Debris entity.
     *
     * @return new Ship Debris Entity.
     */
    public static Entity createShipDebris() {
        Entity shipDebris = new Entity(EntityType.SHIP_DEBRIS)
                .addComponent(new TextureRenderComponent("images/ship/ship_debris.png"))
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new ColliderComponent())
                .addComponent(new ShipDebrisComponent())
                .addComponent(new HitboxComponent());

        shipDebris.setScale(1f, 1f);

        return shipDebris;
    }
}
