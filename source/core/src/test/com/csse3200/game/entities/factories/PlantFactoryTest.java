package com.csse3200.game.entities.factories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.plants.PlantAreaOfEffectComponent;
import com.csse3200.game.components.plants.PlantComponent;
import com.csse3200.game.components.plants.PlantMouseHoverComponent;
import com.csse3200.game.components.plants.PlantProximityComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.plants.BasePlantConfig;
import com.csse3200.game.entities.configs.plants.PlantConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

/**
 * This test class aims to test the PlantFactory which is responsible for
 * creating different plants in the game.
 */
class PlantFactoryTest {
    PlantConfigs stats;
    Entity plant;
    int health = 1;
    String name = "plant";
    String type = "type";
    String description = "description";
    int idealWaterLevel = 1;
    int adultLifeSpan = 1;
    int maxHealth = 1;
    int[] growthStageThresholds = new int[]{1,1,1};
    String[] soundArray = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
    float xPosition = 1f;
    float yPosition = 1f;
    double delta = 0.001;

    // Mocked dependencies
    @Mock
    static CropTileComponent mockCropTile;
    @Mock
    ResourceService mockResourceService;
    @Mock
    Entity mockEntity;
    @Mock
    AnimationRenderComponent mockAnimationRenderComponent;

    /**
     * Set up required objects and mocked dependencies before each test.
     */
    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        setupMocks();
        setupGdxFiles();

        plant = PlantFactory.createTest(mockCropTile)
                .addComponent(new PlantAreaOfEffectComponent(2f, "None"))
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new ColliderComponent().setSensor(true))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new PlantMouseHoverComponent())
                .addComponent(new PlantProximityComponent())
                .addComponent(new PlantComponent(health,name, type, description, idealWaterLevel,
                        adultLifeSpan, maxHealth, mockCropTile, growthStageThresholds, soundArray));

        plant.setPosition(xPosition,yPosition);
        plant.getComponent(PlantComponent.class).getCropTile().getEntity().getScale();
        plant.scaleHeight(yPosition);
        PhysicsUtils.setScaledCollider(plant, xPosition, yPosition);
    }

    /**
     * Sets up mock objects required for the test.
     */
    void setupMocks() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerResourceService(mockResourceService);
        when(mockEntity.getComponent(AnimationRenderComponent.class)).thenReturn(mockAnimationRenderComponent);
        when(mockCropTile.getEntity()).thenReturn(mockEntity);
        when(mockEntity.getPosition()).thenReturn(new Vector2(5, 5));
    }

    /**
     * Mocks the file operations for the GDX framework.
     *
     * @throws IOException if there is an error accessing or reading the file.
     */
    void setupGdxFiles() throws IOException {
        Gdx.files = mock(Files.class);
        FileHandle mockFileHandle = mock(FileHandle.class);
        String filePath = "configs/plant.json";
        InputStream inputStream = new FileInputStream(filePath);
        String jsonContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        when(mockFileHandle.reader("UTF-8")).thenReturn(new StringReader(jsonContent));
        when(Gdx.files.internal(anyString())).thenReturn(mockFileHandle);

        stats = FileLoader.readClass(PlantConfigs.class, filePath);
        PlantFactory.setStats(stats);
    }

    /**
     * Test to ensure plant configurations are loaded correctly.
     *
     * @param id - The unique identifier for the plant.
     * @param health - The health value of the plant.
     * @param name - The name of the plant.
     * @param type - The type category of the plant.
     * @param description - A brief description of the plant's characteristics or purpose.
     * @param water - The ideal water level for the plant.
     * @param life - The expected adult lifespan of the plant.
     * @param maxHealth - The maximum possible health value of the plant.
     */
    @ParameterizedTest
    @MethodSource("plantConfigProvider")
    void shouldLoadPlantConfigs(String id, int health, String name, String type,
                                String description, float water, int life,
                                int maxHealth) {
        BasePlantConfig actualValues = getActualValue(id);
        String errMsg = "Mismatched value for plant " + id + ": %s";

        assertTrue(health >= actualValues.health, String.format(errMsg, "health"));
        assertEquals(name, actualValues.name, String.format(errMsg, "name"));
        assertEquals(type, actualValues.type, String.format(errMsg, "type"));
        assertEquals(description, actualValues.description, String.format(errMsg, "description"));
        assertEquals(water, actualValues.idealWaterLevel, String.format(errMsg, "water level"));
        assertEquals(life, actualValues.adultLifeSpan, String.format(errMsg, "life span"));
        assertTrue(maxHealth >= actualValues.maxHealth, String.format(errMsg, "max health"));
    }

    /**
     * Data provider for plant configurations.
     *
     * @return Stream of Arguments containing plant data.
     */
    static Stream<Arguments> plantConfigProvider() {
        return Stream.of(
                Arguments.of("cosmicCob", 3, "Cosmic Cob", "FOOD",
                        "A nutritious snack with everything a human needs to survive, the local " +
                                "fauna won’t touch it though. Suspiciously high in protein and fat…",
                        (float) 1, 3, 200),
                Arguments.of("aloeVera", 2, "Aloe Vera", "HEALTH",
                        "A unique plant that once ground down to a chunky red paste can be used " +
                                "to heal significant wounds, it’s a miracle!", (float) 1.2, 5, 150),
                Arguments.of("hammerPlant", 5, "Hammer Plant", "REPAIR",
                        "A useful plant resembling a hand holding a hammer that repairs the " +
                                "other nearby plants, maybe they were friends!", (float) 0.7, 7, 250),
                Arguments.of("venusFlyTrap", 10, "Space Snapper", "DEFENCE",
                        "A hangry plant that will gobble any nasty pests nearby. Keep small pets " +
                                "and children out of snapping distance!", (float) 0.5, 15, 300),
                Arguments.of("waterWeed", 2, "Atomic Algae", "PRODUCTION",
                        "A highly efficient oxygen-producing plant.", (float) 1.5, 4, 100),
                Arguments.of("nightshade", 2, "Deadly Nightshade",
                        "DEADLY", "Grows deadly poisonous berries.", (float) 1, 2, 130)
        );
    }

    /**
     * Utility method to fetch the actual plant configuration based on the plant name.
     *
     * @param id - The unique identifier of the plant.
     * @return The actual plant configuration.
     */
    BasePlantConfig getActualValue(String id) {
        return switch (id) {
            case "cosmicCob" -> stats.cosmicCob;
            case "aloeVera" -> stats.aloeVera;
            case "hammerPlant" -> stats.hammerPlant;
            case "venusFlyTrap" -> stats.spaceSnapper;
            case "waterWeed" -> stats.atomicAlgae;
            case "nightshade" -> stats.deadlyNightshade;
            default -> throw new IllegalArgumentException("Unknown plant name: " + id);
        };
    }

    /**
     * Test for the creation of a base plant. Ensures that all necessary components
     * are properly attached to the created plant entity.
     */
    @Test
    void shouldCreateBasePlant() {
        assertNotNull(plant, "Plant Entity should not be null");
    }

    /**
     * Test to ensure that the plant has the PlantAreaOfEffectComponent.
     */
    @Test
    void shouldHavePlantAOEComponent() {
        PlantAreaOfEffectComponent aoeComponent = plant.getComponent(PlantAreaOfEffectComponent.class);
        assertNotNull(aoeComponent, "PlantAreaOfEffectComponent should not be null");
    }

    /**
     * Test to ensure that the plant has the PhysicsComponent.
     */
    @Test
    void shouldHavePhysicsComponent() {
        PhysicsComponent physicsComponent = plant.getComponent(PhysicsComponent.class);
        assertNotNull(physicsComponent, "Plant PhysicsComponent should not be null");
        assertEquals(BodyDef.BodyType.StaticBody, physicsComponent.getBody().getType(),
                "Plant physics body type should be StaticBody");
    }

    /**
     * Test to ensure that the plant has the ColliderComponent.
     */
    @Test
    void shouldHaveColliderComponent() {
        ColliderComponent colliderComponent = plant.getComponent(ColliderComponent.class);
        colliderComponent.create();
        assertNotNull(colliderComponent, "Plant ColliderComponent should not be null");
        assertTrue(colliderComponent.getFixture().isSensor(),
                "Plant collider should be a sensor");
    }

    /**
     * Test to ensure that the plant has the HitboxComponent.
     */
    @Test
    void shouldHaveHitboxComponent() {
        HitboxComponent hitboxComponent = plant.getComponent(HitboxComponent.class);
        hitboxComponent.create();
        assertNotNull(hitboxComponent, "Plant HitboxComponent should not be null");
        assertTrue(hitboxComponent.getFixture().isSensor(),
                "Plant Hitbox should be a sensor");
        assertEquals(PhysicsLayer.OBSTACLE, hitboxComponent.getLayer(),
                "Plant Hitbox layer should be OBSTACLE");
    }

    /**
     * Test to ensure that the plant has the PlantMouseHoverComponent.
     */
    @Test
    void shouldHavePlantMouseHoverComponent() {
        PlantMouseHoverComponent mouseHoverComponent = plant.getComponent(PlantMouseHoverComponent.class);
        assertNotNull(mouseHoverComponent, "PlantMouseHoverComponent should not be null");
    }

    /**
     * Test to ensure that the plant has the PlantProximityComponent.
     */
    @Test
    void shouldHavePlantProximityComponent() {
        PlantProximityComponent proximityComponent = plant.getComponent(PlantProximityComponent.class);
        assertNotNull(proximityComponent, "PlantProximityComponent should not be null");
    }

    /**
     * Test to ensure that the plant has the PlantComponent.
     */
    @Test
    void shouldHavePlantComponent() {
        PlantComponent plantComponent = plant.getComponent(PlantComponent.class);
        assertNotNull(plantComponent, "PlantComponent should not be null");
    }

    /**
     * Test to ensure plant has correct health value
     */
    @Test
    void testComponentHealth() {
        PlantComponent plantComponent = plant.getComponent(PlantComponent.class);
        assertEquals(health, plantComponent.getPlantHealth());
    }

    /**
     * Test to ensure plant has correct name
     */
    @Test
    void testComponentName() {
        PlantComponent plantComponent = plant.getComponent(PlantComponent.class);
        assertEquals(name, plantComponent.getPlantName());
    }

    /**
     * Test to ensure plant has correct type
     */
    @Test
    void testComponentType() {
        PlantComponent plantComponent = plant.getComponent(PlantComponent.class);
        assertEquals(type, plantComponent.getPlantType());
    }

    /**
     * Test to ensure plant has correct description
     */
    @Test
    void testComponentDescription() {
        PlantComponent plantComponent = plant.getComponent(PlantComponent.class);
        assertEquals(description, plantComponent.getPlantDescription());
    }

    /**
     * Test to ensure plant has correct idealWaterLevel
     */
    @Test
    void testComponentIdealWaterLevel() {
        PlantComponent plantComponent = plant.getComponent(PlantComponent.class);
        assertEquals(idealWaterLevel, plantComponent.getIdealWaterLevel());
    }

    /**
     * Test to ensure plant has correct adultLifeSpan
     */
    @Test
    void testComponentAdultLifeSpan() {
        PlantComponent plantComponent = plant.getComponent(PlantComponent.class);
        assertEquals(adultLifeSpan, plantComponent.getAdultLifeSpan());
    }

    /**
     * Test to ensure plant has correct maxHealth
     */
    @Test
    void testComponentMaxHealth() {
        PlantComponent plantComponent = plant.getComponent(PlantComponent.class);
        assertEquals(maxHealth, plantComponent.getMaxHealth());
    }

    /**
     * Tests if the position of the plant is correct.
     */
    @Test
    void testPosition() {
        assertEquals(xPosition, plant.getPosition().x, delta, "Mismatched value for plant position x");
        assertEquals(yPosition, plant.getPosition().y, delta, "Mismatched value for plant position y");
    }

    /**
     * Tests if the scale of the plant is correct.
     */
    @Test
    void testScale(){
        assertEquals(yPosition, plant.getScale().y, delta, "Mismatched value for plant scale");
    }

    /**
     * Tests that the setScaledCollider sets the collider of the plant entity correctly.
     */
    @Test
    void testSetScaledCollider() {
        Entity plant = new Entity();
        plant.setScale(2f, 2f);
        ColliderComponent mockCollider = mock(ColliderComponent.class);
        plant.addComponent(mockCollider);

        PhysicsUtils.setScaledCollider(plant, 0.5f, 0.2f);

        Vector2 expectedScale = new Vector2(1f, 0.4f);
        verify(mockCollider, times(1)).setAsBoxAligned(
                eq(expectedScale),
                eq(PhysicsComponent.AlignX.CENTER),
                eq(PhysicsComponent.AlignY.BOTTOM)
        );
    }
}
