package com.csse3200.game.entities.factories;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.csse3200.game.entities.configs.plants.BasePlantConfig;
import com.csse3200.game.entities.configs.plants.PlantConfigs;
import com.csse3200.game.files.FileLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.StringReader;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PlantFactoryTest {
    private PlantConfigs stats;

    @BeforeEach
    public void setUp() {
        Gdx.files = mock(Files.class);
        FileHandle mockFileHandle = mock(FileHandle.class);

        // Return the mocked FileHandle when Gdx.files.local(anyString()) is invoked
        when(Gdx.files.internal(anyString())).thenReturn(mockFileHandle);
        // Return the JSON content when the reader method of the mocked FileHandle is called
        when(mockFileHandle.reader("UTF-8")).thenReturn(new StringReader("{\n" +
                "  \"cosmicCob\": {\n" +
                "    \"health\": 100,\n" +
                "    \"name\": \"Cosmic Cob\",\n" +
                "    \"type\": \"FOOD\",\n" +
                "    \"description\": \"Nutritious space corn!\",\n" +
                "    \"idealWaterLevel\": 0.7,\n" +
                "    \"adultLifeSpan\": 5,\n" +
                "    \"maxHealth\": 300\n" +
                "  },\n" +
                "  \"aloeVera\": {\n" +
                "    \"health\": 50,\n" +
                "    \"name\": \"Aloe Vera\",\n" +
                "    \"type\": \"HEALTH\",\n" +
                "    \"description\": \"Produces gel that can be used for healing\",\n" +
                "    \"idealWaterLevel\": 0.7,\n" +
                "    \"adultLifeSpan\": 5,\n" +
                "    \"maxHealth\": 300\n" +
                "  },\n" +
                "  \"hammerPlant\": {\n" +
                "    \"health\": 10,\n" +
                "    \"name\": \"Hammer Plant\",\n" +
                "    \"type\": \"REPAIR\",\n" +
                "    \"description\": \"Repairs plants within its healing radius\",\n" +
                "    \"idealWaterLevel\": 0.7,\n" +
                "    \"adultLifeSpan\": 5,\n" +
                "    \"maxHealth\": 300\n" +
                "  },\n" +
                "  \"venusFlyTrap\": {\n" +
                "    \"health\": 100,\n" +
                "    \"name\": \"Space Snapper\",\n" +
                "    \"type\": \"DEFENCE\",\n" +
                "    \"description\" : \"I eat the fauna!\",\n" +
                "    \"idealWaterLevel\": 0.7,\n" +
                "    \"adultLifeSpan\": 5,\n" +
                "    \"maxHealth\": 300\n" +
                "  },\n" +
                "  \"waterWeed\": {\n" +
                "    \"health\": 10,\n" +
                "    \"name\": \"Atomic Algae\",\n" +
                "    \"type\": \"PRODUCTION\",\n" +
                "    \"description\": \"Test description\",\n" +
                "    \"idealWaterLevel\": 0.7,\n" +
                "    \"adultLifeSpan\": 5,\n" +
                "    \"maxHealth\": 300\n" +
                "  },\n" +
                "  \"atropaBelladonna\": {\n" +
                "    \"health\": 200,\n" +
                "    \"name\": \"Deadly Nightshade\",\n" +
                "    \"type\": \"DEADLY\",\n" +
                "    \"description\": \"Grows deadly poisonous berries\",\n" +
                "    \"idealWaterLevel\": 0.7,\n" +
                "    \"adultLifeSpan\": 5,\n" +
                "    \"maxHealth\": 300\n" +
                "  },\n" +
                "  \"nicotianaTabacum\": {\n" +
                "    \"health\": 20,\n" +
                "    \"name\": \"Tobacco\",\n" +
                "    \"type\": \"DEADLY\",\n" +
                "    \"description\": \"Toxic addicted plant leaves\",\n" +
                "    \"idealWaterLevel\": 0.7,\n" +
                "    \"adultLifeSpan\": 5,\n" +
                "    \"maxHealth\": 300\n" +
                "  },\n" +
                "  \"sunFlower\": {\n" +
                "    \"health\" : 10,\n" +
                "    \"name\": \"Horticultural Heater\",\n" +
                "    \"type\": \"PRODUCTION\",\n" +
                "    \"description\": \"Warms up the nearby area\",\n" +
                "    \"idealWaterLevel\": 0.7,\n" +
                "    \"adultLifeSpan\": 5,\n" +
                "    \"maxHealth\": 300\n" +
                "  }\n" +
                "}\n"));

        // Load stats from the JSON using the FileLoader
        stats = FileLoader.readClass(PlantConfigs.class, "configs/plant.json");
        PlantFactory.setStats(stats);

    }

    @ParameterizedTest
    @MethodSource("plantConfigProvider")
    public void testPlantConfigsLoadedCorrectly(String plant, int health, String name, String type,
                                                String description, float water, int life, int maxHealth) {
        BasePlantConfig actualValues = getHealthValue(plant);

        assertNotNull(actualValues, plant + " is null!");
        assertEquals(health, actualValues.health, "Health value for plant " + plant +
                " does not match expected!");
        assertNotNull(actualValues.name, plant + " name is null!");
        assertEquals(type, actualValues.type, "type for plant " + plant +
                " does not match expected!");
        assertNotNull(actualValues.description, plant + " description is null!");
        assertEquals(water, actualValues.idealWaterLevel, "idealWaterLevel for plant "
                + plant + " does not match expected!");
        assertEquals(life, actualValues.adultLifeSpan, "adultLifeSpan for plant " + plant
                + " does not match expected!");
        assertEquals(maxHealth, actualValues.maxHealth, "maxHealth for plant " + plant
                + " does not match expected!");
    }

    private static Stream<Arguments> plantConfigProvider() {
        return Stream.of(
                Arguments.of("cosmicCob", 100, "Cosmic Cob", "FOOD",
                        "Nutritious space corn!", (float) 0.7, 5, 300),
                Arguments.of("aloeVera", 50, "Aloe Vera", "HEALTH",
                        "Produces gel that can be used for healing", (float) 0.7, 5, 300),
                Arguments.of("hammerPlant", 10, "Hammer Plant", "REPAIR",
                        "Repairs plants within its healing radius", (float) 0.7, 5, 300),
                Arguments.of("venusFlyTrap", 100, "Space Snapper", "DEFENCE",
                        "I eat the fauna!", (float) 0.7, 5, 300),
                Arguments.of("waterWeed", 10, "Atomic Algae", "PRODUCTION",
                        "Test description", (float) 0.7, 5, 300),
                Arguments.of("atropaBelladonna", 200, "Deadly Nightshade",
                        "DEADLY", "Grows deadly poisonous berries", (float) 0.7, 5, 300),
                Arguments.of("nicotianaTabacum", 20, "Tobacco", "DEADLY",
                        "Toxic addicted plant leaves", (float) 0.7, 5, 300),
                Arguments.of("sunFlower", 10, "Horticultural Heater",
                        "PRODUCTION", "Warms up the nearby area", (float) 0.7, 5, 300)
        );
    }


    private BasePlantConfig getHealthValue(String plant) {
        // Here, use reflection or any other method to dynamically access the right field
        // from the `stats` object. This is just a basic example.
        switch (plant) {
            case "cosmicCob":
                return stats.cosmicCob;
            case "aloeVera":
                return stats.aloeVera;
            case "hammerPlant":
                return stats.hammerPlant;
            case "venusFlyTrap":
                return stats.venusFlyTrap;
            case "waterWeed":
                return stats.waterWeed;
            case "atropaBelladonna":
                return stats.atropaBelladonna;
            case "nicotianaTabacum":
                return stats.nicotianaTabacum;
            case "sunFlower":
                return stats.sunFlower;
            default:
                throw new IllegalArgumentException("Unknown plant name: " + plant);
        }
    }



    @AfterEach
    public void tearDown() {
        PlantFactory.resetStats();
    }
}
