package com.csse3200.game.components.inventory;

import com.badlogic.gdx.Input;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Factory to create a mock player entity for testing.
 * Only includes necessary components for testing.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */
@ExtendWith(GameExtension.class)
public class TestInventoryUI {
    Entity player;
    InventoryDisplay inventoryDisplay;

    /**
     * Create a player entity.
     */
    @BeforeEach
    void createPlayer() {
        ServiceLocator.registerInputService(new InputService());
        inventoryDisplay = mock(InventoryDisplay.class);
        player =
                new Entity()
                        .addComponent(new PlayerActions())
                        .addComponent(new KeyboardPlayerInputComponent())
                        .addComponent(inventoryDisplay)
                        .addComponent(new InventoryComponent(new ArrayList<>()));
    }
    @Test
    void testToggleInventory() {
        //verify(inventoryDisplay);
        player.create();
        verify(inventoryDisplay).create();
        player.getEvents().addListener("toggleInventory",inventoryDisplay::toggleOpen);
        player.getComponent(KeyboardPlayerInputComponent.class).setActions(player.getComponent(PlayerActions.class));
        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.I);
        verify(inventoryDisplay).toggleOpen();



    }


}
