package com.csse3200.game.areas.terrain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
public class CropTileComponentTest {
    private Entity cropTile1, cropTile2, cropTile3, cropTile4, cropTile5;
    @BeforeEach
    public void init() {
        cropTile1 = new Entity().addComponent(new CropTileComponent(1f, 0.5f));
        cropTile2 = new Entity().addComponent(new CropTileComponent(0.5f, 0.5f));
        cropTile3 = new Entity().addComponent(new CropTileComponent(1f, 1.0f));
        cropTile4 = new Entity().addComponent(new CropTileComponent(0f, 1.0f));
        cropTile5 = new Entity().addComponent(new CropTileComponent(1f, 0.0f));
        cropTile1.create();
        cropTile2.create();
        cropTile3.create();
        cropTile4.create();
        cropTile5.create();
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
    public void testIsOccupied1() {
        assertFalse(cropTile1.getComponent(CropTileComponent.class).isOccupied());
    }

}
