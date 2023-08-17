package com.csse3200.game.entities.factories;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class ToolFactory {

    // Base tool to build upon
    public static Entity createBaseTool() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent());
    }

    //TODO: do we need to config physics component?
    //TODO: how does the tool interact with the player? + how can we test the tool (put in players hand?)

    public static Entity createTestTool() {
        Entity testTool = createBaseTool()
                .addComponent(new TextureRenderComponent("images/tool_shovel.png"));
        testTool.scaleHeight(1f); // scales the entity to height 1
        return testTool;
    }

    // Hoe tool
    public static Entity createHoe() {
        return  createBaseTool()
                .addComponent(new TextureRenderComponent("images/tool_hoe.png"));
    }
}
