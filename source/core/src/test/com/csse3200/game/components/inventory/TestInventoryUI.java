package com.csse3200.game.components.inventory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ServiceLocator;

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
    InventoryComponent inventory;

    /**
     * Create a player entity.
     */
    @BeforeEach
    void createPlayer() {
        Stage stage = mock(Stage.class);

        RenderService renderService = new RenderService();
        renderService.setStage(stage);

        ServiceLocator.registerRenderService(renderService);

        ServiceLocator.registerInputService(new InputService());
        inventoryDisplay = spy(new InventoryDisplay("updateInventory", "toggleInventory", 30, 10, true));
        player =
                new Entity()
                        .addComponent(new PlayerActions())
                        .addComponent(new KeyboardPlayerInputComponent())
                        .addComponent(inventoryDisplay)
                        .addComponent(inventory);
    }
    /*
    @Test
    void testToggleInventory() {
        player.create();
        verify(inventoryDisplay).create();
        assert(inventoryDisplay.getInventory() == player.getComponent(InventoryComponent.class));
        player.getComponent(KeyboardPlayerInputComponent.class).setActions(player.getComponent(PlayerActions.class));
        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.I);
        verify(inventoryDisplay).toggleOpen();
    }
     */
}

