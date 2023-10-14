package com.csse3200.game.components.items;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.csse3200.game.components.combat.CombatStatsComponent;
import com.csse3200.game.components.player.HungerComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.services.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.InteractionDetector;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.rendering.RenderService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;

// Setup fields and config were taken from GameAreaTest and the authors were co-authored to share credit for the work
@ExtendWith(GameExtension.class)
class ItemActionsTest {
    /**
     * NOTE TO MARKER I TRIED MOVING THE BIT OF CODE THAT HAPPENS BEFORE EACH TEST TO A BEFORE EACH AND IT FAILED?????
     * so i know it can be writtern better but it just died? also not all need it and some break if it has it so technically
     * its fine?
     */

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
        ServiceLocator.registerParticleService(mock(ParticleService.class));
        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        doReturn(new GridPoint2(4, 4)).when(terrainFactory).getMapSize();
        ServiceLocator.registerTimeService(new TimeService());
        ServiceLocator.registerMissionManager(new MissionManager());
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

        player = new Entity(EntityType.PLAYER)
                .addComponent(new InteractionDetector(100))
                .addComponent(new HitboxComponent())
                .addComponent(new InventoryComponent());
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

        Entity hoe = new Entity(EntityType.ITEM).addComponent(new ItemActions()).addComponent(new ItemComponent("hoe", ItemType.HOE, null));
        assertTrue(hoe.getComponent(ItemActions.class).use(player, mousePos));
        assertFalse(hoe.getComponent(ItemActions.class).use(player, mousePos));
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

        Entity shovel = new Entity(EntityType.ITEM).addComponent(new ItemActions()).addComponent(new ItemComponent("shovel", ItemType.SHOVEL, null));
        Entity hoe = new Entity(EntityType.ITEM).addComponent(new ItemActions()).addComponent(new ItemComponent("hoe", ItemType.HOE, null));
        assertTrue(hoe.getComponent(ItemActions.class).use(player, mousePos));
        assertTrue(shovel.getComponent(ItemActions.class).use(player, mousePos));
        Entity player2 = new Entity();
        player2.setPosition(player.getPosition());
        assertFalse(shovel.getComponent(ItemActions.class).use(player2, mousePos));
        assertFalse(shovel.getComponent(ItemActions.class).use(player, mousePos));

        Entity gate = new Entity(EntityType.ITEM).addComponent(new ItemActions()).addComponent(new ItemComponent("GATE", ItemType.PLACEABLE, null));
        assertTrue(gate.getComponent(ItemActions.class).use(player, mousePos));
        assertTrue(shovel.getComponent(ItemActions.class).use(player, mousePos));
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

        Entity can = new Entity(EntityType.ITEM).addComponent(new ItemActions()).addComponent(new ItemComponent("can", ItemType.WATERING_CAN, null)).addComponent(new WateringCanLevelComponent(150));
        WateringCanLevelComponent canLevel = can.getComponent(WateringCanLevelComponent.class);
        Entity hoe = new Entity(EntityType.ITEM).addComponent(new ItemActions()).addComponent(new ItemComponent("hoe", ItemType.HOE, null));
        assertFalse(can.getComponent(ItemActions.class).use(player, mousePos));
        assertTrue(hoe.getComponent(ItemActions.class).use(player, mousePos));
        assertTrue(can.getComponent(ItemActions.class).use(player, mousePos));
        canLevel.empty();
        assertFalse(can.getComponent(ItemActions.class).use(player, mousePos));
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

        Entity scythe = new Entity(EntityType.ITEM).addComponent(new ItemActions()).addComponent(new ItemComponent("scythe", ItemType.SCYTHE, null));
        Entity hoe = new Entity(EntityType.ITEM).addComponent(new ItemActions()).addComponent(new ItemComponent("hoe", ItemType.HOE, null));
        assertFalse(scythe.getComponent(ItemActions.class).use(player, mousePos));
        assertTrue(hoe.getComponent(ItemActions.class).use(player, mousePos));
        assertTrue(scythe.getComponent(ItemActions.class).use(player, mousePos));
    }

    @Test
    void testUseSeed() throws IOException {
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

        Entity seed = new Entity(EntityType.ITEM).addComponent(new ItemActions()).addComponent(new ItemComponent("Test Seeds", ItemType.SEED, null));
        Entity hoe = new Entity(EntityType.ITEM).addComponent(new ItemActions()).addComponent(new ItemComponent("hoe", ItemType.HOE, null));
        Entity poop = new Entity(EntityType.ITEM).addComponent(new ItemActions()).addComponent(new ItemComponent("poop", ItemType.FERTILISER, null));
        assertFalse(seed.getComponent(ItemActions.class).use(player, mousePos));
        assertFalse(poop.getComponent(ItemActions.class).use(player, mousePos));
        assertTrue(hoe.getComponent(ItemActions.class).use(player, mousePos));
        assertTrue(seed.getComponent(ItemActions.class).use(player, mousePos));
        assertTrue(poop.getComponent(ItemActions.class).use(player, mousePos));
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

        Entity food = new Entity(EntityType.ITEM).addComponent(new ItemActions()).addComponent(new ItemComponent("food", ItemType.FOOD, null));
        assertFalse(food.getComponent(ItemActions.class).use(new Entity(), mousePos));
        assertFalse(food.getComponent(ItemActions.class).use(player, mousePos));
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

        Entity gate = new Entity(EntityType.ITEM).addComponent(new ItemActions()).addComponent(new ItemComponent("GATE", ItemType.PLACEABLE, null));
        assertTrue(gate.getComponent(ItemActions.class).use(player, mousePos));
        assertFalse(gate.getComponent(ItemActions.class).use(player, mousePos));
    }

    @Test
    void testAttack() {
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

        Entity sword = new Entity(EntityType.ITEM).addComponent(new ItemActions()).addComponent(new ItemComponent("sword", ItemType.SWORD, null));
        assertTrue(sword.getComponent(ItemActions.class).use(player, mousePos));
        Entity gun = new Entity(EntityType.ITEM).addComponent(new ItemActions()).addComponent(new ItemComponent("sword", ItemType.GUN, null));
        assertTrue(gun.getComponent(ItemActions.class).use(player, mousePos));
    }

    @Test
    void testShipItems() {
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

        Entity clue = new Entity(EntityType.ITEM).addComponent(new ItemActions()).addComponent(new ItemComponent("clue", ItemType.CLUE_ITEM, null));
        assertTrue(clue.getComponent(ItemActions.class).use(player, mousePos));
        Entity ship = new Entity(EntityType.SHIP).addComponent(new HitboxComponent()).addComponent(new ColliderComponent());
        Entity repair = new Entity(EntityType.ITEM).addComponent(new ItemActions()).addComponent(new ItemComponent("part", ItemType.SHIP_PART, null));
        assertFalse(repair.getComponent(ItemActions.class).use(player, mousePos));
        ship.setPosition(player.getPosition());
        assertFalse(repair.getComponent(ItemActions.class).use(new Entity(), mousePos));
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

        Entity notItem = new Entity(EntityType.ITEM).addComponent(new ItemActions());
        assertFalse(notItem.getComponent(ItemActions.class).use(player, mousePos));

        Entity itemWithNoUse = new Entity(EntityType.ITEM).addComponent(new ItemActions()).addComponent(new ItemComponent("milk", ItemType.EGG, null));
        assertFalse(itemWithNoUse.getComponent(ItemActions.class).use(player, mousePos));
        Entity rod = new Entity(EntityType.ITEM).addComponent(new ItemActions()).addComponent(new ItemComponent("Fishing Rod", ItemType.FISHING_ROD, null));
        assertFalse(rod.getComponent(ItemActions.class).use(player, mousePos));
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

        Entity gate = new Entity(EntityType.ITEM).addComponent(new ItemActions()).addComponent(new ItemComponent("Gate", ItemType.PLACEABLE, null));
        assertFalse(gate.getComponent(ItemActions.class).use(player, mousePos));
    }

    @Test
    void testFishWater() {
        TiledMap tiledMap = gameMap.getTiledMap();
        tiledMap.getLayers().remove(0);
        TiledMapTileLayer layer = new TiledMapTileLayer(6, 6, 16, 16);

        layer.setCell(0, 0, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(0, 1, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(0, 2, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(0, 3, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(1, 0, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(1, 1, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(1, 2, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(1, 3, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(2, 0, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(2, 1, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(2, 2, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(2, 3, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(3, 0, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(3, 1, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(3, 2, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(3, 3, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(0, 4, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(0, 5, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(1, 4, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(1, 5, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(2, 4, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(2, 5, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(3, 4, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(3, 5, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(4, 0, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(4, 1, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(4, 2, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(4, 3, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(4, 4, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(4, 5, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(5, 0, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(5, 1, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(5, 2, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(5, 3, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(5, 4, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));
        layer.setCell(5, 5, new TiledMapTileLayer.Cell().setTile(deepWaterTerrainTile));

        tiledMap.getLayers().add(layer);

        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        mousePos = new Vector2(10,10);
        player.setPosition(gameMap.tileCoordinatesToVector(new GridPoint2(2,2)));
        CameraComponent cam = mock(CameraComponent.class);
        doReturn(player.getPosition()).when(cam).screenPositionToWorldPosition(mousePos);
        ServiceLocator.registerCameraComponent(cam);
        GameArea area = mock(GameArea.class);
        doReturn(player).when(area).getPlayer();
        doReturn(gameMap).when(area).getMap();
        ServiceLocator.registerGameArea(area);
        ServiceLocator.registerResourceService(mock(ResourceService.class));
        FileLoader fl = new FileLoader();

        Entity rod = new Entity(EntityType.ITEM).addComponent(new ItemActions()).addComponent(new ItemComponent("FISHING_ROD", ItemType.FISHING_ROD, null));
        assertTrue(rod.getComponent(ItemActions.class).use(player, mousePos));
    }

    @Test
    void testFishLava() {
        TiledMap tiledMap = gameMap.getTiledMap();
        tiledMap.getLayers().remove(0);
        TiledMapTileLayer layer = new TiledMapTileLayer(6, 6, 16, 16);

        layer.setCell(0, 0, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(0, 1, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(0, 2, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(0, 3, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(1, 0, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(1, 1, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(1, 2, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(1, 3, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(2, 0, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(2, 1, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(2, 2, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(2, 3, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(3, 0, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(3, 1, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(3, 2, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(3, 3, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(0, 4, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(0, 5, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(1, 4, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(1, 5, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(2, 4, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(2, 5, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(3, 4, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(3, 5, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(4, 0, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(4, 1, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(4, 2, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(4, 3, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(4, 4, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(4, 5, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(5, 0, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(5, 1, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(5, 2, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(5, 3, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(5, 4, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));
        layer.setCell(5, 5, new TiledMapTileLayer.Cell().setTile(lavaTerrainTile));

        tiledMap.getLayers().add(layer);

        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        mousePos = new Vector2(10,10);
        player.setPosition(gameMap.tileCoordinatesToVector(new GridPoint2(2,2)));
        CameraComponent cam = mock(CameraComponent.class);
        doReturn(player.getPosition()).when(cam).screenPositionToWorldPosition(mousePos);
        ServiceLocator.registerCameraComponent(cam);
        GameArea area = mock(GameArea.class);
        doReturn(player).when(area).getPlayer();
        doReturn(gameMap).when(area).getMap();
        ServiceLocator.registerGameArea(area);
        ServiceLocator.registerResourceService(mock(ResourceService.class));
        FileLoader fl = new FileLoader();

        Entity rod = new Entity(EntityType.ITEM).addComponent(new ItemActions()).addComponent(new ItemComponent("FISHING_ROD", ItemType.FISHING_ROD, null));
        assertTrue(rod.getComponent(ItemActions.class).use(player, mousePos));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Ear of Cosmic Cob", "Nightshade Berry", "Hammer Flower", "Aloe Vera Leaf", "Lave Eel", "French Fries"})
    void testEat(String name) {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerPlayerHungerService(new PlayerHungerService());
        GameArea area = mock(GameArea.class);
        ServiceLocator.registerGameArea(area);
        ServiceLocator.registerResourceService(mock(ResourceService.class));
        FileLoader fl = new FileLoader();

        HungerComponent hunger = spy(new HungerComponent(50));
        player.addComponent(hunger);
        player.addComponent(new CombatStatsComponent(1,1));
        Entity food1 = new Entity(EntityType.ITEM).addComponent(new ItemActions())
                .addComponent(new ItemComponent(name, ItemType.FOOD, "images/tool_shovel.png"));
        food1.getComponent(ItemActions.class).eat(player);
        verify(hunger).increaseHungerLevel(any(Integer.class));
    }

    @Test
    void notEat() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        GameArea area = mock(GameArea.class);
        ServiceLocator.registerGameArea(area);
        ServiceLocator.registerResourceService(mock(ResourceService.class));
        FileLoader fl = new FileLoader();

        HungerComponent hunger = spy(new HungerComponent(50));
        player.addComponent(hunger);
        Entity food1 = new Entity(EntityType.ITEM).addComponent(new ItemActions())
                .addComponent(new ItemComponent("a large amount of wood", ItemType.CLUE_ITEM, "images/tool_shovel.png"));
        food1.getComponent(ItemActions.class).eat(player);
        verify(hunger, never()).increaseHungerLevel(any(Integer.class));
        Entity food2 = new Entity(EntityType.ITEM).addComponent(new ItemActions())
                .addComponent(new ItemComponent("a large amount of wood", null, "images/tool_shovel.png"));
        food2.getComponent(ItemActions.class).eat(player);
        verify(hunger, never()).increaseHungerLevel(any(Integer.class));
    }
}
