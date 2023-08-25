package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.math.MathUtils;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.plants.PlantComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import java.util.function.Supplier;
import javax.swing.MenuElement;

/**
 * Component which stores information about plots of land on which crops and plants can grow.
 * Listens to a variety of trigger-able events to update state of the tile, including its water
 * content and whether it is fertilised.
 */
public class CropTileComponent extends Component {

    private static final float waterDecreaseRate = 0.05f;
    private static final float idealWaterFalloffSharpness = 2.0f;
    private static final float idealWaterFalloffTolerance = 1.2f;
    private static final float waterDamageThreshold = 0.1f;

    private float waterContent;
    private final float soilQuality;
    private boolean isFertilised;
    private Entity plant;


    /**
     * Creates a new crop tile component, with a specified initial water content, and an initial soil quality (higher values indicate higher soil
     * quality, meaning better growth rate).
     *
     * @param initialWaterContent The initial amount of water in the crop tile. Value should be
     *                            between 0 and 2 inclusive - 0 indicates tile is completely dry,
     *                            and 2 indicates tile is flooded, water content of 1 leads to ideal
     *                            growth rate for most plants
     * @param soilQuality The initial soil quality of the crop tile. Higher soil quality increases
     *                    the calculated growth rate. Should be greater than 0.
     */
    public CropTileComponent(float initialWaterContent, float soilQuality) {
        this.waterContent = initialWaterContent;
        this.soilQuality = soilQuality;
        this.isFertilised = false;
        this.plant = null;
    }

    /**
     * Adds player-triggered events listeners to the entity.
     */
    @Override
    public void create() {
        entity.getEvents().addListener("water", this::waterTile);
        entity.getEvents().addListener("fertilise", this::fertiliseTile);
        entity.getEvents().addListener("plant", this::plantCrop);
        entity.getEvents().addListener("harvest", this::harvestCrop);
        entity.getEvents().addListener("destroy", this::destroyTile);
    }

    /**
     * Decreases water content in the tile by a constant amount.
     */
    @Override
    public void update() {
        waterContent -= waterDecreaseRate * ServiceLocator.getTimeSource().getDeltaTime();
        if (waterContent < 0) {
            waterContent = 0;
        } else if (waterContent > 2) {
            waterContent = 2;
        }
    }

    private void waterTile(float amount) {
        waterContent += amount;
    }

    private void fertiliseTile() {
        isFertilised = true;
    }

    private void destroyTile() {
        if (isOccupied()) {
            plant.dispose();
        }
        this.dispose();
    }

    private void plantCrop(Supplier<Entity> plantFactoryMethod) {
        if (isOccupied()) {
            return;
        }
        plant = plantFactoryMethod.get();
        // Sets position of plant to be the center of the tile
        plant.setPosition(entity.getCenterPosition());
    }

    private void harvestCrop() {
        if (!isOccupied()) {
            return;
        }
        isFertilised = false;
        plant.getEvents().trigger("harvest");
        PlantComponent component = plant.getComponent(PlantComponent.class);
        // Check if plant was destroyed after harvest
        if (component == null) {
            plant = null;
        }
    }

    /**
     * Gets the growth rate of the tile, assuming a water content value of 1 yields the maximum
     * growth rate. The calculated growth rate is dependent on the water content and soil quality of
     * this tile, and whether it is fertilised.
     *
     * @return The calculated growth rate of the tile with an ideal water content value of 1, which
     * is dependent on water content, soil quality, and whether the tile has been fertilised.
     */
    public double getGrowthRate() {
        return getGrowthRate(1.0f);
    }

    /**
     * Gets the growth rate of the tile, specifying the water content of the tile which yields the
     * maximum growth rate. The calculated growth rate is dependent on the water content and soil
     * quality of this tile, and whether it is fertilised.
     * 
     * @param idealWaterAmount The amount which yields the maximum growth rate.
     * @return The calculated growth rate of the tile, dependent on water content, soil quality, and
     * whether the tile has been fertilised.
     */
    public double getGrowthRate(float idealWaterAmount) {
        double waterMultiplier = 1 / (Math.exp(Math.pow(Math.abs(waterContent - idealWaterAmount), idealWaterFalloffSharpness))) - 1 / MathUtils.E;
        waterMultiplier *= 1 / (1 - 1 / MathUtils.E);
        waterMultiplier = (waterMultiplier - waterDamageThreshold) / (1 - waterDamageThreshold);
        waterMultiplier = waterMultiplier > 0 ? Math.pow(waterMultiplier, idealWaterFalloffTolerance) : -1.0;
        return waterMultiplier > 0 ? soilQuality * (isFertilised ? 2 : 1) * waterMultiplier : -1.0;
    }

    private boolean isOccupied() {
        return plant != null;
    }

}
