package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.Mission;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A {@link Quest} where the player has to interact with a certain amount of plants.
 */
public class PlantInteractionQuest extends Quest {

    /**
     * Type of interaction or {@link com.csse3200.game.missions.MissionManager.MissionEvent}
     */
    private final MissionManager.MissionEvent interactionType;

    /**
     * Set of names of the plant types the player should interact with.
     */
    private final Set<String> plantTypes;

    /**
     * Number of interactions the player needs to complete the {@link PlantInteractionQuest}
     */
    private final int interactionsTarget;

    /**
     * Number of plant interaction the player has already had
     */
    private int numberOfInteractions;


    /**
     * Creates a {@link PlantInteractionQuest}
     * @param name Quest name
     * @param reward Reward the player can collect once complete
     * @param interactionType Type of plant interaction
     * @param plantTypes Plant types the player can interact with
     * @param interactionsTarget Number of interactions the player needs to complete the quest
     */
    public PlantInteractionQuest(String name, Reward reward, MissionManager.MissionEvent interactionType,
                                 Set<String> plantTypes, int interactionsTarget) {
        super(name, reward);

        this.interactionType = interactionType;
        this.plantTypes = plantTypes;
        this.interactionsTarget = Math.max(interactionsTarget, 0);
        this.numberOfInteractions = 0;
    }

    /**
     * Creates a {@link PlantInteractionQuest} with an expiry
     * @param name Quest name
     * @param reward Reward the player can collect once complete
     * @param expiryDuration How long until the Quest will expire
     * @param interactionType Type of plant interaction
     * @param plantTypes Plant types the player can interact with
     * @param interactionsTarget Number of interactions the player needs to complete
     */
    public PlantInteractionQuest(String name, Reward reward, int expiryDuration,
                                 MissionManager.MissionEvent interactionType, Set<String> plantTypes,
                                 int interactionsTarget) {
        super(name, reward, expiryDuration, false);

        this.interactionType = interactionType;
        this.plantTypes = plantTypes;
        this.interactionsTarget = Math.max(interactionsTarget, 0);
        this.numberOfInteractions = 0;
    }

    /**
     * Registers the {@link PlantInteractionQuest} with the {@link MissionManager} by adding listeners to the
     * interaction type.
     * @param missionManagerEvents A reference to the {@link EventHandler} on the
     *                             {@link MissionManager}, with which relevant events should be
     *                             listened to.
     */
    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        missionManagerEvents.addListener(interactionType.name(), this::updateState);
    }

    /**
     * Updates the state by incrementing the number of plant interactions the player has made
     * if its the correct plant type.
     * @param plantType The plant type
     */
    private void updateState(String plantType) {
        if (plantTypes.contains(plantType) && ++numberOfInteractions >= interactionsTarget) {
            numberOfInteractions = interactionsTarget;
        }
        notifyUpdate();
    }

    /**
     * Checks if {@link PlantInteractionQuest} is complete
     * @return True iff player has completed at least the amount of target interactions
     */
    @Override
    public boolean isCompleted() {
        return numberOfInteractions >= interactionsTarget;
    }

    /**
     * Description of the {@link PlantInteractionQuest}
     * @return - Long description of the {@link PlantInteractionQuest} with player progress
     */
    @Override
    public String getDescription() {
        StringBuilder descriptionBuilder = new StringBuilder();

        switch (interactionType) {
            case PLANT_CROP -> descriptionBuilder.append("Plant ");
            case HARVEST_CROP -> descriptionBuilder.append("Harvest ");
            default -> {
                // This should not occur - if it does, do nothing
            }
        }
        descriptionBuilder.append(interactionsTarget);
        descriptionBuilder.append(" crops of type ");
        boolean isFirst = true;
        List<String> plantList = new ArrayList<>(plantTypes);
        plantList.sort(null);
        for (String plantType : plantList) {
            if (!isFirst) {
                descriptionBuilder.append(", ");
            }
            descriptionBuilder.append(plantType);
            isFirst = false;
        }
        descriptionBuilder.append(".\n");
        descriptionBuilder.append(getShortDescription());
        descriptionBuilder.append(".");

        return descriptionBuilder.toString();
    }

    /**
     * Short description of the {@link PlantInteractionQuest}
     * @return - short description of the players plant interaction progress
     */
    @Override
    public String getShortDescription() {
        return numberOfInteractions + " out of " + interactionsTarget + switch (interactionType) {
            case PLANT_CROP -> " crops planted";
            case HARVEST_CROP -> " crops harvested";
            default -> "";
        };
    }

    /**
     * Reads in the current {@link PlantInteractionQuest} from a {@link JsonValue}
     * @param progress The {@link JsonValue} representing the progress of the {@link Mission} as determined by the value
     *                 returned in {@link #getProgress()}.
     */
    @Override
    public void readProgress(JsonValue progress) {
        numberOfInteractions = progress.asInt();
    }

    /**
     * Gets the {@link PlantInteractionQuest}
     * @return Number of plant interactions the player has made
     */
    @Override
    public Object getProgress() {
        return numberOfInteractions;
    }

    /**
     * Resets the number of plant interactions to 0
     */
    @Override
    protected void resetState() {
        numberOfInteractions = 0;
    }
}
