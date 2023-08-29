package com.csse3200.game.components.plants;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.components.Component;

/**
 * Class for all plants in the game.
 */
public class PlantComponent extends Component {
    private int plantHealth;            // Initial plant health
    private int maxHealth;              // Maximum health this plant can reach.
    private String plantName;           // User facing plant name
    private String plantType;           // Type of plant (food, health, repair, defence, production, deadly)
    private String plantDescription;    // User facing description of the plant
    private boolean decay = false;
    private float idealWaterLevel;              // Ideal water level. A factor when determining the growth rate.
    private float currentAge;               // Current age of the plant in in-game days
    private int growthStage;                    // Growth stage of a plant.
    private int adultLifeSpan;                  // How long a crop plant lives before starting to decay from old age.

    /** The crop tile on which this plant is planted on. */
    private CropTileComponent cropTile;

    /**
     * Constructor used for plant types that have no extra properties.
     *
     * @param health             health of the plant
     * @param name               name of the plant
     * @param plantType          type of the plant
     * @param plantDescription   description of the plant
     */
    public PlantComponent(int health, String name, String plantType, String plantDescription,
                          float idealWaterLevel, int adultLifeSpan, int maxHealth,
                          CropTileComponent cropTile) {
        this.plantHealth = health;
        this.plantName = name;
        this.plantType = plantType;
        this.plantDescription = plantDescription;
        this.idealWaterLevel = idealWaterLevel;
        this.adultLifeSpan = adultLifeSpan;
        this.maxHealth = maxHealth;
        this.cropTile = cropTile;
        this.currentAge = 0;
        this.growthStage = 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create() {
        super.create();

        // Initialise event listeners.
        entity.getEvents().addListener("harvest", this::harvest);
        entity.getEvents().addListener("destroyPlant", this::destroyPlant);
        entity.getEvents().addListener("attack", (Integer damage) -> increasePlantHealth(-damage));
    }

    /**
     * Returns the current plant health
     *
     * @return current plant health
     */
    public int getPlantHealth() {
        return this.plantHealth;
    }

    public int getMaxHealth() {
        return this.maxHealth;
    }

    /**
     * Set the current plant health
     * @param health - current plant health
     */
    public void setPlantHealth(int health) {
        this.plantHealth = health;
    }

    /**
     * Increase (or decrease) the plant health by some value
     * @param plantHealthIncrement - plant health affected value
     */
    public void increasePlantHealth(int plantHealthIncrement) {
        this.plantHealth += plantHealthIncrement;
    }

    /**
     * Returns the name of the plant
     *
     * @return name of the plant
     */
    public String getPlantName() {
        return this.plantName;
    }

    /**
     * Returns the type of the plant
     *
     * @return type of the plant
     */
    public String getPlantType() {
        return this.plantType;
    }

    /**
     * Returns the plant description
     *
     * @return plant description
     */
    public String getPlantDescription() {
        return this.plantDescription;
    }

    /**
     * Set the decay boolean.
     * @param decay - Whether the plant is decaying or not
     */
    public void setDecay(boolean decay) {
        this.decay = decay;
    }

    /**
     * Find out if the plant is decaying or not.
     * @return If the plant is in a state of decay or not
     */
    public boolean isDecay() {
        return this.decay;
    }

    /**
     * Get the ideal water level of a plant
     * @return ideal water level
     */
    public float getIdealWaterLevel() {
        return this.idealWaterLevel;
    }

    /**
     * Get the current age of a plant in days
     * @return current age
     */
    public float getCurrentAge() {
        return this.currentAge;
    }

    /**
     * Sets the current age of a plant in days
     *
     * @param newAge - The new age the plant is being updated to
     */
    public void setCurrentAge(float newAge) {
        this.currentAge = newAge;
    }

    /**
     * Get the current growth stage of a plant
     * @return current growth stage
     */
    public int getGrowthStage() {
        return this.growthStage;
    }

    /**
     * Set the growth stage of a plant.
     *
     * @param newGrowthStage - The updated growth stage of the plant, between 1 and 7.
     */
    public void setGrowthStage(int newGrowthStage) {
        this.growthStage = newGrowthStage;
    }

    /**
     * get the adult life span of a plant
     *
     * @return adult life span
     */
    public int getAdultLifeSpan() {
        return this.adultLifeSpan;
    }


    /**
     * Set the adult life span of a plant.
     * @param adultLifeSpan - The number of days the plant will exist as an adult plant
     */
    public void setAdultLifeSpan(int adultLifeSpan) {
        this.adultLifeSpan = adultLifeSpan;
    }

    /**
     * Increment the current growth stage of a plant by 1
     *
     * @param growthIncrement - The number of growth stages the plant will increase by
     */
    public void increaseGrowthStage(int growthIncrement) {
        this.growthStage += growthIncrement;
    }

    /**
     * Increase the current age of a plant by some integer value
     *
     * @param ageIncrement - The number of days the age will increase by
     */
    public void increaseCurrentAge(float ageIncrement) {
        this.currentAge += ageIncrement;
    }

    /**
     * Check if a plant is dead
     *
     * @return if the plant is fully decayed
     */
    public boolean isDead() {
       return this.growthStage >= 7;
    }

    /**
     * Harvests this plant, causing it to drop its produce.
     *
     * <p>If this plant is not in a growth stage where is can be harvested, nothing will happen.
     */
    private void harvest() {
        // Stub. When complete will cause the plant to drop items such as seeds and fruits.
        // If the plant is a one-time use (e.g., carrots), then it will destroy itself.
        destroyPlant();
    }

    /**
     * Destroys this plant and clears the crop tile.
     */
    private void destroyPlant() {
        cropTile.setUnoccupied();
        entity.dispose();
    }
}
