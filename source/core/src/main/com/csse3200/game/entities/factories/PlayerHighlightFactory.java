package com.csse3200.game.entities.factories;

import com.csse3200.game.components.player.PlayerHighlightComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;

/**
 * Factory for creating the player highlight entity
 */
public class PlayerHighlightFactory {
    /**
     * Creates the player highlight entity
     * @return the player highlight entity
     */
    public static Entity createPlayerHighlight() {
        DynamicTextureRenderComponent renderComponent = new DynamicTextureRenderComponent("images/yellowSquare.png");
        renderComponent.setLayer(0);
        return new Entity()
                .addComponent(new PlayerHighlightComponent())
                .addComponent(renderComponent);
    }
}