package com.csse3200.game.areas.terrain;

import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.extensions.GameExtension;

@ExtendWith(GameExtension.class)
public class GameMapTest {

    /*
     * private GameMap gameMap;
     * private TerrainFactory terrainFactory;
     * private TerrainTile terrainTile1, terrainTile2, terrainTile3;
     * 
     * @BeforeAll
     * static void config() {
     * Gdx.graphics = mock(Graphics.class);
     * // sampling a 4x4 grid from the map for testing
     * when(Gdx.graphics.getWidth()).thenReturn(64);
     * when(Gdx.graphics.getHeight()).thenReturn(64);
     * }
     * 
     * @BeforeEach
     * void setup() {
     * CameraComponent cameraComponent = mock(CameraComponent.class);
     * terrainFactory = new TerrainFactory(cameraComponent);
     * gameMap = new GameMap(terrainFactory);
     * 
     * TiledMapTileLayer layer = new TiledMapTileLayer(10000,1000,16,16);
     * gameMap.getTiledMap().getLayers().add(layer);
     * 
     * TextureRegion textureRegion = mock(TextureRegion.class);
     * terrainTile1 = new TerrainTile(textureRegion,
     * TerrainTile.TerrainCategory.PATH); // traversable & tillable
     * terrainTile2 = new TerrainTile(textureRegion,
     * TerrainTile.TerrainCategory.BEACHSAND); // traversable & !tillable
     * terrainTile3 = new TerrainTile(textureRegion,
     * TerrainTile.TerrainCategory.DEEPWATER); // !traversable & !tillable
     * 
     * Cell cell1 = new Cell().setTile(terrainTile1);
     * Cell cell2 = new Cell().setTile(terrainTile2);
     * Cell cell3 = new Cell().setTile(terrainTile3);
     * 
     * ((TiledMapTileLayer) gameMap.getTiledMap().getLayers().get(0)).setCell(0, 0,
     * cell1);
     * ((TiledMapTileLayer) gameMap.getTiledMap().getLayers().get(0)).setCell(0, 2,
     * cell2);
     * ((TiledMapTileLayer) gameMap.getTiledMap().getLayers().get(0)).setCell(3, 1,
     * cell3);
     * }
     * 
     * @Test
     * void testGetTerrainFactory() {
     * assertEquals(terrainFactory, gameMap.getTerrainFactory());
     * }
     * 
     * @Test
     * void testGetTiledMap() {
     * assertInstanceOf(TiledMap.class, gameMap.getTiledMap());
     * }
     * 
     * @Test
     * void testGetEventHandler() {
     * assertInstanceOf(EventHandler.class, gameMap.getEventHandler());
     * }
     * 
     * @Test
     * void testGetInitialMapSize() {
     * assertEquals(new GridPoint2(1000,1000), gameMap.getMapSize());
     * }
     * 
     * @Test
     * void testGetTile() {
     * assertEquals(terrainTile1, gameMap.getTile(0, 0));
     * assertEquals(terrainTile2, gameMap.getTile(0, 2));
     * assertEquals(terrainTile3, gameMap.getTile(3, 1));
     * }
     * 
     * @Test
     * void testWorldCoordinatesToPixelPosition() {
     * assertEquals(new GridPoint2(8, 56),
     * gameMap.worldCoordinatesToPixelPosition(0, 0));
     * assertEquals(new GridPoint2(24, 40),
     * gameMap.worldCoordinatesToPixelPosition(1, 1));
     * assertEquals(new GridPoint2(40, 8),
     * gameMap.worldCoordinatesToPixelPosition(2, 3));
     * }
     * 
     * @Test
     * void testPixelPositionToWorldCoordinates() {
     * assertEquals(new GridPoint2(0, 0), gameMap.pixelPositionToWorldCoordinates(6,
     * 50));
     * assertEquals(new GridPoint2(1, 1),
     * gameMap.pixelPositionToWorldCoordinates(26, 39));
     * assertEquals(new GridPoint2(2, 3),
     * gameMap.pixelPositionToWorldCoordinates(38, 7));
     * }
     * 
     * @Test
     * void testGetTerrainTileCategory() {
     * gameMap.setTileTerrainCategory(TerrainTile.TerrainCategory.DIRT, 0, 0);
     * gameMap.setTileTerrainCategory(TerrainTile.TerrainCategory.SHALLOWWATER, 0,
     * 2);
     * gameMap.setTileTerrainCategory(TerrainTile.TerrainCategory.ICE, 3, 1);
     * 
     * assertEquals(TerrainTile.TerrainCategory.DIRT,
     * gameMap.getTileTerrainCategory(0, 0));
     * assertEquals(TerrainTile.TerrainCategory.SHALLOWWATER,
     * gameMap.getTileTerrainCategory(0, 2));
     * assertEquals(TerrainTile.TerrainCategory.ICE,
     * gameMap.getTileTerrainCategory(3, 1));
     * }
     * 
     * @Test
     * void testTileTraversable() {
     * assertTrue(gameMap.isTileTraversable(0,0));
     * assertTrue(gameMap.isTileTraversable(0,2));
     * assertFalse(gameMap.isTileTraversable(3,1));
     * }
     * 
     * @Test
     * void testTileOccupied() {
     * gameMap.setTileOccupied(0,0);
     * assertTrue(gameMap.isTileOccupied(0,0));
     * gameMap.setTileUnoccupied(0,0);
     * assertFalse(gameMap.isTileOccupied(0,0));
     * }
     * 
     * @Test
     * void testTileTillable() {
     * assertTrue(gameMap.isTileTillable(0,0));
     * assertFalse(gameMap.isTileTillable(0,2));
     * assertFalse(gameMap.isTileTillable(3,1));
     * }
     * 
     */
}
