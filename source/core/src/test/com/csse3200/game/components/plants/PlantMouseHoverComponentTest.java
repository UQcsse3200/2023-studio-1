package com.csse3200.game.components.plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for PlantMouseHoverComponent
 */
@ExtendWith({})
public class PlantMouseHoverComponentTest {
    PlantMouseHoverComponent plantMouseHoverComponent;

    @Mock
    private CameraComponent cameraComponent;


    @BeforeEach
    public void setUp() {
        plantMouseHoverComponent = new PlantMouseHoverComponent();
    }

    @Test
    public void test() {
        //
    }

    @Test
    public void testUpdateInfo() {
        //when(Gdx.input.getX()).thenReturn(1);
        //when(Gdx.input.getY()).thenReturn(1);

        //plantMouseHoverComponent.updateInfo();

        //verify(ServiceLocator.getPlantInfoService().getEvents()).trigger("showPlantInfo", anyString(), anyString());
    }

    /**
     * Test isPlantDead is false by default
     */
    @Test
    public void testIsPlantDeadIsFalse() {
        assertFalse(plantMouseHoverComponent.isPlantDead());
    }

    /**
     * Test isPlantDead is true when plantDied is called
     */
    @Test
    public void testIsPlantDeadIsTrue() {
        plantMouseHoverComponent.plantDied();
        assertTrue(plantMouseHoverComponent.isPlantDead());
    }

}
