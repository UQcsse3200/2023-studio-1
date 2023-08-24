package com.csse3200.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.LinkOption;
import java.util.ArrayList;

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
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;

        // Add item to inventory
        this.entity.getComponent(InventoryComponent.class).addItem(target);

        // make it small for inventory:
        target.scaleHeight(0.5f);
        // TODO: THIS -> ServiceLocator.getGameArea().
        // remove it from game area (map):


        // remove it from game area

        //target.setEnabled(false);

        //target.getComponent(TextureRenderComponent.class).setEnabled(false);

        // dispose components:
        //target.getComponent(TextureRenderComponent.class).dispose();

        //target.getComponent(TextureRenderComponent.class).dispose();
        //target.getComponent(HitboxComponent.class).dispose();
        //target.dispose();

        System.out.println("picked up: " + target.getComponent(ItemComponent.class).getItemName());


        // print debugging
        //ArrayList<Entity> inv = new ArrayList<>(this.entity.getComponent(InventoryComponent.class).getInventory());
        //for (Entity i : inv) {
        //    System.out.print("item:" + i + "\t");
        //}

    }
}
