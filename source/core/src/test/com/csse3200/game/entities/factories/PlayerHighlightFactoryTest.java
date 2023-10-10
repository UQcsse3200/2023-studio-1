package com.csse3200.game.entities.factories;

import com.csse3200.game.components.player.PlayerHighlightComponent;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

import com.csse3200.game.entities.Entity;



/**
 * Tests for the PlayerHighlightFactory
 */
class PlayerHighlightFactoryTest {
    /**
     * Tests that the player highlight entity is created and that the components are not null
     */
    @Test
    void testCreatePlayerHighlight() {
        Entity playerHighlight = PlayerHighlightFactory.createPlayerHighlight();

        // Check if the entity is not null
        Assertions.assertNotNull(playerHighlight);

        // Check if PlayerHighlightComponent is present
        PlayerHighlightComponent playerHighlightComponent = playerHighlight.getComponent(PlayerHighlightComponent.class);
        Assertions.assertNotNull(playerHighlightComponent);

        // Check if DynamicTextureRenderComponent is present
        DynamicTextureRenderComponent renderComponent = playerHighlight.getComponent(DynamicTextureRenderComponent.class);
        Assertions.assertNotNull(renderComponent);
    }

    /**
     * Tests that the PlayerHighlightFactory class can be created
     */
    @Test
    void testPlayerHighlightFactoryInstantiation() {
        Entity playerHighlight = PlayerHighlightFactory.createPlayerHighlight();
        Assertions.assertNotNull(playerHighlight);
    }
}