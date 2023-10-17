package com.csse3200.game.components.plants;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests for PlantInfoDisplayComponent
 */
@ExtendWith(GameExtension.class)
public class

PlantInfoDisplayComponentTest {
    PlantInfoDisplayComponent plantInfoDisplayComponent;

    /**
     * Sets up the test environment before each test case.
     */
    @BeforeEach
    public void setUp() {
        plantInfoDisplayComponent = new PlantInfoDisplayComponent();
    }

    /**
     * madeFirstContact should return false by default
     */
    @Test
    public void testGetMadeFirstContact_False() {
        assertFalse(plantInfoDisplayComponent.getMadeFirstContact());
    }

    /**
     * Test if getMadeFirstContact returns true when MadeFirstContact is called
     */
    @Test
    public void testMadeFirstContact_True() {
        plantInfoDisplayComponent.madeFirstContact();
        assertTrue(plantInfoDisplayComponent.getMadeFirstContact());
    }
}
