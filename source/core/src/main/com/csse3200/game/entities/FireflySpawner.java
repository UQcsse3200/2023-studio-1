package com.csse3200.game.entities;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.services.ServiceLocator;

import java.security.SecureRandom;

public class FireflySpawner {

    private static final int MAX_START_FIREFLIES = 75;

    private int mapSizeX;

    private int mapSizeY;

    private SecureRandom random;

    public FireflySpawner() {
        GridPoint2 mapSize = ServiceLocator.getGameArea().getMap().getMapSize();
        mapSizeX = mapSize.x;
        mapSizeY = mapSize.y;

        random = new SecureRandom();

        ServiceLocator.getTimeService().getEvents().addListener("nightTime", this::startSpawning);
    }

    private void startSpawning() {
        for (int i = 0; i < MAX_START_FIREFLIES; i++) {
            spawnFirefly();
        }
    }

    private void spawnFirefly() {
        GridPoint2 randomGrid;
        do {
            randomGrid = new GridPoint2(random.nextInt(mapSizeX), random.nextInt(mapSizeY));
        } while (!ServiceLocator.getGameArea().getMap().getTile(randomGrid).isTraversable());
        ServiceLocator.getGameArea().spawnEntityAt(NPCFactory.createFireFlies(null), randomGrid, true, true);
    }
}
