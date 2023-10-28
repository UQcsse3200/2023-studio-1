package com.csse3200.game.components.plants;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.plants.PlantInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Tests for PlantMouseHoverComponent class
 */
@ExtendWith(GameExtension.class)
class PlantMouseHoverComponentTest {
    PlantMouseHoverComponent plantMouseHoverComponent;

    @Mock
    private PlantInfoService plantInfoService;

    /**
     * Sets up the test environment before each test case.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        plantMouseHoverComponent = new PlantMouseHoverComponent();
        plantMouseHoverComponent.setEntity(new Entity());
        ServiceLocator.registerPlantInfoService(plantInfoService);
        when(plantInfoService.getEvents()).thenReturn(new EventHandler());
    }

    /**
     * Test isPlantDead is false by default
     */
    @Test
    void testIsPlantDead_False() {
        assertFalse(plantMouseHoverComponent.isPlantDead());
    }

    /**
     * Test isPlantDead is true when setPlantDied is called
     */
    @Test
    void testIsPlantDead_True() {
        plantMouseHoverComponent.setPlantDied(true);
        assertTrue(plantMouseHoverComponent.isPlantDead());
    }

    /**
     * Test isShowInfo is false by default
     */
    @Test
    void testIsShowInfo_False() {
        assertFalse(plantMouseHoverComponent.isShowInfo());
    }

    /**
     * Test isShowInfo is true when setShowInfo is called
     */
    @Test
    void testIsShowInfo_True() {
        plantMouseHoverComponent.setShowInfo(true);
        assertTrue(plantMouseHoverComponent.isShowInfo());
    }

    /**
     * Test noMoreUse is false by default
     */
    @Test
    void testIsNoMoreUse_False() {
        assertFalse(plantMouseHoverComponent.isNoMoreUse());
    }

    /**
     * Test noMoreUse is true when setNoMoreUse is called
     */
    @Test
    void testIsNoMoreUse_True() {
        plantMouseHoverComponent.setNoMoreUse(true);
        assertTrue(plantMouseHoverComponent.isNoMoreUse());
    }

    /**
     * Test showInfo, plantDead, and noMoreUse are false when create is called
     */
    @Test
    void testCreate() {
        plantMouseHoverComponent.create();
        assertFalse(plantMouseHoverComponent.isShowInfo());
        assertFalse(plantMouseHoverComponent.isPlantDead());
        assertFalse(plantMouseHoverComponent.isNoMoreUse());
    }
}
