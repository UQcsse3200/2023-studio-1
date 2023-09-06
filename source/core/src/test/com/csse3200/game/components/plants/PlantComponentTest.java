package com.csse3200.game.components.plants;

import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.csse3200.game.entities.factories.PlantFactory.createBasePlant;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class PlantComponentTest {

    private PlantComponent testPlant;
    @BeforeEach
    void beforeEach() {
        CropTileComponent testCropTile = new CropTileComponent(1, 10);
        testPlant = new PlantComponent(100, "testPlant", "defence", "This is a " +
                "plant created for testing.", 1, 2, 500, testCropTile, new int[] {}, new String[] {}, new String[] {});
    }

    @Test
    void testGetPlantHealth() {
        assertEquals(100, testPlant.getPlantHealth());
    }

    @Test
    void testGetMaxHealth() {
        assertEquals(500, testPlant.getMaxHealth());
    }

    @Test
    void testSetPlantHealth() {
        testPlant.setPlantHealth(50);
        assertEquals(50, testPlant.getPlantHealth());
    }

    @Test
    void testIncreasePlantHealth() {
        int plantHealthIncrement = 2;
        testPlant.increasePlantHealth(plantHealthIncrement);
        assertEquals(102, testPlant.getPlantHealth());
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
    void testGetCurrentAge() {
        assertEquals(0, testPlant.getCurrentAge());
    }

    @Test
    void testSetCurrentAge() {
        testPlant.setCurrentAge(2.5F);
        assertEquals(2.5F, testPlant.getCurrentAge());
    }

    @Test
    void testGetIdealWaterLevel() {
        assertEquals(1, testPlant.getIdealWaterLevel());
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
    void testIsDeadTrue() {
        testPlant.setGrowthStage(7);
        assertTrue(testPlant.isDead());
    }

    @Test
    void testIsDeadFalse() {
        assertFalse(testPlant.isDead());
    }


}
