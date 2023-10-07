package com.csse3200.game.components;

import com.csse3200.game.components.placeables.LightController;
import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LightControllerTest {

    @Test
    void turnOnOff() {
        Entity lightEntity = new Entity().addComponent(new LightController());

        assertTrue(lightEntity.getComponent(LightController.class).turnOn());
        assertTrue(lightEntity.getComponent(LightController.class).turnOff());
    }
}