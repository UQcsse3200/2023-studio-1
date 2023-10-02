package com.csse3200.game.components.placeables;

import com.csse3200.game.components.AuraLightComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
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


    public boolean turnOn() {
        AuraLightComponent light = entity.getComponent(AuraLightComponent.class);
        if (light != null) {
            if (!light.getActive()) {
                light.toggleLight();
                entity.getComponent(AnimationRenderComponent.class).startAnimation("light_on");
            }
        }
        return true;
    }

    public boolean turnOff() {
        AuraLightComponent light = entity.getComponent(AuraLightComponent.class);
        if (light != null) {
            if (light.getActive()) {
                light.toggleLight();
                entity.getComponent(AnimationRenderComponent.class).startAnimation("light_off");
            }
        }
        return true;
    }
}
