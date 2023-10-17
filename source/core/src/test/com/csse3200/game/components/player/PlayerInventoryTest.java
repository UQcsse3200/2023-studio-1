package com.csse3200.game.components.player;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.areas.TestGameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.input.InputService;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import com.csse3200.game.services.sound.EffectSoundFile;
import com.csse3200.game.services.sound.InvalidSoundFileException;
import com.csse3200.game.services.sound.SoundFile;
import com.csse3200.game.services.sound.SoundService;
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

    static String[] texturePaths = {
            "images/tool_shovel.png",
            "images/tool_hoe.png",
            "images/tool_scythe.png",
            "images/selected.png",
            "images/itemFrame.png",
            "images/bin.png"
    };

    @BeforeEach
    void initialiseTest() {

        ServiceLocator.registerTimeService(new TimeService());
        ServiceLocator.registerMissionManager(new MissionManager());
        ServiceLocator.registerResourceService(new ResourceService());
        // Set up the inventory with two initial items
        TestGameArea ga = new TestGameArea();
        GameMap gm = mock(GameMap.class);
        ga.setGameMap(gm);
        ServiceLocator.registerGameArea(ga);
        ServiceLocator.registerInputService(new InputService());
        inventoryComponent = spy(new InventoryComponent(new ArrayList<>()));
        player = new Entity().addComponent(inventoryComponent)
                .addComponent(new PlayerActions())
                .addComponent(new KeyboardPlayerInputComponent());
        player.getComponent(KeyboardPlayerInputComponent.class).setActions(player.getComponent(PlayerActions.class));
        player.create();
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.getResourceService().loadTextures(texturePaths);
        ServiceLocator.getResourceService().loadAll();
        ServiceLocator.getResourceService().getAsset("images/tool_shovel.png", Texture.class);
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
    @Test
    void tabToChangeInventory() {
        ServiceLocator.registerSoundService(new SoundService());
        List<SoundFile> effects = new ArrayList<>();
        effects.add(EffectSoundFile.SWITCH_TOOLBAR);
        effects.add(EffectSoundFile.HOTKEY_SELECT);
        try {
            ServiceLocator.getSoundService().getEffectsMusicService().loadSounds(effects);
        } catch (InvalidSoundFileException e) {
            throw new RuntimeException(e);
        }
        ServiceLocator.registerPhysicsService(new PhysicsService());
        player.getComponent(KeyboardPlayerInputComponent.class).setActions(player.getComponent(PlayerActions.class));
        player.create();

        ArrayList<Entity> items = new ArrayList<>(List.of(
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Hoe", ItemType.HOE, "images/tool_hoe.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Scythe", ItemType.SCYTHE, "images/tool_scythe.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Hoe1", ItemType.HOE, "images/tool_hoe.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Shovel", ItemType.SHOVEL, "images/tool_shovel.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Item", ItemType.FERTILISER, "images/tool_shovel.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Hoe2", ItemType.HOE, "images/tool_hoe.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Scythe1", ItemType.SCYTHE, "images/tool_scythe.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Hoe3", ItemType.HOE, "images/tool_hoe.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Shovel1", ItemType.SHOVEL, "images/tool_shovel.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Item1", ItemType.FERTILISER, "images/tool_shovel.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Scythe2", ItemType.SCYTHE, "images/tool_scythe.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Hoe4", ItemType.HOE, "images/tool_hoe.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Shovel2", ItemType.SHOVEL, "images/tool_shovel.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Item2", ItemType.FERTILISER, "images/tool_shovel.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Scythe3", ItemType.SCYTHE, "images/tool_scythe.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Hoe5", ItemType.HOE, "images/tool_hoe.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Shovel3", ItemType.SHOVEL, "images/tool_shovel.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Item3", ItemType.FERTILISER, "images/tool_shovel.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Scythe4", ItemType.SCYTHE, "images/tool_scythe.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Hoe6", ItemType.HOE, "images/tool_hoe.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Shovel4", ItemType.SHOVEL, "images/tool_shovel.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Item4", ItemType.FERTILISER, "images/tool_shovel.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Scythe5", ItemType.SCYTHE, "images/tool_scythe.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Hoe7", ItemType.HOE, "images/tool_hoe.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Shovel5", ItemType.SHOVEL, "images/tool_shovel.png")),
                new Entity(EntityType.ITEM).addComponent(new ItemComponent("Item5", ItemType.FERTILISER, "images/tool_shovel.png"))
        ));
        inventoryComponent.setInventory(items);

        // Check held item will change to next 10 items in inventory when tab is pressed
        AtomicInteger i = new AtomicInteger(8);
        items.forEach((item)-> {
            if (i.get() == 17) {
                i.set(7);
            }
            player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.NUM_0);
            player.getComponent(KeyboardPlayerInputComponent.class).keyDown(i.get());
            assert Objects.equals(inventoryComponent.getHeldItem().getComponent(ItemComponent.class).getItemName(), item.getComponent(ItemComponent.class).getItemName());
            i.set((i.get() + 1));
            if (i.get() == 8) {
                player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.TAB);
            }
        });
    }


}
