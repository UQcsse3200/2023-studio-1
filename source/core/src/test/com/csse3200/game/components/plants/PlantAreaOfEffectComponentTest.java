package com.csse3200.game.components.plants;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests for PlantAreaOfEffectComponent
 */
@ExtendWith({})
public class PlantAreaOfEffectComponentTest {
    PlantAreaOfEffectComponent plantAreaOfEffectComponent;

    Fixture fixture;
    Fixture fixture2;
    Entity entity;

    @BeforeEach
    public void setUp() {
        plantAreaOfEffectComponent = new PlantAreaOfEffectComponent(1f, "");

        fixture = mock(Fixture.class);
        fixture2 = mock(Fixture.class);
        entity = mock(Entity.class);
    }

    /**
     * Verifies that the returned radius is equal to the expected value.
     */
    @Test
    public void testGetRadius() {
        assertEquals(1f, plantAreaOfEffectComponent.getRadius(), "Radius should be equal to 1f");
    }

    /**
     * Verifies that the radius is correctly set and can be retrieved afterwards.
     */
    @Test
    public void testSetRadius() {
        float newRadius = 2f;
        plantAreaOfEffectComponent.setRadius(newRadius);
        assertEquals(newRadius, plantAreaOfEffectComponent.getRadius(), "Radius should be equal to 2f after setting");
    }

    /**
     * Verifies that the returned effect type is equal to the expected value.
     */
    @Test
    public void testGetEffect() {
        assertEquals("", plantAreaOfEffectComponent.getEffectType(), "Effect should be an empty string");
    }

    /**
     * Verifies that the effect type is correctly set and can be retrieved afterwards.
     */
    @Test
    public void testSetEffect() {
        String newEffect = "Fire";
        plantAreaOfEffectComponent.setEffectType(newEffect);
        assertEquals(newEffect, plantAreaOfEffectComponent.getEffectType(), "Effect should be equal to 'Fire' after setting");
    }

    @Test
    public void testOnCollisionStartReturns() {
        Fixture me = mock(Fixture.class);
        Fixture other = mock(Fixture.class);

        plantAreaOfEffectComponent.onCollisionStart(me, other);
        assertEquals(0, plantAreaOfEffectComponent.getEntitiesInRange().size());
    }

    @Test
    public void testOnCollisionEndReturns() {
        Fixture me = mock(Fixture.class);
        Fixture other = mock(Fixture.class);

        plantAreaOfEffectComponent.onCollisionEnd(me, other);
        assertEquals(0, plantAreaOfEffectComponent.getEntitiesInRange().size());
    }
    
}
