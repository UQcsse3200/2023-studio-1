package com.csse3200.game.components.plants;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.TimeService;
import com.csse3200.game.services.plants.PlantInfoService;
import com.csse3200.game.services.sound.EffectsMusicService;
import com.csse3200.game.services.sound.SoundService;
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
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for verifying the functionality of PlantComponent.
 */
@ExtendWith(GameExtension.class)
public class PlantComponentTest {

    PlantComponent testPlant;
    CropTileComponent mockCropTile;
    Entity mockEntity;
    DynamicTextureRenderComponent mockTextureComponent;
    ResourceService mockResourceService;
    Sound mockSound;
    PlantAreaOfEffectComponent mockPlantAreaOfEffect;
    PlantInfoService mockPlantInfoService;
    SoundService mockSoundService;


    int health = 100;
    String name = "testPlant";
    String type = "DEFENCE";
    String description = "Test plant";
    int idealWaterLevel = 1;
    int adultLifeSpan = 2;
    int maxHealth = 500;
    int[] growthStageThresholds = new int[]{1,2,3};

    @Mock
    Entity entity;

    /**
     * Sets up the necessary mock components and initializes the test plant component before each test.
     */
    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerTimeService(mock(TimeService.class));
        when(ServiceLocator.getTimeService().getMinute()).thenReturn(20);
        mockSoundService = mock(SoundService.class);
        ServiceLocator.registerSoundService(mockSoundService);

        when(mockSoundService.getEffectsMusicService()).thenReturn(mock(EffectsMusicService.class));

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

    /**
     * Tests if the plant health getter returns the correct value.
     */
    @Test
    void testGetPlantHealth() {
        assertEquals(health, testPlant.getPlantHealth());
    }

    /**
     * Tests if setting the plant's health correctly updates its value.
     */
    @Test
    void testSetPlantHealth() {
        testPlant.setPlantHealth(50);
        assertEquals(50, testPlant.getPlantHealth());
    }

    /**
     * Tests if the plant max health getter returns the correct value.
     */
    @Test
    void testGetMaxHealth() {
        assertEquals(maxHealth, testPlant.getMaxHealth());
    }

    /**
     * Tests if increasing the plant's health correctly updates its value.
     */
    @Test
    void testIncreasePlantHealth() {
        int plantHealthIncrement = 2;
        testPlant.setGrowthStage(PlantComponent.GrowthStage.ADULT.getValue());
        testPlant.increasePlantHealth(plantHealthIncrement);
        assertEquals(health + plantHealthIncrement, testPlant.getPlantHealth());
    }

    /**
     * Tests if decreasing the plant's health correctly updates its value.
     */
    @Test
    void testDecreasePlantHealth() {
        int plantHealthIncrement = -2;
        testPlant.setGrowthStage(PlantComponent.GrowthStage.ADULT.getValue());
        testPlant.increasePlantHealth(plantHealthIncrement);
        assertEquals(health + plantHealthIncrement, testPlant.getPlantHealth());
    }

    /**
     * Tests if the plant name getter returns the correct value.
     */
    @Test
    void testGetPlantName() {
        assertEquals(name, testPlant.getPlantName());
    }

    @Test
    void testPlantNameEffect_hammer() {
        PlantComponent plant = new PlantComponent(health, "Hammer Plant", type, description, idealWaterLevel,
                adultLifeSpan, maxHealth, mockCropTile, growthStageThresholds);
        assertEquals("Health", plant.getAdultEffect());
    }

    @Test
    void testPlantNameEffect_snapper() {
        PlantComponent plant = new PlantComponent(health, "Space Snapper", type, description, idealWaterLevel,
                adultLifeSpan, maxHealth, mockCropTile, growthStageThresholds);
        assertEquals("Eat", plant.getAdultEffect());
    }

    @Test
    void testPlantNameEffect_nightshade() {
        PlantComponent plant = new PlantComponent(health, "Deadly Nightshade", type, description, idealWaterLevel,
                adultLifeSpan, maxHealth, mockCropTile, growthStageThresholds);
        assertEquals("Poison", plant.getAdultEffect());
    }

    @Test
    void testPlantNameEffect_other() {
        assertEquals("Sound", testPlant.getAdultEffect());
    }

    /**
     * Tests if the plant type getter returns the correct value.
     */
    @Test
    void testGetPlantType() {
        assertEquals(type, testPlant.getPlantType());
    }

    /**
     * Tests if the plant description getter returns the correct value.
     */
    @Test
    void testGetPlantDescription() {
        assertEquals(description, testPlant.getPlantDescription());
    }

    /**
     * Tests if setting the plant's decay correctly updates its value.
     */
    @Test
    void testSetDecayTrue() {
        testPlant.setDecay();
        assertTrue(testPlant.isDecay());
    }

    /**
     * Tests if setting the plant's decay correctly updates its value.
     */
    @Test
    void testIsDecayFalse() {
        assertFalse(testPlant.isDecay());
    }

    /**
     * Tests if the plant ideal water level getter returns the correct value.
     */
    @Test
    void testGetIdealWaterLevel() {
        assertEquals(idealWaterLevel, testPlant.getIdealWaterLevel());
    }

    /**
     * Tests if the plant growth stage getter returns the correct value.
     */
    @Test
    void testGetGrowthStage() {
        assertEquals(1, testPlant.getGrowthStage().getValue());
    }

    /**
     * Tests if setting the plant's growth stage correctly updates its value.
     */
    @Test
    void testSetGrowthStage() {
        for (int i = 2; i < 7; i++) {
            testPlant.setGrowthStage(i);
            assertEquals(i, testPlant.getGrowthStage().getValue());
        }
    }

    /**
     * Tests if the plant adult life span getter returns the correct value.
     */
    @Test
    void testGetAdultLifeSpan() {
        assertEquals(adultLifeSpan, testPlant.getAdultLifeSpan());
    }

    /**
     * Tests if setting the plant's adult life span correctly updates its value.
     */
    @Test
    void testSetAdultLifeSpan() {
        testPlant.setAdultLifeSpan(3);
        assertEquals(3, testPlant.getAdultLifeSpan());
    }

    /**
     * Tests if increasing the plant's growth stage correctly updates its value.
     */
    @Test
    void testIncreaseGrowthStage() {
        testPlant.increaseGrowthStage(1);
        assertEquals(2, testPlant.getGrowthStage().getValue());
    }

    /**
     * Tests if the plant current growth level getter returns the correct value.
     */
    @Test
    void testGetCurrentGrowthLevel() {
        assertEquals(1, testPlant.getCurrentGrowthLevel());
    }

    /**
     * Tests if the plant current max health getter returns the correct value.
     */
    @Test
    void testGetCurrentMaxHealth() {
        assertEquals(maxHealth, testPlant.getCurrentMaxHealth());
    }

    /**
     * Tests if the plant number of days as adult getter returns the correct value.
     */
    @Test
    void testGetNumOfDaysAsAdult() {
        assertEquals(0, testPlant.getNumOfDaysAsAdult());
    }

    /**
     * Tests if setting the plant's num of days as adult correctly updates its value.
     */
    @Test
    void testSetNumOfDaysAsAdult() {
        testPlant.setNumOfDaysAsAdult(1);
        assertEquals(1, testPlant.getNumOfDaysAsAdult());
    }

    /**
     * Tests if increasing the plant's current growth level correctly updates its value.
     */
    @Test
    void testIncreaseCurrentGrowthLevelPositive(){
        when(mockCropTile.getGrowthRate(1.0f)).thenReturn(0.5);
        testPlant.setGrowthStage(PlantComponent.GrowthStage.JUVENILE.getValue());
        testPlant.increaseCurrentGrowthLevel();
        assertEquals(1, testPlant.getCurrentGrowthLevel());
    }

    /**
     * Tests if decreasing the plant's current growth level correctly updates its value.
     */
    @Test
    void testIncreaseCurrentGrowthLevelNegative() {
        when(mockCropTile.getGrowthRate(1.0f)).thenReturn(-0.5);
        testPlant.setGrowthStage(PlantComponent.GrowthStage.ADULT.getValue());
        testPlant.increaseCurrentGrowthLevel();
        assertEquals(health - 2, testPlant.getPlantHealth());
    }

    /**
     * Tests if setting the plant to dead correctly shows is dead.
     */
    @Test
    void testIsDeadTrue() {
        testPlant.setGrowthStage(6);
        assertTrue(testPlant.isDead());
    }

    /**
     * Tests if setting the plant to seedling correctly shows not dead.
     */
    @Test
    void testIsDeadFalse() {
        testPlant.setGrowthStage(1);
        assertFalse(testPlant.isDead());
    }

    /**
     * Tests if setting the plant's growth stage correctly updates its max health.
     */
    @Test
    void testUpdateMaxHealth_GrowthStage1() {
        testPlant.setGrowthStage(1);
        testPlant.updateMaxHealth();
        assertEquals(maxHealth * 0.05, testPlant.getCurrentMaxHealth(), 0.01);
    }

    /**
     * Tests if setting the plant's growth stage correctly updates its max health.
     */
    @Test
    void testUpdateMaxHealth_GrowthStage2() {
        testPlant.setGrowthStage(2);
        testPlant.updateMaxHealth();
        assertEquals(maxHealth * 0.1, testPlant.getCurrentMaxHealth(), 0.01);
    }

    /**
     * Tests if setting the plant's growth stage correctly updates its max health.
     */
    @Test
    void testUpdateMaxHealth_GrowthStage3() {
        testPlant.setGrowthStage(3);
        testPlant.updateMaxHealth();
        assertEquals(maxHealth * 0.3, testPlant.getCurrentMaxHealth(), 0.01);
    }

    /**
     * Tests if setting the plant's growth stage correctly updates its max health.
     */
    @Test
    void testUpdateMaxHealth_GrowthStage4() {
        testPlant.setGrowthStage(4);
        testPlant.updateMaxHealth();
        assertEquals(maxHealth, testPlant.getCurrentMaxHealth(), 0.01);
    }

    /**
     * Tests for plant unexpected growth stage
     */
    @Test
    void testSetGrowthStage_UnexpectedGrowthStage() {
        assertThrows(IllegalArgumentException.class, () -> testPlant.setGrowthStage(7));
    }

    /**
     * test for update methods
     */
    @Test
    void testUpdate() {
        testPlant.minuteUpdate();
        testPlant.hourUpdate();
    }

    /**
     * test for day update method
     */
    @Test
    void testDayUpdate() {
        testPlant.dayUpdate();
    }

    /**
     * test adult life not adult
     */
    @Test
    void testAdultLifeSpan_notAdult() {
        PlantComponent plant = mock(PlantComponent.class);
        plant.setGrowthStage(1);
        plant.adultLifeSpanCheck();

        verify(plant, never()).playSound(any(), any());
        verify(plant, never()).updateTexture();
    }

    /**
     * test for adult life adult
     */
    @Test
    void testAdultLifeSpan_Adult() {
        testPlant.setGrowthStage(4);
        testPlant.adultLifeSpanCheck();

        assertEquals(4, testPlant.getGrowthStage().getValue());
        assertEquals(1, testPlant.getNumOfDaysAsAdult());
    }

    @Test
    void testSetHarvestYields() {
        Map<String, Integer> sampleHarvestYields = new HashMap<>();
        sampleHarvestYields.put("a", 1);
        sampleHarvestYields.put("b", 2);

        testPlant.setHarvestYields(sampleHarvestYields);
        Map<String, Integer> internalHarvestYields = testPlant.getHarvestYields();

        assertNotSame(sampleHarvestYields, internalHarvestYields);
        assertEquals(sampleHarvestYields, internalHarvestYields);
    }

    /**
     * test setter for harvest yields 
     */
    @Test
    void testSetHarvestYields_unexpected() {
        Map<String, Integer> sampleHarvestYields = new HashMap<>();
        sampleHarvestYields.put("a", 1);
        sampleHarvestYields.put("b", 2);

        testPlant.setHarvestYields(sampleHarvestYields);
        Map<String, Integer> internalHarvestYields = testPlant.getHarvestYields();

        assertThrows(UnsupportedOperationException.class, () -> {
            internalHarvestYields.put("c", 3);
        });
    }

    /**
     * test force seedling
     */
    @Test
    void testForceSeedling() {
        testPlant.forceSeedling();
        assertFalse(testPlant.isDead());
    }

    /**
     * test force sprout
     */
    @Test
    void testForceSprout() {
        testPlant.setGrowthStage(2);
        testPlant.forceSprout();
        assertFalse(testPlant.isDead());
    }

    /**
     * test force juvinile
     */
    @Test
    void testForceJuvenile() {
        testPlant.setGrowthStage(3);
        testPlant.forceJuvenile();
        assertFalse(testPlant.isDead());
    }

    /**
     * test force adult
     */
    @Test
    void testForceAdult() {
        testPlant.setGrowthStage(4);
        testPlant.forceAdult();
        assertFalse(testPlant.isDead());
    }

    /**
     * test force decay
     */
    @Test
    void testForceDecay() {
        testPlant.setGrowthStage(5);
        testPlant.forceDecay();
        assertFalse(testPlant.isDead());
    }

    /**
     * test force dead
     */
    @Test
    void testForceDead() {
        testPlant.setGrowthStage(6);
        testPlant.forceDead();
        assertTrue(testPlant.isDead());
    }

    /**
     * test growth stage sprout
     */
    @Test
    void testGrowthStage_2() {
        testPlant.setGrowthStage(2);
        testPlant.forceGrowthStage("sprout");
        assertFalse(testPlant.isDead());
        assertEquals(2, testPlant.getGrowthStage().getValue());
    }

    /**
     * test growth stage juvenile
     */
    @Test
    void testGrowthStage_3() {
        testPlant.setGrowthStage(3);
        testPlant.forceGrowthStage("juvenile");
        assertFalse(testPlant.isDead());
        assertEquals(3, testPlant.getGrowthStage().getValue());
    }

    /**
     * test growth stage decaying
     */
    @Test
    void testGrowthStage_5() {
        testPlant.setGrowthStage(5);
        testPlant.forceGrowthStage("decay");
        assertFalse(testPlant.isDead());
        assertEquals(5, testPlant.getGrowthStage().getValue());
    }

    /**
     * test growth stage dead
     */
    @Test
    void testGrowthStage_6() {
        testPlant.setGrowthStage(6);
        testPlant.forceGrowthStage("dead");
        assertTrue(testPlant.isDead());
        assertEquals(6, testPlant.getGrowthStage().getValue());
    }
}
