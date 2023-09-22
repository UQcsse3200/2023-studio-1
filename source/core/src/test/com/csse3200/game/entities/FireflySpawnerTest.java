package com.csse3200.game.entities;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class FireflySpawnerTest {
    // setup / fields / config taken from GameMapTest
    private GameMap gameMap;
    private static TerrainTile pathTerrainTile;
    private static TerrainTile beachSandTerrainTile;
    private static TerrainTile grassTerrainTile;
    private static TerrainTile dirtTerrainTile;
    private static TerrainTile shallowWaterTerrainTile;
    private static TerrainTile desertTerrainTile;
    private static TerrainTile snowTerrainTile;
    private static TerrainTile iceTerrainTile;
    private static TerrainTile deepWaterTerrainTile;
    private static TerrainTile rockTerrainTile;
    private static TerrainTile lavaTerrainTile;
    private static TerrainTile lavaGroundTerrainTile;
    private static TerrainTile gravelTerrainTile;
    private static TerrainTile flowingWaterTerrainTile;

    @BeforeAll
    static void config() {
        pathTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.PATH);
        beachSandTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.BEACHSAND);
        grassTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.GRASS);
        dirtTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.DIRT);
        shallowWaterTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.SHALLOWWATER);
        desertTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.DESERT);
        snowTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.SNOW);
        iceTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.ICE);
        deepWaterTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.DEEPWATER);
        rockTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.ROCK);
        lavaTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.LAVA);
        lavaGroundTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.LAVAGROUND);
        gravelTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.GRAVEL);
        flowingWaterTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.FLOWINGWATER);
    }

    @BeforeEach
    void setup() {
        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        doReturn(new GridPoint2(4, 4)).when(terrainFactory).getMapSize();

        TerrainComponent terrainComponent = mock(TerrainComponent.class);
        doReturn(0.5f).when(terrainComponent).getTileSize();

        gameMap = new GameMap(terrainFactory);
        gameMap.setTerrainComponent(terrainComponent);

        GameArea area = mock(GameArea.class);
        doReturn(gameMap).when(area).getMap();

        ServiceLocator.registerGameArea(area);


        TiledMap tiledMap = gameMap.getTiledMap();

        TiledMapTileLayer layer = new TiledMapTileLayer(4, 4, 16, 16);

        layer.setCell(0, 0, new TiledMapTileLayer.Cell().setTile(pathTerrainTile));
        layer.setCell(0, 1, new TiledMapTileLayer.Cell().setTile(pathTerrainTile));
        layer.setCell(0, 2, new TiledMapTileLayer.Cell().setTile(pathTerrainTile));
        layer.setCell(0, 3, new TiledMapTileLayer.Cell().setTile(beachSandTerrainTile));
        layer.setCell(1, 0, new TiledMapTileLayer.Cell().setTile(grassTerrainTile));
        layer.setCell(1, 1, new TiledMapTileLayer.Cell().setTile(dirtTerrainTile));
        layer.setCell(1, 2, new TiledMapTileLayer.Cell().setTile(shallowWaterTerrainTile));
        layer.setCell(1, 3, new TiledMapTileLayer.Cell().setTile(desertTerrainTile));
        layer.setCell(2, 0, new TiledMapTileLayer.Cell().setTile(snowTerrainTile));
        layer.setCell(2, 1, new TiledMapTileLayer.Cell().setTile(iceTerrainTile));
        layer.setCell(2, 2, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(2, 3, new TiledMapTileLayer.Cell().setTile(rockTerrainTile));
        layer.setCell(3, 0, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(3, 1, new TiledMapTileLayer.Cell().setTile(lavaGroundTerrainTile));
        layer.setCell(3, 2, new TiledMapTileLayer.Cell().setTile(gravelTerrainTile));
        layer.setCell(3, 3, new TiledMapTileLayer.Cell().setTile(flowingWaterTerrainTile));

        tiledMap.getLayers().add(layer);
    }

    @Test
    void spawnFirefly() {
        new FireflySpawner();
        ServiceLocator.registerEntityService(new EntityService());
        TimeService ts = new TimeService();
        ServiceLocator.registerTimeService(ts);
        ts.setHour(20);
        // Wait for some entities to be made
        try {
            wait(100);
        } catch (Exception e) {
            // squash
        }
        for (Entity e : ServiceLocator.getEntityService().getEntities()) {
            if (e.getType() == EntityType.FireFlies) {
                assertTrue(true);
            }
        }
        assertFalse(false);
    }

    @Test
    void fireFlyDies() {
        new FireflySpawner();
        ServiceLocator.registerEntityService(new EntityService());
        TimeService ts = new TimeService();
        ServiceLocator.registerTimeService(ts);
        ts.setHour(20);
        // Wait for some entities to be made
        try {
            wait(100);
        } catch (Exception e) {
            // squash
        }

    }
}
