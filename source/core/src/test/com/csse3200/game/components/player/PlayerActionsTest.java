package com.csse3200.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.combat.ProjectileComponent;
import com.csse3200.game.components.tasks.FollowTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.entities.factories.ProjectileFactory;
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
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PlayerActionsTest {

    private List<Entity> areaEntities;

    private Entity player;
    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, game time
        PhysicsService physicsService = new PhysicsService();

        ServiceLocator.registerTimeSource(new GameTime());

        ServiceLocator.registerPhysicsService(physicsService);

        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(mock(ResourceService.class));

        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerTimeService(new TimeService());
        ServiceLocator.registerPlanetOxygenService(new PlanetOxygenService());

        ServiceLocator.registerGameArea(mock(GameArea.class));

        areaEntities = new ArrayList<>();
        player = new Entity();
        player.addComponent(new PlayerActions());
        player.setPosition(0,0);
        Entity chicken = new Entity();
        chicken.addComponent(new CombatStatsComponent(10, 0));
        chicken.setPosition(1,1);
        areaEntities.add(player);
        areaEntities.add(chicken);

        when(ServiceLocator.getGameArea().getAreaEntities()).thenReturn(areaEntities);
        when(ServiceLocator.getResourceService().getAsset(any(), any())).thenReturn(mock(Sound.class));
        when(ServiceLocator.getCameraComponent().screenPositionToWorldPosition(any())).thenReturn(new Vector2(2, 2));
    }

    @Test
    void playerShouldAttackNPC() {
        PlayerActions playerActions = player.getComponent(PlayerActions.class);
        playerActions.attack(new Vector2(2,2));
        assertEquals(5, areaEntities.get(1).getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    void playerShouldShootNPC() {
        //doNothing().when(ProjectileFactory.createPlayerProjectile());
        //when(ProjectileFactory.createPlayerProjectile()).thenReturn(mock(Entity.class));
        //mockStatic(ProjectileFactory.class);
        //doReturn(new Entity()).when(ProjectileFactory.createPlayerProjectile());
        //when((any(Entity.class).getComponent(ProjectileComponent.class))).thenReturn(mock(ProjectileComponent.class));
        PlayerActions playerActions = player.getComponent(PlayerActions.class);
        //playerActions.shoot(new Vector2(2,2));
        //assertEquals(3, areaEntities.size());
    }

}
