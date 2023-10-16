package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.components.plants.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.configs.plants.BasePlantConfig;
import com.csse3200.game.entities.configs.plants.PlantConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.sound.EffectSoundFile;
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
        Entity aoeAnimator = createPlantAoeAnimatorEntity();
        AnimationRenderComponent animator = setupPlantAnimations(config.atlasPath);

        int[] growthThresholds = {config.sproutThreshold, config.juvenileThreshold, config.adultThreshold};

        Entity plant = new Entity(EntityType.PLANT)
                .addComponent(animator)
                .addComponent(new PlantAreaOfEffectComponent(2f, "None"))
                .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
                .addComponent(new ColliderComponent().setSensor(true))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new PlantMouseHoverComponent())
                .addComponent(new PlantProximityComponent())
                .addComponent(new PlantComponent(config.health, config.name, config.type,
                        config.description, config.idealWaterLevel, config.adultLifeSpan,
                        config.maxHealth, cropTile, growthThresholds));

        aoeAnimator.setCenterPosition(plant.getComponent(PlantComponent.class).getCropTile().getEntity().getCenterPosition().add(-1.5f, -1.5f));
        plant.getComponent(PlantComponent.class).addAoeAnimatorEntity(aoeAnimator);

        // Set plant position over crop tile.
        var cropTilePosition = cropTile.getEntity().getPosition();
        plant.setPosition(cropTilePosition.x, cropTilePosition.y + 0.4f);
        plant.getComponent(PlantComponent.class).getCropTile().getEntity().getScale();
        plant.getComponent(AnimationRenderComponent.class).scaleEntity();
        plant.scaleHeight(2f);
        PhysicsUtils.setScaledCollider(plant, 0.5f, 0.2f);

        return plant;
    }

    /**
     * Registers player animations to the AnimationRenderComponent.
     * @param atlasPath - The path of the relevant animation atlas.
     * @return animation component
     */
    private static AnimationRenderComponent setupPlantAnimations(String atlasPath) {
        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset(atlasPath, TextureAtlas.class),
                16f);

        animator.addAnimation("default", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("1_seedling", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("2_sprout", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("3_juvenile", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("4_adult", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("5_decaying", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("6_dead", 0.1f, Animation.PlayMode.LOOP);

        animator.addAnimation("1_seedling_dead", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("2_sprout_dead", 0.1f, Animation.PlayMode.LOOP);


        animator.startAnimation("1_seedling");

        return animator;
    }

    private static  Entity createPlantAoeAnimatorEntity() {
        Entity aoeAnimator = new Entity(EntityType.DUMMY);

        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/plants/plant_aoe.atlas", TextureAtlas.class),
                16f, 3);
        animator.addAnimation("default", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("deadly_nightshade_aoe_", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("hammer_plant_aoe_", 0.1f, Animation.PlayMode.LOOP);

        animator.startAnimation("default");

        aoeAnimator.addComponent(animator);

        return aoeAnimator;
    }

    /**
     * Creates a cosmicCob entity that is a Food type plant.
     *
     * @param cropTile Crop tile upon which the plant is planted
     * @return entity
     */
    public static Entity createCosmicCob(CropTileComponent cropTile) {
        Entity cosmicCob = createBasePlant(stats.cosmicCob, cropTile);
        PlantComponent plantComponent = cosmicCob.getComponent(PlantComponent.class);
        plantComponent.setHarvestYields(Map.of(
                "Cosmic Cob Seeds", 2,
                "Ear of Cosmic Cob", 1
        ));
        plantComponent.setSounds(EffectSoundFile.PLANT_CLICK, EffectSoundFile.COSMIC_COB_CLICK_LORE,
                EffectSoundFile.PLANT_DECAY, EffectSoundFile.COSMIC_COB_DECAY_LORE,
                EffectSoundFile.PLANT_DESTROY, EffectSoundFile.COSMIC_COB_DESTROY_LORE,
                EffectSoundFile.PLANT_NEARBY, EffectSoundFile.COSMIC_COB_NEARBY_LORE);
        return cosmicCob;
    }

    /**
     * Creates an AloeVera entity that is a health type plant.
     *
     * @param cropTile Crop tile upon which the plant is planted
     * @return entity
     */
    public static Entity createAloeVera(CropTileComponent cropTile) {
        Entity aloeVera = createBasePlant(stats.aloeVera, cropTile);
        PlantComponent plantComponent = aloeVera.getComponent(PlantComponent.class);
        plantComponent.setHarvestYields(Map.of(
                "Aloe Vera Seeds", 2,
                "Aloe Vera Leaf", 1
        ));
        plantComponent.setSounds(EffectSoundFile.PLANT_CLICK, EffectSoundFile.ALOE_VERA_CLICK_LORE,
                EffectSoundFile.PLANT_DECAY, EffectSoundFile.ALOE_VERA_DECAY_LORE,
                EffectSoundFile.PLANT_DESTROY, EffectSoundFile.ALOE_VERA_DESTROY_LORE,
                EffectSoundFile.PLANT_NEARBY, EffectSoundFile.ALOE_VERA_NEARBY_LORE);
        return aloeVera;
    }

    /**
     * Creates a HammerPlant entity that is a repair type plant.
     *
     * @param cropTile Crop tile upon which the plant is planted
     * @return entity
     */
    public static Entity createHammerPlant(CropTileComponent cropTile) {
        Entity hammerPlant = createBasePlant(stats.hammerPlant, cropTile);
        PlantComponent plantComponent = hammerPlant.getComponent(PlantComponent.class);
        plantComponent.setHarvestYields(Map.of(
                "Hammer Plant Seeds", 2,
                "Hammer Flower", 1
        ));
        plantComponent.setSounds(EffectSoundFile.PLANT_CLICK, EffectSoundFile.HAMMER_PLANT_CLICK_LORE,
                EffectSoundFile.PLANT_DECAY, EffectSoundFile.HAMMER_PLANT_DECAY_LORE,
                EffectSoundFile.PLANT_DESTROY, EffectSoundFile.HAMMER_PLANT_DESTROY_LORE,
                EffectSoundFile.PLANT_NEARBY, EffectSoundFile.HAMMER_PLANT_NEARBY_LORE);
        return hammerPlant;
    }

    /**
     * Creates an venusFlyTrap entity that is a defence type plant.
     *
     * @param cropTile Crop tile upon which the plant is planted
     * @return entity
     */
    public static Entity createSpaceSnapper(CropTileComponent cropTile) {
        Entity spaceSnapper = createBasePlant(stats.spaceSnapper, cropTile);
        spaceSnapper.getComponent(AnimationRenderComponent.class)
                .addAnimation("digesting", 0.1f, Animation.PlayMode.LOOP);
        PlantComponent plantComponent = spaceSnapper.getComponent(PlantComponent.class);
        plantComponent.setHarvestYields(Map.of(
                "Space Snapper Seeds", 2
        ));
        plantComponent.setSounds(EffectSoundFile.PLANT_CLICK, EffectSoundFile.SPACE_SNAPPER_CLICK_LORE,
                EffectSoundFile.PLANT_DECAY, EffectSoundFile.SPACE_SNAPPER_DECAY_LORE,
                EffectSoundFile.PLANT_DESTROY, EffectSoundFile.SPACE_SNAPPER_DESTROY_LORE,
                EffectSoundFile.PLANT_NEARBY, EffectSoundFile.SPACE_SNAPPER_NEARBY_LORE);
        return spaceSnapper;
    }

    /**
     * Creates a waterWeed entity that is a production type plant.
     *
     * @param cropTile Crop tile upon which the plant is planted
     * @return entity
     */
    public static Entity createAtomicAlgae(CropTileComponent cropTile) {
        Entity atomicAlgae = createBasePlant(stats.atomicAlgae, cropTile);
        PlantComponent plantComponent = atomicAlgae.getComponent(PlantComponent.class);
        plantComponent.setHarvestYields(Map.of(
                "Atomic Algae Seeds", 2
        ));
        plantComponent.setSounds(EffectSoundFile.PLANT_CLICK, EffectSoundFile.ATOMIC_ALGAE_CLICK_LORE,
                EffectSoundFile.PLANT_DECAY, EffectSoundFile.ATOMIC_ALGAE_DECAY_LORE,
                EffectSoundFile.PLANT_DESTROY, EffectSoundFile.ATOMIC_ALGAE_DESTROY_LORE,
                EffectSoundFile.PLANT_NEARBY, EffectSoundFile.ATOMIC_ALGAE_NEARBY_LORE);
        return atomicAlgae;
    }

    /**
     * Creates a Nightshade entity that is a deadly type plant.
     *
     * @param cropTile Crop tile upon which the plant is planted
     * @return entity
     */
    public static Entity createDeadlyNightshade(CropTileComponent cropTile) {
        Entity deadlyNightshade = createBasePlant(stats.deadlyNightshade, cropTile);
        PlantComponent plantComponent = deadlyNightshade.getComponent(PlantComponent.class);
        plantComponent.setHarvestYields(Map.of(
                "Deadly Nightshade Seeds", 2,
                "Nightshade Berry", 3
        ));
        plantComponent.setSounds(EffectSoundFile.PLANT_CLICK, EffectSoundFile.DEADLY_NIGHTSHADE_CLICK_LORE,
                EffectSoundFile.PLANT_DECAY, EffectSoundFile.DEADLY_NIGHTSHADE_DECAY_LORE,
                EffectSoundFile.PLANT_DESTROY, EffectSoundFile.DEADLY_NIGHTSHADE_DESTROY_LORE,
                EffectSoundFile.PLANT_NEARBY, EffectSoundFile.DEADLY_NIGHTSHADE_NEARBY_LORE);
        return deadlyNightshade;
    }


    /**
     * Create a plant for testing
     * @param cropTile Crop tile upon which the plant is planted
     * @return new plant entity
     */
    public static Entity createTest(CropTileComponent cropTile) {
        return new Entity(EntityType.PLANT);
    }
}