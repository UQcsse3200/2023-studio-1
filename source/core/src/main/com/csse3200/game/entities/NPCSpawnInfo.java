package com.csse3200.game.entities;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;
import java.util.function.Function;

public class NPCSpawnInfo {
    private final int maxSpawnCount;
    private final Function<Entity, Entity> spawner;
    private final Entity player;
    private int growthRate;
    private int spawnCount;
    private final int spawnHour;
    private GameArea gameArea;
    private boolean hourReached;
    private final int randomRange;
    private int randomGoal;
    private int randomCount;

    public NPCSpawnInfo(int maxSpawnCount, Function<Entity, Entity> spawner, Entity player, int growthRate,
                        int initialSpawnCount, int spawnHour, int randomRange) {
        this.maxSpawnCount = maxSpawnCount;
        this.spawner = spawner;
        this.player = player;
        this.growthRate = growthRate;
        this.spawnCount = initialSpawnCount;
        this.spawnHour = spawnHour;
        this.randomRange = randomRange % 24;
        randomCount = 0;
        hourReached = false;
    }

    public void setGameArea(GameArea gameArea) {
        this.gameArea = gameArea;
    }

    public void startSpawner() {
        ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::hourUpdate);
    }

    public void hourUpdate() {
        //Spawn exactly on spawnHour, no randomisation
        if (randomRange == 0) {
            if (ServiceLocator.getTimeService().getHour() == spawnHour) {
                spawnNPC();
            }

        //Spawn on a random hour (in 'randomRange') after spawnHour
        } else if (hourReached){
            if (randomCount == randomGoal) {
                spawnNPC();

            //Increment randomCount on next hour once hourReached
            } else {
                randomCount++;
            }

        //Once at spawnHour, get randomGoal for next spawn
        } else if (ServiceLocator.getTimeService().getHour() == spawnHour) {
            hourReached = true;
            randomGoal = (int) (Math.random() * randomRange);
        }
    }

    public void spawnNPC() {
        //Reset for next spawn
        randomCount = 0;
        randomGoal = 0;
        hourReached = false;

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
        if (maxSpawnCount > (spawnCount + growthRate)) {
            spawnCount = (spawnCount + growthRate);
        }
    }
}
