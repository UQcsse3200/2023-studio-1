package com.csse3200.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.tasks.FollowTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputService;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerActionsTest {
    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, game time
        PhysicsService physicsService = new PhysicsService();

        ServiceLocator.registerTimeSource(new GameTime());

        ServiceLocator.registerPhysicsService(physicsService);

        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());

        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerTimeService(new TimeService());
        ServiceLocator.registerPlanetOxygenService(new PlanetOxygenService());

        ServiceLocator.registerGameArea(mock(GameArea.class));
    }

    @Test
    void playerShouldAttackNPC() {
        Entity target = new Entity();
        target.addComponent(new PlayerActions());
        PlayerActions playerActions = target.getComponent(PlayerActions.class);
    }

}
