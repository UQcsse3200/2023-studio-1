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
    private int plantAge = 0;           // Age of the plant in days as an integer
    private boolean decay = false;
    private float idealWaterLevel;              // Ideal water level. A factor when determining the growth rate.
    private float currentAge;               // Current age of the plant
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
     * @param health    current plant health
     */
    public void setPlantHealth(int health) {
        this.plantHealth = health;
    }

    /**
     * Increase (or decrease) the plant health by some value
     * @param value     plant health affected value
     */
    public void increasePlantHealth(int value) {
        this.plantHealth += value;
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
     * @param decay
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
     * Get this plants current age in days
     * @return the number of days the plant has existed for
     */
    public int getPlantAge() {
        return this.plantAge;
    }

    /**
     * Set this plant's age in days
     * @param age   plant's age (in days)
     */
    public void setPlantAge(int age) {
        this.plantAge = age;
    }

    /**
     * Get the ideal water level of a plant
     * @return ideal water level
     */
    public float getIdealWaterLevel() {
        return this.idealWaterLevel;
    }

    /**
     * Get the current age of a plant
     * @return current age
     */
    public float getCurrentAge() {
        return this.currentAge;
    }

    /**
     * Sets the current age of a plant
     * i.e. all plants start at a current age of 0, and
     * then they start to increase
     */
    public void setCurrentAge() {
        this.currentAge = 0;
    }

    /**
     * Get the current growth stage of a plant
     * @return current growth stage
     */
    public int getGrowthStage() {
        return this.growthStage;
    }

    /**
     * Sets the growth stage of a plant.
     * All plants starts growth stage of 0.
     */
    public void setGrowthStage() {
        this.growthStage = 0;
    }

    /**
     * get the adult life span of a plant
     * @return adult life span
     */
    public int getAdultLifeSpan() {
        return this.adultLifeSpan;
    }

    /**
     * Set the adult life span of a plant.
     * @param adultLifeSpan
     */
    public void setAdultLifeSpan(int adultLifeSpan) {
        this.adultLifeSpan = adultLifeSpan;
    }

    /**
     * Increment the current growth stage of a plant by 1
     */
    public void incrementGrowthStage() {
        this.growthStage += 1;
    }

    /**
     * increase the current age of a plant by some value
     * @param value
     */
    public void increaseCurrentAge(int value) {
        this.currentAge += value;
    }

    /**
     * check if a plant is still alive
     * @return
     */
    public boolean isDead() {
        if (this.growthStage >= 7) {
            return true;
        } else {
            return false;
        }
    }
}
