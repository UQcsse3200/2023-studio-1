package com.csse3200.game.components.ship;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClueComponent extends Component {

    // TODO: update to a proper list of desired locations
    private static final List<Vector2> possibleLocations = new ArrayList<>(Arrays.asList(
            new Vector2(7, 7),
            new Vector2(12, 12),
            new Vector2(32, 32)
    ));

    private final Vector2 currentLocation;

    public ClueComponent() {
        currentLocation = possibleLocations.get(0);
        Collections.rotate(possibleLocations, 1);
    }

    public void create() {
        super.create();
        entity.getEvents().addListener("destroy", this::destroy);
    }

    public Vector2 getCurrentLocation() {
        return currentLocation;
    }

    void destroy() {
        ServiceLocator.getGameArea().getPlayer().getComponent(InventoryComponent.class).removeItem(entity);
        entity.dispose();
    }
}