package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.rewards.Reward;

public class AutoQuest extends Quest {

    private final String description;

    public AutoQuest(String name, Reward reward, String description) {
        super(name, reward);
        this.description = description;
    }

    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        collectReward();
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getShortDescription() {
        return description;
    }

    @Override
    public void readProgress(JsonValue progress) {
    }

    @Override
    public Object getProgress() {
        return 0;
    }

    @Override
    protected void resetState() {
    }

}
