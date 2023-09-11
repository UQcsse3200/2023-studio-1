package com.csse3200.game.components.plants;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;
import com.csse3200.game.services.ResourceService;
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

    PlantComponent testPlant;
    CropTileComponent mockCropTile;
    Entity mockEntity;
    TimeService mockTimeService;
    EventHandler mockEventHandler;
    MockedStatic<ServiceLocator> mockServiceLocator;
    DynamicTextureRenderComponent mockTextureComponent;
    ResourceService mockResourceService;
    Sound mockSound;

    int health = 100;
    String name = "testPlant";
    String type = "DEFENCE";
    String description = "Test plant";
    int idealWaterLevel = 1;
    int adultLifeSpan = 2;
    int maxHealth = 500;
    int[] growthStageThresholds = new int[]{1,2,3};
    String[] soundArray = new String[]{"sound1", "sound2"};
    String[] imagePaths = new String[]{"path1", "path2", "path3", "path4", "path5"};

    @BeforeEach
    void beforeEach() {
        mockCropTile = mock(CropTileComponent.class);
        mockEntity = mock(Entity.class);
        mockTextureComponent = mock(DynamicTextureRenderComponent.class);
        mockResourceService = mock(ResourceService.class);
        mockSound = mock(Sound.class);
        ServiceLocator.registerResourceService(mockResourceService);

        when(mockResourceService.getAsset(anyString(), eq(Sound.class))).thenReturn(mockSound);

        testPlant = new PlantComponent(health, name, type, description, idealWaterLevel,
                adultLifeSpan, maxHealth, mockCropTile, growthStageThresholds,soundArray,imagePaths);
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

        verify(mockEntity.getEvents()).addListener(eq("harvest"), (EventListener0) any());
        verify(mockEntity.getEvents()).addListener(eq("destroyPlant"), (EventListener0) any());
        verify(mockEntity.getEvents()).addListener(eq("attack"), (EventListener0) any());
        verify(mockTimeService.getEvents()).addListener(eq("hourUpdate"), (EventListener0) any());
        verify(mockTimeService.getEvents()).addListener(eq("dayUpdate"), (EventListener0) any());
        verify(mockEntity).getComponent(DynamicTextureRenderComponent.class);
    }*/

    @Test
    void testGetPlantHealth() {
        assertEquals(health, testPlant.getPlantHealth());
    }

    @Test
    void testSetPlantHealth() {
        testPlant.setPlantHealth(50);
        assertEquals(50, testPlant.getPlantHealth());
    }

    @Test
    void testGetMaxHealth() {
        assertEquals(maxHealth, testPlant.getMaxHealth());
    }

    @Test
    void testIncreasePlantHealth() {
        int plantHealthIncrement = 2;
        testPlant.increasePlantHealth(plantHealthIncrement);
        assertEquals(health + plantHealthIncrement, testPlant.getPlantHealth());
    }

    @Test
    void testDecreasePlantHealth() {
        int plantHealthIncrement = -2;
        testPlant.increasePlantHealth(plantHealthIncrement);
        assertEquals(health + plantHealthIncrement, testPlant.getPlantHealth());
    }

    @Test
    void testGetPlantName() {
        assertEquals(name, testPlant.getPlantName());
    }

    @Test
    void testGetPlantType() {
        assertEquals(type, testPlant.getPlantType());
    }

    @Test
    void testGetPlantDescription() {
        assertEquals(description, testPlant.getPlantDescription());
    }

    @Test
    void testIsDecayFalse() {
        assertFalse(testPlant.isDecay());
    }

    @Test
    void testGetIdealWaterLevel() {
        assertEquals(idealWaterLevel, testPlant.getIdealWaterLevel());
    }

    @Test
    void testGetGrowthStage() {
        assertEquals(1, testPlant.getGrowthStage().getValue());
    }

    @Test
    void testSetGrowthStage() {
        testPlant.setGrowthStage(3);
        assertEquals(3, testPlant.getGrowthStage().getValue());
    }

    @Test
    void testGetAdultLifeSpan() {
        assertEquals(adultLifeSpan, testPlant.getAdultLifeSpan());
    }

    @Test
    void testSetAdultLifeSpan() {
        testPlant.setAdultLifeSpan(3);
        assertEquals(3, testPlant.getAdultLifeSpan());
    }

    @Test
    void testIncreaseGrowthStage() {
        testPlant.increaseGrowthStage(1);
        assertEquals(2, testPlant.getGrowthStage().getValue());
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
        assertEquals(health - 10, testPlant.getPlantHealth());
    }

    @Test
    void testIsDeadTrue() {
        testPlant.setGrowthStage(6);
        assertTrue(testPlant.isDead());
    }

    @Test
    void testIsDeadFalse() {
        testPlant.setGrowthStage(1);
        assertFalse(testPlant.isDead());
    }

    @Test
    void testUpdateMaxHealth_GrowthStage1() {
        testPlant.setGrowthStage(1);
        testPlant.updateMaxHealth();
        assertEquals(maxHealth * 0.05, testPlant.getCurrentMaxHealth(), 0.01);
    }

    @Test
    void testUpdateMaxHealth_GrowthStage2() {
        testPlant.setGrowthStage(2);
        testPlant.updateMaxHealth();
        assertEquals(maxHealth * 0.1, testPlant.getCurrentMaxHealth(), 0.01);
    }

    @Test
    void testUpdateMaxHealth_GrowthStage3() {
        testPlant.setGrowthStage(3);
        testPlant.updateMaxHealth();
        assertEquals(maxHealth * 0.3, testPlant.getCurrentMaxHealth(), 0.01);
    }

    @Test
    public void testUpdateMaxHealth_GrowthStage4() {
        testPlant.setGrowthStage(4);
        testPlant.updateMaxHealth();
        assertEquals(maxHealth, testPlant.getCurrentMaxHealth(), 0.01);
    }

    @Test
    void testUpdateMaxHealth_UnexpectedGrowthStage() {
        testPlant.setGrowthStage(5);
        assertThrows(IllegalStateException.class, () -> testPlant.updateMaxHealth());
    }

    @Test
    void testBeginDecay_Decay() {
        testPlant.setGrowthStage(4);
        testPlant.setNumOfDaysAsAdult(adultLifeSpan);
        testPlant.beginDecay();
        assertTrue(testPlant.isDecay());
    }

    @Test
    void testBeginDecay_NotDecay() {
        for (int stage = 1; stage < 4; stage++) {
            testPlant.setGrowthStage(stage);
            testPlant.setNumOfDaysAsAdult(adultLifeSpan);
            testPlant.beginDecay();
            assertFalse(testPlant.isDecay());
        }
    }

    @Test
    public void testInvalidFunctionForPlaySound() {
        assertThrows(IllegalStateException.class, () -> testPlant.playSound("invalidFunctionName"));
    }
}
