package com.csse3200.game.missions.rewards;

import com.csse3200.game.services.ServiceLocator;

public class WinReward extends Reward {

    @Override
    public void collect() {
        ServiceLocator.getMissionManager().getEvents().trigger("winScreen");
    }

}
