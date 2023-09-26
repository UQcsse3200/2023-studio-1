package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;

public class ClearDebrisQuest extends Quest {

    private final int numberOfDebrisToClear;
    private int numberOfDebrisCleared;

    public ClearDebrisQuest(String name, Reward reward, int numberOfDebrisToClear) {
        super(name, reward);

        this.numberOfDebrisToClear = numberOfDebrisToClear;
        this.numberOfDebrisCleared = 0;
    }

    public ClearDebrisQuest(String name, Reward reward, int expiryDuration, boolean isMandatory, int numberOfDebrisToClear) {
        super(name, reward, expiryDuration, isMandatory);

        this.numberOfDebrisToClear = numberOfDebrisToClear;
        this.numberOfDebrisCleared = 0;
    }

    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        missionManagerEvents.addListener(MissionManager.MissionEvent.DEBRIS_CLEARED.name(), this::updateState);
    }

    private void updateState() {
        if (++numberOfDebrisCleared >= numberOfDebrisToClear) {
            numberOfDebrisCleared = numberOfDebrisToClear;
        }
        notifyUpdate();
    }

    @Override
    public boolean isCompleted() {
        return numberOfDebrisCleared >= numberOfDebrisToClear;
    }

    @Override
    public String getDescription() {
        return "Gather scattered parts of your ship.\nUse your shovel to clear "
                + numberOfDebrisToClear + " Ship Debris in the world!\n"
                + getShortDescription() + ".";
    }

    @Override
    public String getShortDescription() {
        return numberOfDebrisCleared + " out of " + numberOfDebrisToClear + " debris pieces cleared";
    }

    @Override
    public void readProgress(JsonValue progress) {
        numberOfDebrisCleared = progress.asInt();
    }

    @Override
    public Object getProgress() {
        return numberOfDebrisCleared;
    }

    @Override
    protected void resetState() {
        numberOfDebrisCleared = 0;
    }

}
