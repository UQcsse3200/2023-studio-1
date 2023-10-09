package com.csse3200.game.services.plants;

import com.csse3200.game.components.plants.PlantInfoDisplayComponent;
import com.csse3200.game.events.EventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for PlantCommandService
 */
@ExtendWith({})
public class PlantCommandServiceTest {
    PlantCommandService plantCommandService;

    @BeforeEach
    public void setUp() {
        plantCommandService = new PlantCommandService();
    }

    /**
     * Test for getEvents when EventHandler is not null
     */
    @Test
    public void testGetEvents() {
        EventHandler event = plantCommandService.getEvents();
        assertNotNull(event);
    }


}
