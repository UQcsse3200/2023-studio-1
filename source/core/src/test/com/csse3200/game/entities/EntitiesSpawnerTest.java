package com.csse3200.game.entities;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import net.dermetfan.gdx.physics.box2d.PositionController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(GameExtension.class)
class EntitiesSpawnerTest {
    private TimeService timeService;
    private int initialEntityCount;
    private EntitiesSpawner entitiesSpawner;
    private List<EntitySpawner> entitySpawners;
    private Entity player;
    private TerrainTile pathTerrainTile;
    private GameAreaSimple area;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        GameTime gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
        timeService = new TimeService();
        ServiceLocator.registerTimeService(timeService);

        pathTerrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.PATH);

        area = new GameAreaSimple();

        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        doReturn(new GridPoint2(1, 1)).when(terrainFactory).getMapSize();

        GameMap gameMap = new GameMap(terrainFactory);
        ServiceLocator.registerGameArea(area);

        initialEntityCount = getDummyEntityCount();
        player = new Entity();
        TerrainComponent terrain = mock(TerrainComponent.class);
        doReturn(new Vector2(0f, 0f)).when(terrain).tileToWorldPosition(0, 0);
        doReturn(new Vector2(0f, 0f)).when(terrain).tileToWorldPosition(new GridPoint2(0, 0));

        area.setTerrain(terrain);
    }

    //This method is meant to mirror what a normal create method would look like
    //e.g., createCow
    private Entity createDummyEntity(Entity player) {
        return new Entity(EntityType.Dummy);
    }

    private int getDummyEntityCount() {
        Array<Entity> entities = ServiceLocator.getEntityService().getEntities();
        int count = 0;
        for (int i = 0; i < entities.size; i++) {
            if (entities.get(i).getType().equals(EntityType.Dummy)) {
                count++;
            }
        }
        return count;
    }

    @Test
    void checkInitialSpawn() {
        entitySpawners = new ArrayList<>();
        entitySpawners.add(new EntitySpawner(1, this::createDummyEntity, player,
                0, 1, 0, 0, 0));
        entitiesSpawner = new EntitiesSpawner(entitySpawners);
        entitiesSpawner.setGameAreas(area);
        entitiesSpawner.spawnNow();
        assertEquals(initialEntityCount + 1, getDummyEntityCount());
    }


    @Test
    void checkRandomSpawnRange() {
        entitySpawners = new ArrayList<>();
        entitySpawners.add(new EntitySpawner(1, this::createDummyEntity, player,
                0, 1, 0, 5, 0));
        entitiesSpawner = new EntitiesSpawner(entitySpawners);
        entitiesSpawner.setGameAreas(area);
        entitiesSpawner.startPeriodicSpawning();
        timeService.setHour(0);
        timeService.setHour(1);
        timeService.setHour(1);
        timeService.setHour(1);
        timeService.setHour(1);
        assertEquals(initialEntityCount + 1, getDummyEntityCount());
    }

    @Test
    void checkDayRange() {
        entitySpawners = new ArrayList<>();
        entitySpawners.add(new EntitySpawner(1, this::createDummyEntity, player,
                0, 1, 0, 0, 2));
        entitiesSpawner = new EntitiesSpawner(entitySpawners);
        entitiesSpawner.setGameAreas(area);
        entitiesSpawner.startPeriodicSpawning();
        timeService.setHour(0);
        assertEquals(initialEntityCount, getDummyEntityCount());
    }

    @Test
    void checkPeriodicSameDaySpawn() {
        entitySpawners = new ArrayList<>();
        entitySpawners.add(new EntitySpawner(1, this::createDummyEntity, player,
                0, 1, 0, 0, 0));
        entitySpawners.add(new EntitySpawner(1, this::createDummyEntity, player,
                0, 1, 1, 0, 0));
        entitySpawners.add(new EntitySpawner(1, this::createDummyEntity, player,
                0, 1, 2, 0, 0));
        entitiesSpawner = new EntitiesSpawner(entitySpawners);
        entitiesSpawner.setGameAreas(area);

        entitiesSpawner.startPeriodicSpawning();
        assertEquals(initialEntityCount, getDummyEntityCount());

        timeService.setHour(0);
        assertEquals(initialEntityCount + 1, getDummyEntityCount());

        timeService.setHour(1);
        assertEquals(initialEntityCount + 2, getDummyEntityCount());

        timeService.setHour(2);
        assertEquals(initialEntityCount + 3, getDummyEntityCount());
    }

    @Test
    void checkPeriodicMultiDaySpawn() {
        entitySpawners = new ArrayList<>();
        entitySpawners.add(new EntitySpawner(1, this::createDummyEntity, player,
                0, 1, 0, 0, 1));
        entitySpawners.add(new EntitySpawner(1, this::createDummyEntity, player,
                0, 1, 0, 0, 2));
        entitySpawners.add(new EntitySpawner(1, this::createDummyEntity, player,
                0, 1, 0, 0, 3));
        entitiesSpawner = new EntitiesSpawner(entitySpawners);
        entitiesSpawner.setGameAreas(area);

        entitiesSpawner.startPeriodicSpawning();
        assertEquals(initialEntityCount, getDummyEntityCount());

        timeService.setHour(0);
        assertEquals(initialEntityCount, getDummyEntityCount());

        timeService.setHour(0);
        assertEquals(initialEntityCount + 1, getDummyEntityCount());

        timeService.setHour(0);
        assertEquals(initialEntityCount + 2, getDummyEntityCount());

        timeService.setHour(0);
        assertEquals(initialEntityCount + 4, getDummyEntityCount());

        timeService.setHour(0);
        timeService.setHour(0);
        assertEquals(initialEntityCount + 6, getDummyEntityCount());
    }

    class GameAreaSimple extends GameArea {
        @Override
        public void create() {

        }

        @Override
        public ClimateController getClimateController() {
            return null;
        }

        @Override
        public Entity getTractor() {
            return null;
        }

        @Override
        public GameMap getMap() {
            TerrainFactory terrainFactory = mock(TerrainFactory.class);
            doReturn(new GridPoint2(1, 1)).when(terrainFactory).getMapSize();
            GameMap gameMap = new GameMap(terrainFactory);
            setupGameMap(gameMap);
            return gameMap;
        }

        public void setTerrain(TerrainComponent terrain) {
            this.terrain = terrain;
        }

        public TerrainComponent getTerrain() {
            return terrain;
        }

        private void setupGameMap(GameMap gameMap) {
            TerrainComponent terrainComponent = mock(TerrainComponent.class);

            gameMap.setTerrainComponent(terrainComponent);

            TiledMap tiledMap = gameMap.getTiledMap();

            TiledMapTileLayer layer = new TiledMapTileLayer(1, 1, 16, 16);

            layer.setCell(0, 0, new TiledMapTileLayer.Cell().setTile(pathTerrainTile));
            tiledMap.getLayers().add(layer);
        }
    }
}

