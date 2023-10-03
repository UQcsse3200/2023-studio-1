package com.csse3200.game.missions.rewards;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(GameExtension.class)
class EntityRewardTest {

    private List<Entity> entities1, empty;
    private EntityReward r1, r2;


    @BeforeEach
    void beforeTest() {
        GameArea gameArea = mock(SpaceGameArea.class);
        ServiceLocator.registerGameArea(gameArea);

        Entity player = mock(Entity.class);
        when(ServiceLocator.getGameArea().getPlayer()).thenReturn(player);
        Vector2 playerPosition = mock(Vector2.class);
        when(ServiceLocator.getGameArea().getPlayer().getPosition()).thenReturn(playerPosition);

        entities1 = List.of(
                mock(Entity.class),
                mock(Entity.class),
                mock(Entity.class)
        );

        empty = new ArrayList<>();

        // Create EntityReward objects
        r1 = new EntityReward(entities1);
        r2 = new EntityReward(empty);
    }

    @AfterEach
    void afterTest() {
        ServiceLocator.clear();
    }

    @Test
    public void testCollect() {
        // Ensure EntityReward is not collected initially
        assertFalse(r1.isCollected());
        assertFalse(r2.isCollected());

        r1.collect();
        r2.collect();

        assertTrue(r1.isCollected());
        assertTrue(r2.isCollected());

        Vector2 playerPosition = ServiceLocator.getGameArea().getPlayer().getPosition();
        GridPoint2 playerPositionGrid = new GridPoint2((int) playerPosition.x, (int) playerPosition.y);
        for (Entity e : entities1) {
            verify(ServiceLocator.getGameArea()).spawnEntityAt(e, playerPositionGrid, true, true);
        }
    }
}
