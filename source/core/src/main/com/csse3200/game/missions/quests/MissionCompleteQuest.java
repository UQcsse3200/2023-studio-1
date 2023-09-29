package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;

/**
 * A MissionCompleteQuest class for when a quest which is based around completing a number of missions
 */
public class MissionCompleteQuest extends Quest {
    private final int numberOfQuestsToComplete; // The number of quests to complete
    private int numberOfQuestsComplete; // The number of quests completed

    /**
     * Creates a new MissionCompleteQuest with the given name, reward and number of quests to complete
     * @param name The name of the quest
     * @param reward The reward for completing the quest
     * @param numberOfQuestsToComplete The number of quests to complete
     */
    public MissionCompleteQuest(String name, Reward reward, int numberOfQuestsToComplete) {
        super(name, reward);

        this.numberOfQuestsToComplete = numberOfQuestsToComplete;
        this.numberOfQuestsComplete = 0;
    }

    /**
     * Creates a new MissionCompleteQuest with the given name, reward, expiry duration, whether the quest is mandatory and number of quests to complete
     * @param name The name of the quest
     * @param reward The reward for completing the quest
     * @param expiryDuration The expiry duration of the quest
     * @param isMandatory Whether the quest is mandatory
     * @param numberOfQuestsToComplete The number of quests to complete
     */
    public MissionCompleteQuest(String name, Reward reward, int expiryDuration, boolean isMandatory, int numberOfQuestsToComplete) {
        super(name, reward, expiryDuration, isMandatory);
        this.numberOfQuestsToComplete = numberOfQuestsToComplete;
        this.numberOfQuestsComplete = 0;
    }

    /**
     * Registers the mission
     */
    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        missionManagerEvents.addListener(MissionManager.MissionEvent.REWARD_COMPLETE.name(), this::updateState);
    }

    /**
     * Updates the state of the quest
     */
    private void updateState() {
        if (++numberOfQuestsComplete >= numberOfQuestsToComplete) {
            numberOfQuestsComplete = numberOfQuestsToComplete;
        }
        notifyUpdate();
    }

    /**
     * Returns whether the quest is completed
     * @return Whether the quest is completed
     */
    @Override
    public boolean isCompleted() {
        return numberOfQuestsComplete >= numberOfQuestsToComplete;
    }

    /**
     * Returns the description of the quest
     * @return The description of the quest
     */
    @Override
    public String getDescription() {
        return "Gather scattered parts of your ship.\nUse your shovel to clear "
                + numberOfQuestsToComplete + " Ship Debris in the world!\n"
                + getShortDescription() + ".";
    }

    /**
     * Returns the short description of the quest
     * @return The short description of the quest
     */
    @Override
    public String getShortDescription() {
        return numberOfQuestsComplete + " out of " + numberOfQuestsToComplete + " debris pieces cleared";
    }

    /**
     * Reads the progress of the quest
     * @param progress jsonValue of the progress
     */
    @Override
    public void readProgress(JsonValue progress) {
        numberOfQuestsComplete = progress.asInt();
    }

    /**
     * Returns the progress of the quest
     * @return The progress of the quest
     */
    @Override
    public Object getProgress() {
        return numberOfQuestsComplete;
    }

    /**
     * Resets the state of the quest
     */
    @Override
    protected void resetState() {
        numberOfQuestsComplete = 0;
    }

}
