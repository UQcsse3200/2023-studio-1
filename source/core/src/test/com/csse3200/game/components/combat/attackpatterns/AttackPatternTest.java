package com.csse3200.game.components.combat.attackpatterns;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.components.InteractionDetector;
import com.csse3200.game.components.combat.ProjectileComponent;
import com.csse3200.game.components.combat.attackpatterns.AttackPatternComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class AttackPatternTest {

    private AttackPatternComponent attackPatternComponent;
    private Entity entity;
    private InteractionDetector interactionDetector;
    private GameTime gameTime;




    @BeforeEach
    void setUp() {
        // Set up game time
        gameTime = mock(GameTime.class);
        when(gameTime.getTime()).thenReturn(0L);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerGameArea(mock(GameArea.class));
        ServiceLocator.registerResourceService(mock(ResourceService.class));


        interactionDetector = spy(new InteractionDetector(1.0f));
        entity = new Entity()
                .addComponent(interactionDetector)
                .addComponent(new PhysicsComponent());

        when(interactionDetector.getEntitiesInRange()).thenReturn(List.of(new Entity()));
    }

    @Test
    void testAttackLoopRuns() {
        attackPatternComponent = spy(getAttackPatternComponent(null));
        entity.addComponent(attackPatternComponent);
        entity.create();

        // start attack loop when entity is detected
        entity.getEvents().trigger("entityDetected", mock(Entity.class));

        // check runs attack
        verify(attackPatternComponent, times(1)).attack();

        // trigger more 9 attacks
        for (int i = 0; i < 9; i++) {
            when(gameTime.getTime()).thenReturn(1000L + 1000L * i);
            entity.update();
        }

        // check runs attack 10 times
        verify(attackPatternComponent, times(10)).attack();
    }

    @Test
    void testOxygenEaterAttack() {
        attackPatternComponent = spy(getAttackPatternComponent(EntityType.OxygenEater));

        entity.addComponent(attackPatternComponent);
        entity.create();

        // start attack loop when entity is detected
        entity.getEvents().trigger("entityDetected", mock(Entity.class));
        entity.update();

        // check attack starts before shoot
        verify(attackPatternComponent, times(1)).attack();

        // check shoot by checking if entities are added to GameArea
        verify(ServiceLocator.getGameArea(), times(0)).spawnEntity(any());

        // wait for delay
        when(gameTime.getTime()).thenReturn(200L);
        entity.update();

        // verify projectile spawns
        verify(ServiceLocator.getGameArea(), times(1)).spawnEntity(any());

        // pass 9 seconds, with entity exiting range after 3 seconds
        for (int i = 0; i < 9; i++) {
            when(gameTime.getTime()).thenReturn(1000L + 1000L * i);
            if (i == 3) {
                when(interactionDetector.getEntitiesInRange()).thenReturn(new ArrayList<>());
            }
            entity.update();
        }

        // check only 3 more projectiles spawn
        verify(ServiceLocator.getGameArea(), times(4)).spawnEntity(any());
    }

    @Test
    void testDragonflyAttack() {
        attackPatternComponent = spy(getAttackPatternComponent(EntityType.Dragonfly));

        entity.addComponent(attackPatternComponent);
        entity.create();

        // start attack loop when entity is detected
        entity.getEvents().trigger("entityDetected", mock(Entity.class));
        entity.update();

        // check attack starts before shoot
        verify(attackPatternComponent, times(1)).attack();

        // check shoot by checking if entities are added to GameArea
        verify(ServiceLocator.getGameArea(), times(0)).spawnEntity(any());

        // wait for delay
        when(gameTime.getTime()).thenReturn(500L);
        entity.update();

        // verify 3 projectiles spawn
        verify(ServiceLocator.getGameArea(), times(3)).spawnEntity(any());

        // pass 9 seconds, with entity exiting range after 3 seconds
        for (int i = 0; i < 9; i++) {
            when(gameTime.getTime()).thenReturn(1000L + 1000L * i);
            if (i == 3) {
                when(interactionDetector.getEntitiesInRange()).thenReturn(new ArrayList<>());
            }
            entity.update();
        }

        // check only 9 more projectiles spawn
        verify(ServiceLocator.getGameArea(), times(12)).spawnEntity(any());
    }


    private AttackPatternComponent getAttackPatternComponent(EntityType type) {
        if (type == EntityType.Dragonfly) {
            return new DragonflyAttackPattern(1f, this::createMockProjectile);
        } else if (type == EntityType.OxygenEater) {
            return new OxygenEaterAttackPattern(1f, this::createMockProjectile);
        }

        return new AttackPatternComponent(1f);
    }

    private Entity createMockProjectile() {
        Entity projectile = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ProjectileComponent(1f));

        projectile.create();

        return projectile;
    }
}
