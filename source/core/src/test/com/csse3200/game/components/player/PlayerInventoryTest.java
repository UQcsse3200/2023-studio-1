package com.csse3200.game.components.player;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.extensions.GameExtension;

@ExtendWith(GameExtension.class)
public class PlayerInventoryTest {
    private Entity player;
    private InventoryComponent inventoryComponent;
    private Entity item1;
    private Entity item2;
    @BeforeEach
    void initialiseTest() {
        inventoryComponent = spy(new InventoryComponent(new ArrayList<>()));
        player = new Entity()
                .addComponent(inventoryComponent);
        player.create();
        item1 = new Entity(EntityType.Item).addComponent(new ItemComponent("Hoe", ItemType.HOE, new Texture("images/tool_shovel.png")));
        item2 = new Entity(EntityType.Item).addComponent(new ItemComponent("Scythe",ItemType.SCYTHE, new Texture("images/tool_shovel.png")));
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

}
