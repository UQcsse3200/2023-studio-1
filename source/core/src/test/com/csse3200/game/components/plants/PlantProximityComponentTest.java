package com.csse3200.game.components.plants;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Tests for PlantProximityComponent class
 */
@ExtendWith(GameExtension.class)
public class PlantProximityComponentTest {
    PlantProximityComponent plantProximityComponent;

    /**
     * Sets up the test environment before each test case.
     */
    @BeforeEach
    public void setUp() {
        plantProximityComponent = new PlantProximityComponent();
        plantProximityComponent.setEntity(mock(Entity.class));
    }

    /**
     * Checks whether the initial radius is set to the expected value.
     */
    @Test
    public void testConstructor() {
        assertEquals(8f, plantProximityComponent.getRadius());
    }
}
