package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;

public class ShipRepairQuest extends Quest {

    private final int targetShipRepairProgress;
    private int currentShipRepairProgress;

    public ShipRepairQuest(String name, Reward reward, int targetShipRepairProgress) {
        super(name, reward);

        this.targetShipRepairProgress = Math.max(0, targetShipRepairProgress);
        this.currentShipRepairProgress = 0;
    }

    public ShipRepairQuest(String name, Reward reward, int expiryDuration, int targetShipRepairProgress) {
        super(name, reward, expiryDuration, false);

        this.targetShipRepairProgress = Math.max(0, targetShipRepairProgress);
        this.currentShipRepairProgress = 0;
    }

    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        missionManagerEvents.addListener(MissionManager.MissionEvent.SHIP_PART_ADDED.name(), this::updateState);
    }

    private void updateState() {
        if (++currentShipRepairProgress >= targetShipRepairProgress) {
            currentShipRepairProgress = targetShipRepairProgress;
        }
        notifyUpdate();
    }

    @Override
    public boolean isCompleted() {
        return currentShipRepairProgress >= targetShipRepairProgress;
    }

    @Override
    public String getDescription() {
        return "Repair your ship and unlock useful features.\nAdd scavenged " + targetShipRepairProgress
                + " parts from your ship's hull to your ship.\n" + getShortDescription() + ".";
    }

    @Override
    public String getShortDescription() {
        return currentShipRepairProgress + " out of " + targetShipRepairProgress + " additional ship parts to be added";
    }

    @Override
    public void readProgress(JsonValue progress) {
        currentShipRepairProgress = progress.asInt();
    }

    @Override
    public Object getProgress() {
        return currentShipRepairProgress;
    }

    @Override
    protected void resetState() {
        currentShipRepairProgress = 0;
    }

}
