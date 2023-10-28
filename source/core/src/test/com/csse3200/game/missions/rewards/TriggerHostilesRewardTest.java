package com.csse3200.game.missions.rewards;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.entities.EntitiesSpawner;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntitySpawner;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class TriggerHostilesRewardTest {

    TriggerHostilesReward triggerHostilesReward;
    SpaceGameArea spaceGameArea;

    Entity player;
    GameMap gameMap;
    GridPoint2 playerPos;

    EntitiesSpawner hostileSpawner;
    TerrainTile terrainTile;



    @BeforeEach
    void init() {
        spaceGameArea = mock(SpaceGameArea.class);
        ServiceLocator.registerGameArea(spaceGameArea);

        player = mock(Entity.class);
        when(spaceGameArea.getPlayer()).thenReturn(player);
        when(spaceGameArea.getPlayer().getCenterPosition()).thenReturn(new Vector2(0,0));

        gameMap = mock(GameMap.class);
        when(spaceGameArea.getMap()).thenReturn(gameMap);
        terrainTile = mock(TerrainTile.class);
        when(gameMap.getTile(any(GridPoint2.class))).thenReturn(terrainTile);
        when(terrainTile.isTraversable()).thenReturn(false).thenReturn(true);

        playerPos = new GridPoint2(0,0);
        when(spaceGameArea.getMap().vectorToTileCoordinates(spaceGameArea.getPlayer().getCenterPosition())).thenReturn(playerPos);

        triggerHostilesReward = new TriggerHostilesReward(List.of(mock(Entity.class)));

        // Create a mock EntitySpawner
        EntitySpawner entitySpawner = mock(EntitySpawner.class);

        // Create a List containing the mock EntitySpawner
        List<EntitySpawner> entitySpawnerList = List.of(entitySpawner);

        hostileSpawner = mock(EntitiesSpawner.class);

        // Configure gameArea to return the hostileSpawner
        when(spaceGameArea.getHostileSpawner()).thenReturn(hostileSpawner);
    }

    @Test
    void testCollect() {
        triggerHostilesReward.collect();
        SpaceGameArea gameArea = (SpaceGameArea) ServiceLocator.getGameArea();
        SpaceGameArea spaceGameArea = (SpaceGameArea) gameArea;
        EntitiesSpawner gameAreaHostileSpawner = spaceGameArea.getHostileSpawner();
        verify(gameAreaHostileSpawner).startPeriodicSpawning();    }
}
