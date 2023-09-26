package com.csse3200.game.components.player;

import com.badlogic.gdx.Gdx;
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
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.csse3200.game.events.EventHandler;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests for the PlayerHighlightComponent
 */
@ExtendWith(GameExtension.class)
public class PlayerHighlightComponentTest {
    private Entity playerHighlight; // the playerHighlight entity

    /**
     * Sets up the playerHighlight entity before each test with the component and creates the entity.
     */
    @BeforeEach
    public void setup() {
        playerHighlight = new Entity().addComponent(new PlayerHighlightComponent());
        playerHighlight.create();
    }

    /**
     * Tests that the playerHighlight entity is created and that the events are not null
     */
    @Test
    public void create() {
        assertNotNull(playerHighlight.getComponent(PlayerHighlightComponent.class));
        EventHandler events = playerHighlight.getEvents();
        assertNotNull(events);
    }

    /**
     * Tests that the playerHighlight is muted and unmuted correctly
     */
    @Test
    public void isMuted() {
        assertFalse(playerHighlight.getComponent(PlayerHighlightComponent.class).isMuted());
        playerHighlight.getComponent(PlayerHighlightComponent.class).mute();
        assertTrue(playerHighlight.getComponent(PlayerHighlightComponent.class).isMuted());
        playerHighlight.getComponent(PlayerHighlightComponent.class).unMute();
        assertFalse(playerHighlight.getComponent(PlayerHighlightComponent.class).isMuted());
    }

    /**
     * Tests that the playerHighlight is updating its position
     * based off mouse input correctly.
     */
    @Test
    public void updatePosition() {

        GameMap gameMap;
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        doReturn(new GridPoint2(4, 4)).when(terrainFactory).getMapSize();

        TerrainComponent terrainComponent = mock(TerrainComponent.class);
        doReturn(0.5f).when(terrainComponent).getTileSize();

        gameMap = new GameMap(terrainFactory);
        gameMap.setTerrainComponent(terrainComponent);

        TiledMap tiledMap = gameMap.getTiledMap();

        Entity player = new Entity(EntityType.Player).addComponent(new InteractionDetector(1));

        playerHighlight.setPosition(new Vector2(1,1));
        Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        player.setPosition(gameMap.tileCoordinatesToVector(new GridPoint2(2,2)));
        CameraComponent cam = mock(CameraComponent.class);
        doReturn(player.getPosition()).when(cam).screenPositionToWorldPosition(mousePos);
        ServiceLocator.registerCameraComponent(cam);

        GameArea area = mock(GameArea.class);
        doReturn(player).when(area).getPlayer();
        doReturn(gameMap).when(area).getMap();
        ServiceLocator.registerGameArea(area);
        ServiceLocator.registerResourceService(mock(ResourceService.class));


        playerHighlight.getComponent(PlayerHighlightComponent.class).updatePosition();
        assertEquals(playerHighlight.getPosition(), new Vector2(1.5f,0.5f));


        doReturn(new Vector2 (0,1)).when(cam).screenPositionToWorldPosition(mousePos);
        playerHighlight.getComponent(PlayerHighlightComponent.class).updatePosition();
        assertEquals(playerHighlight.getPosition(), new Vector2(0.5f,0.5f));

        doReturn(new Vector2 (1,0)).when(cam).screenPositionToWorldPosition(mousePos);
        playerHighlight.getComponent(PlayerHighlightComponent.class).updatePosition();
        assertEquals(playerHighlight.getPosition(), new Vector2(1.5f,0.5f));

        doReturn(new Vector2 (1,1)).when(cam).screenPositionToWorldPosition(mousePos);
        playerHighlight.getComponent(PlayerHighlightComponent.class).updatePosition();
        assertEquals(playerHighlight.getPosition(), new Vector2(1.5f,0.5f));

        doReturn(new Vector2 (0,0)).when(cam).screenPositionToWorldPosition(mousePos);
        playerHighlight.getComponent(PlayerHighlightComponent.class).updatePosition();
        assertEquals(playerHighlight.getPosition(), new Vector2(0.5f,0.5f));

        playerHighlight.update();
        assertEquals(playerHighlight.getPosition(), new Vector2(0.5f,0.5f));
    }

    @Test
    public void getTexturePath() {
        assertEquals("images/yellowSquare.png", playerHighlight.getComponent(PlayerHighlightComponent.class).getTexturePath());
    }


}