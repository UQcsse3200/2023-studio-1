package com.csse3200.game.components.player;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemPickupComponent extends Component {
    private HitboxComponent hitboxComponent;
    private static final Logger logger = LoggerFactory.getLogger(GdxGame.class);

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollision);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    private void onCollision(Fixture me, Fixture other) {

        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hit-box, ignore
            return;
        }

        if (!PhysicsLayer.contains(PhysicsLayer.ITEM, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;

        // Add item to inventory
        this.entity.getComponent(InventoryComponent.class).addItem(target);

        // remove it from game area (map):
        ServiceLocator.getGameArea().removeEntity(target);
    }
}
