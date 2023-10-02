package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;

public class TameAnimalsQuest extends Quest {

    private final int numberOfAnimalsToTame;
    private int numberOfAnimalsTamed;

    public TameAnimalsQuest(String name, Reward reward, int numberOfAnimalsToTame) {
        super(name, reward);

        this.numberOfAnimalsToTame = Math.max(0, numberOfAnimalsToTame);
        this.numberOfAnimalsTamed = 0;
    }

    public TameAnimalsQuest(String name, Reward reward, int expiryDuration, int numberOfAnimalsToTame) {
        super(name, reward, expiryDuration, false);

        this.numberOfAnimalsToTame = Math.max(0, numberOfAnimalsToTame);
        this.numberOfAnimalsTamed = 0;
    }

    @Override
    public void registerMission(EventHandler missionManagerEvents) {
        missionManagerEvents.addListener(MissionManager.MissionEvent.ANIMAL_TAMED.name(), this::updateState);
    }

    private void updateState() {
        if (++numberOfAnimalsTamed >= numberOfAnimalsToTame) {
            numberOfAnimalsTamed = numberOfAnimalsToTame;
        }
        notifyUpdate();
    }

    @Override
    public boolean isCompleted() {
        return numberOfAnimalsTamed >= numberOfAnimalsToTame;
    }

    @Override
    public String getDescription() {
        return "Taming certain animals yields special drops.\n" + "Tame " + numberOfAnimalsToTame +
                " tameable animals by feeding them plants.\n" + getShortDescription() + ".";
    }

    @Override
    public String getShortDescription() {
        return numberOfAnimalsTamed + " out of " + numberOfAnimalsToTame + " animals tamed";
    }

    @Override
    public void readProgress(JsonValue progress) {
        numberOfAnimalsTamed = progress.asInt();
    }

    @Override
    public Object getProgress() {
        return numberOfAnimalsTamed;
    }

    @Override
    protected void resetState() {
        numberOfAnimalsTamed = 0;
    }

}
