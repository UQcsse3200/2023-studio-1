package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.Mission;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;

/**
 * FertiliseCropTilesQuest class is a Quest that tracks the number of CropTiles have been fertilised.
 */
public class FertiliseCropTilesQuest extends Quest {

    /**
     * Number of cropTileComponents the player has to fertilise to complete the Quest
     */
    private final int target;

    /**
     * Number of cropTileComponents the player has currently fertilised.
     */
    private int numberOfTilesFertilised;

    /**
     * Creates a {@link FertiliseCropTilesQuest}
     * @param name - the name of the {@link FertiliseCropTilesQuest}
     * @param reward - the Reward a player will receive after completing the Quest
     * @param numberOfTilesToFertilise - the number of cropTileComponents the player has currently fertilised
     */
    public FertiliseCropTilesQuest(String name, Reward reward, int numberOfTilesToFertilise) {
        super(name, reward);

        this.target = Math.max(numberOfTilesToFertilise, 0);
        numberOfTilesFertilised = 0;
    }

    /**
     * Creates a {@link FertiliseCropTilesQuest}.
     * @param name The name of the {@link FertiliseCropTilesQuest}
     * @param reward The {@link Reward} for completion of the {@link Quest}
     * @param expiryDuration The time limit to complete the {@link Quest} once accepted
     * @param numberOfTilesToFertilise The number of crop tiles to fertilise before completing the
     *                                 {@link Quest}
     */
    public FertiliseCropTilesQuest(String name, Reward reward, int expiryDuration, int numberOfTilesToFertilise) {
        super(name, reward, expiryDuration, false);

        this.target = Math.max(numberOfTilesToFertilise, 0);
        numberOfTilesFertilised = 0;
    }

    /**
     * Registers the {@link FertiliseCropTilesQuest} with the {@link com.csse3200.game.missions.Mission} by
     * no listening to the FERTILISE_CROP event.
     * @param missionManagerEvents A reference to the {@link EventHandler} on the
     *                             {@link MissionManager}, with which relevant events should be
     *                             listened to.
     */
    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        missionManagerEvents.addListener(
                MissionManager.MissionEvent.FERTILISE_CROP.name(),
                this::updateState);
    }

    /**
     * Returns true if the number of crop tiles the player has fertilised is greater than or equal
     * to the target number of tiles.
     * @return True if the player has fertilised the target number of fertilised tiles.
     */
    @Override
    public boolean isCompleted() {
        return numberOfTilesFertilised >= target;
    }

    /**
     * The description of the Quest to give a representation of the player's progress in the Quest.
     * @return - Human-readable description of the {@link FertiliseCropTilesQuest}.
     */
    @Override
    public String getDescription() {
        return "Fertilising crop tiles will cause your plants to grow faster.\nApply fertiliser to "
                + target + " tiles.\n" + getShortDescription() + ".";
    }

    /**
     * Gives the player's progress in the {@link FertiliseCropTilesQuest}.
     * @return - Human-readable String of how many cropTiles the player has fertilised and how much they have left to
     * clear.
     */
    @Override
    public String getShortDescription() {
        return numberOfTilesFertilised + " out of " + target + " crop tiles fertilised";
    }

    /**
     * Read in the amount of debris the player has cleared from a JsonValue.
     * @param progress The {@link JsonValue} representing the progress of the {@link Mission} as determined by the value
     *                 returned in {@link #getProgress()}.
     */
    @Override
    public void readProgress(JsonValue progress) {
        numberOfTilesFertilised = progress.asInt();
    }

    /**
     * Get the number of cropTiles the player has fertilised since Quest was registered.
     * @return - number of cropTiles fertilised
     */
    @Override
    public Object getProgress() {
        return numberOfTilesFertilised;
    }

    /**
     * Increments the number of crop tiles the player has fertilised.
     */
    private void updateState() {
        if (++numberOfTilesFertilised >= target) {
            numberOfTilesFertilised = target;
        }
        notifyUpdate();
    }

    /**
     * Resets the number of cropTiles the player has fertilised to 0.
     */
    @Override
    protected void resetState() {
        numberOfTilesFertilised = 0;
    }

}
