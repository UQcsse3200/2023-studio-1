package com.csse3200.game.components.placeables;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


// TODO: the bitmap will have to be tested in connEntityUtil.

@ExtendWith(GameExtension.class)
public class SprinklerComponentTest {
    private static Entity s1;
    private static Entity p1;
    private static final Vector2 s1_pos = new Vector2(2, 2);
    private static final Vector2 p1_pos = new Vector2(3, 2);
    private static TerrainTile sTile;  // tile where sprinkler is placed
    private static TerrainTile pTile;  // tile where pump is placed
    private static GameArea gameArea;
    private static GameMap gameMap;
    private static TimeService timeService;

    @BeforeAll
    public static void config() {
    }

    @BeforeEach
    public void setup() {
        // TODO could move a lot of this into beforeAll.
        // create game area and map
        gameArea = mock(GameArea.class);
        gameMap = mock(GameMap.class);
        when(gameArea.getMap()).thenReturn(gameMap);
        timeService = mock(TimeService.class);
        when(timeService.getEvents()).thenReturn(new EventHandler());
        ServiceLocator.registerGameArea(gameArea);
        ServiceLocator.registerTimeService(timeService);

        // Create tiles for test area
        /* We get a test area that looks like this:
         *   x x x x x x
         *   x x x x x x
         *   x x S P x x
         *   x x x x x x
         *   x x x x x x
         * with: x = empty, S = sprinkler, P = pump */
        sTile = new TerrainTile(null, TerrainTile.TerrainCategory.DIRT);
        pTile = new TerrainTile(null, TerrainTile.TerrainCategory.DIRT);
        TerrainTile emptyTile = new TerrainTile(null, TerrainTile.TerrainCategory.DIRT);
        for (int x = 0; x <= 6; x++) {
            for (int y = 0; y <= 5; y++) {
                Vector2 pos = new Vector2(x, y);
                if (pos.equals(s1_pos)) {
                    when(gameMap.getTile(pos)).thenReturn(sTile);
                } else if (pos.equals(p1_pos)) {
                    when(gameMap.getTile(pos)).thenReturn(pTile);
                } else {
                    when(gameMap.getTile(pos)).thenReturn(emptyTile);
                }
            }
        }

        // create sprinkler and pump entities.
        DynamicTextureRenderComponent dtrc = mock(DynamicTextureRenderComponent.class);
        s1 = new Entity(EntityType.SPRINKLER)
                .addComponent(new SprinklerComponent())
                .addComponent(dtrc);

        p1 = new Entity(EntityType.PUMP)
                .addComponent(new SprinklerComponent())
                .addComponent(dtrc);

        // before each test, ensure components are not null
        assertNotNull(s1.getComponent(SprinklerComponent.class));
        assertNotNull(p1.getComponent(SprinklerComponent.class));
        assertNotNull(s1.getComponent(DynamicTextureRenderComponent.class));
        assertNotNull(p1.getComponent(DynamicTextureRenderComponent.class));

        // set p1 as a pump
        p1.getComponent(SprinklerComponent.class).setPump();

        /*
         * remember to call setOccupant(), setPosition() and create()
         *  sTile/sTile/s1/p1 respectively in each test.
         */
    }

    @Test
    public void setPumpTest() {
        assertTrue(p1.getComponent(SprinklerComponent.class).getPump());
    }

    @Test
    public void sprinklerNotPowered() {
        sTile.setOccupant(s1);
        s1.setPosition(s1_pos);
        s1.create();
        assertFalse(s1.getComponent(SprinklerComponent.class).getPowered());
    }

    @Test
    public void pumpIsPowered() {
        // a pump should be powered by default
        pTile.setOccupant(p1);
        p1.setPosition(p1_pos);
        p1.create();
        assertTrue(p1.getComponent(SprinklerComponent.class).getPowered());
    }

    @Test
    public void pumpPowersSprinkler() {
        // place a pump and sprinkler next to each other, the pump should power the sprinkler.
        pTile.setOccupant(p1);
        sTile.setOccupant(s1);
        p1.setPosition(p1_pos);
        s1.setPosition(s1_pos);
        p1.create();
        s1.create();
        assertTrue(s1.getComponent(SprinklerComponent.class).getPowered());
    }

    @Test
    public void sprinklerLosesPower() {
        // remove a pump from a sprinklers vicinity and check it loses power.
        pTile.setOccupant(p1);
        sTile.setOccupant(s1);
        p1.setPosition(p1_pos);
        s1.setPosition(s1_pos);
        p1.create();
        s1.create();
        assertTrue(s1.getComponent(SprinklerComponent.class).getPowered());
        /* this doesn't remove p1 from the ground, because we don't need to -
            it just tells the adjacent sprinkler it was removed. */
        p1.getEvents().trigger("destroyConnections");
        assertFalse(s1.getComponent(SprinklerComponent.class).getPowered());
    }

    @Test
    public void sprinklerWaters() {
        // haven't figured this test out yet
    }
}
