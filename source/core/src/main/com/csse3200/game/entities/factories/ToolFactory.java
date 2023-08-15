package com.csse3200.game.entities.factories;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;

public class ToolFactory {

    // Base tool to build upon
    public static Entity createBaseTool() {
        Entity tool = new Entity()
                .addComponent(new PhysicsComponent());  // not sure what this does but we'll need it.
        return tool;
    }

    // Hoe tool
    public static Entity createHoe() {
        Entity hoe = createBaseTool();
        // do some more stuff here...
        /* add more components to build on baseTool:
        hoe
                .addComponent(new TextureRendererComponent("path/to/hoe_texture.png"));

         */
        return hoe;
    }
}
