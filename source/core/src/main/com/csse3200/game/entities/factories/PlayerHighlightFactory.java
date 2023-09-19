package com.csse3200.game.entities.factories;

import com.csse3200.game.components.player.PlayerHighlightComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;

public class PlayerHighlightFactory {
    public static Entity createPlayerHighlight() {
        DynamicTextureRenderComponent renderComponent = new DynamicTextureRenderComponent("images/yellowSquare.png");
        return new Entity()
                .addComponent(new PlayerHighlightComponent())
                .addComponent(renderComponent);
    }
}
