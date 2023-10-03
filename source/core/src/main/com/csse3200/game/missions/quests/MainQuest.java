package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.Mission;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;

import java.util.HashSet;
import java.util.Set;

/**
 * MainQuests are to represent the 3 main quests of the game as outlined in the storyline wiki.
 * These {@link MainQuest}s have a set of quests the player must complete and a timelimit.
 */
public class MainQuest extends Quest {

    /**
     * Names of the {@link Quest}s the player must complete.
     */
    private final Set<String> questsToComplete;

    /**
     * Names of the {@link Quest}s the player has already completed.
     */
    private final Set<String> questsCompleted;

    /**
     * Short {@link String} representation of what the player must complete.
     */
    private final String goal;

    /**
     * Creates a {@link MainQuest}
     * @param name - name of the {@link MainQuest}
     * @param reward - {@link Reward} player collects on completion
     * @param daysToExpiry - how many in-game days until {@link MainQuest} is expired
     * @param questsToComplete - set of {@link Quest} names for the player to complete
     * @param goal - short {@link String} representation of what the player must do
     */
    public MainQuest(String name, Reward reward, int daysToExpiry, Set<String> questsToComplete, String goal) {
        super(name, reward, daysToExpiry * 24, true);

        this.questsToComplete = questsToComplete;
        this.questsCompleted = new HashSet<>();
        this.goal = goal;
    }

    /**
     * Registers the {@link MainQuest} with the {@link MissionManager} by adding a listener to the
     * QUEST_REWARD_COLLECTED {@link com.csse3200.game.missions.MissionManager.MissionEvent}
     * @param missionManagerEvents A reference to the {@link EventHandler} on the
     *                             {@link MissionManager}, with which relevant events should be
     *                             listened to.
     */
    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        missionManagerEvents.addListener(MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), this::addQuest);
    }

    /**
     * If a {@link Quest} is completed that is in the questsToComplete set then it is added to the questsCompleted
     * @param questName - name of the {@link Quest} that was completed
     */
    private void addQuest(String questName) {
        if (questsToComplete.contains(questName)) {
            questsCompleted.add(questName);
        }
        notifyUpdate();
    }

    /**
     * Checks if the {@link MainQuest} is complete.
     * @return - true iff player has completed all of the questsToComplete as specific in constructor.
     */
    @Override
    public boolean isCompleted() {
        return questsCompleted.size() >= questsToComplete.size();
    }

    /**
     * Gets a description of the {@link MainQuest}.
     * @return - a description containing the goal and {@link Quest}s the player has yet to complete.
     */
    @Override
    public String getDescription() {
        StringBuilder descriptionBuilder = new StringBuilder();

        descriptionBuilder.append("You must ");
        descriptionBuilder.append(goal);
        descriptionBuilder.append("!\n");
        descriptionBuilder.append("Complete the quests: ");
        boolean isFirst = true;
        for (String questName : questsToComplete) {
            if (!questsCompleted.contains(questName)) {
                if (!isFirst) {
                    descriptionBuilder.append(", ");
                }
                descriptionBuilder.append(questName);
                isFirst = false;
            }
        }
        descriptionBuilder.append(".");

        return descriptionBuilder.toString();
    }

    /**
     * Gives a short description of the {@link MainQuest}'s progress
     * @return - description with how many {@link Quest}s are yet to be completed
     */
    @Override
    public String getShortDescription() {
        return (questsToComplete.size() - questsCompleted.size()) + " required quests to be completed";
    }

    /**
     * Reads the progress specified in the {@link JsonValue} into the questsCompleted
     * @param progress The {@link JsonValue} representing the progress of the {@link Mission} as determined by the value
     *                 returned in {@link #getProgress()}.
     */
    @Override
    public void readProgress(JsonValue progress) {
        resetState();
        questsCompleted.addAll(Set.of(progress.asStringArray()));
    }

    /**
     * Gives the current progress of the {@link MainQuest}.
     * @return - an {@link Array} containing the names of the {@link Quest}s that are completed
     */
    @Override
    public Object getProgress() {
        return questsCompleted.toArray();
    }

    /**
     * Resets the state of the {@link MainQuest} by clearing the questsCompleted set.
     */
    @Override
    protected void resetState() {
        questsCompleted.clear();
    }
}
