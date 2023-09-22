package com.csse3200.game.components;

import com.csse3200.game.services.ServiceLocator;

/**
 * This class is only for Light Placeables will be moved into that folder when
 * the rest of my team catches up and finishes their placeables lol
 */
public class LightController extends Component {

    @Override
    public void update() {
        // Would've liked to use the events but won't work if you place during the night
        if (ServiceLocator.getTimeService().isDay()) {
            turnOff();
        } else {
            turnOn();
        }
    }


    private void turnOn() {
        AuraLightComponent light = entity.getComponent(AuraLightComponent.class);
        if (light != null) {
            if (!light.getActive()) {
                light.toggleLight();
            }
        }
    }

    private void turnOff() {
        AuraLightComponent light = entity.getComponent(AuraLightComponent.class);
        if (light != null) {
            if (light.getActive()) {
                light.toggleLight();
            }
        }
    }
}
