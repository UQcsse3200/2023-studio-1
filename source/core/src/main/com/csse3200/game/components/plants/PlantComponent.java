package com.csse3200.game.components.plants;
import static com.badlogic.gdx.math.MathUtils.random;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.function.Supplier;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Json;
import com.csse3200.game.areas.SpaceGameArea;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for all plants in the game.
 */
public class PlantComponent extends Component {

    /**
     * Initial plant health
     */
    private int plantHealth;

    /**
     * Maximum health that this plant can reach as an adult
     */
    private final int maxHealth;

    /**
     * User facing plant name
     */
    private final String plantName;

    /**
     * The type of plant (food, health, repair, defence, production, deadly)
     */
    private final String plantType;

    /**
     * User facing description of the plant
     */
    private final String plantDescription;

    /**
     * Ideal water level of the plant. A factor when determining the growth rate.
     */
    private final float idealWaterLevel;

    /**
     * The growth stage of the plant
     */
    public enum GrowthStage {
        SEEDLING(1),
        SPROUT(2),
        JUVENILE(3),
        ADULT(4),
        DECAYING(5),
        DEAD(6);

        private int value;

        GrowthStage(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private GrowthStage growthStages;

    /**
     * How long a plant lives as an adult before starting to decay from old age.
     */
    private int adultLifeSpan;

    /**
     * Used to determine when a plant enters a new growth stage (for growth stages 2, 3, 4)
     */
    private int currentGrowthLevel;

    /**
     * The crop tile on which this plant is planted on.
     */
    private CropTileComponent cropTile;

    /**
     * The growth thresholds for different growth stages (Sprout Juvenile, Adult).
     */
    private final int[] growthStageThresholds = {0, 0, 0};

    /**
     * The paths to the sounds associated with the plant.
     */
    private String[] sounds;

    /**
     * The current max health. This limits the amount of health a plant can have at different growth stages.
     */
    private int currentMaxHealth = 0;

    /**
     * The maximum health a plant can have at different growth stages (stages 1, 2, 3).
     */
    private int[] maxHealthAtStages = {0, 0, 0};

    /**
     * Used to track how long a plant has been an adult.
     */
    private int numOfDaysAsAdult;

    /**
     * The animation render component used to display the correct image based on the current growth stage.
     */
    private AnimationRenderComponent currentAnimator;

    /**
     * The name of the animation for each growth stage.
     */
    private final String[] animationImages = {"1_seedling", "2_sprout", "3_juvenile", "4_adult", "5_decaying", "6_dead",
            "6_sprout_dead"};

    /**
     * The yield to be dropped when this plant is harvested as a map of item names to drop
     * quantities.
     */
    private Map<String, Integer> harvestYields;

    /**
     * The effect a plant has when it is an adult.
     */
    private String adultEffect;

    /**
     * Indicated whether a plant is eating. Can only be true if the plant is a space snapper.
     */
    private boolean isEating;

    /**
     * Constant used to control how long a space snapper waits before eating again.
     */
    private int eatingCoolDown = 60;

    /**
     * Count of minutes since the space snapper ate. Used to determine whether the plant is ready to eat again.
     */
    private int countMinutesOfDigestion;

    private static final Logger logger = LoggerFactory.getLogger(PlantComponent.class);

    /**
     * Indicates whether the player is in close enough proximity for this plant to make sounds.
     */
    private boolean playerInProximity;

    private boolean plantDestroyed = false;
    private boolean deadBeforeMaturity = false;

    private boolean forced = false;

    /**
     * Constructor used for plant types that have no extra properties. This is just used for testing.
     *
     * @param health - health of the plant
     * @param name - name of the plant
     * @param plantType - type of the plant
     * @param plantDescription - description of the plant
     * @param idealWaterLevel - The ideal water level for a plant
     * @param adultLifeSpan - How long a plant will live for once it becomes an adult
     * @param maxHealth - The maximum health a plant can reach as an adult
     * @param cropTile - The cropTileComponent where the plant will be located.
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
        this.currentGrowthLevel = 0;
        this.growthStages = GrowthStage.SEEDLING;
        this.numOfDaysAsAdult = 0;
        this.currentMaxHealth = maxHealth;

        // Initialise default values for growth stage thresholds.
        this.growthStageThresholds[0] = 11;
        this.growthStageThresholds[1] = 21;
        this.growthStageThresholds[2] = 41;

        // Initialise max health values to be changed at each growth stage
        this.maxHealthAtStages[0] = (int)(0.05 * this.maxHealth);
        this.maxHealthAtStages[1] = (int)(0.1 * this.maxHealth);
        this.maxHealthAtStages[2] = (int)(0.3 * this.maxHealth);

        this.adultEffect = "None";
        this.isEating = false;
        this.countMinutesOfDigestion = 0;
        this.playerInProximity = false;

    }

    /**
     * Constructor used for plant types that have growthStageThresholds different from the default
     * values.
     *
     * @param health - health of the plant
     * @param name - name of the plant
     * @param plantType - type of the plant
     * @param plantDescription - description of the plant
     * @param idealWaterLevel - The ideal water level for a plant
     * @param adultLifeSpan - How long a plant will live for once it becomes an adult
     * @param maxHealth - The maximum health a plant can reach as an adult
     * @param cropTile - The cropTileComponent where the plant will be located.
     * @param growthStageThresholds - A list of three integers that represent the growth thresholds.
     * @param soundsArray - A list of all sound files file paths as strings
     */
    public PlantComponent(int health, String name, String plantType, String plantDescription,
                          float idealWaterLevel, int adultLifeSpan, int maxHealth,
                          CropTileComponent cropTile, int[] growthStageThresholds,
                          String[] soundsArray) {
        this.plantHealth = health;
        this.plantName = name;
        this.plantType = plantType;
        this.plantDescription = plantDescription;
        this.idealWaterLevel = idealWaterLevel;
        this.adultLifeSpan = adultLifeSpan;
        this.maxHealth = maxHealth;
        this.cropTile = cropTile;
        this.currentGrowthLevel = 1;
        this.sounds = soundsArray;
        this.growthStages = GrowthStage.SEEDLING;
        this.numOfDaysAsAdult = 0;
        this.currentMaxHealth = maxHealth;

        // Initialise growth stage thresholds for specific plants.
        this.growthStageThresholds[0] = growthStageThresholds[0];
        this.growthStageThresholds[1] = growthStageThresholds[1];
        this.growthStageThresholds[2] = growthStageThresholds[2];

        // Initialise max health values to be changed at each growth stage
        this.maxHealthAtStages[0] = (int)(0.05 * maxHealth);
        this.maxHealthAtStages[1] = (int)(0.1 * maxHealth);
        this.maxHealthAtStages[2] = (int)(0.3 * maxHealth);

        switch (this.plantName) {
            case "Hammer Plant" -> this.adultEffect = "Health";
            case "Space Snapper" -> this.adultEffect = "Eat";
            case "Deadly Nightshade" -> this.adultEffect = "Poison";
            default -> this.adultEffect = "Sound";
        }
        this.isEating = false;
        this.countMinutesOfDigestion = 0;
        this.playerInProximity = false;
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
        entity.getEvents().addListener("attack", this::attack);
        ServiceLocator.getTimeService().getEvents().addListener("minuteUpdate", this::minuteUpdate);
        ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::hourUpdate);
        ServiceLocator.getTimeService().getEvents().addListener("dayUpdate", this::dayUpdate);
        ServiceLocator.getPlantCommandService().getEvents().addListener("forceGrowthStage", this::forceGrowthStage);

        ServiceLocator.getPlantInfoService().increasePlantGrowthStageCount(1, "alive");
        ServiceLocator.getPlantInfoService().increaseSeedsPlanted(1, plantName);

        this.currentAnimator = entity.getComponent(AnimationRenderComponent.class);
        updateTexture();
        updateMaxHealth();
    }

    /**
     * Functionality for the plant that needs to update every minute.
     */
    public void minuteUpdate() {
        int min = ServiceLocator.getTimeService().getMinute();

        if (min % 20 == 0) {
            increaseCurrentGrowthLevel();
            updateGrowthStage();
            updateMaxHealth();
        }

        // Handle digestion functionality.
        digestion();

        // Check if the plant should be decaying or dead.
        decayCheck();
    }

    /**
     * Functionality for the plant that needs to update every hour.
     */
    public void hourUpdate() {
        // Currently no hourly updates needed.
    }

    /**
     * Functionality for the plant that needs to update every day.
     */
    public void dayUpdate() {
        adultLifeSpanCheck();
    }

    /**
     * Check if the plant has exceeded its adult lifespan.
     */
    public void adultLifeSpanCheck() {
        // If the plant reaches its adult life span then start decaying.
        if (getGrowthStage().getValue() == GrowthStage.ADULT.getValue()) {
            this.numOfDaysAsAdult += 1;
            if (getNumOfDaysAsAdult() > getAdultLifeSpan()) {
                entity.getComponent(PlantAreaOfEffectComponent.class).setEffectType("Decay");
                entity.getComponent(PlantAreaOfEffectComponent.class).setRadius(2f);
                setGrowthStage(getGrowthStage().getValue() + 1);
                playSound("decays");
                updateTexture();
            }
        }
    }

    /**
     * Check if the plant is already decaying and needs to die. Also check if the plant
     * has died before reaching adult growth stage.
     */
    public void decayCheck() {
        if (!plantDestroyed) {
            if (getGrowthStage().getValue() == GrowthStage.ADULT.getValue()) {
                if (getPlantHealth() <= 0) {
                    setGrowthStage(GrowthStage.DECAYING.getValue());
                    updateTexture();
                }
            }

            // If the plants health drops to zero while decaying, then update the growth stage to dead.
            if (getGrowthStage().getValue() == GrowthStage.DECAYING.getValue()) {
                if (getPlantHealth() <= 0) {
                    setGrowthStage(GrowthStage.DEAD.getValue());
                    playSound("destroy");
                    updateTexture();
                }

            // If the plants health drops to zero before it becomes an adult its dead.
            // Only destroyed immediately if the plant is a seedling.
            } else if (getGrowthStage().getValue() < GrowthStage.ADULT.getValue())
                if (getPlantHealth() <= 0) {
                    if (getGrowthStage().getValue() != GrowthStage.SEEDLING.getValue()) {
                        deadBeforeMaturity = true;
                        setGrowthStage(GrowthStage.DEAD.getValue());
                        updateTexture();
                    } else {
                        destroyPlant();
                    }
                }
        }
    }

    /**
     * Set the area of effect radius based on the type of plant.
     */
    public void setAreaOfEffectRadius() {
        switch (this.plantName) {
            case "Space Snapper" -> entity.getComponent(PlantAreaOfEffectComponent.class).setRadius(1.5f);
            case "Hammer Plant" -> entity.getComponent(PlantAreaOfEffectComponent.class).setRadius(3f);
            case "Deadly Nightshade" -> entity.getComponent(PlantAreaOfEffectComponent.class).setRadius(2.5f);
            default -> entity.getComponent(PlantAreaOfEffectComponent.class).setRadius(2f);
        }

    }

    /**
     * Update the growth stage of a plant based on its current growth level.
     * This method is called every (in game) day at midday (12pm) and midnight.
     * If the currentGrowthLevel exceeds the corresponding growth threshold, then the plant will
     * advance to the next growth stage.
     * When a plant becomes an adult, it has an adult life span.
     *
     * Also, if the plant is in a state of decay then decrease the health every hour.
     */
    public void updateGrowthStage() {
        if ((getGrowthStage().getValue() < GrowthStage.ADULT.getValue())) {
            if (this.currentGrowthLevel >= this.growthStageThresholds[getGrowthStage().getValue() - 1]) {
                setGrowthStage(getGrowthStage().getValue() + 1);
                if (getGrowthStage().getValue() == GrowthStage.ADULT.getValue()) {
                    entity.getComponent(PlantAreaOfEffectComponent.class).setEffectType(this.adultEffect);
                    setAreaOfEffectRadius();
                }
                updateTexture();
            }
        }
    }

    /**
     * Check if the space snapper is currently eating. If it is then implement a cool down period before the
     * plant is ready to eat again.
     */
    public void digestion() {
        if (this.isEating) {

            this.countMinutesOfDigestion += 1;

            if (this.countMinutesOfDigestion >= eatingCoolDown) {
                this.isEating = false;
                this.countMinutesOfDigestion = 0;
                updateTexture();
            }
        }
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
     * @param health - current plant health
     */
    public void setPlantHealth(int health) {
        this.plantHealth = health;
    }

    /**
     * Returns the max health of the plant
     *
     * @return current max health
     */

    public int getMaxHealth() {
        return this.maxHealth;
    }

    /**
     * Increase (or decrease) the plant health by some value
     * @param plantHealthIncrement - plant health affected value
     */
    public void increasePlantHealth(int plantHealthIncrement) {

        if (getGrowthStage().getValue() <= GrowthStage.ADULT.getValue()) {
            this.plantHealth += plantHealthIncrement;
        } else {
            return;
        }

        int growthStage = getGrowthStage().getValue();
        if ((growthStage < GrowthStage.ADULT.getValue())
                && plantHealth > maxHealthAtStages[growthStage - 1]) {
            this.plantHealth = maxHealthAtStages[growthStage - 1];
        } else if (growthStage == GrowthStage.ADULT.getValue()
                && plantHealth > maxHealth) {
            this.plantHealth = maxHealth;
        }

        if (this.plantHealth < 0) {
            this.plantHealth = 0;
        }
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
     * Set the plant to decaying stage.
     */
    public void setDecay() {
        this.growthStages = GrowthStage.DECAYING;
    }

    /**
     * Find out if the plant is decaying or not.
     * @return If the plant is in a state of decay or not
     */
    public boolean isDecay() {
        return this.growthStages == GrowthStage.DECAYING;
    }

    /**
     * Get the ideal water level of a plant
     * @return ideal water level
     */
    public float getIdealWaterLevel() {
        return this.idealWaterLevel;
    }

    /**
     * Get the current growth stage of a plant
     * @return current growth stage
     */
    public GrowthStage getGrowthStage() {
        return this.growthStages;
    }

    /**
     * Set the growth stage of a plant.
     *
     * @param newGrowthStage - The updated growth stage of the plant, between 1 and 6.
     */
    public void setGrowthStage(int newGrowthStage) {

        if (newGrowthStage == GrowthStage.DEAD.getValue()) {
            ServiceLocator.getPlantInfoService().increasePlantGrowthStageCount(-1, "alive");
            if (!deadBeforeMaturity && !forced) {
                ServiceLocator.getPlantInfoService().increasePlantGrowthStageCount(-1, "decay");
            } else if (forced) {
                forced = false;
            }
        } else if (newGrowthStage == GrowthStage.DECAYING.getValue()) {
            ServiceLocator.getPlantInfoService().increasePlantGrowthStageCount(1, "decay");
        }

        if (newGrowthStage >= 1 && newGrowthStage <= GrowthStage.values().length) {
            this.growthStages = GrowthStage.values()[newGrowthStage - 1];
        } else {
            throw new IllegalArgumentException("Invalid growth stage value.");
        }
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
     * @param adultLifeSpan The number of days the plant will exist as an adult plant
     */
    public void setAdultLifeSpan(int adultLifeSpan) {
        this.adultLifeSpan = adultLifeSpan;
    }

    /**
     * Increment the current growth stage of a plant by 1
     *
     * @param growthIncrement The number of growth stages the plant will increase by
     */
    public void increaseGrowthStage(int growthIncrement) {
        this.growthStages.value += growthIncrement;
    }

    /**
     * Return the current growth level of the plant.
     * @return The current growth level
     */
    public int getCurrentGrowthLevel() {
        return this.currentGrowthLevel;
    }

    /**
     * Retrieves the current maximum health value of the plant.
     * @return The current maximum health of the plant.
     */
    public int getCurrentMaxHealth() {
        return this.currentMaxHealth;
    }

    /**
     * Retrieves the number of days the plant has been an adult.
     * @return The number of days the plant has been in its adult stage.
     */
    public int getNumOfDaysAsAdult() {
        return numOfDaysAsAdult;
    }

    /**
     * Sets the number of days the plant has been an adult.
     * @param numOfDaysAsAdult The number of days to set the plant's adult stage duration to.
     */
    public void setNumOfDaysAsAdult(int numOfDaysAsAdult) {
        this.numOfDaysAsAdult = numOfDaysAsAdult;
    }

    /**
     * Sets the harvest yield of this plant.
     *
     * <p>This will store an unmodifiable copy of the given map. Modifications made to the given
     * map after being set will not be reflected in the plant.
     *
     * @param harvestYields Map of item names to drop quantities.
     */
    public void setHarvestYields(Map<String, Integer> harvestYields) {
        this.harvestYields = Map.copyOf(harvestYields);
    }

    /**
     * Increase the currentGrowthLevel based on the growth rate provided by the CropTileComponent where the
     * plant is located.
     * If the growthRate received is negative, this indicates that the conditions are inhospitable for the
     * plant, and it will lose some health.
     * currentGrowthLevel can not increase once a plant is an adult, but the tile can become inhospitable and
     * decrease the adult plants' health.
     * If the plant is already decaying, do nothing.
     */
    void increaseCurrentGrowthLevel() {
        int growthRate = (int)(this.cropTile.getGrowthRate(this.idealWaterLevel) * 10);
        float waterLevel = cropTile.getWaterContent();
        // Check if the growth rate is negative
        // That the plant is not decaying
        if ((growthRate < 0) && !isDecay() && (getGrowthStage().getValue() <= GrowthStage.ADULT.getValue())) {
            increasePlantHealth(-1);
        } else if ( getGrowthStage().getValue() < GrowthStage.ADULT.getValue()
                    && !isDecay()
                    && waterLevel > 0) {
            this.currentGrowthLevel += growthRate;
            increasePlantHealth(1);
        } else if (waterLevel == 0) {
            increasePlantHealth(-1);
        }
    }

    /**
     * Check if a plant is dead
     *
     * @return if the plant is fully decayed
     */
    public boolean isDead() {
        return this.growthStages.getValue() == GrowthStage.DEAD.getValue();
    }

    /**
     * Harvests this plant, causing it to drop its produce.
     *
     * <p>If this plant is not in a growth stage where is can be harvested, nothing will happen.
     */
    private void harvest() {
        if (growthStages != GrowthStage.ADULT) {
            // Cannot harvest when not an adult (or decaying).
            return;
        }

        harvestYields.forEach((itemName, quantity) -> {
            Supplier<Entity> itemSupplier = ItemFactory.getItemSupplier(itemName);
            for (int i = 0; i < quantity; i++) {
                Entity item = itemSupplier.get();
                item.setPosition(entity.getPosition());
                ServiceLocator.getEntityService().register(item);
            }
        });
        ServiceLocator.getMissionManager().getEvents().trigger(
                MissionManager.MissionEvent.HARVEST_CROP.name(),
                getPlantName());
        ServiceLocator.getPlantInfoService().increasePlantsHarvested(1, plantName);
        destroyPlant();

    }

    /**
     * Destroys this plant and clears the crop tile.
     */
    private void destroyPlant() {
        if (getGrowthStage().getValue() < GrowthStage.DEAD.getValue()) {
            ServiceLocator.getPlantInfoService().increasePlantGrowthStageCount(-1, "alive");
        }

        // This is such a cumbersome way of doing this, but there is an annoying bug that
        // occurs when the PhysicsComponent is disposed of.

        entity.getComponent(PlantAreaOfEffectComponent.class).dispose();

        //entity.getComponent(PhysicsComponent.class).dispose();

        entity.getComponent(ColliderComponent.class).dispose();
        entity.getComponent(HitboxComponent.class).dispose();
        entity.getComponent(PlantMouseHoverComponent.class).plantDied();
        entity.getComponent(PlantMouseHoverComponent.class).dispose();
        entity.getComponent(PlantProximityComponent.class).dispose();
        entity.getComponent(PlantComponent.class).dispose();
        entity.getComponent(AnimationRenderComponent.class).dispose();
        cropTile.setUnoccupied();

        plantDestroyed = true;


    }


    /**
     * To attack plants and damage their health.
     */
    private void attack() {
        int attackDamage = 10;
        increasePlantHealth(-attackDamage);
    }

    /**
     * Update the maximum health of the plant based on its current growth stage.
     * Called whenever the growth stage is changed
     */
    public void updateMaxHealth() {
        switch (getGrowthStage()) {
            case SEEDLING -> this.currentMaxHealth = this.maxHealthAtStages[0];
            case SPROUT -> this.currentMaxHealth = this.maxHealthAtStages[1];
            case JUVENILE -> this.currentMaxHealth = this.maxHealthAtStages[2];
            case ADULT, DECAYING, DEAD -> this.currentMaxHealth = this.maxHealth;
            default -> throw new IllegalStateException("Unexpected value: " + getGrowthStage());
        };
    }

    /**
     * Update the texture of the plant based on its current growth stage.
     */
    public void updateTexture() {
        if (!this.isEating && (getGrowthStage().getValue() <= GrowthStage.DEAD.getValue())) {
            if (this.currentAnimator != null) {
                if (deadBeforeMaturity) {
                    currentAnimator.startAnimation("6_sprout_dead");
                } else {
                    this.currentAnimator.startAnimation(this.animationImages[getGrowthStage().getValue() - 1]);
                }

            }
        } else if (this.isEating) {
            if (this.currentAnimator != null) {
                this.currentAnimator.startAnimation("digesting");
            }

        }
    }

    /**
     * Determine what sound needs to be called based on the type of interaction.
     * The sounds are separated into tuple pairs for each event.The sound will only be played if the
     * player is close enough to the plant. This is determined by the PlantProximityComponent.
     *
     * @param functionCalled The type of interaction with the plant.
     */
    public void playSound(String functionCalled) {
        if (!plantDestroyed && playerInProximity) {

            logger.debug("The sound being called: " + functionCalled);

            switch (functionCalled) {
                case "click" -> chooseSound(sounds[0], sounds[1]);
                case "decays" -> chooseSound(sounds[2], sounds[3]);
                case "destroy" -> chooseSound(sounds[4], sounds[5]);
                case "nearby" -> chooseSound(sounds[6], sounds[7]);
                default -> throw new IllegalStateException("Unexpected function: " + functionCalled);
            }
        }
    }

    /**
     * Decide what sound to play. There is a small random chance that a lore sound will be selected, otherwise
     * the normal sound will be played.
     * @param lore Sound associated with the secret lore
     * @param notLore Normal sound that the player will be expecting.
     */
    void chooseSound(String lore, String notLore) {
        boolean playLoreSound = random.nextInt(100) <= 0; //Gives 1% chance of being true
        Sound soundEffect;
        logger.debug("is the sound lore?: " + playLoreSound);
        if (!playLoreSound) {
            soundEffect = ServiceLocator.getResourceService().getAsset(lore, Sound.class);
        } else {
            soundEffect = ServiceLocator.getResourceService().getAsset(notLore, Sound.class);
        }
        soundEffect.play();
    }

    public void setCurrentAge(float age) {
    }

    public float getCurrentAge() {
        return 0;
    }

    /**
     * Return the cropTile where the plant is located.
     * @return the cropTile.
     */
    public CropTileComponent getCropTile() {
        return this.cropTile;
    }

    /**
     * Is the plant eating right now.
     * @return isEating
     */
    public boolean getIsEating() {
        return this.isEating;
    }

    /**
     * Tell the plant it is now eating an animal.
     */
    public void setIsEating() {
        //this.countOfHoursOfDigestion = 0;
        this.countMinutesOfDigestion = 0;
        this.isEating = true;
    }

    /**
     * Function used when debugging. Allows for the plant to instantly become a seedling from any growth stage.
     */
    public void forceSeedling() {

        // If the plant is already a seedling do nothing.
        if (getGrowthStage().getValue() == GrowthStage.SEEDLING.getValue()) {
            return;
        }

        if (deadBeforeMaturity) {
            deadBeforeMaturity = false;
        }

        if (getGrowthStage() == GrowthStage.DEAD) {
            ServiceLocator.getPlantInfoService().increasePlantGrowthStageCount(1, "alive");
        }

        this.setGrowthStage(GrowthStage.SEEDLING.getValue());
        this.setPlantHealth(2);
        this.setCurrentGrowthLevel(20);
        entity.getComponent(PlantAreaOfEffectComponent.class).setEffectType("None");
        updateTexture();
        updateMaxHealth();
    }

    /**
     * Function used when debugging. Allows for the plant to instantly become a sprout from any growth stage.
     */
    public void forceSprout() {

        // If the plant is already a sprout do nothing.
        if (getGrowthStage().getValue() == GrowthStage.SPROUT.getValue()) {
            return;
        }

        if (deadBeforeMaturity) {
            deadBeforeMaturity = false;
        }

        if (getGrowthStage() == GrowthStage.DEAD) {
            ServiceLocator.getPlantInfoService().increasePlantGrowthStageCount(1, "alive");
        }

        this.setGrowthStage(GrowthStage.SPROUT.getValue());
        this.setPlantHealth(10);
        this.setCurrentGrowthLevel(50);
        entity.getComponent(PlantAreaOfEffectComponent.class).setEffectType("None");
        updateTexture();
        updateMaxHealth();
    }

    /**
     * Function used when debugging. Allows for the plant to instantly become a juvenile from any growth stage.
     */
    public void forceJuvenile() {

        // If the plant is already a juvenile do nothing.
        if (getGrowthStage().getValue() == GrowthStage.JUVENILE.getValue()) {
            return;
        }

        if (deadBeforeMaturity) {
            deadBeforeMaturity = false;
        }

        if (getGrowthStage() == GrowthStage.DEAD) {
            ServiceLocator.getPlantInfoService().increasePlantGrowthStageCount(1, "alive");
        }

        this.setGrowthStage(GrowthStage.JUVENILE.getValue());
        this.setPlantHealth(20);
        this.setCurrentGrowthLevel(70);
        entity.getComponent(PlantAreaOfEffectComponent.class).setEffectType("None");
        updateTexture();
        updateMaxHealth();
    }

    /**
     * Function used when debugging. Allows for the plant to instantly become an adult from any growth stage.
     */
    public void forceAdult() {

        // If the plant is already an adult do nothing.
        if (getGrowthStage().getValue() == GrowthStage.ADULT.getValue()) {
            return;
        }

        if (deadBeforeMaturity) {
            deadBeforeMaturity = false;
        }

        if (getGrowthStage() == GrowthStage.DEAD) {
            ServiceLocator.getPlantInfoService().increasePlantGrowthStageCount(1, "alive");
        }

        this.setGrowthStage(GrowthStage.ADULT.getValue());
        this.setPlantHealth(30);
        updateTexture();
        entity.getComponent(PlantAreaOfEffectComponent.class).setEffectType(this.adultEffect);
        setAreaOfEffectRadius();
        updateMaxHealth();
    }

    /**
     * Function used when debugging. Allows for the plant to instantly start decaying from any growth stage.
     */
    public void forceDecay() {

        // If the plant is already decaying do nothing.
        if (getGrowthStage().getValue() == GrowthStage.DECAYING.getValue()) {
            return;
        }

        if (deadBeforeMaturity) {
            deadBeforeMaturity = false;
        }

        if (getGrowthStage() == GrowthStage.DEAD) {
            ServiceLocator.getPlantInfoService().increasePlantGrowthStageCount(1, "alive");
        }

        this.setGrowthStage(GrowthStage.DECAYING.getValue());
        this.setPlantHealth(30);
        entity.getComponent(PlantAreaOfEffectComponent.class).setEffectType("Decay");
        playSound("decays");
        updateTexture();
        updateMaxHealth();
    }

    /**
     * Function used when debugging. Allows for the plant to instantly die from any growth stage.
     */
    public void forceDead() {
        forced = true;
        if (deadBeforeMaturity) {
            deadBeforeMaturity = false;
        }
        // If the plant is already dead do nothing.
        if (getGrowthStage().getValue() == GrowthStage.DEAD.getValue()) {
            return;
        }

        ServiceLocator.getPlantInfoService().setDecayingPlantCount(0);

        this.setGrowthStage(GrowthStage.DEAD.getValue());
        this.setPlantHealth(0);
        updateTexture();
        playSound("destroy");
        updateMaxHealth();
    }

    public void forceGrowthStage(String growthStage) {
        if (!plantDestroyed) {
            switch (growthStage) {
                case "seedling" -> forceSeedling();
                case "sprout" -> forceSprout();
                case "juvenile" -> forceJuvenile();
                case "adult" -> forceAdult();
                case "decay" -> forceDecay();
                case "dead" -> forceDead();
            }
        }
    }

    /**
     * Return a string will all the relevant information about this plant.
     * The returned string must be formatted such that each piece of information is on a new line.
     * @return - Formatted string with all relevant information about the plant.
     */
    public String currentInfo() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String waterLevel = decimalFormat.format(cropTile.getWaterContent());
        String idealWaterLevel = decimalFormat.format(this.idealWaterLevel);
        String growthLevel = decimalFormat.format(currentGrowthLevel);
        String currentMaxHealth = Integer.toString(this.currentMaxHealth);
        String waterLevelStatus = "";

        float waterLevelDiff = cropTile.getWaterContent() - this.idealWaterLevel;

        if (waterLevelDiff < -0.2) {
            waterLevelStatus = "Under watered";
        } else if (waterLevelDiff > 0.2) {
            waterLevelStatus = "Over watered";
        } else {
            waterLevelStatus = "Ideal water level";
        }


        String returnString =
                "Growth Stage: " + getGrowthStage().name() +
                "\nWater level: " + waterLevel + "/" + idealWaterLevel +
                "\nWater Status: " + waterLevelStatus +
                "\nHealth: " + plantHealth + "/" + currentMaxHealth;


        if (getGrowthStage().getValue() < GrowthStage.ADULT.getValue()) {
            returnString += "\nGrowth Level: " + growthLevel + "/" +
                    growthStageThresholds[getGrowthStage().getValue() - 1];
        }
        return  returnString;
    }

    /**
     * Set the currentGrowthLevel of the plant. This is used for testing purposes.
     * @param currentGrowthLevel - Desired growth level of the plant.
     */
    public void setCurrentGrowthLevel(int currentGrowthLevel) {
        this.currentGrowthLevel = currentGrowthLevel;
    }

    /**
     * Indicates when the player is in the plants proximity for playing sounds or not.
     * @param bool - If the player is/(is not) in proximity.
     */
    public void setPlayerInProximity(boolean bool) {
        this.playerInProximity = bool;
    }

    @Override
    public void write(Json json) {
        json.writeObjectStart(this.getClass().getSimpleName());
        json.writeValue("name", getPlantName());
        json.writeValue("health", getPlantHealth());
        json.writeValue("age", 0);
        json.writeValue("growth", getCurrentGrowthLevel());
        json.writeObjectEnd();
    }

    @Override
    public void read(Json json, JsonValue plantData) {
        ServiceLocator.getGameArea().spawnEntity(entity);
        plantHealth = plantData.getInt("health");
        currentGrowthLevel = plantData.getInt("growth");
        // age = plantData.getFloat("age");
    }
}
