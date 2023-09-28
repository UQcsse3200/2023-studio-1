package com.csse3200.game.components.combat;

import com.csse3200.game.components.npc.AnimalAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.AnimationRenderComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class ProjectileAnimationControllerTest {
    private Entity entity;
    private ProjectileAnimationController controller;
    private AnimationRenderComponent animator;

    @BeforeEach
    void beforeEach() {
        controller = new ProjectileAnimationController();
        animator = mock(AnimationRenderComponent.class);

        entity = new Entity()
                .addComponent(controller)
                .addComponent(animator);
        entity.create();
    }

    @Test
    void checkFlightInitialised() {
        verify(animator).startAnimation("flight");
    }

    @Test
    void checkTriggersAnimations() {
        entity.getEvents().trigger("flightStart");

        // account for start flight animation
        verify(animator, times(2)).startAnimation("flight");

        entity.getEvents().trigger("impactStart");
        verify(animator).startAnimation("impact");
    }
}
