package com.csse3200.game.components.player;
import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.List;
import com.csse3200.game.entities.Entity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
public class PlayerInventoryTest {
    private Entity player;
    private InventoryComponent inventoryComponent;
    private Entity item1;
    private Entity item2;
    @BeforeEach
    void initialiseTest() {
        inventoryComponent = spy(new InventoryComponent(new ArrayList<>()));
        player = new Entity(type)
                .addComponent(inventoryComponent);
        player.create();
        item1 = new Entity(type).addComponent(new ItemComponent("Hoe", ItemType.HOE, new Texture("images/tool_shovel.png")));
        item2 = new Entity(type).addComponent(new ItemComponent("Scythe",ItemType.SCYTHE, new Texture("images/tool_shovel.png")));
    }

    @Test
    void checkAddInventory() {
        assertTrue(player.getComponent(InventoryComponent.class).getInventory().isEmpty());
        player.getComponent(InventoryComponent.class).addItem(item1);
        verify(inventoryComponent).addItem(item1);
        player.getComponent(InventoryComponent.class).addItem(item2);
        verify(inventoryComponent).addItem(item2);
        assertArrayEquals(new List[]{player.getComponent(InventoryComponent.class).getInventory()},
                new List[]{inventoryComponent.getInventory()});
    }
    @Test
    void checkRemoveItem() {
        assertTrue(inventoryComponent.getInventory().isEmpty());
        player.getComponent(InventoryComponent.class).addItem(item1);
        verify(inventoryComponent).addItem(any(Entity.class));
        player.getComponent(InventoryComponent.class).removeItem(item1);
        assertTrue(inventoryComponent.getInventory().isEmpty());
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
