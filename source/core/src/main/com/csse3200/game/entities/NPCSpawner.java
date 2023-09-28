package com.csse3200.game.entities;

import com.csse3200.game.areas.GameArea;

import java.util.List;

public class NPCSpawner {
    private List<NPCSpawnInfo> toSpawn;

    //Give list of animals to spawn
    public NPCSpawner(List<NPCSpawnInfo> toSpawn) {
        this.toSpawn = toSpawn;
    }

    public void setGameAreas(GameArea gameArea) {
        for (NPCSpawnInfo spawnInfo : toSpawn) {
            spawnInfo.setGameArea(gameArea);
        }
    }

    //Begins the spawning behaviour
    public void startSpawning() {
        for (NPCSpawnInfo spawnInfo : toSpawn) {
            spawnInfo.startSpawner();
        }
    }
}
