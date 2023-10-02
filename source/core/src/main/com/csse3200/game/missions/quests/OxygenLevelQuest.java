package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.rewards.Reward;
import com.csse3200.game.services.OxygenLevel;
import com.csse3200.game.services.ServiceLocator;

public class OxygenLevelQuest extends Quest {

    private final OxygenLevel oxygenLevel;
    private final String oxygenLevelDescription;

    private final int hourTarget;
    private int currentHour;

    private final int targetOxygenLevel;

    public OxygenLevelQuest(String name, Reward reward,
                            OxygenLevel oxygenLevel, String oxygenLevelDescription,
                            int hourTarget, int targetOxygenLevel) {
        super(name, reward);

        this.oxygenLevel = oxygenLevel;
        this.oxygenLevelDescription = oxygenLevelDescription;

        this.hourTarget = Math.max(hourTarget, 0);
        this.currentHour = 0;
        this.targetOxygenLevel = targetOxygenLevel;
    }

    public OxygenLevelQuest(String name, Reward reward, int expiryDuration, OxygenLevel oxygenLevel,
                            String oxygenLevelDescription, int hourTarget, int targetOxygenLevel) {
        super(name, reward, expiryDuration, false);

        this.oxygenLevel = oxygenLevel;
        this.oxygenLevelDescription = oxygenLevelDescription;

        this.hourTarget = Math.max(hourTarget, 0);
        this.currentHour = 0;
        this.targetOxygenLevel = targetOxygenLevel;
    }

    @Override
    public void registerMission(EventHandler ignored) {
        ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::updateState);
    }

    private void updateState() {
        currentHour++;
        notifyUpdate();
    }

    @Override
    public boolean isCompleted() {
        return currentHour >= hourTarget && oxygenLevel.getOxygenPercentage() >= targetOxygenLevel;
    }

    @Override
    public String getDescription() {
        return "Oxygen is key for human survival.\n" + getShortDescription() + ".";
    }

    @Override
    public String getShortDescription() {
        int timeRemaining = Math.max(hourTarget - currentHour, 0);
        return "Get " + oxygenLevelDescription + " to be greater than " + targetOxygenLevel + "% in " +
                (timeRemaining >= 24 ? timeRemaining / 24 + " days" : timeRemaining  + " hours" );
    }

    @Override
    public void readProgress(JsonValue progress) {
        currentHour = progress.asInt();
    }

    @Override
    public Object getProgress() {
        return currentHour;
    }

    @Override
    protected void resetState() {
        currentHour = 0;
    }

}
