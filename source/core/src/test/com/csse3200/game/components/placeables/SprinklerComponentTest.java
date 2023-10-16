package com.csse3200.game.components.placeables;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.plants.PlantComponent;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(GameExtension.class)
public class SprinklerComponentTest {
    private static Entity p1;
    private static Entity s1;
    private static Entity s2;
    private static final Vector2 p1_pos = new Vector2(2, 2);
    private static final Vector2 s1_pos = new Vector2(3, 2);
    private static final Vector2 s2_pos = new Vector2(4, 2);
    private static TerrainTile p1Tile;  // tile where pump is placed
    private static TerrainTile s1Tile;  // tile where sprinkler is placed
    private static TerrainTile s2Tile;  // tile where sprinkler is placed
    private static GameArea gameArea;
    private static GameMap gameMap;
    private static TimeService timeService;

    @BeforeEach
    public void setup() {
        // create game area and map
        gameArea = mock(GameArea.class);
        gameMap = mock(GameMap.class);
        when(gameArea.getMap()).thenReturn(gameMap);
        timeService = mock(TimeService.class);
        when(timeService.getEvents()).thenReturn(new EventHandler());
        ServiceLocator.registerGameArea(gameArea);
        ServiceLocator.registerTimeService(timeService);
        ClimateController climateController = new ClimateController();
        when(gameArea.getClimateController()).thenReturn(climateController);

        // Create tiles for test area
        /* We get a test area that looks like this:
         *   x x x x x x x
         *   x x x x x x x
         *   x x P S S x x
         *   x x x x x x x
         *   x x x x x x x
         * with: x = empty tiles, S = will contain sprinkler, P = will contain pump */
        p1Tile = new TerrainTile(null, TerrainTile.TerrainCategory.DIRT);
        s1Tile = new TerrainTile(null, TerrainTile.TerrainCategory.DIRT);
        s2Tile = new TerrainTile(null, TerrainTile.TerrainCategory.DIRT);
        for (int x = 0; x <= 6; x++) {
            for (int y = 0; y <= 4; y++) {
                Vector2 pos = new Vector2(x, y);
                if (pos.equals(p1_pos)) {
                    when(gameMap.getTile(pos)).thenReturn(p1Tile);
                } else if (pos.equals(s1_pos)) {
                    when(gameMap.getTile(pos)).thenReturn(s1Tile);
                } else if (pos.equals(s2_pos)) {
                    when(gameMap.getTile(pos)).thenReturn(s2Tile);
                } else {
                    when(gameMap.getTile(pos)).thenReturn(new TerrainTile(null, TerrainTile.TerrainCategory.DIRT));
                }
            }
        }

        // create sprinkler and pump entities.
        DynamicTextureRenderComponent dtrc = mock(DynamicTextureRenderComponent.class);

        p1 = new Entity(EntityType.PUMP)
                .addComponent(new SprinklerComponent())
                .addComponent(dtrc);

        s1 = new Entity(EntityType.SPRINKLER)
                .addComponent(new SprinklerComponent())
                .addComponent(dtrc);

        s2 = new Entity(EntityType.SPRINKLER)
                .addComponent(new SprinklerComponent())
                .addComponent(dtrc);

        // before each test, ensure components are not null
        assertNotNull(p1.getComponent(SprinklerComponent.class));
        assertNotNull(s1.getComponent(SprinklerComponent.class));
        assertNotNull(s2.getComponent(SprinklerComponent.class));
        assertNotNull(p1.getComponent(DynamicTextureRenderComponent.class));
        assertNotNull(s1.getComponent(DynamicTextureRenderComponent.class));
        assertNotNull(s2.getComponent(DynamicTextureRenderComponent.class));

        // set p1 as a pump
        p1.getComponent(SprinklerComponent.class).setPump();

        /*
         * remember to call setOccupant(), setPosition() and create()
         *  sTile/sTile/s1/p1 respectively in each test.
         */
    }

    @Test
    public void setPumpTest() {
        p1.setPosition(p1_pos);
        p1.create();
        assertTrue(p1.getComponent(SprinklerComponent.class).getPump());
    }

    @Test
    public void sprinklerNotPowered() {
        s1Tile.setOccupant(s1);
        s1.setPosition(s1_pos);
        s1.create();
        assertFalse(s1.getComponent(SprinklerComponent.class).getPowered());
    }

    @Test
    public void pumpIsPowered() {
        // a pump should be powered by default
        p1Tile.setOccupant(p1);
        p1.setPosition(p1_pos);
        p1.create();
        assertTrue(p1.getComponent(SprinklerComponent.class).getPowered());
    }

    @Test
    public void pumpPowersSprinkler() {
        // place a pump and sprinkler next to each other, the pump should power the sprinkler.
        p1Tile.setOccupant(p1);
        s1Tile.setOccupant(s1);
        p1.setPosition(p1_pos);
        s1.setPosition(s1_pos);
        p1.create();
        s1.create();
        assertTrue(s1.getComponent(SprinklerComponent.class).getPowered());
    }

    @Test
    public void sprinklerLosesPower() {
        // remove a pump from a sprinklers vicinity and check it loses power.
        p1Tile.setOccupant(p1);
        s1Tile.setOccupant(s1);
        p1.setPosition(p1_pos);
        s1.setPosition(s1_pos);
        p1.create();
        s1.create();
        assertTrue(s1.getComponent(SprinklerComponent.class).getPowered());
        /* this doesn't remove p1 from the ground, because we don't need to -
            it just tells the adjacent sprinkler it was removed. */
        p1.getEvents().trigger("onDestroy");
        assertFalse(s1.getComponent(SprinklerComponent.class).getPowered());
    }

    @Test
    public void sprinklerTransfersPower() {
        // place a sprinkler next to a powered sprinkler,
        // the powered sprinkler should power the newly placed sprinkler
        p1Tile.setOccupant(p1);
        s1Tile.setOccupant(s1);
        s2Tile.setOccupant(s2);
        p1.setPosition(p1_pos);
        s1.setPosition(s1_pos);
        s2.setPosition(s2_pos);
        p1.create();
        s1.create();
        s2.create();
        assertTrue(s1.getComponent(SprinklerComponent.class).getPowered());
    }

    @Test
    public void sprinklerStopsTransferPower() {
        // remove a sprinkler that is providing power to an adjacent sprinkler,
        // assert the adjacent sprinkler loses power
        p1Tile.setOccupant(p1);
        s1Tile.setOccupant(s1);
        s2Tile.setOccupant(s2);
        p1.setPosition(p1_pos);
        s1.setPosition(s1_pos);
        s2.setPosition(s2_pos);
        p1.create();
        s1.create();
        s2.create();
        assertTrue(s1.getComponent(SprinklerComponent.class).getPowered());
        /* this doesn't remove s1 from the ground, because we don't need to -
            it just tells the adjacent sprinkler it was removed. */
        s1.getEvents().trigger("onDestroy");
        assertFalse(s2.getComponent(SprinklerComponent.class).getPowered());
    }

    @Test
    public void sprinklerWatersCropTiles() {
        s1Tile.setOccupant(s1);
        s1.setPosition(s1_pos);
        s1.create();
        // set the sprinkler to powered, so it can sprinkle
        s1.getComponent(SprinklerComponent.class).setPower(true);

        // check that each tile in the sprinklers AOE gets watered
        for (Vector2 pos : s1.getComponent(SprinklerComponent.class).aoe) {
            // generate a cropTile for this position
            CropTileComponent cropComp = new CropTileComponent(0, 0);
            Entity cropTile = new Entity(EntityType.TILE).addComponent(cropComp);
            cropTile.create();
            ServiceLocator.getGameArea().getMap().getTile(pos).setOccupant(cropTile);
            // check initial value is 0 / un-watered.
            assertEquals(0, cropComp.getWaterContent());

            // make sprinkler water
            s1.getComponent(SprinklerComponent.class).sprinkle();
            assertEquals(0.25, cropComp.getWaterContent());
        }
    }

    @Test
    public void sprinklerWatersPlants() {
        s1Tile.setOccupant(s1);
        s1.setPosition(s1_pos);
        s1.create();
        // set the sprinkler to powered, so it can sprinkle
        s1.getComponent(SprinklerComponent.class).setPower(true);

        // check that each tile with a plant in the sprinklers AOE gets watered to the amount the plant requires
        for (Vector2 pos : s1.getComponent(SprinklerComponent.class).aoe) {
            // generate a cropTile for this position
            CropTileComponent cropComp = new CropTileComponent(0, 0);
            Entity cropTile = new Entity(EntityType.TILE).addComponent(cropComp);
            cropTile.create();
            // 1 is a reasonable "ideal water level".
            PlantComponent plant = new PlantComponent(1, "name", "type", "desc", 1, 1, 1, cropComp);
            Entity plantEntity = new Entity(EntityType.PLANT).addComponent(plant);
            cropComp.setPlant(plantEntity);
            ServiceLocator.getGameArea().getMap().getTile(pos).setOccupant(cropTile);
            // check initial value is 0 / un-watered.
            assertEquals(0, cropComp.getWaterContent());

            // make sprinkler water the tile with the plant
            s1.getComponent(SprinklerComponent.class).sprinkle();
            assertEquals(plant.getIdealWaterLevel(), cropComp.getWaterContent());
        }
    }
}
