package com.csse3200.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.LinkOption;

public class ItemPickupComponent extends Component {
    private short targetLayer;
    private HitboxComponent hitboxComponent;
    private static final Logger logger = LoggerFactory.getLogger(GdxGame.class);


    public ItemPickupComponent() {
        this.targetLayer = PhysicsLayer.ITEM;
    }

    /**
     * Create a component which allows the playet to pickup an item.
     * @param targetLayer The physics layer of the target's collider.
     */
    public ItemPickupComponent(short targetLayer) {
        this.targetLayer = targetLayer;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollision);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    private void onCollision(Fixture me, Fixture other) {

        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }

        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }

        // Pickup the item
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;

        target.setEnabled(false);

        //ServiceLocator.getEntityService().unregister(target);
        //target.setPosition(this.entity.getPosition());
        System.out.println("PICKUP ITEM");
        logger.debug("Picked up Item: " + other.toString());

        // Add item to inventory
        this.entity.getComponent(InventoryComponent.class).addItem(target);
    }
}
