package com.csse3200.game.entities;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.extensions.GameExtension;

import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;



@ExtendWith(GameExtension.class)
public class EntitySpawnerTest {

    List<EntitySpawner> toSpawn;

    EntitySpawner spawner;
    EntitiesSpawner controller;
    Entity entity;
    Entity player;

    TimeService clock;
    TimeService clockSpy;
    GameArea gameArea;
    GameMap map;

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
        gameArea = new GameArea() {
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
                        return createMap();
                    }
                };
        ServiceLocator.registerGameArea(gameArea);
        this.clock = new TimeService();
        this.clockSpy = spy(this.clock);
        ServiceLocator.registerTimeService(this.clockSpy);

        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerResourceService(new ResourceService());
        RenderService renderService = new RenderService();

        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);

        GameTime gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
    }

    private GameMap createMap() {
        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        doReturn(new GridPoint2(4, 4)).when(terrainFactory).getMapSize();
        map = new GameMap(terrainFactory);

        TiledMapTileLayer layer = new TiledMapTileLayer(4, 4, 16, 16);

        layer.setCell(0, 0, new TiledMapTileLayer.Cell().setTile(pathTerrainTile));
        layer.setCell(0, 1, new TiledMapTileLayer.Cell().setTile(pathTerrainTile));
        layer.setCell(0, 2, new TiledMapTileLayer.Cell().setTile(pathTerrainTile));
        layer.setCell(0, 3, new TiledMapTileLayer.Cell().setTile(beachSandTerrainTile));
        layer.setCell(1, 0, new TiledMapTileLayer.Cell().setTile(grassTerrainTile));
        layer.setCell(1, 1, new TiledMapTileLayer.Cell().setTile(dirtTerrainTile));
        layer.setCell(1, 2, new TiledMapTileLayer.Cell().setTile(grassTerrainTile));
        layer.setCell(1, 3, new TiledMapTileLayer.Cell().setTile(desertTerrainTile));
        layer.setCell(2, 0, new TiledMapTileLayer.Cell().setTile(snowTerrainTile));
        layer.setCell(2, 1, new TiledMapTileLayer.Cell().setTile(iceTerrainTile));
        layer.setCell(2, 2, new TiledMapTileLayer.Cell().setTile(desertTerrainTile));
        layer.setCell(2, 3, new TiledMapTileLayer.Cell().setTile(desertTerrainTile));
        layer.setCell(3, 0, new TiledMapTileLayer.Cell().setTile(grassTerrainTile));
        layer.setCell(3, 1, new TiledMapTileLayer.Cell().setTile(desertTerrainTile));
        layer.setCell(3, 2, new TiledMapTileLayer.Cell().setTile(desertTerrainTile));
        layer.setCell(3, 3, new TiledMapTileLayer.Cell().setTile(desertTerrainTile));

        TerrainComponent terrainComponent = mock(TerrainComponent.class);
        doReturn(0.5f).when(terrainComponent).getTileSize();

        map = new GameMap(terrainFactory);
        map.setTerrainComponent(terrainComponent);
        TiledMap tiledMap = map.getTiledMap();
        tiledMap.getLayers().add(layer);
        return map;
        }


    private Entity createDummyEntity(Entity player) {
        entity = new Entity();
        entity.create();
        return entity;
    }

    /**
    @Test
    void checkinitialSpawn() {
        this.toSpawn = new ArrayList<>();
        int initialSize = ServiceLocator.getEntityService().getSize();
        spawner = new EntitySpawner(10, this::createDummyEntity, player, 0, 3,
                1, 0, 2);
        this.toSpawn.add(spawner);

        controller = new EntitiesSpawner(this.toSpawn);
        controller.setGameAreas(gameArea);
        ServiceLocator.getTimeService().setHour(1);
        controller.spawnNow();
        assertTrue(initialSize < ServiceLocator.getEntityService().getSize());
    }
    */
}
