package com.csse3200.game.components.player;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.HashMap;

import com.csse3200.game.entities.EntityType;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;

@ExtendWith(GameExtension.class)
class PlayerInventoryTest {
    private Entity player;
    private InventoryComponent inventoryComponent;
    private Entity item1;
    private Entity item2;

    String[] texturePaths = {"images/tool_shovel.png"};
    @BeforeEach
    void initialiseTest() {
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.getResourceService().loadTextures(texturePaths);
        ServiceLocator.getResourceService().loadAll();

        inventoryComponent = spy(new InventoryComponent(new ArrayList<>()));
        player = new Entity()
                .addComponent(inventoryComponent);
        player.create();
        player.getEvents().addListener("use", (Vector2 mousePos, Entity itemInHand) -> inventoryComponent.useItem());
        item1 = new Entity(EntityType.ITEM).addComponent(new ItemComponent("Hoe", ItemType.HOE, "images/tool_shovel.png"));
        item2 = new Entity(EntityType.ITEM).addComponent(new ItemComponent("Scythe",ItemType.SCYTHE, "images/tool_shovel.png"));
    }

    @Test
    void checkAddInventory() {

        assertTrue(player.getComponent(InventoryComponent.class).getItemPlace().isEmpty());
        player.getComponent(InventoryComponent.class).addItem(item1);
        verify(inventoryComponent).addItem(item1);
        player.getComponent(InventoryComponent.class).addItem(item2);
        verify(inventoryComponent).addItem(item2);
        assertArrayEquals(new HashMap[]{player.getComponent(InventoryComponent.class).getItemPlace()},
                new HashMap[]{inventoryComponent.getItemPlace()});
    }
    @Test
    void checkRemoveItem() {
        assertTrue(inventoryComponent.getItemPlace().isEmpty());
        player.getComponent(InventoryComponent.class).addItem(item1);
        verify(inventoryComponent).addItem(any(Entity.class));
        player.getComponent(InventoryComponent.class).removeItem(item1);
        assertTrue(inventoryComponent.getItemPlace().isEmpty());
        verify(inventoryComponent).removeItem(any(Entity.class));
    }
    @Test
    void checkSetHeldItem() {
        player.getComponent(InventoryComponent.class).addItem(item1);
        player.getComponent(InventoryComponent.class).setHeldItem(0);
        assertSame(inventoryComponent.getHeldItem(), item1);
        player.getComponent(InventoryComponent.class).removeItem(item1);
    }
    @Test
    void checkGetHeldItem() {
        player.getComponent(InventoryComponent.class).addItem(item1);
        player.getComponent(InventoryComponent.class).setHeldItem(0);
        assertSame(inventoryComponent.getHeldItem(), item1);
    }

    // checks if using an item decreases the count of perishable items by 1, else removes the item.
    @Test
    void useItemShouldRemovePerishableItem() {
        Entity perishable = new Entity(EntityType.ITEM).addComponent(new ItemComponent("TestItem",ItemType.FERTILISER, "images/tool_shovel.png",true));

        inventoryComponent.addItem(perishable);
        inventoryComponent.setHeldItem(0);
        assertEquals(inventoryComponent.getHeldItem(), perishable);
        player.getEvents().trigger("use",new Vector2(0,0), inventoryComponent.getHeldItem());
        verify(inventoryComponent).removeItem(perishable);
        assertFalse(inventoryComponent.hasItem(perishable));

    }


    // checks if using an item does not remove a nonperishable item.
    @Test
    void useItemShouldNotRemoveNonperishableItem() {
        Entity item = new Entity(EntityType.ITEM).addComponent(new ItemComponent("TestItem",ItemType.HOE, "images/tool_shovel.png",false));

        inventoryComponent.addItem(item);
        inventoryComponent.setHeldItem(0);
        assertEquals(inventoryComponent.getHeldItem(), item);
        player.getEvents().trigger("use",new Vector2(0,0), inventoryComponent.getHeldItem());
        assertTrue(inventoryComponent.hasItem(item));

    }

}
