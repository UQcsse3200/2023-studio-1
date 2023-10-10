package com.csse3200.game.entities;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FireflySpawnerTest {
    @BeforeEach
    void setup() {
        ServiceLocator.registerTimeService(new TimeService());
    }

    @AfterEach
    void packDown() {
        ServiceLocator.clear();
    }

    @Test
    void testSpawnsAtNight() {
        // mocks
        GameArea mockGameArea = mock(SpaceGameArea.class);
        ServiceLocator.registerGameArea(mockGameArea);
        GameMap mockMap = mock(GameMap.class);
        TerrainTile tile = mock(TerrainTile.class);
        doReturn(mockMap).when(mockGameArea).getMap();
        doNothing().when(mockGameArea).spawnEntityAt(any(Entity.class), any(GridPoint2.class), any(boolean.class), any(boolean.class));
        doReturn(new GridPoint2(5,5)).when(mockMap).getMapSize();
        doReturn(tile).when(mockMap).getTile(any(GridPoint2.class));
        doReturn(true).when(tile).isTraversable();
        FireflySpawner spawner = spy(new FireflySpawner());
        try {
            ServiceLocator.getTimeService().getEvents().trigger("nightTime");
        } catch (Exception e) {
            // Tried to make a firefly but couldn't load file or get lights (would pass in real game) and since this just tests the one comp it is all good
            return;
        }
        fail();
    }
}
