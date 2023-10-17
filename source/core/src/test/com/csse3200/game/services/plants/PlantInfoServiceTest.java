package com.csse3200.game.services.plants;

import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for PlantInfoService class
 */
@ExtendWith(GameExtension.class)
public class PlantInfoServiceTest {
    PlantInfoService plantInfoService;

    /**
     * Sets up the test environment before each test case.
     */
    @BeforeEach
    public void setUp() {
        plantInfoService = new PlantInfoService();
    }

    /**
     * Test for the getAlivePlantCount method with default settings.
     */
    @Test
    public void testGetAlivePlantCount_default() {
        assertEquals(0, plantInfoService.getAlivePlantCount());
    }

    /**
     * Test for the getDecayingPlantCount method with default settings.
     */
    @Test
    public void testGetDecayingPlantCount_default() {
        assertEquals(0, plantInfoService.getDecayingPlantCount());
    }

    /**
     * Test for the getTotalSeedsPlanted method with default settings.
     */
    @Test
    public void testTotalSeedsPlanted_default() {
        assertEquals(0, plantInfoService.getTotalSeedsPlanted());
    }

    /**
     * Test for the getTotalPlantHarvestCount method with default settings.
     */
    @Test
    public void testTotalPlantHarvestCount_default() {
        assertEquals(0, plantInfoService.getTotalPlantHarvestCount());
    }

    /**
     * Test for the increasePlantGrowthStageCount method when the plant is alive.
     */
    @Test
    public void testIncreasePlantGrowthStageCount_alive() {
        plantInfoService.increasePlantGrowthStageCount(1, "alive");
        assertEquals(1, plantInfoService.getAlivePlantCount());
        assertEquals(0, plantInfoService.getDecayingPlantCount());
    }

    /**
     * Test for the increasePlantGrowthStageCount method when the plant is decaying.
     */
    @Test
    public void testIncreasePlantGrowthStageCount_decay() {
        plantInfoService.increasePlantGrowthStageCount(1, "decay");
        assertEquals(0, plantInfoService.getAlivePlantCount());
        assertEquals(1, plantInfoService.getDecayingPlantCount());
    }

    /**
     * Test for the increasePlantGrowthStageCount method with a negative count.
     */
    @Test
    public void testIncreasePlantGrowthStageCount_negative() {
        plantInfoService.increasePlantGrowthStageCount(-1, "alive");
        assertEquals(-1, plantInfoService.getAlivePlantCount());
        assertEquals(0, plantInfoService.getDecayingPlantCount());
    }

    /**
     * Test for the increaseSeedsPlanted method with an unknown plant name.
     */
    @Test
    public void testIncreaseSeedsPlanted_wrongName() {
        plantInfoService.increaseSeedsPlanted(2, "CosmicCob");
        assertEquals(0, plantInfoService.getTotalSeedsPlanted());
    }

    /**
     * Test for the increaseSeedsPlanted method with the "Cosmic Cob" plant.
     */
    @Test
    public void testIncreaseSeedsPlanted_cob() {
        plantInfoService.increaseSeedsPlanted(2, "Cosmic Cob");
        assertEquals(2, plantInfoService.getTotalSeedsPlanted());
    }

    /**
     * Test for the increaseSeedsPlanted method with the "Aloe Vera" plant.
     */
    @Test
    public void testIncreaseSeedsPlanted_aloe() {
        plantInfoService.increaseSeedsPlanted(2, "Aloe Vera");
        assertEquals(2, plantInfoService.getTotalSeedsPlanted());
    }

    /**
     * Test for the increaseSeedsPlanted method with the "Hammer Plant" plant.
     */
    @Test
    public void testIncreaseSeedsPlanted_hammer() {
        plantInfoService.increaseSeedsPlanted(2, "Hammer Plant");
        assertEquals(2, plantInfoService.getTotalSeedsPlanted());
    }

    /**
     * Test for the increaseSeedsPlanted method with the "Space Snapper" plant.
     */
    @Test
    public void testIncreaseSeedsPlanted_snapper() {
        plantInfoService.increaseSeedsPlanted(2, "Space Snapper");
        assertEquals(2, plantInfoService.getTotalSeedsPlanted());
    }

    /**
     * Test for the increaseSeedsPlanted method with the "Deadly Nightshade" plant.
     */
    @Test
    public void testIncreaseSeedsPlanted_nightshade() {
        plantInfoService.increaseSeedsPlanted(2, "Deadly Nightshade");
        assertEquals(2, plantInfoService.getTotalSeedsPlanted());
    }

    /**
     * Test for the increaseSeedsPlanted method with the "Atomic Algae" plant.
     */
    @Test
    public void testIncreaseSeedsPlanted_algae() {
        plantInfoService.increaseSeedsPlanted(2, "Atomic Algae");
        assertEquals(2, plantInfoService.getTotalSeedsPlanted());
    }

    /**
     * Test for the increaseSeedsPlanted method with a negative count.
     */
    @Test
    public void testIncreaseSeedsPlanted_negative() {
        plantInfoService.increaseSeedsPlanted(-1, "Atomic Algae");
        assertEquals(-1, plantInfoService.getTotalSeedsPlanted());
    }

    /**
     * Test for the increasePlantsHarvested method with a negative count.
     */
    @Test
    public void testIncreasePlantsHarvested_negative() {
        plantInfoService.increasePlantsHarvested(-1, "Atomic Algae");
        assertEquals(-1, plantInfoService.getTotalPlantHarvestCount());
    }

    /**
     * Test for the increasePlantsHarvested method with an unknown plant name.
     */
    @Test
    public void testIncreasePlantsHarvested_wrongName() {
        plantInfoService.increasePlantsHarvested(1, "AtomicAlgae");
        assertEquals(0, plantInfoService.getTotalPlantHarvestCount());
    }

    /**
     * Test for the increasePlantsHarvested method with the "Cosmic Cob" plant.
     */
    @Test
    public void testIncreasePlantsHarvested_cob() {
        plantInfoService.increasePlantsHarvested(1, "Cosmic Cob");
        assertEquals(1, plantInfoService.getTotalPlantHarvestCount());
    }

    /**
     * Test for the increasePlantsHarvested method with the "Aloe Vera" plant.
     */
    @Test
    public void testIncreasePlantsHarvested_aloe() {
        plantInfoService.increasePlantsHarvested(1, "Aloe Vera");
        assertEquals(1, plantInfoService.getTotalPlantHarvestCount());
    }

    /**
     * Test for the increasePlantsHarvested method with the "Hammer Plant" plant.
     */
    @Test
    public void testIncreasePlantsHarvested_hammer() {
        plantInfoService.increasePlantsHarvested(1, "Hammer Plant");
        assertEquals(1, plantInfoService.getTotalPlantHarvestCount());
    }

    /**
     * Test for the increasePlantsHarvested method with the "Space Snapper" plant.
     */
    @Test
    public void testIncreasePlantsHarvested_snapper() {
        plantInfoService.increasePlantsHarvested(1, "Space Snapper");
        assertEquals(1, plantInfoService.getTotalPlantHarvestCount());
    }

    /**
     * Test for the increasePlantsHarvested method with the "Deadly Nightshade" plant.
     */
    @Test
    public void testIncreasePlantsHarvested_nightshade() {
        plantInfoService.increasePlantsHarvested(1, "Deadly Nightshade");
        assertEquals(1, plantInfoService.getTotalPlantHarvestCount());
    }

    /**
     * Test for the increasePlantsHarvested method with the "Atomic Algae" plant.
     */
    @Test
    public void testIncreasePlantsHarvested_algae() {
        plantInfoService.increasePlantsHarvested(1, "Atomic Algae");
        assertEquals(1, plantInfoService.getTotalPlantHarvestCount());
    }

    /**
     * Test for the increasePlantsHarvested method that updates clearPlantInfo event.
     */
    @Test
    public void testIncreasePlantsHarvested_updateClearInfo() {
        EventHandler event = mock(EventHandler.class);
        plantInfoService.setEvents(event);
        plantInfoService.increasePlantsHarvested(1, "Atomic Algae");

        verify(event, times(1)).trigger("clearPlantInfo");
    }

    /**
     * Test for the increaseSeedsPlanted method that updates clearPlantInfo event.
     */
    @Test
    public void testIncreaseSeedsPlanted_updateClearInfo() {
        EventHandler event = mock(EventHandler.class);
        plantInfoService.setEvents(event);
        plantInfoService.increaseSeedsPlanted(1, "Atomic Algae");

        verify(event, times(1)).trigger("clearPlantInfo");
    }

    /**
     * Test for the updateClearInfo method.
     */
    @Test
    public void testUpdateClearInfo() {
        EventHandler event = mock(EventHandler.class);
        plantInfoService.setEvents(event);
        plantInfoService.updateClearInfo();

        verify(event, times(1)).trigger("clearPlantInfo");
    }

    /**
     * Test for the plantInfoSummary method with default settings.
     */
    @Test
    public void testPlantInfoSummary_default() {
        String summary = "Total Seeds Planted: 0";
        assertEquals(summary, plantInfoService.plantInfoSummary());
    }

    /**
     * Test for the plantInfoSummary method when plants have been harvested.
     */
    @Test
    public void testPlantInfoSummary_harvested() {
        plantInfoService.increasePlantsHarvested(1, "Cosmic Cob");
        String summary = "Total Seeds Planted: 0" +
                "\nPlants Harvested: 1";
        assertEquals(summary, plantInfoService.plantInfoSummary());
    }

    /**
     * Test for the plantInfoSummary method when seeds have been planted.
     */
    @Test
    public void testPlantInfoSummary_planted() {
        plantInfoService.increaseSeedsPlanted(1, "Cosmic Cob");
        String summary = "Total Seeds Planted: 1";
        assertEquals(summary, plantInfoService.plantInfoSummary());
    }

    /**
     * Test for the plantInfoSummary method when there are decaying plants.
     */
    @Test
    public void testPlantInfoSummary_decay() {
        plantInfoService.setDecayingPlantCount(1);
        String summary = "Total Seeds Planted: 0" +
                "\nDecaying Plants: 1";
        assertEquals(summary, plantInfoService.plantInfoSummary());
    }
}
