package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;

import java.util.Set;

public class PlantCropsQuest extends Quest {

    private final Set<String> plantTypes;

    private final int numberOfCropsToPlant;
    private int numberOfCropsPlanted;

    public PlantCropsQuest(String name, Reward reward, Set<String> plantTypes, int numberOfCropsToPlant) {
        super(name, reward);

        this.plantTypes = plantTypes;
        this.numberOfCropsToPlant = numberOfCropsToPlant;
        this.numberOfCropsPlanted = 0;
    }

    public PlantCropsQuest(String name, Reward reward, int expiryDuration, boolean isMandatory, Set<String> plantTypes, int numberOfCropsToPlant) {
        super(name, reward, expiryDuration, isMandatory);

        this.plantTypes = plantTypes;
        this.numberOfCropsToPlant = numberOfCropsToPlant;
        this.numberOfCropsPlanted = 0;
    }

    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        missionManagerEvents.addListener(
                MissionManager.MissionEvent.PLANT_CROP.name(),
                this::updateState);
    }

    private void updateState(String plantType) {
        if (plantTypes.contains(plantType) && ++numberOfCropsPlanted >= numberOfCropsToPlant) {
            numberOfCropsPlanted = numberOfCropsToPlant;
        }
        notifyUpdate();
    }

    @Override
    public boolean isCompleted() {
        return numberOfCropsPlanted >= numberOfCropsToPlant;
    }

    @Override
    public String getDescription() {
        StringBuilder descriptionBuilder = new StringBuilder();

        descriptionBuilder.append("Plant ");
        descriptionBuilder.append(numberOfCropsToPlant);
        descriptionBuilder.append(" crops of type ");
        boolean isFirst = true;
        for (String plantType : plantTypes) {
            if (!isFirst) {
                descriptionBuilder.append(", ");
            }
            descriptionBuilder.append(plantType);
            isFirst = false;
        }
        descriptionBuilder.append(" on crop tiles.\n");
        descriptionBuilder.append(numberOfCropsPlanted);
        descriptionBuilder.append(" out of ");
        descriptionBuilder.append(numberOfCropsToPlant);
        descriptionBuilder.append(" crops planted.");

        return descriptionBuilder.toString();
    }

    @Override
    public String getShortDescription() {
        return numberOfCropsPlanted + " out of " + numberOfCropsToPlant + " crops planted.";
    }

    @Override
    public void readProgress(JsonValue progress) {
        numberOfCropsPlanted = progress.asInt();
    }

    @Override
    public Object getProgress() {
        return numberOfCropsPlanted;
    }

    @Override
    protected void resetState() {
        numberOfCropsPlanted = 0;
    }
}
