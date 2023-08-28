package com.csse3200.game.areas.terrain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Function;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class CropTileComponentTest {
    private Entity cropTile1, cropTile2, cropTile3, cropTile4, cropTile5, cropTile6, cropTile7;
    @BeforeEach
    public void init() {
        cropTile1 = new Entity().addComponent(new CropTileComponent(1f, 0.5f));
        cropTile2 = new Entity().addComponent(new CropTileComponent(0.5f, 0.5f));
        cropTile3 = new Entity().addComponent(new CropTileComponent(1f, 1.0f));
        cropTile4 = new Entity().addComponent(new CropTileComponent(0f, 1.0f));
        cropTile5 = new Entity().addComponent(new CropTileComponent(1f, 0.0f));
        cropTile6 = new Entity().addComponent(new CropTileComponent(2.5f, 0.5f));
        cropTile7 = new Entity().addComponent(new CropTileComponent(-1.0f, 0.5f));
        cropTile1.create();
        cropTile2.create();
        cropTile3.create();
        cropTile4.create();
        cropTile5.create();
        cropTile6.create();
        cropTile7.create();
        /*RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);*/
        GameTime gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
        /*ServiceLocator.registerPhysicsService(new PhysicsService());*/
    }

    @Test
    public void shouldGiveExpectedGrowthRate() {
        assertEquals(0.5, cropTile1.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(0.27694, cropTile2.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(1.0, cropTile3.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(-1.0, cropTile4.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
        assertEquals(0, cropTile5.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
    }

    @Test
    public void shouldGiveWateredGrowthRate() {
        cropTile1.getEvents().trigger("water", 0.5f);
        cropTile2.getEvents().trigger("water", 0.5f);
        cropTile3.getEvents().trigger("water", 0.5f);
        cropTile4.getEvents().trigger("water", 0.5f);
        cropTile5.getEvents().trigger("water", 0.5f);
        assertEquals(0.27694, cropTile1.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(0.5, cropTile2.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(0.55387, cropTile3.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(0.55387, cropTile4.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(0, cropTile5.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
        cropTile4.getEvents().trigger("water", 0.5f);
        assertEquals(1.0, cropTile4.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        cropTile4.getEvents().trigger("water", 0.5f);
        assertEquals(0.55387, cropTile4.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        Entity dryTile = new Entity().addComponent(new CropTileComponent(0.0f, 1.0f));
        dryTile.create();
        dryTile.getEvents().trigger("water", 1.0f);
        assertEquals(1.0, dryTile.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        dryTile.getEvents().trigger("water", 1.0f);
        assertEquals(-1.0, dryTile.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
    }

    @Test
    public void shouldGiveFertilisedGrowthRate() {
        cropTile1.getEvents().trigger("fertilise");
        cropTile2.getEvents().trigger("fertilise");
        cropTile3.getEvents().trigger("fertilise");
        cropTile4.getEvents().trigger("fertilise");
        cropTile5.getEvents().trigger("fertilise");
        assertEquals(1, cropTile1.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(0.55387, cropTile2.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(2.0, cropTile3.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(-1.0, cropTile4.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
        assertEquals(0, cropTile5.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
        /* Fertilising twice should not do anything */
        cropTile1.getEvents().trigger("fertilise");
        cropTile2.getEvents().trigger("fertilise");
        cropTile3.getEvents().trigger("fertilise");
        cropTile4.getEvents().trigger("fertilise");
        cropTile5.getEvents().trigger("fertilise");
        assertEquals(1, cropTile1.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(0.55387, cropTile2.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(2.0, cropTile3.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(-1.0, cropTile4.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
        assertEquals(0, cropTile5.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
    }

    @Test
    public void testUpdate() {
        GameTime gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        cropTile1.update();
        cropTile6.update();
        cropTile7.update();
        assertEquals(-1, cropTile6.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
        assertEquals(-1, cropTile7.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
    }

    @Test
    public void testSetUnoccupied1() {
        cropTile1.getComponent(CropTileComponent.class).setUnoccupied();
        assertNull(cropTile1.getComponent(CropTileComponent.class).getPlant());
    }

    @Test
    public void testSetUnoccupied2() {
        ServiceLocator.registerEntityService(new EntityService());
        Entity plant = mock(Entity.class);
        Function<CropTileComponent, Entity> factoryMethod1 = cropTile1 -> plant;
        cropTile1.getEvents().trigger("plant", factoryMethod1);
        ServiceLocator.getEntityService().register(plant);
        cropTile1.getEvents().trigger("destroyPlant");
        cropTile1.getComponent(CropTileComponent.class).setUnoccupied();
        assertNull(cropTile1.getComponent(CropTileComponent.class).getPlant());
    }

    @Test
    public void testPlantCrop() {
        ServiceLocator.registerEntityService(new EntityService());
        Entity plant = mock(Entity.class);
        Function<CropTileComponent, Entity> factoryMethod1 = cropTile1 -> plant;
        cropTile2.getEvents().trigger("plant", factoryMethod1);
        ServiceLocator.getEntityService().register(plant);
        assertNotNull(cropTile2.getComponent(CropTileComponent.class).getPlant());
    }
}
