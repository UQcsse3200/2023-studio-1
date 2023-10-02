package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;

import java.util.Set;

public class ManageHostilesQuest extends Quest {

    private final Set<EntityType> hostileTypes;

    private final int numberOfHostilesToBeKilled;
    private int numberOfHostilesKilled;

    public ManageHostilesQuest(String name, Reward reward, Set<EntityType> hostileTypes, int numberOfHostilesToBeKilled) {
        super(name, reward);

        this.hostileTypes = hostileTypes;
        this.numberOfHostilesToBeKilled = Math.max(0, numberOfHostilesToBeKilled);
        this.numberOfHostilesKilled = 0;

        // Possibly do some sort of check to make sure the provided hostileTypes set contains only hostile entityTypes
    }

    public ManageHostilesQuest(String name, Reward reward, int expiryDuration, boolean isMandatory, Set<EntityType> hostileTypes, int numberOfHostilesToBeKilled) {
        super(name, reward, expiryDuration, isMandatory);

        this.hostileTypes = hostileTypes;
        this.numberOfHostilesToBeKilled = Math.max(0, numberOfHostilesToBeKilled);
        this.numberOfHostilesKilled = 0;

        // Possibly do some sort of check to make sure the provided hostileTypes set contains only hostile entityTypes
    }

    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        missionManagerEvents.addListener(MissionManager.MissionEvent.ANIMAL_DEFEATED.name(), this::updateState);
        missionManagerEvents.addListener(MissionManager.MissionEvent.ANIMAL_EATEN.name(), this::updateState);
    }

    private void updateState(EntityType hostileType) {
        if (hostileTypes.contains(hostileType) && ++numberOfHostilesKilled >= numberOfHostilesToBeKilled) {
            numberOfHostilesKilled = numberOfHostilesToBeKilled;
        }
        notifyUpdate();
    }

    @Override
    public boolean isCompleted() {
        return numberOfHostilesKilled >= numberOfHostilesToBeKilled;
    }

    @Override
    public String getDescription() {
        StringBuilder descriptionBuilder = new StringBuilder();

        descriptionBuilder.append("Manage the presence of hostile creatures on your farm.\n");
        descriptionBuilder.append("Deal with hostile creatures by defeating them with weapons, or let your Space Snappers eat them.\nDeal with ");
        descriptionBuilder.append(numberOfHostilesToBeKilled);
        descriptionBuilder.append(" creatures of type ");
        boolean isFirst = true;
        for (EntityType hostileType : hostileTypes) {
            if (!isFirst) {
                descriptionBuilder.append(", ");
            }
            descriptionBuilder.append(switch (hostileType) {
                case OxygenEater -> "Oxygen Eater";
                default -> "";
            });
            isFirst = false;
        }
        descriptionBuilder.append(".\n");
        descriptionBuilder.append(getShortDescription());
        descriptionBuilder.append(".");

        return descriptionBuilder.toString();
    }

    @Override
    public String getShortDescription() {
        return numberOfHostilesKilled + " out of " + numberOfHostilesToBeKilled + " hostiles dealt with";
    }

    @Override
    public void readProgress(JsonValue progress) {
        numberOfHostilesKilled = progress.asInt();
    }

    @Override
    public Object getProgress() {
        return numberOfHostilesKilled;
    }

    @Override
    protected void resetState() {
        numberOfHostilesKilled = 0;
    }
}
