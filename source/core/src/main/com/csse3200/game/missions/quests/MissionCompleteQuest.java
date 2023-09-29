package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;

public class MissionCompleteQuest extends Quest {
    private final int numberOfQuestsToComplete;
    private int numberOfQuestsComplete;

    public MissionCompleteQuest(String name, Reward reward, int numberOfQuestsToComplete) {
        super(name, reward);

        this.numberOfQuestsToComplete = numberOfQuestsToComplete;
        this.numberOfQuestsComplete = 0;
    }

    public MissionCompleteQuest(String name, Reward reward, int expiryDuration, boolean isMandatory, int numberOfQuestsToComplete) {
        super(name, reward, expiryDuration, isMandatory);
        this.numberOfQuestsToComplete = numberOfQuestsToComplete;
        this.numberOfQuestsComplete = 0;
    }

    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        missionManagerEvents.addListener(MissionManager.MissionEvent.MISSION_COMPLETE.name(), this::updateState);
    }

    private void updateState() {
        if (++numberOfQuestsComplete >= numberOfQuestsToComplete) {
            numberOfQuestsComplete = numberOfQuestsToComplete;
        }
        notifyUpdate();
    }

    @Override
    public boolean isCompleted() {
        return numberOfQuestsComplete >= numberOfQuestsToComplete;
    }

    @Override
    public String getDescription() {
        return "Gather scattered parts of your ship.\nUse your shovel to clear "
                + numberOfQuestsToComplete + " Ship Debris in the world!\n"
                + getShortDescription() + ".";
    }

    @Override
    public String getShortDescription() {
        return numberOfQuestsComplete + " out of " + numberOfQuestsToComplete + " debris pieces cleared";
    }

    @Override
    public void readProgress(JsonValue progress) {
        numberOfQuestsComplete = progress.asInt();
    }

    @Override
    public Object getProgress() {
        return numberOfQuestsComplete;
    }

    @Override
    protected void resetState() {
        numberOfQuestsComplete = 0;
    }

}
