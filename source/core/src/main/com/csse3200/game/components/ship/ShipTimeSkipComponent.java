package com.csse3200.game.components.ship;

import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceLocator;

/**
 * Handles allowing the player to sleep through the night by interacting
 * with the ship once the repair threshold is reached.
 */
public class ShipTimeSkipComponent extends Component {
    private boolean unlocked;
    private static final int REPAIR_THRESHOLD = 3;

    @Override
    public void create() {
        super.create();
        unlocked = true;

        entity.getEvents().addListener("progressUpdated", this::progressUpdated);
        entity.getEvents().addListener("interact", this::triggerTimeSkip);
        ServiceLocator.getTimeService().getEvents().addListener("morningTime", this::stopSleeping);
    }

    /**
     * If the ship has enough repairs, unlock the time skip functionality
     * @param repairsMade number of repairs on the Ship Entity
     */
    private void progressUpdated(int repairsMade) {
        if (repairsMade >= REPAIR_THRESHOLD) {
            unlocked = true;
        }
    }

    /**
     * If time skip is unlocked, trigger the sleep function on the time service.
     */
    private void triggerTimeSkip() {
        if (unlocked) {
            ServiceLocator.getTimeSource().setTimeScale(100f);
        }
    }
    /**
     * If time skip is unlocked, return to normal speed during the morning.
     */
    private void stopSleeping() {
        if (unlocked) {
            ServiceLocator.getTimeSource().setTimeScale(1f);
        }
    }
}
