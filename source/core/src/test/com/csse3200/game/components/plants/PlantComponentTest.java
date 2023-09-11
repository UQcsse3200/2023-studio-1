package com.csse3200.game.components.plants;

import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class PlantComponentTest {

    private PlantComponent testPlant;
    private CropTileComponent mockCropTile;
    private Entity mockEntity;
    private TimeService mockTimeService;
    private EventHandler mockEventHandler;
    private MockedStatic<ServiceLocator> mockServiceLocator;
    private DynamicTextureRenderComponent mockTextureComponent;


    @BeforeEach
    void beforeEach() {
        mockCropTile = mock(CropTileComponent.class);
        mockEntity = mock(Entity.class);
        mockTextureComponent = mock(DynamicTextureRenderComponent.class);


        testPlant = new PlantComponent(100, "testPlant", "defence",
                "This is a plant created for testing.", 1, 2, 500, mockCropTile,
                new int[]{1,2,3}, new String[]{"sound1", "sound2"},
                new String[]{"path1", "path2", "path3", "path4", "path5"});
        testPlant.setEntity(mockEntity);
    }

    /**
    @Test
    void testCreate() {
        mockTimeService = mock(TimeService.class);
        mockEventHandler = mock(EventHandler.class);
        mockServiceLocator = mockStatic(ServiceLocator.class);

        when(mockEntity.getEvents()).thenReturn(mockEventHandler);
        when(mockTimeService.getEvents()).thenReturn(mockEventHandler);
        mockServiceLocator.when(ServiceLocator::getTimeService).thenReturn(mockTimeService);

        testPlant.create();

        verify(mockEventHandler).addListener(eq("harvest"), (EventListener0) any());
        verify(mockEventHandler).addListener(eq("destroyPlant"), (EventListener0) any());
        verify(mockEventHandler).addListener(eq("attack"), (EventListener0) any());
    }
     */

    @Test
    void testGetPlantHealth() {
        assertEquals(100, testPlant.getPlantHealth());
    }

    @Test
    void testSetPlantHealth() {
        testPlant.setPlantHealth(50);
        assertEquals(50, testPlant.getPlantHealth());
    }

    @Test
    void testGetMaxHealth() {
        assertEquals(500, testPlant.getMaxHealth());
    }

    @Test
    void testIncreasePlantHealth() {
        int plantHealthIncrement = 2;
        testPlant.increasePlantHealth(plantHealthIncrement);
        assertEquals(102, testPlant.getPlantHealth());
    }

    @Test
    void testDecreasePlantHealth() {
        int plantHealthIncrement = -2;
        testPlant.increasePlantHealth(plantHealthIncrement);
        assertEquals(98, testPlant.getPlantHealth());
    }

    @Test
    void testGetPlantName() {
        assertEquals("testPlant", testPlant.getPlantName());
    }

    @Test
    void testGetPlantType() {
        assertEquals("defence", testPlant.getPlantType());
    }

    @Test
    void testGetPlantDescription() {
        assertEquals("This is a plant created for testing.", testPlant.getPlantDescription());
    }

    @Test
    void testSetDecay() {
        testPlant.setDecay(true);
        assertTrue(testPlant.isDecay());
    }

    @Test
    void testIsDecayFalse() {
        assertFalse(testPlant.isDecay());
    }

    @Test
    void testIsDecayTrue() {
        testPlant.setDecay(true);
        assertTrue(testPlant.isDecay());
    }

    @Test
    void testGetIdealWaterLevel() {
        assertEquals(1, testPlant.getIdealWaterLevel());
    }

    @Test
    void testGetCurrentAge() {
        assertEquals(0, testPlant.getCurrentAge());
    }

    @Test
    void testSetCurrentAge() {
        testPlant.setCurrentAge(2.5F);
        assertEquals(2.5F, testPlant.getCurrentAge());
    }

    @Test
    void testGetGrowthStage() {
        assertEquals(1, testPlant.getGrowthStage());
    }

    @Test
    void testSetGrowthStage() {
        testPlant.setGrowthStage(3);
        assertEquals(3, testPlant.getGrowthStage());
    }

    @Test
    void testGetAdultLifeSpan() {
        assertEquals(2, testPlant.getAdultLifeSpan());
    }

    @Test
    void testSetAdultLifeSpan() {
        testPlant.setAdultLifeSpan(3);
        assertEquals(3, testPlant.getAdultLifeSpan());
    }

    @Test
    void testIncreaseGrowthStage() {
        testPlant.increaseGrowthStage(1);
        assertEquals(2, testPlant.getGrowthStage());
    }

    @Test
    void testIncreaseCurrentAge() {
        testPlant.increaseCurrentAge(1);
        assertEquals(1, testPlant.getCurrentAge());
    }

    @Test
    void testGetCurrentGrowthLevel() {
        assertEquals(0, testPlant.getCurrentGrowthLevel());
    }

    @Test
    void testIncreaseCurrentGrowthLevelPositive(){
        when(mockCropTile.getGrowthRate(1.0f)).thenReturn(0.5);
        testPlant.increaseCurrentGrowthLevel();
        assertEquals(5, testPlant.getCurrentGrowthLevel());
    }

    @Test
    void testIncreaseCurrentGrowthLevelNegative() {
        when(mockCropTile.getGrowthRate(1.0f)).thenReturn(-0.5);
        testPlant.increaseCurrentGrowthLevel();
        assertEquals(90, testPlant.getPlantHealth());
    }

    @Test
    void testIsDeadTrue() {
        testPlant.setGrowthStage(7);
        assertTrue(testPlant.isDead());
    }

    @Test
    void testIsDeadFalse() {
        assertFalse(testPlant.isDead());
    }


}
