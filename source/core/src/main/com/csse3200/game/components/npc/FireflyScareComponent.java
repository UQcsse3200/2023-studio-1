package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceLocator;

public class FireflyScareComponent extends Component {

    @Override
    public void update() {
        if (ServiceLocator.getTimeService().isDay()) {
            // Die
            ServiceLocator.getGameArea().removeEntity(entity);
        }
    }
}
