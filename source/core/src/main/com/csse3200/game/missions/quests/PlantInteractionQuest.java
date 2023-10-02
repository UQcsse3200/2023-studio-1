package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;

import java.util.Set;

public class PlantInteractionQuest extends Quest {

    private final MissionManager.MissionEvent interactionType;
    private final Set<String> plantTypes;

    private final int interactionsTarget;
    private int numberOfInteractions;

    public PlantInteractionQuest(String name, Reward reward, MissionManager.MissionEvent interactionType,
                                 Set<String> plantTypes, int interactionsTarget) {
        super(name, reward);

        this.interactionType = interactionType;
        this.plantTypes = plantTypes;
        this.interactionsTarget = interactionsTarget;
        this.numberOfInteractions = 0;
    }

    public PlantInteractionQuest(String name, Reward reward, int expiryDuration,
                                 MissionManager.MissionEvent interactionType, Set<String> plantTypes,
                                 int interactionsTarget) {
        super(name, reward, expiryDuration, false);

        this.interactionType = interactionType;
        this.plantTypes = plantTypes;
        this.interactionsTarget = interactionsTarget;
        this.numberOfInteractions = 0;
    }

    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        missionManagerEvents.addListener(interactionType.name(), this::updateState);
    }

    private void updateState(String plantType) {
        if (plantTypes.contains(plantType) && ++numberOfInteractions >= interactionsTarget) {
            numberOfInteractions = interactionsTarget;
        }
        notifyUpdate();
    }

    @Override
    public boolean isCompleted() {
        return numberOfInteractions >= interactionsTarget;
    }

    @Override
    public String getDescription() {
        StringBuilder descriptionBuilder = new StringBuilder();

        switch (interactionType) {
            case PLANT_CROP -> descriptionBuilder.append("Plant ");
            case HARVEST_CROP -> descriptionBuilder.append("Harvest ");
            default -> {
                // This should not occur - if it does, do nothing
            }
        }
        descriptionBuilder.append(interactionsTarget);
        descriptionBuilder.append(" crops of type ");
        boolean isFirst = true;
        for (String plantType : plantTypes) {
            if (!isFirst) {
                descriptionBuilder.append(", ");
            }
            descriptionBuilder.append(plantType);
            isFirst = false;
        }
        descriptionBuilder.append(".\n");
        descriptionBuilder.append(getShortDescription());
        descriptionBuilder.append(".");

        return descriptionBuilder.toString();
    }

    @Override
    public String getShortDescription() {
        return numberOfInteractions + " out of " + interactionsTarget + switch (interactionType) {
            case PLANT_CROP -> " crops planted";
            case HARVEST_CROP -> " crops harvested";
            default -> "";
        };
    }

    @Override
    public void readProgress(JsonValue progress) {
        numberOfInteractions = progress.asInt();
    }

    @Override
    public Object getProgress() {
        return numberOfInteractions;
    }

    @Override
    protected void resetState() {
        numberOfInteractions = 0;
    }
}
