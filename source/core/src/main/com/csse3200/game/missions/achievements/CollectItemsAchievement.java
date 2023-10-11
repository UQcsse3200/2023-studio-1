package com.csse3200.game.missions.achievements;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.Mission;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;

/**
 * An {@link Achievement} completed by collecting a certain number of items
 */
public class CollectItemsAchievement extends Achievement {

    /**
     * Number of items the player needs to collect
     */
    private final int numberOfItemsToCollect;

    /**
     * Number of items they have collected
     */
    private int numberOfItemsCollected;

    /**
     * An achievement where player is required to collect a certain number of items
     * @param name - Human-readable name of the Achievement.
     * @param numberOfItems - Amount of items the player needs to collect.
     */
    public CollectItemsAchievement(String name, int numberOfItems) {
        super(name);
        this.numberOfItemsToCollect = Math.max(numberOfItems, 0);
        this.numberOfItemsCollected = 0;
    }

    /**
     * Registers the Achievement with the {@link MissionManager} by listening to the ITEMS_COLLECTED Mission event.
     * @param missionManagerEvents - A reference to the {@link EventHandler} on the
     *                             {@link MissionManager}, with which relevant events should be
     *                             listened to.
     */
    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        missionManagerEvents.addListener(MissionManager.MissionEvent.ITEMS_COLLECTED.name(), this::updateState);
    }

    /**
     * Updating the state of the CollectItemsAchievement. If called this method will increment the numberOfItemsCollected
     * and then notifyUpdate().
     */
    private void updateState() {
        if (++numberOfItemsCollected >= numberOfItemsToCollect) {
            numberOfItemsCollected = numberOfItemsToCollect;
        }
        notifyUpdate();
    }

    /**
     * Checks if the CollectItemsAchievement is complete
     * @return - True if player has cleared at least numberOfItemsToCollect, otherwise False.
     */
    @Override
    public boolean isCompleted() {
        return numberOfItemsCollected >= numberOfItemsToCollect;
    }

    /**
     * The description of the Achievement to give a representation of the player's progress in the Achievement.
     * @return - Human-readable description of the CollectItemsAchievement.
     */
    @Override
    public String getDescription() {
        return "Gather some useful items for your journey.\nCollect "
                + numberOfItemsToCollect + " items in your inventory!\n"
                + getShortDescription() + ".";
    }

    /**
     * Gives the player's progress in the CollectItemsAchievement.
     * @return - Human-readable String of how many items the player has collected and how many they have left to collect
     */
    @Override
    public String getShortDescription() {
        return numberOfItemsCollected + " out of " + numberOfItemsToCollect + " items collected!";
    }

    /**
     * Read in the number of items the player has collected from a JsonValue.
     * @param progress The {@link JsonValue} representing the progress of the {@link Mission} as determined by the value
     *                 returned in {@link #getProgress()}.
     */
    @Override
    public void readProgress(JsonValue progress) {
        numberOfItemsCollected = progress.asInt();
    }

    /**
     * Get the number of debris the player has cleared since Achievements was registered.
     * @return - number of items collected
     */
    @Override
    public Object getProgress() {
        return numberOfItemsCollected;
    }

    /**
     * Resets the number of debris cleared to 0.
     */
    protected void resetState() {
        numberOfItemsCollected = 0;
    }
}
