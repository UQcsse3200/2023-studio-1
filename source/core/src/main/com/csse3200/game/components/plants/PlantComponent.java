package com.csse3200.game.components.plants;
import com.csse3200.game.components.Component;

/**
 * Class for all plants in the game.
 */
public class PlantComponent extends Component {
    private int plantHealth;            // Initial plant health
    private String plantName;           // User facing plant name
    private String plantType;           // Type of plant (food, health, repair, defence, production)
    private String plantDescription;    // User facing description of the plant
    private int plantAge;               // Age of the plant in days as an integer
    boolean decay = false;

    /**
     * Constructor used for plant types that have no extra properties.
     *
     * @param health
     * @param name
     * @param plantType
     * @param plantDescription
     */
    public PlantComponent(int health, String name, String plantType, String plantDescription) {
        this.plantHealth = health;
        this.plantName = name;
        this.plantType = plantType;
        this.plantDescription = plantDescription;
        this.plantAge = 0;
    }

    /**
     * Returns the current plant health
     *
     * @return current plant health
     */
    public int getPlantHealth() {
        return this.plantHealth;
    }

    /**
     * Set the current plant health
     * @param health
     */
    public void setPlantHealth(int health) {
        this.plantHealth = health;
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
     * Set this plants age in days
     * @param age
     */
    public void setPlantAge(int age) {
        this.plantAge = age;
    }

}
