package com.csse3200.game.entities.factories;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.plants.PlantComponent;
import com.csse3200.game.entities.configs.plants.*;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * Factory to create plant entities.
 *
 * <p>Predefined plant properties are loaded from a config file stored as a json file and should
 * have the properties stored in the PlantConfig classes
 */
public class PlantFactory {
    private static final PlantConfigs stats =
            FileLoader.readClass(PlantConfigs.class, "configs/plant.json");

    /**
     * Creates a generic plant to be used as a base for more specific plant creation methods.
     *
     * @return entity
     */
    private static Entity createBasePlant() {

        Entity plant = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE));

        plant.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);

        return  plant;
    }

    /**
     * Creates a cosmicCob entity that is a Food type plant.
     *
     * @return entity
     */
    public static Entity createCosmicCob() {
        BasePlantConfig config = stats.cosmicCob;

        Entity plant = createBasePlant()
                .addComponent(new TextureRenderComponent("images/corn_temp.png"))
                .addComponent(new PlantComponent(config.health, config.name, config.type,
                        config.description));

        plant.getComponent(TextureRenderComponent.class).scaleEntity();
        plant.scaleHeight(1f);
        PhysicsUtils.setScaledCollider(plant, 0.5f, 0.2f);
        return plant;
    }

    /**
     * Creates an AloeVera entity that is a health type plant.
     *
     * @return entity
     */
    public static Entity createAloeVera() {
        BasePlantConfig config = stats.aloeVera;

        Entity plant = createBasePlant()
                .addComponent(new TextureRenderComponent("images/aloe_temp.png"))
                .addComponent(new PlantComponent(config.health, config.name, config.type,
                        config.description));

        plant.getComponent(TextureRenderComponent.class).scaleEntity();
        plant.scaleHeight(1f);
        PhysicsUtils.setScaledCollider(plant, 0.5f, 0.2f);
        return plant;
    }

    /**
     * Creates a HammerPlant entity that is a repair type plant.
     *
     * @return entity
     */
    public static Entity createHammerPlant() {
        BasePlantConfig config = stats.hammerPlant;

        Entity plant = createBasePlant()
                .addComponent(new TextureRenderComponent("images/test_cactus.png"))
                .addComponent(new PlantComponent(config.health, config.name, config.type,
                        config.description));

        plant.getComponent(TextureRenderComponent.class).scaleEntity();
        plant.scaleHeight(1f);
        PhysicsUtils.setScaledCollider(plant, 0.5f, 0.2f);
        return plant;
    }

    /**
     * Creates a AtropaBelladonna entity that is a deadly type plant.
     *
     * @return entity
     */

    public static Entity createAtropaBelladonna() {
        BasePlantConfig config = stats.atropaBelladonna;

        Entity plant = createBasePlant()
                .addComponent(new TextureRenderComponent("images/belladonna.png"))
                .addComponent(new PlantComponent(config.health, config.name, config.type,
                        config.description));

        plant.getComponent(TextureRenderComponent.class).scaleEntity();
        plant.scaleHeight(1f);
        PhysicsUtils.setScaledCollider(plant, 0.5f, 0.2f);
        return plant;
    }

    /**
     * Creates a Nicotiana Tabacum entity that is a deadly type plant.
     *
     * @return entity
     */
    public static Entity createNicotianaTabacum() {
        BasePlantConfig config = stats.nicotianaTabacum;

        Entity plant = createBasePlant()
                .addComponent(new TextureRenderComponent("images/tobacco.png"))
                .addComponent(new PlantComponent(config.health, config.name, config.type,
                        config.description));

        plant.getComponent(TextureRenderComponent.class).scaleEntity();
        plant.scaleHeight(1f);
        PhysicsUtils.setScaledCollider(plant, 0.5f, 0.2f);
        return plant;
    }


    /**
     * Creates an venusFlyTrap entity that is a defence type plant.
     *
     * @return entity
     */
    public static Entity createVenusFlyTrap() {
        BasePlantConfig config = stats.venusFlyTrap;

        Entity plant = createBasePlant()
                .addComponent(new TextureRenderComponent("images/test_cactus.png"))
                .addComponent(new PlantComponent(config.health, config.name, config.type,
                        config.description));

        plant.getComponent(TextureRenderComponent.class).scaleEntity();
        plant.scaleHeight(1f);
        PhysicsUtils.setScaledCollider(plant, 0.5f, 0.2f);
        return plant;
    }
}
