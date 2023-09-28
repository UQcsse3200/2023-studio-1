package com.csse3200.game.entities;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;
import java.util.function.Function;

public class NPCSpawnInfo {
    private static final double GROWTH_RATE = 1.2;
    private int maxSpawnCount;
    private Function<Entity, Entity> spawner;
    private Entity player;
    private double growthFactor;
    private int spawnCount;
    private int daysToNextSpawn;
    private int days;
    private int hoursToNextSpawn;
    private int hours;
    private GameArea gameArea;

    public NPCSpawnInfo(int maxSpawnCount, Function<Entity, Entity> spawner, Entity player, int growthFactor,
                        int initialSpawnCount, int daysToNextSpawn, int hoursToNextSpawn) {
        this.maxSpawnCount = maxSpawnCount;
        this.spawner = spawner;
        this.player = player;
        this.growthFactor = growthFactor;
        this.spawnCount = initialSpawnCount;
        this.daysToNextSpawn = daysToNextSpawn;
        this.hoursToNextSpawn = hoursToNextSpawn;
        days = 0;
        hours = 0;
    }

    public void setGameArea(GameArea gameArea) {
        this.gameArea = gameArea;
    }

    public void startSpawner() {
        ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::hourUpdate);
        ServiceLocator.getTimeService().getEvents().addListener("dayUpdate", this::dayUpdate);
    }

    public void hourUpdate() {
        if (days == daysToNextSpawn) {
            hours++;
            spawnNPC();
        }
    }

    public void dayUpdate() {
        days++;
        spawnNPC();

    }

    public void spawnNPC() {
        if (hours != hoursToNextSpawn || days != daysToNextSpawn) {
            return;
        }

        //Reset day and hour counters for next spawn
        hours = 0;
        days = 0;

        //Spawn entities
        for (int i = 0; i < spawnCount; i++) {
            //Get random traverseable tile to spawn the entity
            List<GridPoint2> traverseables = gameArea.getMap().getTraversableTileCoordinates();
            int randomTileIndex = (int) Math.floor(Math.random() * (traverseables.size() - 1));
            GridPoint2 position = traverseables.get(randomTileIndex);

            //Create entity and spawn on gameArea
            Entity entity = spawner.apply(player);
            gameArea.spawnEntityAt(entity, position, true, true);
        }

        //Apply growth rate
        growthFactor *= GROWTH_RATE;
        if (maxSpawnCount > (int) (spawnCount * growthFactor)) {
            spawnCount = (int) (spawnCount * growthFactor);
        }
    }
}
