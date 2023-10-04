package com.csse3200.game.components.inventory;

import com.badlogic.gdx.scenes.scene2d.Stage;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

/**
 * Factory to create a mock player entity for testing.
 * Only includes necessary components for testing.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */
@ExtendWith(GameExtension.class)
public class TestInventoryManager {
    InventoryDisplayManager displayManager;
    Stage stage;

    static String[] skinPaths = {
            "gardens-of-the-galaxy/gardens-of-the-galaxy.json"
    };

    /**
     * Create a player entity.
     */
    @BeforeEach
    void createConditions() {
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.getResourceService().loadSkins(skinPaths);
        ServiceLocator.getResourceService().loadAll();
        stage = mock(Stage.class);

        RenderService renderService = new RenderService();
        renderService.setStage(stage);

        ServiceLocator.registerRenderService(renderService);

        displayManager = new InventoryDisplayManager(stage);

        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerInventoryDisplayManager(displayManager);
    }
    @Test
    void testAdd() {
        InventoryDisplay displayOne = new InventoryDisplay("testRefresh", "testOpen", 30, 10, false);
        InventoryDisplay displayTwo = new InventoryDisplay("testRefresh2", "testOpen2", 30, 10, false);
        displayManager.addInventoryDisplay(displayOne);
        displayManager.addInventoryDisplay(displayTwo);
        assertTrue(displayManager.getInventoryDisplays().contains(displayOne));
        assertTrue(displayManager.getInventoryDisplays().contains(displayTwo));
    }
    @Test
    void testRemove() {
        InventoryDisplay displayOne = new InventoryDisplay("testRefresh", "testOpen", 30, 10, false);
        InventoryDisplay displayTwo = new InventoryDisplay("testRefresh2", "testOpen2", 30, 10, false);
        displayManager.addInventoryDisplay(displayOne);
        displayManager.addInventoryDisplay(displayTwo);
        displayManager.removeInventoryDisplay(displayOne);
        displayManager.removeInventoryDisplay(displayTwo);
        assertFalse(displayManager.getInventoryDisplays().contains(displayOne));
        assertFalse(displayManager.getInventoryDisplays().contains(displayTwo));
    }
}
