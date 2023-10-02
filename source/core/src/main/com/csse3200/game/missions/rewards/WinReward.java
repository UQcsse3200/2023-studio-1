package com.csse3200.game.missions.rewards;

import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.services.ServiceLocator;

/**
 * A {@link Reward} that will trigger the win screen when collected.
 */
public class WinReward extends Reward {

    /**
     * Will trigger the winScreen event in the {@link MissionManager}
     */
    @Override
    public void collect() {
        setCollected();
        ServiceLocator.getMissionManager().getEvents().trigger("winScreen");
    }

}
