package com.csse3200.game.components.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.InteractionDetector;
import com.csse3200.game.components.npc.TamableComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.security.Provider;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Setup fields and config were taken from GameAreaTest and the authors were co-authored to share credit for the work
@ExtendWith(GameExtension.class)
public class ItemActionsTest {
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
    private Entity player;
    private Vector2 mousePos;

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

        layer.setCell(0, 0, new TiledMapTileLayer.Cell().setTile(new TerrainTile(null, TerrainTile.TerrainCategory.DIRT)));
        layer.setCell(0, 1, new TiledMapTileLayer.Cell().setTile(new TerrainTile(null, TerrainTile.TerrainCategory.DIRT)));
        layer.setCell(0, 2, new TiledMapTileLayer.Cell().setTile(new TerrainTile(null, TerrainTile.TerrainCategory.DIRT)));
        layer.setCell(0, 3, new TiledMapTileLayer.Cell().setTile(new TerrainTile(null, TerrainTile.TerrainCategory.DIRT)));
        layer.setCell(1, 0, new TiledMapTileLayer.Cell().setTile(new TerrainTile(null, TerrainTile.TerrainCategory.DIRT)));
        layer.setCell(1, 1, new TiledMapTileLayer.Cell().setTile(new TerrainTile(null, TerrainTile.TerrainCategory.DIRT)));
        layer.setCell(1, 2, new TiledMapTileLayer.Cell().setTile(new TerrainTile(null, TerrainTile.TerrainCategory.DIRT)));
        layer.setCell(1, 3, new TiledMapTileLayer.Cell().setTile(new TerrainTile(null, TerrainTile.TerrainCategory.DIRT)));
        layer.setCell(2, 0, new TiledMapTileLayer.Cell().setTile(new TerrainTile(null, TerrainTile.TerrainCategory.DIRT)));
        layer.setCell(2, 1, new TiledMapTileLayer.Cell().setTile(new TerrainTile(null, TerrainTile.TerrainCategory.DIRT)));
        layer.setCell(2, 2, new TiledMapTileLayer.Cell().setTile(new TerrainTile(null, TerrainTile.TerrainCategory.DIRT)));
        layer.setCell(2, 3, new TiledMapTileLayer.Cell().setTile(new TerrainTile(null, TerrainTile.TerrainCategory.DIRT)));
        layer.setCell(3, 0, new TiledMapTileLayer.Cell().setTile(new TerrainTile(null, TerrainTile.TerrainCategory.DIRT)));
        layer.setCell(3, 1, new TiledMapTileLayer.Cell().setTile(new TerrainTile(null, TerrainTile.TerrainCategory.DIRT)));
        layer.setCell(3, 2, new TiledMapTileLayer.Cell().setTile(new TerrainTile(null, TerrainTile.TerrainCategory.DIRT)));
        layer.setCell(3, 3, new TiledMapTileLayer.Cell().setTile(new TerrainTile(null, TerrainTile.TerrainCategory.DIRT)));

        tiledMap.getLayers().add(layer);

        player = new Entity(EntityType.Player).addComponent(new InteractionDetector(100)).addComponent(new HitboxComponent());
    }

    @Test
    void testUseHoe() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        mousePos = new Vector2(10,10);
        player.setPosition(gameMap.tileCoordinatesToVector(new GridPoint2(1,1)));
        CameraComponent cam = mock(CameraComponent.class);
        doReturn(player.getPosition()).when(cam).screenPositionToWorldPosition(mousePos);
        ServiceLocator.registerCameraComponent(cam);
        GameArea area = mock(GameArea.class);
        doReturn(player).when(area).getPlayer();
        doReturn(gameMap).when(area).getMap();
        ServiceLocator.registerGameArea(area);
        ServiceLocator.registerResourceService(mock(ResourceService.class));
        FileLoader fl = new FileLoader();

        Entity hoe = new Entity(EntityType.Item).addComponent(new ItemActions()).addComponent(new ItemComponent("hoe", ItemType.HOE, null));
        assertTrue(hoe.getComponent(ItemActions.class).use(player, mousePos, gameMap));
        assertFalse(hoe.getComponent(ItemActions.class).use(player, mousePos, gameMap));
    }

    @Test
    void testUseShovel() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        mousePos = new Vector2(10,10);
        player.setPosition(gameMap.tileCoordinatesToVector(new GridPoint2(1,1)));
        CameraComponent cam = mock(CameraComponent.class);
        doReturn(player.getPosition()).when(cam).screenPositionToWorldPosition(mousePos);
        ServiceLocator.registerCameraComponent(cam);
        GameArea area = mock(GameArea.class);
        doReturn(player).when(area).getPlayer();
        doReturn(gameMap).when(area).getMap();
        ServiceLocator.registerGameArea(area);
        ServiceLocator.registerResourceService(mock(ResourceService.class));
        FileLoader fl = new FileLoader();

        Entity shovel = new Entity(EntityType.Item).addComponent(new ItemActions()).addComponent(new ItemComponent("shovel", ItemType.SHOVEL, null));
        Entity hoe = new Entity(EntityType.Item).addComponent(new ItemActions()).addComponent(new ItemComponent("hoe", ItemType.HOE, null));
        assertTrue(hoe.getComponent(ItemActions.class).use(player, mousePos, gameMap));
        assertTrue(shovel.getComponent(ItemActions.class).use(player, mousePos, gameMap));
        Entity player2 = new Entity();
        player2.setPosition(player.getPosition());
        assertFalse(shovel.getComponent(ItemActions.class).use(player2, mousePos, gameMap));
        assertFalse(shovel.getComponent(ItemActions.class).use(player, mousePos, gameMap));

        Entity gate = new Entity(EntityType.Item).addComponent(new ItemActions()).addComponent(new ItemComponent("Gate", ItemType.PLACEABLE, null));
        assertTrue(gate.getComponent(ItemActions.class).use(player, mousePos, gameMap));
        assertTrue(shovel.getComponent(ItemActions.class).use(player, mousePos, gameMap));

        Entity chest = new Entity(EntityType.Item).addComponent(new ItemActions()).addComponent(new ItemComponent("Chest", ItemType.PLACEABLE, null)).addComponent(new InventoryComponent(null));
        assertTrue(chest.getComponent(ItemActions.class).use(player, mousePos, gameMap));
        assertTrue(shovel.getComponent(ItemActions.class).use(player, mousePos, gameMap));
    }

    @Test
    void testUseWater() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        mousePos = new Vector2(10,10);
        player.setPosition(gameMap.tileCoordinatesToVector(new GridPoint2(1,1)));
        CameraComponent cam = mock(CameraComponent.class);
        doReturn(player.getPosition()).when(cam).screenPositionToWorldPosition(mousePos);
        ServiceLocator.registerCameraComponent(cam);
        GameArea area = mock(GameArea.class);
        doReturn(player).when(area).getPlayer();
        doReturn(gameMap).when(area).getMap();
        ServiceLocator.registerGameArea(area);
        ServiceLocator.registerResourceService(mock(ResourceService.class));
        FileLoader fl = new FileLoader();

        Entity can = new Entity(EntityType.Item).addComponent(new ItemActions()).addComponent(new ItemComponent("can", ItemType.WATERING_CAN, null));
        Entity hoe = new Entity(EntityType.Item).addComponent(new ItemActions()).addComponent(new ItemComponent("hoe", ItemType.HOE, null));
        assertFalse(can.getComponent(ItemActions.class).use(player, mousePos, gameMap));
        assertTrue(hoe.getComponent(ItemActions.class).use(player, mousePos, gameMap));
        assertTrue(can.getComponent(ItemActions.class).use(player, mousePos, gameMap));
    }

    @Test
    void testUseScythe() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        mousePos = new Vector2(10,10);
        player.setPosition(gameMap.tileCoordinatesToVector(new GridPoint2(1,1)));
        CameraComponent cam = mock(CameraComponent.class);
        doReturn(player.getPosition()).when(cam).screenPositionToWorldPosition(mousePos);
        ServiceLocator.registerCameraComponent(cam);
        GameArea area = mock(GameArea.class);
        doReturn(player).when(area).getPlayer();
        doReturn(gameMap).when(area).getMap();
        ServiceLocator.registerGameArea(area);
        ServiceLocator.registerResourceService(mock(ResourceService.class));
        FileLoader fl = new FileLoader();

        Entity scythe = new Entity(EntityType.Item).addComponent(new ItemActions()).addComponent(new ItemComponent("scythe", ItemType.SCYTHE, null));
        Entity hoe = new Entity(EntityType.Item).addComponent(new ItemActions()).addComponent(new ItemComponent("hoe", ItemType.HOE, null));
        assertFalse(scythe.getComponent(ItemActions.class).use(player, mousePos, gameMap));
        assertTrue(hoe.getComponent(ItemActions.class).use(player, mousePos, gameMap));
        assertTrue(scythe.getComponent(ItemActions.class).use(player, mousePos, gameMap));
    }

    @Test
    void testUseSeed() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        mousePos = new Vector2(10,10);
        player.setPosition(gameMap.tileCoordinatesToVector(new GridPoint2(1,1)));
        CameraComponent cam = mock(CameraComponent.class);
        doReturn(player.getPosition()).when(cam).screenPositionToWorldPosition(mousePos);
        ServiceLocator.registerCameraComponent(cam);
        GameArea area = mock(GameArea.class);
        doReturn(player).when(area).getPlayer();
        doReturn(gameMap).when(area).getMap();
        ServiceLocator.registerGameArea(area);
        ServiceLocator.registerResourceService(mock(ResourceService.class));
        FileLoader fl = new FileLoader();

        Entity seed = new Entity(EntityType.Item).addComponent(new ItemActions()).addComponent(new ItemComponent("deadly nightshade seed", ItemType.SEED, null));
        Entity hoe = new Entity(EntityType.Item).addComponent(new ItemActions()).addComponent(new ItemComponent("hoe", ItemType.HOE, null));
        assertFalse(seed.getComponent(ItemActions.class).use(player, mousePos, gameMap));
        assertTrue(hoe.getComponent(ItemActions.class).use(player, mousePos, gameMap));
        assertTrue(seed.getComponent(ItemActions.class).use(player, mousePos, gameMap));
    }

    @Test
    void testUseFood() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        mousePos = new Vector2(10,10);
        player.setPosition(gameMap.tileCoordinatesToVector(new GridPoint2(1,1)));
        CameraComponent cam = mock(CameraComponent.class);
        doReturn(player.getPosition()).when(cam).screenPositionToWorldPosition(mousePos);
        ServiceLocator.registerCameraComponent(cam);
        GameArea area = mock(GameArea.class);
        doReturn(player).when(area).getPlayer();
        doReturn(gameMap).when(area).getMap();
        ServiceLocator.registerGameArea(area);
        ServiceLocator.registerResourceService(mock(ResourceService.class));
        FileLoader fl = new FileLoader();

        Entity food = new Entity(EntityType.Item).addComponent(new ItemActions()).addComponent(new ItemComponent("food", ItemType.FOOD, null));
        assertFalse(food.getComponent(ItemActions.class).use(new Entity(), mousePos, gameMap));
        assertFalse(food.getComponent(ItemActions.class).use(player, mousePos, gameMap));
    }

    @Test
    void testPlace() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        mousePos = new Vector2(10,10);
        player.setPosition(gameMap.tileCoordinatesToVector(new GridPoint2(1,1)));
        CameraComponent cam = mock(CameraComponent.class);
        doReturn(player.getPosition()).when(cam).screenPositionToWorldPosition(mousePos);
        ServiceLocator.registerCameraComponent(cam);
        GameArea area = mock(GameArea.class);
        doReturn(player).when(area).getPlayer();
        doReturn(gameMap).when(area).getMap();
        ServiceLocator.registerGameArea(area);
        ServiceLocator.registerResourceService(mock(ResourceService.class));
        FileLoader fl = new FileLoader();

        Entity gate = new Entity(EntityType.Item).addComponent(new ItemActions()).addComponent(new ItemComponent("Gate", ItemType.PLACEABLE, null));
        assertTrue(gate.getComponent(ItemActions.class).use(player, mousePos, gameMap));
        assertFalse(gate.getComponent(ItemActions.class).use(player, mousePos, gameMap));
    }

    @Test
    void testNoUse() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        mousePos = new Vector2(10,10);
        player.setPosition(gameMap.tileCoordinatesToVector(new GridPoint2(1,1)));
        CameraComponent cam = mock(CameraComponent.class);
        doReturn(player.getPosition()).when(cam).screenPositionToWorldPosition(mousePos);
        ServiceLocator.registerCameraComponent(cam);
        GameArea area = mock(GameArea.class);
        doReturn(player).when(area).getPlayer();
        doReturn(gameMap).when(area).getMap();
        ServiceLocator.registerGameArea(area);
        ServiceLocator.registerResourceService(mock(ResourceService.class));
        FileLoader fl = new FileLoader();

        Entity notItem = new Entity(EntityType.Item).addComponent(new ItemActions());
        assertFalse(notItem.getComponent(ItemActions.class).use(player, mousePos, gameMap));

        Entity itemWithNoUse = new Entity(EntityType.Item).addComponent(new ItemActions()).addComponent(new ItemComponent("milk", ItemType.EGG, null));
        assertFalse(itemWithNoUse.getComponent(ItemActions.class).use(player, mousePos, gameMap));
    }

    @Test
    void testNoTile() {
        TiledMap tiledMap = gameMap.getTiledMap();
        tiledMap.getLayers().remove(0);
        TiledMapTileLayer layer = new TiledMapTileLayer(4, 4, 16, 16);

        layer.setCell(0, 0, new TiledMapTileLayer.Cell().setTile(null));
        layer.setCell(0, 1, new TiledMapTileLayer.Cell().setTile(null));
        layer.setCell(0, 2, new TiledMapTileLayer.Cell().setTile(null));
        layer.setCell(0, 3, new TiledMapTileLayer.Cell().setTile(null));
        layer.setCell(1, 0, new TiledMapTileLayer.Cell().setTile(null));
        layer.setCell(1, 1, new TiledMapTileLayer.Cell().setTile(null));
        layer.setCell(1, 2, new TiledMapTileLayer.Cell().setTile(null));
        layer.setCell(1, 3, new TiledMapTileLayer.Cell().setTile(null));
        layer.setCell(2, 0, new TiledMapTileLayer.Cell().setTile(null));
        layer.setCell(2, 1, new TiledMapTileLayer.Cell().setTile(null));
        layer.setCell(2, 2, new TiledMapTileLayer.Cell().setTile(null));
        layer.setCell(2, 3, new TiledMapTileLayer.Cell().setTile(null));
        layer.setCell(3, 0, new TiledMapTileLayer.Cell().setTile(null));
        layer.setCell(3, 1, new TiledMapTileLayer.Cell().setTile(null));
        layer.setCell(3, 2, new TiledMapTileLayer.Cell().setTile(null));
        layer.setCell(3, 3, new TiledMapTileLayer.Cell().setTile(null));

        tiledMap.getLayers().add(layer);

        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        mousePos = new Vector2(10,10);
        player.setPosition(gameMap.tileCoordinatesToVector(new GridPoint2(1,1)));
        CameraComponent cam = mock(CameraComponent.class);
        doReturn(player.getPosition()).when(cam).screenPositionToWorldPosition(mousePos);
        ServiceLocator.registerCameraComponent(cam);
        GameArea area = mock(GameArea.class);
        doReturn(player).when(area).getPlayer();
        doReturn(gameMap).when(area).getMap();
        ServiceLocator.registerGameArea(area);
        ServiceLocator.registerResourceService(mock(ResourceService.class));
        FileLoader fl = new FileLoader();

        Entity gate = new Entity(EntityType.Item).addComponent(new ItemActions()).addComponent(new ItemComponent("Gate", ItemType.PLACEABLE, null));
        assertFalse(gate.getComponent(ItemActions.class).use(player, mousePos, gameMap));
    }
}
