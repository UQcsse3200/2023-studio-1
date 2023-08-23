package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;

/**
 * This class represents an animals' ability to be tamed by the user.
 *
 */

public class TamingComponent extends Component {
    /**
     * These thresholds represent the amount of points it takes
     * to tame the animals. Each animal will have a different threshold.
     */
    private final int threshold1 = 100;
    private final int threshold2 = 125;
    private final int threshold3 = 150;

    private Entity entity;


}
