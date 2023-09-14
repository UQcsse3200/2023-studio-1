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
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import java.util.Map;

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
    public static Entity createBasePlant(BasePlantConfig config, CropTileComponent cropTile) {
        int[] growthThresholds = {config.sproutThreshold, config.juvenileThreshold, config.adultThreshold};
        String[] imagePaths =   {   config.imageFolderPath + "1_seedling.png",
                                    config.imageFolderPath + "2_sprout.png",
                                    config.imageFolderPath + "3_juvenile.png",
                                    config.imageFolderPath + "4_adult.png",
                                    config.imageFolderPath + "5_decaying.png"
                                };

        String[] soundsArray =   {
                config.soundFolderPath + "click.wav", config.soundFolderPath + "clickLore.wav",
                config.soundFolderPath + "decay.wav", config.soundFolderPath + "decayLore.wav",
                config.soundFolderPath + "destroy.wav", config.soundFolderPath + "destroyLore.wav",
                config.soundFolderPath + "nearby.wav", config.soundFolderPath + "nearbyLore.wav",
        };

        Entity plant = new Entity(EntityType.Plant)
                .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
                .addComponent(new ColliderComponent().setSensor(true))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new DynamicTextureRenderComponent("images/plants/cosmic_cob/4_adult.png"))
                .addComponent(new PlantComponent(config.health, config.name, config.type,
                        config.description, config.idealWaterLevel, config.adultLifeSpan,
                        config.maxHealth, cropTile, growthThresholds, soundsArray,
                        imagePaths));

        // Set plant position over crop tile.
        var cropTilePosition = cropTile.getEntity().getPosition();
        plant.setPosition(cropTilePosition.x, cropTilePosition.y + 0.5f);
        plant.getComponent(DynamicTextureRenderComponent.class).scaleEntity();
        plant.getComponent(DynamicTextureRenderComponent.class).setLayer(2);

        plant.scaleHeight(2f);
        PhysicsUtils.setScaledCollider(plant, 0.5f, 0.2f);

        return plant;
    }

    /**
     * Creates a cosmicCob entity that is a Food type plant.
     *
     * @param cropTile Crop tile upon which the plant is planted
     * @return entity
     */
    public static Entity createCosmicCob(CropTileComponent cropTile) {
        return createBasePlant(stats.cosmicCob, cropTile);
    }

    /**
     * Creates an AloeVera entity that is a health type plant.
     *
     * @param cropTile Crop tile upon which the plant is planted
     * @return entity
     */
    public static Entity createAloeVera(CropTileComponent cropTile) {
        return createBasePlant(stats.aloeVera, cropTile);
    }

    /**
     * Creates a HammerPlant entity that is a repair type plant.
     *
     * @param cropTile Crop tile upon which the plant is planted
     * @return entity
     */
    public static Entity createHammerPlant(CropTileComponent cropTile) {
        return createBasePlant(stats.hammerPlant, cropTile);

    }

    /**
     * Creates an venusFlyTrap entity that is a defence type plant.
     *
     * @param cropTile Crop tile upon which the plant is planted
     * @return entity
     */
    public static Entity createVenusFlyTrap(CropTileComponent cropTile) {
        return createBasePlant(stats.venusFlyTrap, cropTile);
    }

    /**
     * Creates a waterWeed entity that is a production type plant.
     *
     * @param cropTile Crop tile upon which the plant is planted
     * @return entity
     */
    public static Entity createWaterWeed(CropTileComponent cropTile) {
        return createBasePlant(stats.waterWeed, cropTile);
    }

    /**
     * Creates a Nightshade entity that is a deadly type plant.
     *
     * @param cropTile Crop tile upon which the plant is planted
     * @return entity
     */
    public static Entity createNightshade(CropTileComponent cropTile) {
        return createBasePlant(stats.nightshade, cropTile);

    }


}
