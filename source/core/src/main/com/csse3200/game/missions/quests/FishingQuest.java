package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;

public class FishingQuest extends Quest {

    private final int target;

    private int progress;

    protected FishingQuest(String name, Reward reward, int fishToCatch) {
        super(name, reward);
        target = fishToCatch;
        progress = 0;
    }

    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        missionManagerEvents.addListener(MissionManager.MissionEvent.FISH.name(), this::updateState);
    }

    private void updateState() {
        if (++progress >= target) {
            progress = target;
        }
        notifyUpdate();
    }

    @Override
    public boolean isCompleted() {
        return progress >= target;
    }

    @Override
    public String getDescription() {
        return "Catching fish is a great food source and you can get hidden treasures!.\nGet items from fishing "
                + target + " times.\n" + getShortDescription() + ".";
    }

    @Override
    public String getShortDescription() {
        return progress + " out of " + target + " items gotten";
    }

    @Override
    public void readProgress(JsonValue progress) {
        this.progress = progress.asInt();
    }

    @Override
    public Object getProgress() {
        return progress;
    }

    @Override
    protected void resetState() {
        progress = 0;
    }
}
