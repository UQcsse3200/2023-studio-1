package com.csse3200.game.components.ship;

import com.csse3200.game.components.Component;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.services.ServiceLocator;

/**
 * Handles Ship Debris events.
 *
 * Currently, triggers a DEBRIS_CLEARED event on the mission manager when destroyed.
 */
public class ShipDebrisComponent extends Component {
    private MissionManager missionManager;

    @Override
    public void create() {
        super.create();
        missionManager = ServiceLocator.getMissionManager();

        entity.getEvents().addListener("destroy", this::destroy);
    }

    /**
     * Trigger the mission manager's DEBRIS_CLEARED event then self destruct.
     */
    void destroy() {
        missionManager.getEvents().trigger(MissionManager.MissionEvent.DEBRIS_CLEARED.name());
        entity.dispose();
    }
}