package com.csse3200.game.missions.achievements;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.MissionManager;

/**
 * An {@link Achievement} completed by planting a certain number of crops on crop tiles.
 */
public class PlantCropsAchievement extends Achievement {

    /**
     * The number of crops the player needs to plant before attaining their goal.
     */
    private final int target;
    /**
     * The number of crops the player has currently planted. Starts at 0.
     */
    private int numberOfCropsPlanted;

    /**
     * Creates a {@link PlantCropsAchievement}.
     *
     * @param name The name of the {@link PlantCropsAchievement}
     * @param numberOfCropsToPlant The target number of crops to plant to reach this achievement
     */
    public PlantCropsAchievement(String name, int numberOfCropsToPlant) {
        super(name);
        target = numberOfCropsToPlant;
        numberOfCropsPlanted = 0;
    }

    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        missionManagerEvents.addListener(
                MissionManager.MissionEvent.PLANT_CROP.name(),
                this::updateState);
    }

    /**
     * Returns true if the number of crops the player has planted is greater than or equal to the
     * target number of plants.
     * @return True if the player has planted the target number of plants.
     */
    @Override
    public boolean isCompleted() {
        return numberOfCropsPlanted >= target;
    }

    @Override
    public String getDescription() {
        return "Become a " + getName() + ", plant " + target + " crops on crop tiles!\n"
                + numberOfCropsPlanted + " out of " + target + " crops planted!";
    }

    @Override
    public String getShortDescription() {
        return numberOfCropsPlanted + " out of " + target + " crops planted";
    }

    @Override
    public void setProgress(JsonValue progress) {
        numberOfCropsPlanted = progress.asInt();
    }

    @Override
    public Object getProgress() {
        return numberOfCropsPlanted;
    }

    /**
     * Increments the number of plants the player has planted.
     * @param ignored The {@link String} representation of the plant type. Irrelevant for this
     *                {@link Achievement}.
     */
    private void updateState(String ignored) {
        if (++numberOfCropsPlanted >= target) {
            numberOfCropsPlanted = target;
        }
        notifyUpdate();
    }

}
