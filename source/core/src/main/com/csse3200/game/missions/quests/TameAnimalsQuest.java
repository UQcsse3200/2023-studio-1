package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.Mission;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;

/**
 * A {@link Quest} where the player must tame animals
 */
public class TameAnimalsQuest extends Quest {

    /**
     * Number of animals the player must tame
     */
    private final int numberOfAnimalsToTame;

    /**
     * Number of animals the player has currently tamed
     */
    private int numberOfAnimalsTamed;

    /**
     * Creates a {@link TameAnimalsQuest}
     * @param name Name of the quest
     * @param reward Reward the player can collect on completion
     * @param numberOfAnimalsToTame Number of animals the player must claim
     */
    public TameAnimalsQuest(String name, Reward reward, int numberOfAnimalsToTame) {
        super(name, reward);

        this.numberOfAnimalsToTame = numberOfAnimalsToTame;
        this.numberOfAnimalsTamed = 0;
    }

    /**
     * Creates a {@link TameAnimalsQuest} with an expiry
     * @param name Name of the quest
     * @param reward Reward the player can collect on completion
     * @param expiryDuration How long until the {@link TameAnimalsQuest}
     * @param numberOfAnimalsToTame Number of animals the player must claim
     */
    public TameAnimalsQuest(String name, Reward reward, int expiryDuration, int numberOfAnimalsToTame) {
        super(name, reward, expiryDuration, false);

        this.numberOfAnimalsToTame = numberOfAnimalsToTame;
        this.numberOfAnimalsTamed = 0;
    }

    /**
     * Register the {@link TameAnimalsQuest} with the {@link MissionManager} by adding listener to the
     * ANIMAL_TAMED {@link com.csse3200.game.missions.MissionManager.MissionEvent}
     * @param missionManagerEvents A reference to the {@link EventHandler} on the
     *                             {@link MissionManager}, with which relevant events should be
     *                             listened to.
     */
    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        missionManagerEvents.addListener(MissionManager.MissionEvent.ANIMAL_TAMED.name(), this::updateState);
    }

    /**
     * Increments the number of animals the player has currently tamed
     */
    private void updateState() {
        if (++numberOfAnimalsTamed >= numberOfAnimalsToTame) {
            numberOfAnimalsTamed = numberOfAnimalsToTame;
        }
        notifyUpdate();
    }

    /**
     * Checks if the {@link TameAnimalsQuest} is complete
     * @return Returns true iff the player has tamed at least the target amount of animals
     */
    @Override
    public boolean isCompleted() {
        return numberOfAnimalsTamed >= numberOfAnimalsToTame;
    }

    /**
     * Gets a description of the {@link TameAnimalsQuest}
     * @return The quest's description
     */
    @Override
    public String getDescription() {
        return "Taming certain animals yields special drops.\n" + "Tame " + numberOfAnimalsToTame +
                " tameable animals by feeding them plants.\n" + getShortDescription() + ".";
    }

    /**
     * Gets a short descritiopn of the {@link TameAnimalsQuest}
     * @return The {@link TameAnimalsQuest}'s short description with its progress
     */
    @Override
    public String getShortDescription() {
        return numberOfAnimalsTamed + " out of " + numberOfAnimalsToTame + " animals tamed";
    }

    /**
     * Reads in the current {@link TameAnimalsQuest} from a {@link JsonValue}
     * @param progress The {@link JsonValue} representing the progress of the {@link Mission} as determined by the value
     *                 returned in {@link #getProgress()}.
     */
    @Override
    public void readProgress(JsonValue progress) {
        numberOfAnimalsTamed = progress.asInt();
    }

    /**
     * Gets the {@link TameAnimalsQuest}'s progress
     * @return The number of animals the player has currently tamed
     */
    @Override
    public Object getProgress() {
        return numberOfAnimalsTamed;
    }

    /**
     * Resets number of animals tamed back to 0
     */
    @Override
    protected void resetState() {
        numberOfAnimalsTamed = 0;
    }

}
