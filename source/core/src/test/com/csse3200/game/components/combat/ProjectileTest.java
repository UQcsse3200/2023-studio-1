package com.csse3200.game.components.combat;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.Vector2Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class ProjectileTest {
    private Entity projectileEntity;
    private ProjectileComponent projectileComponent;
    private PhysicsComponent physicsComponent;
    private GameTime gameTime;
    private AnimationRenderComponent mockAnimator;

    @BeforeEach
    public void setUp() {
        ServiceLocator.registerGameArea(mock(GameArea.class));

        // Set up game time
        gameTime = mock(GameTime.class);
        when(gameTime.getTime()).thenReturn(0L);
        when(gameTime.getDeltaTime()).thenReturn(0.25f);

        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());

        projectileComponent = new ProjectileComponent(2f);
        physicsComponent = new PhysicsComponent();
        mockAnimator = mock(AnimationRenderComponent.class);

        projectileEntity = new Entity()
                .addComponent(physicsComponent)
                .addComponent(projectileComponent)
                .addComponent(mockAnimator)
                .addComponent(mock(TouchAttackComponent.class));
        projectileEntity.create();

    }

    @Test
    public void testProjectileExpiresAndDestroyed() {

        // not destroyed
        verify(ServiceLocator.getGameArea(), times(0)).removeEntity(projectileEntity);

        // set game time at expiry
        when(gameTime.getTime()).thenReturn(2000L);
        projectileEntity.update();

        // check removes entity from game area
        verify(ServiceLocator.getGameArea(), times(1)).removeEntity(projectileEntity);

        // check additional triggers does not re-trigger remove
        projectileEntity.getEvents().trigger("destroyProjectile");
        verify(ServiceLocator.getGameArea(), times(1)).removeEntity(projectileEntity);
    }

    @Test
    public void testSetsTargetDirection() {
        // pushes from center position, so set target direction to right of that
        projectileComponent.setTargetDirection(projectileEntity.getCenterPosition().add(Vector2Utils.RIGHT));

        for (int i = 0; i < 3; i++) {
            ServiceLocator.getPhysicsService().getPhysics().update();
            projectileEntity.earlyUpdate();
            projectileEntity.update();
        }

        // test not at start position and moves right
        assertNotEquals(Vector2.Zero, projectileEntity.getPosition());
        assertTrue(projectileEntity.getPosition().x > 0);

        // test different direction;
        // pushes from center position, so set target direction to up of that
        projectileComponent.setTargetDirection(projectileEntity.getCenterPosition().add(Vector2Utils.UP));
        for (int i = 0; i < 3; i++) {
            ServiceLocator.getPhysicsService().getPhysics().update();
            projectileEntity.earlyUpdate();
            projectileEntity.update();
        }

        // test moves up
        assertTrue(projectileEntity.getPosition().y > 0);
    }

    @Test
    public void testSetsSpeed() {
        // pushes from center position, so set target direction to right of that
        projectileComponent.setTargetDirection(projectileEntity.getCenterPosition().add(Vector2Utils.RIGHT));

        for (int i = 0; i < 5; i++) {
            ServiceLocator.getPhysicsService().getPhysics().update();
            projectileEntity.earlyUpdate();
            projectileEntity.update();
        }

        // test not at start position and moves right
        assertNotEquals(Vector2.Zero, projectileEntity.getPosition());
        assertTrue(projectileEntity.getPosition().x > 0);

        // now increase speed and set target to left
        projectileComponent.setSpeed(new Vector2(5f, 5f));
        projectileComponent.setTargetDirection(projectileEntity.getCenterPosition().add(Vector2Utils.LEFT));

        for (int i = 0; i < 3; i++) { // update less times
            ServiceLocator.getPhysicsService().getPhysics().update();
            projectileEntity.earlyUpdate();
            projectileEntity.update();
        }

        // test more left than right because of increased speed
        assertTrue(projectileEntity.getPosition().x < 0);
    }

    @Test
    public void testSetsConstantVelocity() {
        // pushes from center position, so set target direction to right of that
        projectileComponent.setConstantVelocity(false);
        // default speed in right direction results in velocity Vector2(1, 0)
        projectileComponent.setTargetDirection(projectileEntity.getCenterPosition().add(Vector2Utils.RIGHT));

        for (int i = 0; i < 5; i++) {
            ServiceLocator.getPhysicsService().getPhysics().update();
            projectileEntity.earlyUpdate();
            projectileEntity.update();
        }

        // test that velocity reduced
        assertTrue(physicsComponent.getBody().getLinearVelocity().x < 1f);

        // set constant velocity and set velocity again
        projectileComponent.setConstantVelocity(true);
        projectileComponent.setTargetDirection(projectileEntity.getCenterPosition().add(Vector2Utils.RIGHT));

        for (int i = 0; i < 5; i++) {
            ServiceLocator.getPhysicsService().getPhysics().update();
            projectileEntity.earlyUpdate();
            projectileEntity.update();
        }

        // test that velocity still 1, 0
        assertEquals(Vector2Utils.RIGHT, physicsComponent.getBody().getLinearVelocity());
    }

    @Test
    public void testSetGetVelocity() {
        projectileComponent.setVelocity(new Vector2(5f, 5f));
        assertEquals(new Vector2(5f, 5f), projectileComponent.getVelocity());

        projectileComponent.setVelocity(new Vector2(-1f, -1f));
        assertEquals(new Vector2(-1f, -1f), projectileComponent.getVelocity());

        projectileComponent.setVelocity(new Vector2(2f, 2f));
        assertEquals(new Vector2(2f, 2f), projectileComponent.getVelocity());
    }

    @Test
    public void testImpact() {
        // test is not destroyed when impact animation is finished
        projectileComponent.setDestroyOnImpact(false);

        when(mockAnimator.isFinished()).thenReturn(false);
        projectileEntity.getEvents().trigger("impactStart");
        projectileEntity.update();

        // check does not destroy
        verify(ServiceLocator.getGameArea(), times(0)).removeEntity(projectileEntity);

        when(mockAnimator.isFinished()).thenReturn(true);
        projectileEntity.update();

        // check does not destroy
        verify(ServiceLocator.getGameArea(), times(0)).removeEntity(projectileEntity);

        // now test is destroyed on impact
        projectileComponent.setDestroyOnImpact(true);
        when(mockAnimator.isFinished()).thenReturn(false);

        projectileEntity.getEvents().trigger("impactStart");
        projectileEntity.update();

        // check does not destroy
        verify(ServiceLocator.getGameArea(), times(0)).removeEntity(projectileEntity);

        when(mockAnimator.isFinished()).thenReturn(true);
        projectileEntity.update();

        // check does destroy
        verify(ServiceLocator.getGameArea(), times(1)).removeEntity(projectileEntity);
    }
}
