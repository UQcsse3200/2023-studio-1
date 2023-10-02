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
    private static final List<Vector2> possibleLocations = new ArrayList<>(Arrays.asList(
            new Vector2(30, 85),
            new Vector2(20, 60),
            new Vector2(30, 75),
            new Vector2(20, 40),
            new Vector2(20, 75),
            new Vector2(30, 40),
            new Vector2(30, 60)
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