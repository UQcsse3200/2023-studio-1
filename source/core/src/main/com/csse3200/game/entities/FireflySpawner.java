package com.csse3200.game.entities;

import java.security.SecureRandom;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.services.ServiceLocator;

public class FireflySpawner {

    /**
     * The amount of fireflies to spawn in
     */
    private static final int MAX_FIREFLIES = 75;

    /**
     * The width of the map
     */
    private int mapSizeX;

    /**
     * The length of the map
     */
    private int mapSizeY;

    /**
     * Used to get random variables
     */
    private SecureRandom random;

    /**
     * Constructor for the spawner, making this will call startSpawning at nightTime event
     * triggered by TimeService
     */
    public FireflySpawner() {
        GridPoint2 mapSize = ServiceLocator.getGameArea().getMap().getMapSize();
        mapSizeX = mapSize.x;
        mapSizeY = mapSize.y;

        random = new SecureRandom();

        ServiceLocator.getTimeService().getEvents().addListener("nightTime", this::startSpawning);
        ServiceLocator.getGameArea().getClimateController().getEvents().addListener("acidShower", this::startSpawning);
    }

    /**
     * Starts to spawn the fireflies
     * Spawns MAX_FIREFLIES fireflies
     */
    private void startSpawning() {
        for (int i = 0; i < MAX_FIREFLIES; i++) {
            spawnFirefly();
        }
    }

    /**
     * Spawns a firefly at a random traversable grid tile
     */
    private void spawnFirefly() {
        GridPoint2 randomGrid;
        do {
            randomGrid = new GridPoint2(random.nextInt(mapSizeX), random.nextInt(mapSizeY));
        } while (!ServiceLocator.getGameArea().getMap().getTile(randomGrid).isTraversable());
        ServiceLocator.getGameArea().spawnEntityAt(NPCFactory.createFireFlies(), randomGrid, true, true);
    }
}
