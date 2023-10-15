package com.csse3200.game.components.plants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.csse3200.game.services.TimeService;
import com.csse3200.game.services.plants.PlantInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@ExtendWith(GameExtension.class)
class PlantComponentTest {

    PlantComponent testPlant;
    CropTileComponent mockCropTile;
    Entity mockEntity;
    DynamicTextureRenderComponent mockTextureComponent;
    ResourceService mockResourceService;
    Sound mockSound;

    PlantAreaOfEffectComponent mockPlantAreaOfEffect;
    PlantInfoService mockPlantInfoService;

    int health = 100;
    String name = "testPlant";
    String type = "DEFENCE";
    String description = "Test plant";
    int idealWaterLevel = 1;
    int adultLifeSpan = 2;
    int maxHealth = 500;
    int[] growthStageThresholds = new int[]{1,2,3};

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerTimeService(mock(TimeService.class));
        when(ServiceLocator.getTimeService().getMinute()).thenReturn(20);

        mockCropTile = mock(CropTileComponent.class);
        mockEntity = mock(Entity.class);
        mockTextureComponent = mock(DynamicTextureRenderComponent.class);
        mockResourceService = mock(ResourceService.class);
        mockSound = mock(Sound.class);
        mockPlantAreaOfEffect = mock(PlantAreaOfEffectComponent.class);
        ServiceLocator.registerResourceService(mockResourceService);
        mockPlantInfoService = mock(PlantInfoService.class);
        ServiceLocator.registerPlantInfoService(mockPlantInfoService);

        when(mockResourceService.getAsset(anyString(), eq(Sound.class))).thenReturn(mockSound);

        testPlant = new PlantComponent(health, name, type, description, idealWaterLevel,
                adultLifeSpan, maxHealth, mockCropTile, growthStageThresholds);
        testPlant.setEntity(mockEntity);
    }

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
        testPlant.setGrowthStage(PlantComponent.GrowthStage.ADULT.getValue());
        testPlant.increasePlantHealth(plantHealthIncrement);
        assertEquals(health + plantHealthIncrement, testPlant.getPlantHealth());
    }

    @Test
    void testDecreasePlantHealth() {
        int plantHealthIncrement = -2;
        testPlant.setGrowthStage(PlantComponent.GrowthStage.ADULT.getValue());
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
    void testSetDecayTrue() {
        testPlant.setDecay();
        assertTrue(testPlant.isDecay());
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
        assertEquals(1, testPlant.getCurrentGrowthLevel());
    }

    @Test
    void testGetCurrentMaxHealth() {
        assertEquals(maxHealth, testPlant.getCurrentMaxHealth());
    }

    @Test
    void testGetNumOfDaysAsAdult() {
        assertEquals(0, testPlant.getNumOfDaysAsAdult());
    }

    @Test
    void testSetNumOfDaysAsAdult() {
        testPlant.setNumOfDaysAsAdult(1);
        assertEquals(1, testPlant.getNumOfDaysAsAdult());
    }

    @Test
    void testIncreaseCurrentGrowthLevelPositive(){
        when(mockCropTile.getGrowthRate(1.0f)).thenReturn(0.5);
        testPlant.setGrowthStage(PlantComponent.GrowthStage.JUVENILE.getValue());
        testPlant.increaseCurrentGrowthLevel();
        assertEquals(1, testPlant.getCurrentGrowthLevel());
    }

    @Test
    void testIncreaseCurrentGrowthLevelNegative() {
        when(mockCropTile.getGrowthRate(1.0f)).thenReturn(-0.5);
        testPlant.setGrowthStage(PlantComponent.GrowthStage.ADULT.getValue());
        testPlant.increaseCurrentGrowthLevel();
        assertEquals(health - 2, testPlant.getPlantHealth());
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

    @ParameterizedTest
    @CsvSource({
            "1, 0.05",
            "2, 0.1",
            "3, 0.3",
            "4, 1",
    })
    void testUpdateMaxHealth_GrowthStage(int stage, float multiplier) {
        testPlant.setGrowthStage(stage);
        testPlant.updateMaxHealth();
        assertEquals(maxHealth * multiplier, testPlant.getCurrentMaxHealth(), 0.01);
    }

    @Test
    void testSetGrowthStage_UnexpectedGrowthStage() {
        assertThrows(IllegalArgumentException.class, () -> testPlant.setGrowthStage(7));
    }

    /*
    @Test
    void testInvalidFunctionForPlaySound() {
        testPlant.setPlayerInProximity(true);
        assertThrows(IllegalStateException.class, () -> testPlant.playSound("invalidFunctionName"));
    }
    */
}