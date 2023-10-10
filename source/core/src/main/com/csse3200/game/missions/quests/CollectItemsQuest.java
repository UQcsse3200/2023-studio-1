package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.Mission;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;

/**
 * Inventory Quests is a quest that makes the player collect a number of unique items
 */
public class CollectItemsQuest extends Quest {

    /**
     * Number of items the player needs to collect
     */
    private final int numberOfItemsToCollect;

    /**
     * Number of items they have collected
     */
    private int numberOfItemsCollected;

    /**
     * A Quest where player is required to clear a certain amount of debris.
     * @param name - Human-readable name of the Quest.
     * @param reward - Reward player will receive after completing the Quest.
     * @param numberOfItems - Amount of debris the player is required to clear.
     */
    public CollectItemsQuest(String name, Reward reward, int numberOfItems) {
        super(name, reward);
        this.numberOfItemsToCollect = Math.max(numberOfItems, 0);
        this.numberOfItemsCollected = 0;
    }

    /**
     * Constructor for a ClearDebrisQuest where isMandatory and expiryDuration can be specified.
     * @param name - Human-readable name of the Quest.
     * @param reward - Reward player will receive after completing the Quest.
     * @param expiryDuration - Number of in-game hours before the Quest is considered expired.
     * @param numberOfItems - Amount of debris the player is required to clear.
     */
    public CollectItemsQuest(String name, Reward reward, int expiryDuration, int numberOfItems) {
        super(name, reward, expiryDuration, false);

        this.numberOfItemsToCollect = Math.max(numberOfItems, 0);
        this.numberOfItemsCollected = 0;
    }

    /**
     * Registers the Quest with the {@link MissionManager} by listening to the DEBRIS_CLEAR Mission event.
     * @param missionManagerEvents - A reference to the {@link EventHandler} on the
     *                             {@link MissionManager}, with which relevant events should be
     *                             listened to.
     */
    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        missionManagerEvents.addListener(MissionManager.MissionEvent.ITEMS_COLLECTED.name(), this::updateState);
    }

    /**
     * Updating the state of the InventoryQuest. If called this method will increment the numberOfDebrisCleared
     * and then notifyUpdate().
     */
    private void updateState() {
        if (++numberOfItemsCollected >= numberOfItemsToCollect) {
            numberOfItemsCollected = numberOfItemsToCollect;
        }
        notifyUpdate();
    }

    /**
     * Checks if the  InventoryQuest is complete
     * @return - True if player has cleared at least numberOfItemsToCollect, otherwise False.
     */
    @Override
    public boolean isCompleted() {
        return numberOfItemsCollected >= numberOfItemsToCollect;
    }

    /**
     * The description of the Quest to give a representation of the player's progress in the Quest.
     * @return - Human-readable description of the InventoryQuest.
     */
    @Override
    public String getDescription() {
        return "Gather some useful items for your journey.\nCollect "
                + numberOfItemsToCollect + " items in your inventory!\n"
                + getShortDescription() + ".";
    }

    /**
     * Gives the player's progress in the InventoryQuest.
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
     * Get the number of debris the player has cleared since Quest was registered.
     * @return - number of items collected
     */
    @Override
    public Object getProgress() {
        return numberOfItemsCollected;
    }

    /**
     * Resets the number of debris cleared to 0.
     */
    @Override
    protected void resetState() {
        numberOfItemsCollected = 0;
    }

}
