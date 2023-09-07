package com.csse3200.game.entities.factories;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.plants.PlantComponent;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.configs.plants.*;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * Factory to create plant entities.
 *
 * <p>Predefined plant properties are loaded from a config file stored as a json file and should
 * have the properties stored in the PlantConfig classes
 */
public class PlantFactory {
    private static PlantConfigs stats =
            FileLoader.readClass(PlantConfigs.class, "configs/plant.json");

    /**
     * Sets the plant statistics.
     *
     * @param newStats The new statistics to be set for the plant.
     */
    public static void setStats(PlantConfigs newStats) {
        stats = newStats;
    }

    /**
     * Creates a generic plant to be used as a base for more specific plant creation methods.
     *
     * @return entity
     */
    public static Entity createBasePlant() {
        Entity plant = new Entity(EntityType.Plant)
                .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
                .addComponent(new ColliderComponent().setSensor(true))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE));

        return plant;
    }

    /**
     * Creates a cosmicCob entity that is a Food type plant.
     *
     * @param cropTile Crop tile upon which the plant is planted
     * @return entity
     */
    public static Entity createCosmicCob(CropTileComponent cropTile) {
        BasePlantConfig config = stats.cosmicCob;

        int[] growthThresholds = {config.sproutThreshold, config.juvenileThreshold, config.adultThreshold};
        String[] imagePaths = {config.seedlingAsset, config.sproutingAsset, config.juvenileAsset, config.adultAsset,
                config.decayingAsset};

        Entity plant = createBasePlant()
                .addComponent(new DynamicTextureRenderComponent("images/plants/Corn.png"))
                .addComponent(new PlantComponent(config.health, config.name, config.type,
                        config.description, config.idealWaterLevel, config.adultLifeSpan,
                        config.maxHealth, cropTile, growthThresholds, config.soundsArray, imagePaths));

        // Set plant position over crop tile.
        var cropTilePosition = cropTile.getEntity().getPosition();
        plant.setPosition(cropTilePosition.x, cropTilePosition.y + 0.5f);

        plant.getComponent(DynamicTextureRenderComponent.class).scaleEntity();
        plant.scaleHeight(1f);
        PhysicsUtils.setScaledCollider(plant, 0.5f, 0.2f);
        return plant;
    }

    /**
     * Creates an AloeVera entity that is a health type plant.
     *
     * @param cropTile Crop tile upon which the plant is planted
     * @return entity
     */
    public static Entity createAloeVera(CropTileComponent cropTile) {
        BasePlantConfig config = stats.aloeVera;

        int[] growthThresholds = {config.sproutThreshold, config.juvenileThreshold, config.adultThreshold};
        String[] imagePaths = {config.seedlingAsset, config.sproutingAsset, config.juvenileAsset, config.adultAsset,
                config.decayingAsset};

        Entity plant = createBasePlant()
                .addComponent(new DynamicTextureRenderComponent("images/plants/Aloe.png"))
                .addComponent(new PlantComponent(config.health, config.name, config.type,
                        config.description, config.idealWaterLevel, config.adultLifeSpan,
                        config.maxHealth, cropTile, growthThresholds, config.soundsArray, imagePaths));

        // Set plant position over crop tile.
        var cropTilePosition = cropTile.getEntity().getPosition();
        plant.setPosition(cropTilePosition.x, cropTilePosition.y + 0.5f);

        plant.getComponent(DynamicTextureRenderComponent.class).scaleEntity();
        plant.scaleHeight(1f);
        PhysicsUtils.setScaledCollider(plant, 0.5f, 0.2f);
        return plant;
    }

    /**
     * Creates a HammerPlant entity that is a repair type plant.
     *
     * @param cropTile Crop tile upon which the plant is planted
     * @return entity
     */
    public static Entity createHammerPlant(CropTileComponent cropTile) {
        BasePlantConfig config = stats.hammerPlant;

        int[] growthThresholds = {config.sproutThreshold, config.juvenileThreshold, config.adultThreshold};
        String[] imagePaths = {config.seedlingAsset, config.sproutingAsset, config.juvenileAsset, config.adultAsset,
                config.decayingAsset};

        Entity plant = createBasePlant()
                .addComponent(new DynamicTextureRenderComponent("images/plants/Hammer.png"))
                .addComponent(new PlantComponent(config.health, config.name, config.type,
                    config.description, config.idealWaterLevel, config.adultLifeSpan,
                    config.maxHealth, cropTile, growthThresholds, config.soundsArray, imagePaths));

        // Set plant position over crop tile.
        var cropTilePosition = cropTile.getEntity().getPosition();
        plant.setPosition(cropTilePosition.x, cropTilePosition.y + 0.5f);

        plant.getComponent(DynamicTextureRenderComponent.class).scaleEntity();
        plant.scaleHeight(1f);
        PhysicsUtils.setScaledCollider(plant, 0.5f, 0.2f);
        return plant;
    }

    /**
     * Creates a Nightshade entity that is a deadly type plant.
     *
     * @param cropTile Crop tile upon which the plant is planted
     * @return entity
     */

    public static Entity createNightshade(CropTileComponent cropTile) {
        BasePlantConfig config = stats.nightshade;

        int[] growthThresholds = {config.sproutThreshold, config.juvenileThreshold, config.adultThreshold};
        String[] imagePaths = {config.seedlingAsset, config.sproutingAsset, config.juvenileAsset, config.adultAsset,
                config.decayingAsset};

        Entity plant = createBasePlant()
                .addComponent(new DynamicTextureRenderComponent("images/plants/nightshade.png"))
                .addComponent(new PlantComponent(config.health, config.name, config.type,
                        config.description, config.idealWaterLevel, config.adultLifeSpan,
                        config.maxHealth, cropTile, growthThresholds, config.soundsArray, imagePaths));

        // Set plant position over crop tile.
        var cropTilePosition = cropTile.getEntity().getPosition();
        plant.setPosition(cropTilePosition.x, cropTilePosition.y + 0.5f);

        plant.getComponent(DynamicTextureRenderComponent.class).scaleEntity();
        plant.scaleHeight(1f);
        PhysicsUtils.setScaledCollider(plant, 0.5f, 0.2f);
        return plant;
    }

    /**
     * Creates a Tobacco entity that is a deadly type plant.
     *
     * @param cropTile Crop tile upon which the plant is planted
     * @return entity
     */
    public static Entity createTobacco(CropTileComponent cropTile) {
        BasePlantConfig config = stats.tobacco;

        int[] growthThresholds = {config.sproutThreshold, config.juvenileThreshold, config.adultThreshold};
        String[] imagePaths = {config.seedlingAsset, config.sproutingAsset, config.juvenileAsset, config.adultAsset,
                config.decayingAsset};

        Entity plant = createBasePlant()
                .addComponent(new DynamicTextureRenderComponent("images/plants/waterweed.png"))
                .addComponent(new PlantComponent(config.health, config.name, config.type,
                        config.description, config.idealWaterLevel, config.adultLifeSpan,
                        config.maxHealth, cropTile, growthThresholds, config.soundsArray, imagePaths));

        // Set plant position over crop tile.
        var cropTilePosition = cropTile.getEntity().getPosition();
        plant.setPosition(cropTilePosition.x, cropTilePosition.y + 0.5f);

        plant.getComponent(DynamicTextureRenderComponent.class).scaleEntity();
        plant.scaleHeight(1f);
        PhysicsUtils.setScaledCollider(plant, 0.5f, 0.2f);
        return plant;
    }


    /**
     * Creates an venusFlyTrap entity that is a defence type plant.
     *
     * @param cropTile Crop tile upon which the plant is planted
     * @return entity
     */
    public static Entity createVenusFlyTrap(CropTileComponent cropTile) {
        BasePlantConfig config = stats.venusFlyTrap;

        int[] growthThresholds = {config.sproutThreshold, config.juvenileThreshold, config.adultThreshold};
        String[] imagePaths = {config.seedlingAsset, config.sproutingAsset, config.juvenileAsset, config.adultAsset,
                config.decayingAsset};

        Entity plant = createBasePlant()
                .addComponent(new DynamicTextureRenderComponent("images/plants/VenusTrap.png"))
                .addComponent(new PlantComponent(config.health, config.name, config.type,
                        config.description, config.idealWaterLevel, config.adultLifeSpan,
                        config.maxHealth, cropTile, growthThresholds, config.soundsArray, imagePaths));

        // Set plant position over crop tile.
        var cropTilePosition = cropTile.getEntity().getPosition();
        plant.setPosition(cropTilePosition.x, cropTilePosition.y + 0.5f);

        plant.getComponent(DynamicTextureRenderComponent.class).scaleEntity();
        plant.scaleHeight(1f);
        PhysicsUtils.setScaledCollider(plant, 0.5f, 0.2f);
        return plant;
    }

    public static Entity createAtomicAlgae(CropTileComponent cropTile) {
        return null;
    }

    public static Entity createHorticulturalHeater(CropTileComponent cropTile) {
        return null;
    }

    public static Entity createDeadlyNightshade(CropTileComponent cropTile) {
        return null;
    }
}
