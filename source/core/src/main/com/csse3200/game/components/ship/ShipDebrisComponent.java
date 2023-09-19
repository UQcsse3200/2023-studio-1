package com.csse3200.game.components.ship;

import com.csse3200.game.components.Component;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.services.ServiceLocator;

public class ShipDebrisComponent extends Component {
    private MissionManager missionManager;

    @Override
    public void create() {
        super.create();
        missionManager = ServiceLocator.getMissionManager();

        entity.getEvents().addListener("destroy", this::destroy);
    }

    void destroy() {
        missionManager.getEvents().trigger(MissionManager.MissionEvent.DEBRIS_CLEARED.name());
        entity.dispose();
    }
}