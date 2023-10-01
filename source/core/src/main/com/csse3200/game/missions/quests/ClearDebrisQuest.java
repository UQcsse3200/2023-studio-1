package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.Mission;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;

/**
 * ClearDebrisQuest is a Quest that monitors and tracks the player clearing debris.
 */
public class ClearDebrisQuest extends Quest {

    /**
     * Amount of debris the player must clear to complete the Quest
     */
    private final int numberOfDebrisToClear;

    /**
     * Amount of debris the player has already cleared. Also represents the player's progress towards completing the
     * Quest.
     */
    private int numberOfDebrisCleared;

    /**
     * A Quest where player is required to clear a certain amount of debris.
     * @param name - Human-readable name of the Quest.
     * @param reward - Reward player will receive after completing the Quest.
     * @param numberOfDebrisToClear - Amount of debris the player is required to clear.
     */
    public ClearDebrisQuest(String name, Reward reward, int numberOfDebrisToClear) {
        super(name, reward);

        this.numberOfDebrisToClear = Math.max(numberOfDebrisToClear, 0);
        this.numberOfDebrisCleared = 0;
    }

    /**
     * Constructor for a ClearDebrisQuest where isMandatory and expiryDuration can be specified.
     * @param name - Human-readable name of the Quest.
     * @param reward - Reward player will receive after completing the Quest.
     * @param expiryDuration - Number of in-game hours before the Quest is considered expired.
     * @param isMandatory- True if Quest is mandatory, else false.
     * @param numberOfDebrisToClear - Amount of debris the player is required to clear.
     */
    public ClearDebrisQuest(String name, Reward reward, int expiryDuration, boolean isMandatory, int numberOfDebrisToClear) {
        super(name, reward, expiryDuration, isMandatory);

        this.numberOfDebrisToClear = Math.max(numberOfDebrisToClear, 0);
        this.numberOfDebrisCleared = 0;
    }

    /**
     * Registers the Quest with the {@link MissionManager} by listening to the DEBRIS_CLEAR Mission event.
     * @param missionManagerEvents - A reference to the {@link EventHandler} on the
     *                             {@link MissionManager}, with which relevant events should be
     *                             listened to.
     */
    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        missionManagerEvents.addListener(MissionManager.MissionEvent.DEBRIS_CLEARED.name(), this::updateState);
    }

    /**
     * Updating the state of the ClearDebrisQuest. If called this method will increment the numberOfDebrisCleared
     * and then notifyUpdate().
     */
    private void updateState() {
        if (++numberOfDebrisCleared >= numberOfDebrisToClear) {
            numberOfDebrisCleared = numberOfDebrisToClear;
        }
        notifyUpdate();
    }

    /**
     * Checks if the ClearDebrisQuest is complete
     * @return - True if player has cleared at least numberOfDebrisToClear, otherwise False.
     */
    @Override
    public boolean isCompleted() {
        return numberOfDebrisCleared >= numberOfDebrisToClear;
    }

    /**
     * The description of the Quest to give a representation of the player's progress in the Quest.
     * @return - Human-readable description of the ClearDebrisQuest.
     */
    @Override
    public String getDescription() {
        return "Gather scattered parts of your ship.\nUse your shovel to clear "
                + numberOfDebrisToClear + " Ship Debris in the world!\n"
                + getShortDescription() + ".";
    }

    /**
     * Gives the player's progress in the ClearDebrisQuest.
     * @return - Human-readable String of how much debris the player has cleared and how much they have left to clear.
     */
    @Override
    public String getShortDescription() {
        return numberOfDebrisCleared + " out of " + numberOfDebrisToClear + " debris pieces cleared";
    }

    /**
     * Read in the amount of debris the player has cleared from a JsonValue.
     * @param progress The {@link JsonValue} representing the progress of the {@link Mission} as determined by the value
     *                 returned in {@link #getProgress()}.
     */
    @Override
    public void readProgress(JsonValue progress) {
        numberOfDebrisCleared = progress.asInt();
    }

    /**
     * Get the number of debris the player has cleared since Quest was registered.
     * @return - number of debris cleared
     */
    @Override
    public Object getProgress() {
        return numberOfDebrisCleared;
    }

    /**
     * Resets the number of debris cleared to 0.
     */
    @Override
    protected void resetState() {
        numberOfDebrisCleared = 0;
    }

}
