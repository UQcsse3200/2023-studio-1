package com.csse3200.game.services.plants;

import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for PlantCommandService
 */
@ExtendWith(GameExtension.class)
public class PlantCommandServiceTest {
    /**
     * Test for getEvents when EventHandler is not null
     */
    @Test
    public void testGetEvents() {
        PlantCommandService plantCommandService = new PlantCommandService();
        EventHandler event = plantCommandService.getEvents();
        assertNotNull(event);
    }
}
