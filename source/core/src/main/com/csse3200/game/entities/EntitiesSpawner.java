package com.csse3200.game.entities;

import com.csse3200.game.areas.GameArea;

import java.util.List;

/**
 * Handles periodic spawning of multiple entities
 */
public class EntitiesSpawner {
    /**
     * List of NPCSpawnInfo objects that correspond to the entities to spawn
     */
    private final List<EntitySpawner> toSpawn;

    /**
     * Constructor for EntitiesSpawner
     *
     * @param toSpawn list of NPCSpawnInfo objects that correspond to the entities to spawn
     */
    public EntitiesSpawner(List<EntitySpawner> toSpawn) {
        this.toSpawn = toSpawn;
    }

    /**
     * Set GameArea for all spawners
     *
     * @param gameArea
     */
    public void setGameAreas(GameArea gameArea) {
        for (EntitySpawner entitySpawner : toSpawn) {
            entitySpawner.setGameArea(gameArea);
        }
    }

    /**
     * Spawns entities from all spawners without considering triggers
     */
    public void spawnNow() {
        for (EntitySpawner entitySpawner : toSpawn) {
            entitySpawner.spawnEntities();
        }
    }

    /**
     * Start periodic spawning for all spawners
     */
    public void startPeriodicSpawning() {
        for (EntitySpawner entitySpawner : toSpawn) {
            entitySpawner.startSpawner();
        }
    }
}
