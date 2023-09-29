package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;

import java.util.HashSet;
import java.util.Set;

public class MainQuest extends Quest {

    private final Set<String> questsToComplete;
    private final Set<String> questsCompleted;

    private final String goal;

    public MainQuest(String name, Reward reward, int daysToExpiry, Set<String> questsToComplete, String goal) {
        super(name, reward, daysToExpiry * 24, true);

        this.questsToComplete = questsToComplete;
        this.questsCompleted = new HashSet<>();
        this.goal = goal;
    }

    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        missionManagerEvents.addListener(MissionManager.MissionEvent.STORY_REWARD_COLLECTED.name(), this::addQuest);
    }

    private void addQuest(String questName) {
        if (questsToComplete.contains(questName)) {
            questsCompleted.add(questName);
        }
        notifyUpdate();
    }

    @Override
    public boolean isCompleted() {
        return questsCompleted.size() >= questsToComplete.size();
    }

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

    @Override
    public String getShortDescription() {
        return (questsToComplete.size() - questsCompleted.size()) + " required quests to be completed";
    }

    @Override
    public void readProgress(JsonValue progress) {
        resetState();
        questsCompleted.addAll(Set.of(progress.asStringArray()));
    }

    @Override
    public Object getProgress() {
        return questsCompleted.toArray();
    }

    @Override
    protected void resetState() {
        questsCompleted.clear();
    }
}
