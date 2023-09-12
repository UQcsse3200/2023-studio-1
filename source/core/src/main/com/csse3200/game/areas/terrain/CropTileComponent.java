package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.math.MathUtils;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.plants.PlantComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.function.Function;

/**
 * Component which stores information about plots of land on which crops and plants can grow.
 * Listens to a variety of trigger-able events to update state of the tile, including its water
 * content and whether it is fertilised.
 */
public class CropTileComponent extends Component {

	/**
	 * Default rate that a tile's water level decreases
	 */
	private static final float WATER_DECREASE_RATE = 0.05f;

	/**
	 * Ideal water fall off sharpness to calculate growth rate
	 */
	private static final float IDEAL_WATER_FALL_OFF_SHARPNESS = 2.0f;

	/**
	 * How tolerant the growth rate is to the water being different from the ideal value
	 */
	private static final float IDEAL_WATER_FALL_OFF_TOLERANCE = 1.2f;

	/**
	 * Water damage threshold used to calculate growth rate
	 */
	private static final float WATER_DAMAGE_THRESHOLD = 0.1f;

	private float waterContent;
	private final float soilQuality;
	private boolean isFertilised;
	private Entity plant;
	private DynamicTextureRenderComponent currentTexture;


	/**
	 * Creates a new crop tile component, with a specified initial water content, and an initial soil
	 * quality (higher values indicate higher soil quality, meaning better growth rate).
	 *
	 * @param initialWaterContent The initial amount of water in the crop tile. Value should be
	 *                            between 0 and 2 inclusive - 0 indicates tile is completely dry, and
	 *                            2 indicates tile is flooded, water content of 1 leads to ideal
	 *                            growth rate for most plants
	 * @param soilQuality         The initial soil quality of the crop tile. Higher soil quality
	 *                            increases the calculated growth rate. Should be greater than 0.
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
		entity.getEvents().addListener("destroy", this::destroyTile);
		currentTexture = entity.getComponent(DynamicTextureRenderComponent.class);
	}

	/**
	 * Decreases water content in the tile by a constant amount.
	 */
	@Override
	public void update() {
		waterContent -= WATER_DECREASE_RATE * ServiceLocator.getTimeSource().getDeltaTime();
		if (waterContent < 0) {
			waterContent = 0;
		} else if (waterContent > 2) {
			waterContent = 2;
		}

		// Update the texture of the corresponding entity
		if (currentTexture != null) {
			String texturePath = this.getTexturePath();
			currentTexture.setTexture(texturePath);
		}
	}

	/**
	 * Modifies the water content of the tile
	 *
	 * @param amount Value to add/subtract from water level
	 */
	private void waterTile(float amount) {
		waterContent += amount;
	}

	/**
	 * Sets the tile to a fertilised state
	 */
	private void fertiliseTile() {
		isFertilised = true;
		ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.FERTILISE_CROP.name());
	}

	/**
	 * Destroys both the tile and any plant that is on it
	 */
	private void destroyTile() {
		if (isOccupied()) {
			plant.getEvents().trigger("destroyPlant");
			this.setUnoccupied();
		} else {
			entity.dispose();
		}
	}

	/**
	 * Plants a plant entity on the tile and stores the plant as a member variable in the tile
	 * component. It is assumed that the {@link Entity} created from the factory contains a {@link PlantComponent}.
	 *
	 * @param plantFactoryMethod Factory method that is used to create a new plant
	 */
	private void plantCrop(Function<CropTileComponent, Entity> plantFactoryMethod) {
		if (isOccupied()) {
			return;
		}
		plant = plantFactoryMethod.apply(this);
		ServiceLocator.getEntityService().register(plant);

		PlantComponent plantComponent = plant.getComponent(PlantComponent.class);
		if (plantComponent != null) {
			ServiceLocator.getMissionManager().getEvents().trigger(
					MissionManager.MissionEvent.PLANT_CROP.name(),
					plantComponent.getPlantType()
			);
		}
	}

	/**
	 * Gets the growth rate of the tile, assuming a water content value of 1 yields the maximum growth
	 * rate. The calculated growth rate is dependent on the water content and soil quality of this
	 * tile, and whether it is fertilised.
	 *
	 * @return The calculated growth rate of the tile with an ideal water content value of 1, which is
	 * dependent on water content, soil quality, and whether the tile has been fertilised.
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
		double waterMultiplier = 1 / (Math.exp(
				Math.pow(Math.abs(waterContent - idealWaterAmount), IDEAL_WATER_FALL_OFF_SHARPNESS)))
				- 1 / MathUtils.E;
		waterMultiplier *= 1 / (1 - 1 / MathUtils.E);
		waterMultiplier = (waterMultiplier - WATER_DAMAGE_THRESHOLD) / (1 - WATER_DAMAGE_THRESHOLD);
		waterMultiplier =
				waterMultiplier > 0 ? Math.pow(waterMultiplier, IDEAL_WATER_FALL_OFF_TOLERANCE) : -1.0;
		int fertiliserMultiplier = isFertilised ? 2: 1;
		return waterMultiplier > 0 ? soilQuality * fertiliserMultiplier * waterMultiplier : -1.0;
	}

	/**
	 * Helper function to determine whether the tile is occupied by a plant
	 *
	 * @return whether the tile is occupied by the plant
	 */
	private boolean isOccupied() {
		return plant != null;
	}

	/**
	 * Sets the tile to be unoccupied.
	 */
	public void setUnoccupied() {
		isFertilised = false;
		plant = null;
	}

	/**
	 * Determine the appropriate String for the tile's texture based on fertiliser status and
	 * water content.
	 * @return the path to the texture for this CropTileComponent based on its status.
	 */
	private String getTexturePath() {
		String path = "images/cropTile_fertilised.png";
		if (isFertilised) {
			if (waterContent < 0.5) {
				path = "images/cropTile_fertilised.png";
			} else if (waterContent < 1.5) {
				path = "images/watered_cropTile_fertilised.png";
			} else {
				path = "images/overwatered_cropTile_fertilised.png";
			}
		} else {
			if (waterContent < 0.5) {
				path = "images/cropTile.png";
			} else if (waterContent < 1.5) {
				path = "images/watered_cropTile.png";
			} else {
				path = "images/overwatered_cropTile.png";
			}
		}
		return path;
	}
}
