package com.csse3200.game.components.tasks;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.components.plants.PlantAreaOfEffectComponent;
import com.csse3200.game.components.plants.PlantComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import com.csse3200.game.services.plants.PlantInfoService;
import com.csse3200.game.utils.math.Vector2Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
public class MoveToPlantTaskTest {


    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, game time
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerTimeService(new TimeService());

    }

    @Test
    void shouldMoveTowardsTarget() {

        Entity firstTarget = new Entity(EntityType.Plant);
        firstTarget.setPosition(0f, 13f);
        Entity secondTarget = new Entity(EntityType.Plant);
        secondTarget.setPosition(0f, 5f);

        ServiceLocator.getEntityService().register(firstTarget);
        ServiceLocator.getEntityService().register(secondTarget);

        MoveToPlantTask moveToPlantTask = new MoveToPlantTask(10, Vector2Utils.ONE, 1f);

        AITaskComponent ai = new AITaskComponent().addTask(moveToPlantTask);
        Entity entity = makePhysicsEntity().addComponent(ai);
        entity.create();
        entity.setPosition(0f, 10f);

        float initialDistanceFirst = entity.getPosition().dst(firstTarget.getPosition());
        float initialDistanceSecond = entity.getPosition().dst(secondTarget.getPosition());

        // Run the game for a few cycles
        for (int i = 0; i < 3; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }

        // Check priority
        assertEquals(10, moveToPlantTask.getPriority());

        // Should move towards first plant
        float newDistanceFirst = entity.getPosition().dst(firstTarget.getPosition());
        assertTrue(newDistanceFirst < initialDistanceFirst);

        // Should've moved away from second plant
        float newDistanceSecond = entity.getPosition().dst(secondTarget.getPosition());
        assertTrue(newDistanceSecond > initialDistanceSecond);

        // Despawn first plant
        ServiceLocator.getEntityService().unregister(firstTarget);
        firstTarget.dispose();

        // Run the game for a few cycles
        for (int i = 0; i < 3; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }

        // Should've moved towards second plant
        float finalDistanceSecond = entity.getPosition().dst(secondTarget.getPosition());
        assertTrue(finalDistanceSecond < newDistanceSecond);

        // Despawn the second plant
        ServiceLocator.getEntityService().unregister(secondTarget);
        secondTarget.dispose();

        // Run the game for a few cycles
        for (int i = 0; i < 3; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }

        assertEquals(-1, moveToPlantTask.getPriority());

    }



    private Entity makePhysicsEntity() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent());
    }


}
