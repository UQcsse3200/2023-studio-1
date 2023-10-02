package com.csse3200.game.components.player;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.services.ServiceLocator;

public class ItemPickupComponent extends Component {
    private HitboxComponent hitboxComponent;

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
        boolean added = this.entity.getComponent(InventoryComponent.class).addItem(target);
        // if item was successfully added to player inventory, remove it from game area (map):
        if (added) ServiceLocator.getGameArea().removeEntity(target);
        // else, leave item alone.
    }
}
