package com.csse3200.game.components.npc;

import com.csse3200.game.areas.weather.BlizzardEvent;
import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceLocator;

public class FireflyScareComponent extends Component {

    @Override
    public void update() {
        // If it is day and not a blizzard, kill the entity
        if (ServiceLocator.getTimeService().isDay() &&
                !(ServiceLocator.getGameArea().getClimateController().getCurrentWeatherEvent() instanceof BlizzardEvent)) {
            // Die
            ServiceLocator.getGameArea().removeEntity(entity);
        }
    }
}
