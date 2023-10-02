package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.Mission;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;

/**
 * A {@link Quest} where player must repair ship to a certain level.
 */
public class ShipRepairQuest extends Quest {

    /**
     * How much the player must repair the Ship by
     */
    private final int targetShipRepairProgress;

    /**
     * Current amount ship was repaired by since {@link ShipRepairQuest} creation
     */
    private int currentShipRepairProgress;

    /**
     * Creates a {@link ShipRepairQuest}
     * @param name Name of the Quest
     * @param reward Reward the player can collect on completion
     * @param targetShipRepairProgress How much the player must repair ship by
     */
    public ShipRepairQuest(String name, Reward reward, int targetShipRepairProgress) {
        super(name, reward);

        this.targetShipRepairProgress = targetShipRepairProgress;
        this.currentShipRepairProgress = 0;
    }

    /**
     * Creates a {@link ShipRepairQuest} with expiry
     * @param name Name of the Quest
     * @param reward Reward the player can collect on completion
     * @param expiryDuration How long until the {@link ShipRepairQuest} is expired
     * @param targetShipRepairProgress How much the player must repair ship by
     */
    public ShipRepairQuest(String name, Reward reward, int expiryDuration, int targetShipRepairProgress) {
        super(name, reward, expiryDuration, false);

        this.targetShipRepairProgress = targetShipRepairProgress;
        this.currentShipRepairProgress = 0;
    }

    /**
     * Registers {@link ShipRepairQuest} with {@link MissionManager} by listening to SHIP_PART_ADDED event
     * @param missionManagerEvents A reference to the {@link EventHandler} on the
     *                             {@link MissionManager}, with which relevant events should be
     *                             listened to.
     */
    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        missionManagerEvents.addListener(MissionManager.MissionEvent.SHIP_PART_ADDED.name(), this::updateState);
    }

    /**
     * Updates the state by incrementing the amount the ship has been repaired
     */
    private void updateState() {
        if (++currentShipRepairProgress >= targetShipRepairProgress) {
            currentShipRepairProgress = targetShipRepairProgress;
        }
        notifyUpdate();
    }

    /**
     * Checks if {@link ShipRepairQuest} is complete
     * @return True iff the player has repaired the ship by at least the target amount
     */
    @Override
    public boolean isCompleted() {
        return currentShipRepairProgress >= targetShipRepairProgress;
    }

    /**
     * Gets a description of the {@link ShipRepairQuest}
     * @return {@link ShipRepairQuest} description
     */
    @Override
    public String getDescription() {
        return "Repair your ship and unlock useful features.\nAdd scavenged " + targetShipRepairProgress
                + " parts from your ship's hull to your ship.\n" + getShortDescription() + ".";
    }

    /**
     * Gets a short description of the {@link ShipRepairQuest}
     * @return Short quest description showing the players progress
     */
    @Override
    public String getShortDescription() {
        return currentShipRepairProgress + " out of " + targetShipRepairProgress + " additional ship parts to be added";
    }

    /**
     * Reads in the current {@link ShipRepairQuest} from a {@link JsonValue}
     * @param progress The {@link JsonValue} representing the progress of the {@link Mission} as determined by the value
     *                 returned in {@link #getProgress()}.
     */
    @Override
    public void readProgress(JsonValue progress) {
        currentShipRepairProgress = progress.asInt();
    }

    /**
     * The progress of the {@link ShipRepairQuest}
     * @return current ship repair progress
     */
    @Override
    public Object getProgress() {
        return currentShipRepairProgress;
    }

    /**
     * Resets the current ship repair progress back to 0.
     */
    @Override
    protected void resetState() {
        currentShipRepairProgress = 0;
    }

}
