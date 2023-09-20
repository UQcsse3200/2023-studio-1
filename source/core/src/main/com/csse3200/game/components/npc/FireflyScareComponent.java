package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.InteractionDetector;
import com.csse3200.game.services.ServiceLocator;

public class FireflyScareComponent extends Component {

    @Override
    public void update() {
        if (entity.getComponent(InteractionDetector.class).getEntitiesInRange().size() != 0) {
            // Die
            ServiceLocator.getGameArea().removeEntity(entity);
        }
    }
}
