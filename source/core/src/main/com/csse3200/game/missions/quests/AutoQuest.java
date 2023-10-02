package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.Mission;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;

/**
 * AutoQuests are Quests that have no state or dynamic progress to track. They can be used just to trigger desired
 * behaviours through the Reward.
 */
public class AutoQuest extends Quest {

    /**
     * Description of the Quest
     */
    private final String description;

    /**
     * Creates an {@link AutoQuest}
     * @param name - name of the AutoQuest
     * @param reward - the {@link Reward} to be collected
     * @param description - description of the AutoQuest
     */
    public AutoQuest(String name, Reward reward, String description) {
        super(name, reward);
        this.description = description;
    }

    /**
     * On registering this mission the {@link Reward} is collected.
     * @param missionManagerEvents A reference to the {@link EventHandler} on the
     *                             {@link MissionManager}, with which relevant events should be
     *                             listened to.
     */
    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        collectReward();
    }

    /**
     * Always returns true as there is no dynamic progress.
     * @return - true
     */
    @Override
    public boolean isCompleted() {
        return true;
    }

    /**
     * Returns the description specified in the constructor.
     * @return - description of the {@link AutoQuest}
     */
    @Override
    public String getDescription() {
        return getShortDescription();
    }

    /**
     * Returns the description as in {@link #getDescription()} method.
     * @return - description of the {@link AutoQuest}
     */
    @Override
    public String getShortDescription() {
        return description;
    }

    /**
     * Does nothing as {@link AutoQuest}s have no savable progress
     * @param progress The {@link JsonValue} representing the progress of the {@link Mission} as determined by the value
     *                 returned in {@link #getProgress()}.
     */
    @Override
    public void readProgress(JsonValue progress) {
        // AutoQuests do not have saveable progress
    }

    /**
     * Get's the {@link AutoQuest}'s progress.
     * @return - always 0 as there is no progress in an {@link AutoQuest}
     */
    @Override
    public Object getProgress() {
        return 0;
    }

    /**
     * Does nothing as {@link AutoQuest}s have no state to reset.
     */
    @Override
    protected void resetState() {
        // AutoQuests do not have dynamic progress
    }

}
