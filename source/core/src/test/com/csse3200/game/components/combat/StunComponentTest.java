package com.csse3200.game.components.combat;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StunComponentTest {
    private Entity entity;
    private StunComponent stunComponent;
    private GameTime gameTime;


    @BeforeEach
    void setUp() {
        gameTime = mock(GameTime.class);
        when(gameTime.getTime()).thenReturn(0L);
        when(gameTime.getDeltaTime()).thenReturn(0.25f);
        ServiceLocator.registerTimeSource(gameTime);

        GameArea mockArea = mock(GameArea.class);
        when(mockArea.getMap()).thenReturn(mock(GameMap.class));
        ServiceLocator.registerGameArea(mockArea);

        stunComponent = new StunComponent();
        entity = new Entity()
                .addComponent(stunComponent)
                .addComponent(new PhysicsMovementComponent());
        entity.create();
    }

    @Test
    void testSetStun() {
        // check not stunned at start
        assertFalse(stunComponent.isStunned());

        entity.getEvents().trigger("setStun", true);
        assertTrue(stunComponent.isStunned());

        entity.getEvents().trigger("setStun", false);
        assertFalse(stunComponent.isStunned());
    }

    @Test
    void testSetStunDuration() {
        entity.getEvents().trigger("triggerStunDuration", 1f);

        assertTrue(stunComponent.isStunned());
        when(gameTime.getTime()).thenReturn(500L);

        entity.update();
        assertTrue(stunComponent.isStunned());

        when(gameTime.getTime()).thenReturn(1000L);

        entity.update();
        assertFalse(stunComponent.isStunned());
    }
}
