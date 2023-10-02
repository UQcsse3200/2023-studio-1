package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.Mission;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;
import com.csse3200.game.services.OxygenLevel;
import com.csse3200.game.services.ServiceLocator;

/**
 * A {@link Quest} where the player has to reach a certain oxygen level.
 */
public class OxygenLevelQuest extends Quest {

    /**
     * The {@link OxygenLevel}
     */
    private final OxygenLevel oxygenLevel;

    /**
     * A description of the {@link OxygenLevelQuest}
     */
    private final String oxygenLevelDescription;

    /**
     * Hours until the oxygen level has to be reached
     */
    private final int hourTarget;

    /**
     * How many in-game hours since {@link OxygenLevelQuest} was created
     */
    private int currentHour;

    /**
     * Oxygen level game must reach before the {@link OxygenLevelQuest} expires
     */
    private final int targetOxygenLevel;

    /**
     * Creates an {@link OxygenLevelQuest}
     * @param name - quest name
     * @param reward - the reward the player can collect when quest is complete
     * @param oxygenLevel - game's {@link OxygenLevel} object
     * @param oxygenLevelDescription - description of the {@link OxygenLevelQuest}
     * @param hourTarget - hours until the oxygen level has to be reached
     * @param targetOxygenLevel - oxygen level game must reach before the {@link OxygenLevelQuest} expires
     */
    public OxygenLevelQuest(String name, Reward reward,
                            OxygenLevel oxygenLevel, String oxygenLevelDescription,
                            int hourTarget, int targetOxygenLevel) {
        super(name, reward);

        this.oxygenLevel = oxygenLevel;
        this.oxygenLevelDescription = oxygenLevelDescription;

        this.hourTarget = hourTarget;
        this.currentHour = 0;
        this.targetOxygenLevel = targetOxygenLevel;
    }

    /**
     * Creates an {@link OxygenLevelQuest}
     * @param name - quest name
     * @param reward - the reward the player can collect when quest is complete
     * @param expiryDuration - duration until the Quest expires
     * @param oxygenLevel - game's {@link OxygenLevel} object
     * @param oxygenLevelDescription - description of the {@link OxygenLevelQuest}
     * @param hourTarget - hours until the oxygen level has to be reached
     * @param targetOxygenLevel - oxygen level game must reach before the {@link OxygenLevelQuest} expires
     */
    public OxygenLevelQuest(String name, Reward reward, int expiryDuration, OxygenLevel oxygenLevel,
                            String oxygenLevelDescription, int hourTarget, int targetOxygenLevel) {
        super(name, reward, expiryDuration, false);

        this.oxygenLevel = oxygenLevel;
        this.oxygenLevelDescription = oxygenLevelDescription;

        this.hourTarget = hourTarget;
        this.currentHour = 0;
        this.targetOxygenLevel = targetOxygenLevel;
    }

    /**
     * Registers the {@link OxygenLevelQuest} with the {@link com.csse3200.game.missions.MissionManager}
     * by setting up listener to the hourUpdate event in the TimeService.
     * @param ignored A reference to the {@link EventHandler} on the
     *                             {@link MissionManager}, with which relevant events should be
     *                             listened to.
     */
    @Override
    public void registerMission(EventHandler ignored) {
        ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::updateState);
    }

    /**
     * Updates the state of the {@link OxygenLevelQuest} by incrementing the current hour
     */
    private void updateState() {
        currentHour++;
        notifyUpdate();
    }

    /**
     * Checks if the {@link OxygenLevelQuest}
     * @return - returns true iff the current hour is at least as big as the hour target and the oxygen level
     * is at least as big as the target oxygen level.
     */
    @Override
    public boolean isCompleted() {
        return currentHour >= hourTarget && oxygenLevel.getOxygenPercentage() >= targetOxygenLevel;
    }

    /**
     * Gets a description of the {@link OxygenLevelQuest}
     * @return - the quest description with the player's progress
     */
    @Override
    public String getDescription() {
        return "Oxygen is key for human survival.\n" + getShortDescription() + ". " + oxygenLevel.getOxygenPercentage();
    }

    /**
     * Gets a short description of the {@link OxygenLevelQuest}
     * @return - short description containing the player's progress
     */
    @Override
    public String getShortDescription() {
        int timeRemaining = Math.max(hourTarget - currentHour, 0);
        return "Get " + oxygenLevelDescription + " to be greater than " + targetOxygenLevel + "% in " +
                (timeRemaining >= 24 ? timeRemaining / 24 + " days" : timeRemaining  + " hours" );
    }

    /**
     * Reads in the current {@link OxygenLevelQuest} from a {@link JsonValue}
     * @param progress The {@link JsonValue} representing the progress of the {@link Mission} as determined by the value
     *                 returned in {@link #getProgress()}.
     */
    @Override
    public void readProgress(JsonValue progress) {
        currentHour = progress.asInt();
    }

    /**
     * Gets the {@link OxygenLevelQuest}'s progress.
     * @return - number of hours since the quest was initiated
     */
    @Override
    public Object getProgress() {
        return currentHour;
    }

    /**
     * Resets the current hours back to 0.
     */
    @Override
    protected void resetState() {
        currentHour = 0;
    }

}
