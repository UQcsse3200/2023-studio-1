package com.csse3200.game.entities.factories;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class ToolFactory {

    // Base tool to build upon
    public static Entity createBaseTool() {
        Entity tool = new Entity()
                .addComponent(new PhysicsComponent())  /* not sure what this does but we'll need it.*/
                .addComponent(new ColliderComponent());

        return tool;
    }

    //TODO: do we need to config physics component?
    //TODO: how does the tool interact with the player? + how can we test the tool (put in players hand?)

    public static Entity createTestTool() {
        Entity testTool = createBaseTool()
                .addComponent(new TextureRenderComponent("images/tool_shovel.png"));
        //testTool.getComponent(TextureRenderComponent.class).scaleEntity();  // dont know what this does
        testTool.scaleHeight(1f); // scales the Entity!, this could be scaleWidth too
        return testTool;
    }

    // Hoe tool
    public static Entity createHoe() {
        Entity hoe = createBaseTool()
                .addComponent(new TextureRenderComponent("images/tool_hoe.png"));
        // do some more stuff here...
        /* add more components to build on baseTool:

         */
        return hoe;
    }
}
