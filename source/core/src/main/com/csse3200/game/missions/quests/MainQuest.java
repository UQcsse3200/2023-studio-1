package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.MultiReward;

import java.util.ArrayList;
import java.util.List;

public class MainQuest extends Quest {

    private final List<String> questsToComplete;
    private final List<String> questsCompleted;

    public MainQuest(String name, List<String> questsToComplete, MultiReward reward, int daysToExpiry) {
        super(name, reward, daysToExpiry * 24, true);

        this.questsToComplete = questsToComplete;
        this.questsCompleted = new ArrayList<>();
    }

    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        missionManagerEvents.addListener(MissionManager.MissionEvent.MISSION_COMPLETE.name(), this::addQuest);
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
        return null;
    }

    @Override
    public String getShortDescription() {
        return null;
    }

    @Override
    public void readProgress(JsonValue progress) {
        resetState();
        questsCompleted.addAll(List.of(progress.asStringArray()));
    }

    @Override
    public Object getProgress() {
        return questsCompleted;
    }

    @Override
    protected void resetState() {
        questsCompleted.clear();
    }
}
