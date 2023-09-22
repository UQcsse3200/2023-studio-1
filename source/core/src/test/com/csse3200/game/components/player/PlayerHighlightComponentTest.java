package com.csse3200.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.csse3200.game.events.EventHandler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
}