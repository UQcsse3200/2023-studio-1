package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class GameMapTest {
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

        TiledMap tiledMap = gameMap.getTiledMap();

        TiledMapTileLayer layer = new TiledMapTileLayer(4, 4, 16, 16);

        layer.setCell(0, 0, new Cell().setTile(pathTerrainTile));
        layer.setCell(0, 1, new Cell().setTile(pathTerrainTile));
        layer.setCell(0, 2, new Cell().setTile(pathTerrainTile));
        layer.setCell(0, 3, new Cell().setTile(beachSandTerrainTile));
        layer.setCell(1, 0, new Cell().setTile(grassTerrainTile));
        layer.setCell(1, 1, new Cell().setTile(dirtTerrainTile));
        layer.setCell(1, 2, new Cell().setTile(shallowWaterTerrainTile));
        layer.setCell(1, 3, new Cell().setTile(desertTerrainTile));
        layer.setCell(2, 0, new Cell().setTile(snowTerrainTile));
        layer.setCell(2, 1, new Cell().setTile(iceTerrainTile));
        layer.setCell(2, 2, new Cell().setTile(deepWaterTerrainTile));
        layer.setCell(2, 3, new Cell().setTile(rockTerrainTile));
        layer.setCell(3, 0, new Cell().setTile(lavaTerrainTile));
        layer.setCell(3, 1, new Cell().setTile(lavaGroundTerrainTile));
        layer.setCell(3, 2, new Cell().setTile(gravelTerrainTile));
        layer.setCell(3, 3, new Cell().setTile(flowingWaterTerrainTile));

        tiledMap.getLayers().add(layer);
    }

    @Test
    void testGetTiledMapIsTiledMap() {
        assertInstanceOf(TiledMap.class, gameMap.getTiledMap());
    }

    @Test
    void testGetMapSize() {
        assertEquals(new GridPoint2(4,4), gameMap.getMapSize());
    }

    @Test
    void testGetTileAtBottomLeft() {
        assertEquals(pathTerrainTile, gameMap.getTile(new GridPoint2(0, 0)));
    }

    @Test
    void testGetTileAtTopLeft() {
        assertEquals(beachSandTerrainTile, gameMap.getTile(new GridPoint2(0, 3)));
    }

    @Test
    void testGetTileAtBottomRight() {
        assertEquals(lavaTerrainTile, gameMap.getTile(new GridPoint2(3, 0)));
    }

    @Test
    void testGetTileAtTopRight() {
        assertEquals(flowingWaterTerrainTile, gameMap.getTile(new GridPoint2(3, 3)));
    }

    @Test
    void testGetTileAtLeftRow() {
        assertEquals(pathTerrainTile, gameMap.getTile(new GridPoint2(0, 0)));
        assertEquals(pathTerrainTile, gameMap.getTile(new GridPoint2(0, 1)));
        assertEquals(pathTerrainTile, gameMap.getTile(new GridPoint2(0, 2)));
        assertEquals(beachSandTerrainTile, gameMap.getTile(new GridPoint2(0, 3)));
    }

    @Test
    void testGetTileAtTopRow() {
        assertEquals(beachSandTerrainTile, gameMap.getTile(new GridPoint2(0, 3)));
        assertEquals(desertTerrainTile, gameMap.getTile(new GridPoint2(1, 3)));
        assertEquals(rockTerrainTile, gameMap.getTile(new GridPoint2(2, 3)));
        assertEquals(flowingWaterTerrainTile, gameMap.getTile(new GridPoint2(3, 3)));
    }

    @Test
    void testGetTileAtRightRow() {
        assertEquals(lavaTerrainTile, gameMap.getTile(new GridPoint2(3, 0)));
        assertEquals(lavaGroundTerrainTile, gameMap.getTile(new GridPoint2(3, 1)));
        assertEquals(gravelTerrainTile, gameMap.getTile(new GridPoint2(3, 2)));
        assertEquals(flowingWaterTerrainTile, gameMap.getTile(new GridPoint2(3, 3)));
    }

    @Test
    void testGetTileAtBottomRow() {
        assertEquals(pathTerrainTile, gameMap.getTile(new GridPoint2(0, 0)));
        assertEquals(grassTerrainTile, gameMap.getTile(new GridPoint2(1, 0)));
        assertEquals(snowTerrainTile, gameMap.getTile(new GridPoint2(2, 0)));
        assertEquals(lavaTerrainTile, gameMap.getTile(new GridPoint2(3, 0)));
    }

    @Test
    void testGetTileInternalTiles() {
        assertEquals(dirtTerrainTile, gameMap.getTile(new GridPoint2(1, 1)));
        assertEquals(shallowWaterTerrainTile, gameMap.getTile(new GridPoint2(1, 2)));
        assertEquals(iceTerrainTile, gameMap.getTile(new GridPoint2(2, 1)));
        assertEquals(deepWaterTerrainTile, gameMap.getTile(new GridPoint2(2, 2)));
    }

    @Test
    void testVectorToTileCoordinates() {
        assertEquals(new GridPoint2(0, 0), gameMap.vectorToTileCoordinates(new Vector2(0, 0)));
        assertEquals(new GridPoint2(0, 1), gameMap.vectorToTileCoordinates(new Vector2(0, 0.5f)));
        assertEquals(new GridPoint2(0, 2), gameMap.vectorToTileCoordinates(new Vector2(0, 1)));
        assertEquals(new GridPoint2(0, 3), gameMap.vectorToTileCoordinates(new Vector2(0, 1.5f)));
        assertEquals(new GridPoint2(1, 0), gameMap.vectorToTileCoordinates(new Vector2(0.5f, 0)));
        assertEquals(new GridPoint2(1, 1), gameMap.vectorToTileCoordinates(new Vector2(0.5f, 0.5f)));
        assertEquals(new GridPoint2(1, 2), gameMap.vectorToTileCoordinates(new Vector2(0.5f, 1)));
        assertEquals(new GridPoint2(1, 3), gameMap.vectorToTileCoordinates(new Vector2(0.5f, 1.5f)));
        assertEquals(new GridPoint2(2, 0), gameMap.vectorToTileCoordinates(new Vector2(1, 0)));
        assertEquals(new GridPoint2(2, 1), gameMap.vectorToTileCoordinates(new Vector2(1, 0.5f)));
        assertEquals(new GridPoint2(2, 2), gameMap.vectorToTileCoordinates(new Vector2(1, 1)));
        assertEquals(new GridPoint2(2, 3), gameMap.vectorToTileCoordinates(new Vector2(1, 1.5f)));
        assertEquals(new GridPoint2(3, 0), gameMap.vectorToTileCoordinates(new Vector2(1.5f, 0)));
        assertEquals(new GridPoint2(3, 1), gameMap.vectorToTileCoordinates(new Vector2(1.5f, 0.5f)));
        assertEquals(new GridPoint2(3, 2), gameMap.vectorToTileCoordinates(new Vector2(1.5f, 1)));
        assertEquals(new GridPoint2(3, 3), gameMap.vectorToTileCoordinates(new Vector2(1.5f, 1.5f)));
    }

    @Test
    void testTileCoordinatesToVector() {
        assertEquals(new Vector2(0, 0), gameMap.tileCoordinatesToVector(new GridPoint2(0, 0)));
        assertEquals(new Vector2(0, 0.5f), gameMap.tileCoordinatesToVector(new GridPoint2(0, 1)));
        assertEquals(new Vector2(0, 1), gameMap.tileCoordinatesToVector(new GridPoint2(0, 2)));
        assertEquals(new Vector2(0, 1.5f), gameMap.tileCoordinatesToVector(new GridPoint2(0, 3)));
        assertEquals(new Vector2(0.5f, 0), gameMap.tileCoordinatesToVector(new GridPoint2(1, 0)));
        assertEquals(new Vector2(0.5f, 0.5f), gameMap.tileCoordinatesToVector(new GridPoint2(1, 1)));
        assertEquals(new Vector2(0.5f, 1), gameMap.tileCoordinatesToVector(new GridPoint2(1, 2)));
        assertEquals(new Vector2(0.5f, 1.5f), gameMap.tileCoordinatesToVector(new GridPoint2(1, 3)));
        assertEquals(new Vector2(1, 0), gameMap.tileCoordinatesToVector(new GridPoint2(2, 0)));
        assertEquals(new Vector2(1, 0.5f), gameMap.tileCoordinatesToVector(new GridPoint2(2, 1)));
        assertEquals(new Vector2(1, 1), gameMap.tileCoordinatesToVector(new GridPoint2(2, 2)));
        assertEquals(new Vector2(1, 1.5f), gameMap.tileCoordinatesToVector(new GridPoint2(2, 3)));
        assertEquals(new Vector2(1.5f, 0), gameMap.tileCoordinatesToVector(new GridPoint2(3, 0)));
        assertEquals(new Vector2(1.5f, 0.5f), gameMap.tileCoordinatesToVector(new GridPoint2(3, 1)));
        assertEquals(new Vector2(1.5f, 1), gameMap.tileCoordinatesToVector(new GridPoint2(3, 2)));
        assertEquals(new Vector2(1.5f, 1.5f), gameMap.tileCoordinatesToVector(new GridPoint2(3, 3)));
    }

    @Test
    void testGetTraversableTileCoordinatesHasAllTraversableCoordinates() {
        ArrayList<GridPoint2> arrayList = gameMap.getTraversableTileCoordinates();

        assertTrue(arrayList.contains(new GridPoint2(0, 0))); // path
        assertTrue(arrayList.contains(new GridPoint2(0, 1))); // path
        assertTrue(arrayList.contains(new GridPoint2(0, 2))); // path
        assertTrue(arrayList.contains(new GridPoint2(0, 3))); // beach sand
        assertTrue(arrayList.contains(new GridPoint2(1, 0))); // grass
        assertTrue(arrayList.contains(new GridPoint2(1, 1))); // dirt
        assertTrue(arrayList.contains(new GridPoint2(1, 2))); // shallow water
        assertTrue(arrayList.contains(new GridPoint2(1, 3))); // desert
        assertTrue(arrayList.contains(new GridPoint2(2, 0))); // snow
        assertTrue(arrayList.contains(new GridPoint2(2, 1))); // ice
        assertTrue(arrayList.contains(new GridPoint2(3, 1))); // lava ground
        assertTrue(arrayList.contains(new GridPoint2(3, 2))); // gravel
        assertTrue(arrayList.contains(new GridPoint2(3, 3))); // flowing water
    }

    @Test
    void testGetTraversableTileCoordinatesHasNoNonTraversableCoordinates() {
        ArrayList<GridPoint2> arrayList = gameMap.getTraversableTileCoordinates();

        assertFalse(arrayList.contains(new GridPoint2(2, 2))); // deep water
        assertFalse(arrayList.contains(new GridPoint2(2, 3))); // rock
        assertFalse(arrayList.contains(new GridPoint2(3, 0))); // lava
    }

    @Test
    void testGetNonTraversableTileCoordinatesHasNoTraversableCoordinates() {
        ArrayList<GridPoint2> arrayList = gameMap.getNonTraversableTileCoordinates();

        assertFalse(arrayList.contains(new GridPoint2(0, 0))); // path
        assertFalse(arrayList.contains(new GridPoint2(0, 1))); // path
        assertFalse(arrayList.contains(new GridPoint2(0, 2))); // path
        assertFalse(arrayList.contains(new GridPoint2(0, 3))); // beach sand
        assertFalse(arrayList.contains(new GridPoint2(1, 0))); // grass
        assertFalse(arrayList.contains(new GridPoint2(1, 1))); // dirt
        assertFalse(arrayList.contains(new GridPoint2(1, 2))); // shallow water
        assertFalse(arrayList.contains(new GridPoint2(1, 3))); // desert
        assertFalse(arrayList.contains(new GridPoint2(2, 0))); // snow
        assertFalse(arrayList.contains(new GridPoint2(2, 1))); // ice
        assertFalse(arrayList.contains(new GridPoint2(3, 1))); // lava ground
        assertFalse(arrayList.contains(new GridPoint2(3, 2))); // gravel
        assertFalse(arrayList.contains(new GridPoint2(3, 3))); // flowing water
    }

    @Test
    void testGetNonTraversableTileCoordinatesHasAllNonTraversableCoordinates() {
        ArrayList<GridPoint2> arrayList = gameMap.getNonTraversableTileCoordinates();

        assertTrue(arrayList.contains(new GridPoint2(2, 2))); // deep water
        assertTrue(arrayList.contains(new GridPoint2(2, 3))); // rock
        assertTrue(arrayList.contains(new GridPoint2(3, 0))); // lava
    }
}
