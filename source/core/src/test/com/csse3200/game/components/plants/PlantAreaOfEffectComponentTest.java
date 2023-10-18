package com.csse3200.game.components.plants;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for PlantAreaOfEffectComponent class
 */
@ExtendWith(GameExtension.class)
class PlantAreaOfEffectComponentTest {
    PlantAreaOfEffectComponent plantAreaOfEffectComponent;
    Fixture fixture;
    Fixture fixture2;
    Entity entity;

    /**
     * Sets up the test environment before each test case.
     */
    @BeforeEach
    void setUp() {
        plantAreaOfEffectComponent = new PlantAreaOfEffectComponent(1f, "");

        fixture = mock(Fixture.class);
        fixture2 = mock(Fixture.class);
        entity = mock(Entity.class);
    }

    /**
     * Verifies that the returned radius is equal to the expected value.
     */
    @Test
    void testGetRadius() {
        assertEquals(1f, plantAreaOfEffectComponent.getRadius(), "Radius should be equal to 1f");
    }

    /**
     * Verifies that the radius is correctly set and can be retrieved afterwards.
     */
    @Test
    void testSetRadius() {
        float newRadius = 2f;
        plantAreaOfEffectComponent.setRadius(newRadius);
        assertEquals(newRadius, plantAreaOfEffectComponent.getRadius(), "Radius should be equal to 2f after setting");
    }

    /**
     * Verifies that the returned effect type is equal to the expected value.
     */
    @Test
    void testGetEffect() {
        assertEquals("", plantAreaOfEffectComponent.getEffectType(), "Effect should be an empty string");
    }

    /**
     * Verifies that the effect type is correctly set and can be retrieved afterwards.
     */
    @Test
    void testSetEffect() {
        String newEffect = "Fire";
        plantAreaOfEffectComponent.setEffectType(newEffect);
        assertEquals(newEffect, plantAreaOfEffectComponent.getEffectType(), "Effect should be equal to 'Fire' after setting");
    }

    /**
     * Tests the onCollisionStartReturns method when getFixture == me.
     */
    @Test
    void testOnCollisionStartReturns() {
        Fixture me = mock(Fixture.class);
        Fixture other = mock(Fixture.class);

        plantAreaOfEffectComponent.onCollisionStart(me, other);
        assertEquals(0, plantAreaOfEffectComponent.getEntitiesInRange().size());
    }

    /**
     * Tests the onCollisionEndReturns method when getFixture == me.
     */
    @Test
    void testOnCollisionEndReturns() {
        Fixture me = mock(Fixture.class);
        Fixture other = mock(Fixture.class);

        plantAreaOfEffectComponent.onCollisionEnd(me, other);
        assertEquals(0, plantAreaOfEffectComponent.getEntitiesInRange().size());
    }
    
}
