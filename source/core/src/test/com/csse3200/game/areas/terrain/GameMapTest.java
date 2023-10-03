package com.csse3200.game.areas.terrain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;

import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.extensions.GameExtension;

@ExtendWith(GameExtension.class)
public class GameMapTest {
    private GameMap gameMap;
    private static final ArrayList<GridPoint2> traversableTilesList = new ArrayList<>();
    private static final ArrayList<GridPoint2> nonTraversableTilesList = new ArrayList<>();

    @BeforeAll
    static void config() {
        nonTraversableTilesList.add(new GridPoint2(0,0)); //Lava
        traversableTilesList.add(new GridPoint2(0,1)); //Snow
        traversableTilesList.add(new GridPoint2(0,2)); //Grass
        traversableTilesList.add(new GridPoint2(0,3)); //Path
        traversableTilesList.add(new GridPoint2(1,0)); //Lava ground
        traversableTilesList.add(new GridPoint2(1,1)); //Ice
        traversableTilesList.add(new GridPoint2(1,2)); //Dirt
        traversableTilesList.add(new GridPoint2(1,3)); //Path
        traversableTilesList.add(new GridPoint2(2,0)); //Gravel
        nonTraversableTilesList.add(new GridPoint2(2,1)); //Deep water
        traversableTilesList.add(new GridPoint2(2,2)); //Shallow water
        traversableTilesList.add(new GridPoint2(2,3)); //Path
        traversableTilesList.add(new GridPoint2(3,0)); //Flowing Water
        nonTraversableTilesList.add(new GridPoint2(3,1)); //Rock
        traversableTilesList.add(new GridPoint2(3,2)); //Desert
        traversableTilesList.add(new GridPoint2(3,3)); //Beach sand
    }

    @BeforeEach
    void setup() {
        ResourceService resourceService = new ResourceService();
        resourceService.loadTextures(TerrainFactory.mapTextures);
        resourceService.loadAll();
        ServiceLocator.registerResourceService(resourceService);

        TerrainFactory terrainFactory = new TerrainFactory(new CameraComponent());
        gameMap = new GameMap(terrainFactory);

        TerrainComponent terrainComponent = mock(TerrainComponent.class);
        doReturn(TerrainFactory.worldTileSize).when(terrainComponent).getTileSize();
        gameMap.setTerrainComponent(terrainComponent);

        gameMap.loadTestTerrain("configs/TestMaps/gameMapTest_map.txt");
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
    void testTilesLoadedCorrectly() {
        assertEquals(TerrainTile.TerrainCategory.LAVA,
                gameMap.getTile(new GridPoint2(0, 0)).getTerrainCategory(), "(0,0) is not correct");
        assertEquals(TerrainTile.TerrainCategory.SNOW,
                gameMap.getTile(new GridPoint2(0, 1)).getTerrainCategory(), "(0,1) is not correct");
        assertEquals(TerrainTile.TerrainCategory.GRASS,
                gameMap.getTile(new GridPoint2(0, 2)).getTerrainCategory(), "(0,2) is not correct");
        assertEquals(TerrainTile.TerrainCategory.PATH,
                gameMap.getTile(new GridPoint2(0, 3)).getTerrainCategory(), "(0,3) is not correct");
        assertEquals(TerrainTile.TerrainCategory.LAVAGROUND,
                gameMap.getTile(new GridPoint2(1, 0)).getTerrainCategory(), "(1,0) is not correct");
        assertEquals(TerrainTile.TerrainCategory.ICE,
                gameMap.getTile(new GridPoint2(1, 1)).getTerrainCategory(), "(1,1) is not correct");
        assertEquals(TerrainTile.TerrainCategory.DIRT,
                gameMap.getTile(new GridPoint2(1, 2)).getTerrainCategory(), "(1,2) is not correct");
        assertEquals(TerrainTile.TerrainCategory.PATH,
                gameMap.getTile(new GridPoint2(1, 3)).getTerrainCategory(), "(1,3) is not correct");
        assertEquals(TerrainTile.TerrainCategory.GRAVEL,
                gameMap.getTile(new GridPoint2(2, 0)).getTerrainCategory(), "(2,0) is not correct");
        assertEquals(TerrainTile.TerrainCategory.DEEPWATER,
                gameMap.getTile(new GridPoint2(2, 1)).getTerrainCategory(), "(2,1) is not correct");
        assertEquals(TerrainTile.TerrainCategory.SHALLOWWATER,
                gameMap.getTile(new GridPoint2(2, 2)).getTerrainCategory(), "(2,2) is not correct");
        assertEquals(TerrainTile.TerrainCategory.PATH,
                gameMap.getTile(new GridPoint2(2, 3)).getTerrainCategory(), "(2,3) is not correct");
        assertEquals(TerrainTile.TerrainCategory.FLOWINGWATER,
                gameMap.getTile(new GridPoint2(3, 0)).getTerrainCategory(), "(3,0) is not correct");
        assertEquals(TerrainTile.TerrainCategory.ROCK,
                gameMap.getTile(new GridPoint2(3, 1)).getTerrainCategory(), "(3,1) is not correct");
        assertEquals(TerrainTile.TerrainCategory.DESERT,
                gameMap.getTile(new GridPoint2(3, 2)).getTerrainCategory(), "(3,2) is not correct");
        assertEquals(TerrainTile.TerrainCategory.BEACHSAND,
                gameMap.getTile(new GridPoint2(3, 3)).getTerrainCategory(), "(3,3) is not correct");
    }

    @Test
    void testVectorToTileCoordinates() {
        assertEquals(new GridPoint2(0, 0), gameMap.vectorToTileCoordinates(new Vector2(0, 0)));
        assertEquals(new GridPoint2(0, 1), gameMap.vectorToTileCoordinates(new Vector2(0, 1)));
        assertEquals(new GridPoint2(0, 2), gameMap.vectorToTileCoordinates(new Vector2(0, 2)));
        assertEquals(new GridPoint2(0, 3), gameMap.vectorToTileCoordinates(new Vector2(0, 3)));
        assertEquals(new GridPoint2(1, 0), gameMap.vectorToTileCoordinates(new Vector2(1, 0)));
        assertEquals(new GridPoint2(1, 1), gameMap.vectorToTileCoordinates(new Vector2(1, 1)));
        assertEquals(new GridPoint2(1, 2), gameMap.vectorToTileCoordinates(new Vector2(1, 2)));
        assertEquals(new GridPoint2(1, 3), gameMap.vectorToTileCoordinates(new Vector2(1, 3)));
        assertEquals(new GridPoint2(2, 0), gameMap.vectorToTileCoordinates(new Vector2(2, 0)));
        assertEquals(new GridPoint2(2, 1), gameMap.vectorToTileCoordinates(new Vector2(2, 1)));
        assertEquals(new GridPoint2(2, 2), gameMap.vectorToTileCoordinates(new Vector2(2, 2)));
        assertEquals(new GridPoint2(2, 3), gameMap.vectorToTileCoordinates(new Vector2(2, 3)));
        assertEquals(new GridPoint2(3, 0), gameMap.vectorToTileCoordinates(new Vector2(3, 0)));
        assertEquals(new GridPoint2(3, 1), gameMap.vectorToTileCoordinates(new Vector2(3, 1)));
        assertEquals(new GridPoint2(3, 2), gameMap.vectorToTileCoordinates(new Vector2(3, 2)));
        assertEquals(new GridPoint2(3, 3), gameMap.vectorToTileCoordinates(new Vector2(3, 3)));
    }

    @Test
    void testTileCoordinatesToVector() {
        assertEquals(new Vector2(0, 0), gameMap.tileCoordinatesToVector(new GridPoint2(0, 0)));
        assertEquals(new Vector2(0, 1), gameMap.tileCoordinatesToVector(new GridPoint2(0, 1)));
        assertEquals(new Vector2(0, 2), gameMap.tileCoordinatesToVector(new GridPoint2(0, 2)));
        assertEquals(new Vector2(0, 3), gameMap.tileCoordinatesToVector(new GridPoint2(0, 3)));
        assertEquals(new Vector2(1, 0), gameMap.tileCoordinatesToVector(new GridPoint2(1, 0)));
        assertEquals(new Vector2(1, 1), gameMap.tileCoordinatesToVector(new GridPoint2(1, 1)));
        assertEquals(new Vector2(1, 2), gameMap.tileCoordinatesToVector(new GridPoint2(1, 2)));
        assertEquals(new Vector2(1, 3), gameMap.tileCoordinatesToVector(new GridPoint2(1, 3)));
        assertEquals(new Vector2(2, 0), gameMap.tileCoordinatesToVector(new GridPoint2(2, 0)));
        assertEquals(new Vector2(2, 1), gameMap.tileCoordinatesToVector(new GridPoint2(2, 1)));
        assertEquals(new Vector2(2, 2), gameMap.tileCoordinatesToVector(new GridPoint2(2, 2)));
        assertEquals(new Vector2(2, 3), gameMap.tileCoordinatesToVector(new GridPoint2(2, 3)));
        assertEquals(new Vector2(3, 0), gameMap.tileCoordinatesToVector(new GridPoint2(3, 0)));
        assertEquals(new Vector2(3, 1), gameMap.tileCoordinatesToVector(new GridPoint2(3, 1)));
        assertEquals(new Vector2(3, 2), gameMap.tileCoordinatesToVector(new GridPoint2(3, 2)));
        assertEquals(new Vector2(3, 3), gameMap.tileCoordinatesToVector(new GridPoint2(3, 3)));
    }

    @Test
    void testGetTraversableTileCoordinates() {
        ArrayList<GridPoint2> arrayList = gameMap.getTraversableTileCoordinates();

        assertEquals(arrayList, traversableTilesList);
    }

    @Test
    void testGetNonTraversableTileCoordinates() {
        ArrayList<GridPoint2> arrayList = gameMap.getNonTraversableTileCoordinates();

        assertEquals(arrayList, nonTraversableTilesList);
    }

    @AfterEach
    public void cleanUp(){
        // unload assets
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(TerrainFactory.mapTextures);
        resourceService.dispose();
    }
}
