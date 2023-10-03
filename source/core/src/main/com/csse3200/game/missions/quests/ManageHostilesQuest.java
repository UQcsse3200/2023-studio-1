package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.Mission;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A {@link Quest} that requires the player to kill hostiles in order to be completed.
 */
public class ManageHostilesQuest extends Quest {

    /**
     * The type of hostiles the player should kill.
     */
    private final Set<EntityType> hostileTypes;

    /**
     * The number of hostiles the player is required to kill.
     */
    private final int numberOfHostilesToBeKilled;

    /**
     * The number of hostiles the player has killed since creation of {@link ManageHostilesQuest}.
     */
    private int numberOfHostilesKilled;

    /**
     * Creates a {@link ManageHostilesQuest}
     * @param name - the name of the quest
     * @param reward - the {@link Reward} the player can collect on completion
     * @param hostileTypes - the type of hostiles the player should kill
     * @param numberOfHostilesToBeKilled - the number of hostiles the player is required to kill
     */
    public ManageHostilesQuest(String name, Reward reward, Set<EntityType> hostileTypes, int numberOfHostilesToBeKilled) {
        super(name, reward);

        this.hostileTypes = hostileTypes;
        this.numberOfHostilesToBeKilled = Math.max(0, numberOfHostilesToBeKilled);
        this.numberOfHostilesKilled = 0;

        // Possibly do some sort of check to make sure the provided hostileTypes set contains only hostile entityTypes
    }

    /**
     * Creates a {@link ManageHostilesQuest} with a specific expiry
     * @param name - the name of the quest
     * @param reward - the {@link Reward} the player can collect on completion
     * @param expiryDuration - how many in-game hours until {@link ManageHostilesQuest} expires
     * @param hostileTypes - the type of hostiles the player should kill
     * @param numberOfHostilesToBeKilled - the number of hostiles the player is required to kill
     */
    public ManageHostilesQuest(String name, Reward reward, int expiryDuration, Set<EntityType> hostileTypes,
                               int numberOfHostilesToBeKilled) {
        super(name, reward, expiryDuration, false);

        this.hostileTypes = hostileTypes;
        this.numberOfHostilesToBeKilled = Math.max(0, numberOfHostilesToBeKilled);
        this.numberOfHostilesKilled = 0;

        // Possibly do some sort of check to make sure the provided hostileTypes set contains only hostile entityTypes
    }

    /**
     * Registers the {@link ManageHostilesQuest} with the {@link MissionManager} by adding a listener
     * to the ANIMAL_DEFEATED and ANIMAL_EATEN {@link com.csse3200.game.missions.MissionManager.MissionEvent}s
     * @param missionManagerEvents A reference to the {@link EventHandler} on the
     *                             {@link MissionManager}, with which relevant events should be
     *                             listened to.
     */
    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        missionManagerEvents.addListener(MissionManager.MissionEvent.ANIMAL_DEFEATED.name(), this::updateState);
        missionManagerEvents.addListener(MissionManager.MissionEvent.ANIMAL_EATEN.name(), this::updateState);
    }

    /**
     * Increments the number of hostiles the player has killed if the player has killed the correct
     * hostile type.
     * @param hostileType - the type of hostile killed.
     */
    private void updateState(EntityType hostileType) {
        if (hostileTypes.contains(hostileType) && ++numberOfHostilesKilled >= numberOfHostilesToBeKilled) {
            numberOfHostilesKilled = numberOfHostilesToBeKilled;
        }
        notifyUpdate();
    }

    /**
     * Checks whether the {@link ManageHostilesQuest} is complete.
     * @return - returns true iff the player has killed the required amount of hostiles
     */
    @Override
    public boolean isCompleted() {
        return numberOfHostilesKilled >= numberOfHostilesToBeKilled;
    }

    /**
     * Gets a description of the {@link ManageHostilesQuest}
     * @return - a description of the {@link ManageHostilesQuest}
     */
    @Override
    public String getDescription() {
        StringBuilder descriptionBuilder = new StringBuilder();

        descriptionBuilder.append("Manage the presence of hostile creatures on your farm.\n");
        descriptionBuilder.append("Deal with hostile creatures by defeating them with weapons, or let your Space Snappers eat them.\nDeal with ");
        descriptionBuilder.append(numberOfHostilesToBeKilled);
        descriptionBuilder.append(" creatures of type ");
        boolean isFirst = true;
        List<EntityType> hostilesList = new ArrayList<>(hostileTypes);
        hostilesList.sort(null);
        for (EntityType hostileType : hostilesList) {
            if (!isFirst) {
                descriptionBuilder.append(", ");
            }
            descriptionBuilder.append(switch (hostileType) {
                case OxygenEater -> "Oxygen Eater";
                default -> hostileType.toString();
            });
            isFirst = false;
        }
        descriptionBuilder.append(".\n");
        descriptionBuilder.append(getShortDescription());
        descriptionBuilder.append(".");

        return descriptionBuilder.toString();
    }

    /**
     * Gets a short description of the {@link ManageHostilesQuest} containing the player's quest progress.
     * @return - the short description of the players kill progress.
     */
    @Override
    public String getShortDescription() {
        return numberOfHostilesKilled + " out of " + numberOfHostilesToBeKilled + " hostiles dealt with";
    }

    /**
     * Reads in the progress of the {@link ManageHostilesQuest} from a {@link JsonValue}
     * @param progress The {@link JsonValue} representing the progress of the {@link Mission} as determined by the value
     *                 returned in {@link #getProgress()}.
     */
    @Override
    public void readProgress(JsonValue progress) {
        numberOfHostilesKilled = progress.asInt();
    }

    /**
     * Gets the {@link ManageHostilesQuest} progress
     * @return - the number of hostiles the player has killed since the quest creation
     */
    @Override
    public Object getProgress() {
        return numberOfHostilesKilled;
    }

    /**
     * Resets the state of the {@link ManageHostilesQuest} by setting the number of hostiles killed back to 0
     */
    @Override
    protected void resetState() {
        numberOfHostilesKilled = 0;
    }
}
