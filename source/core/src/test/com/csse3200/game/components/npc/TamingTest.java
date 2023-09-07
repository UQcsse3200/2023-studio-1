package com.csse3200.game.components.npc;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.tasks.TamedTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@ExtendWith(GameExtension.class)
public class TamingTest {

    private Entity player;
    private Entity animalToTest;
    private InventoryComponent playerInventory;
    private InventoryComponent playerInvSpy;
    private Entity foodEntity;
    private Entity nonFood;

    @BeforeEach
    void beforeEach() {
        playerInventory = new InventoryComponent(new ArrayList<>());
        playerInvSpy = spy(playerInventory);
        player = new Entity().addComponent(playerInvSpy);
        player.create();

        for (int index = 0; index < 4; index++) {
            foodEntity = new Entity(EntityType.Item);
            ItemComponent fooditem = new ItemComponent("AFood", ItemType.ANIMAL_FOOD,
                    new Texture("images/tool_shovel.png")); //texture is just used as a placeholder.
            foodEntity.addComponent(fooditem);
            playerInvSpy.addItem(foodEntity);
        }
    }

    @Test
    void testUnfed() {
        TamableComponent taming = new TamableComponent(player, 1, 0,
                "Animal Food");
        assertFalse(taming.isTamed());
    }

    @Test
    void testLowTameProbability() {
        playerInvSpy.setHeldItem(0);
        TamableComponent toTame = new TamableComponent(player, 1, 0,
                "AFood");
        animalToTest = new Entity().addComponent(toTame);
        animalToTest.create();

        animalToTest.getEvents().trigger("feed");

        assertTrue(animalToTest.getComponent(TamableComponent.class).isTamed());
    }

    @Test
    void testTameThreshold() {
        TamableComponent toTame = new TamableComponent(player, 2, 1.1,
                "AFood");
        animalToTest = new Entity().addComponent(toTame);
        animalToTest.create();

        for (int loop = 0; loop < 3; loop++) {
            playerInvSpy.setHeldItem(loop);
            animalToTest.getEvents().trigger("feed");
        }
        assertTrue(animalToTest.getComponent(TamableComponent.class).isTamed());
    }

    @Test
    void testNotFavouriteFood() {
        TamableComponent toTame = new TamableComponent(player, 2, 1.1,
                "BFood");
        animalToTest = new Entity().addComponent(toTame);
        animalToTest.create();

        playerInvSpy.setHeldItem(0);
        animalToTest.getEvents().trigger("feed");

        assertFalse(animalToTest.getComponent(TamableComponent.class).isTamed());
    }

    @Test
    void feedAnimalNonEdibleEntity() {
        TamableComponent toTame = new TamableComponent(player, 2, 1.1,
                "BFood");
        animalToTest = new Entity().addComponent(toTame);
        animalToTest.create();
        nonFood = new Entity();
        playerInvSpy.addItem(nonFood);
        int index = playerInvSpy.getInventory().size();
        playerInvSpy.setHeldItem(index - 1);

        animalToTest.getEvents().trigger("feed");

        assertFalse(animalToTest.getComponent(TamableComponent.class).isTamed());
    }
}
