package com.csse3200.game.components.plants;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for PlantProximityComponent class
 */
@ExtendWith(GameExtension.class)
public class PlantProximityComponentTest {
    PlantProximityComponent plantProximityComponent;

    @Mock
    Entity entity;

    @Mock
    Fixture fixture1;

    @Mock
    Fixture fixture2;

    /**
     * Sets up the test environment before each test case.
     */
    @BeforeEach
    public void setUp() {
        plantProximityComponent = new PlantProximityComponent();
        plantProximityComponent.setEntity(entity);
    }

    /**
     * Checks whether the initial radius is set to the expected value.
     */
    @Test
    public void testConstructor() {
        assertEquals(8f, plantProximityComponent.getRadius());
    }
}
