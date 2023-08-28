package com.csse3200.game.entities.factories;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.plants.BasePlantConfig;
import com.csse3200.game.entities.configs.plants.PlantConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;

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
        when(mockFileHandle.reader("UTF-8")).thenReturn(new StringReader("""
                {
                  "cosmicCob": {
                    "health": 100,
                    "name": "Cosmic Cob",
                    "type": "FOOD",
                    "description": "Nutritious space corn!",
                    "idealWaterLevel": 0.7,
                    "adultLifeSpan": 5,
                    "maxHealth": 300
                  },
                  "aloeVera": {
                    "health": 50,
                    "name": "Aloe Vera",
                    "type": "HEALTH",
                    "description": "Produces gel that can be used for healing",
                    "idealWaterLevel": 0.7,
                    "adultLifeSpan": 5,
                    "maxHealth": 300
                  },
                  "hammerPlant": {
                    "health": 10,
                    "name": "Hammer Plant",
                    "type": "REPAIR",
                    "description": "Repairs plants within its healing radius",
                    "idealWaterLevel": 0.7,
                    "adultLifeSpan": 5,
                    "maxHealth": 300
                  },
                  "venusFlyTrap": {
                    "health": 100,
                    "name": "Space Snapper",
                    "type": "DEFENCE",
                    "description" : "I eat the fauna!",
                    "idealWaterLevel": 0.7,
                    "adultLifeSpan": 5,
                    "maxHealth": 300
                  },
                  "waterWeed": {
                    "health": 10,
                    "name": "Atomic Algae",
                    "type": "PRODUCTION",
                    "description": "Test description",
                    "idealWaterLevel": 0.7,
                    "adultLifeSpan": 5,
                    "maxHealth": 300
                  },
                  "atropaBelladonna": {
                    "health": 200,
                    "name": "Deadly Nightshade",
                    "type": "DEADLY",
                    "description": "Grows deadly poisonous berries",
                    "idealWaterLevel": 0.7,
                    "adultLifeSpan": 5,
                    "maxHealth": 300
                  },
                  "nicotianaTabacum": {
                    "health": 20,
                    "name": "Tobacco",
                    "type": "DEADLY",
                    "description": "Toxic addicted plant leaves",
                    "idealWaterLevel": 0.7,
                    "adultLifeSpan": 5,
                    "maxHealth": 300
                  },
                  "sunFlower": {
                    "health" : 10,
                    "name": "Horticultural Heater",
                    "type": "PRODUCTION",
                    "description": "Warms up the nearby area",
                    "idealWaterLevel": 0.7,
                    "adultLifeSpan": 5,
                    "maxHealth": 300
                  }
                }
                """));

        // Load stats from the JSON using the FileLoader
        stats = FileLoader.readClass(PlantConfigs.class, "configs/plant.json");
        PlantFactory.setStats(stats);

    }

    @ParameterizedTest
    @MethodSource("plantConfigProvider")
    public void testPlantConfigsLoadedCorrectly(String plant, int health, String name, String type,
                                                String description, float water, int life, int maxHealth) {
        BasePlantConfig actualValues = getActualValue(plant);

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


    private BasePlantConfig getActualValue(String plant) {
        return switch (plant) {
            case "cosmicCob" -> stats.cosmicCob;
            case "aloeVera" -> stats.aloeVera;
            case "hammerPlant" -> stats.hammerPlant;
            case "venusFlyTrap" -> stats.venusFlyTrap;
            case "waterWeed" -> stats.waterWeed;
            case "atropaBelladonna" -> stats.atropaBelladonna;
            case "nicotianaTabacum" -> stats.nicotianaTabacum;
            case "sunFlower" -> stats.sunFlower;
            default -> throw new IllegalArgumentException("Unknown plant name: " + plant);
        };
    }

    @Test
    public void testCreateBasePlant() {
        PhysicsService mockPhysicsService = mock(PhysicsService.class);
        PhysicsEngine engine = mock(PhysicsEngine.class);
        Body body = mock(Body.class);

        ServiceLocator.registerPhysicsService(new PhysicsService());

        ColliderComponent component = new ColliderComponent();

        Entity plant = PlantFactory.createBasePlant();
        assertNotNull(plant);

        PhysicsComponent physicsComponent = plant.getComponent(PhysicsComponent.class);
        assertNotNull(physicsComponent, "Plant PhysicsComponent should not be null");
        assertEquals(BodyDef.BodyType.StaticBody, physicsComponent.getBody().getType(),
                "Plant physics body type should be StaticBody");

        ColliderComponent colliderComponent = plant.getComponent(ColliderComponent.class);
        assertNotNull(colliderComponent, "Plant ColliderComponent should not be null");
        //assertTrue(colliderComponent.getFixture().isSensor(), "Plant collider should be a sensor");

        HitboxComponent hitboxComponent = plant.getComponent(HitboxComponent.class);
        assertNotNull(hitboxComponent, "Plant HitboxComponent should not be null");
        //assertTrue(hitboxComponent.getFixture().isSensor(), "Plant Hitbox should be a sensor");
        assertEquals(PhysicsLayer.OBSTACLE, hitboxComponent.getLayer(),
                "Plant Hitbox layer should be OBSTACLE");
    }

    @AfterEach
    public void tearDown() {
        PlantFactory.resetStats();
    }
}
