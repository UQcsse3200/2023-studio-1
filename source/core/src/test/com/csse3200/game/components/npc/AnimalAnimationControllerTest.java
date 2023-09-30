package com.csse3200.game.components.npc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.utils.DirectionUtils;

@ExtendWith(GameExtension.class)
class AnimalAnimationControllerTest {
    private Entity entity;
    private AnimalAnimationController controller;
    private AnimationRenderComponent animator;

    @BeforeEach
    void beforeEach() {
        controller = new AnimalAnimationController();
        animator = mock(AnimationRenderComponent.class);

        entity = new Entity()
                .addComponent(controller)
                .addComponent(animator);
        entity.create();
    }

    @Test
    void checkIdleInitialised() {
        verify(animator).startAnimation("idle_right");
    }

    @Test
    void checkTriggersAnimations() {
        entity.getEvents().trigger("walkStart");
        verify(animator).startAnimation("walk_right");

        entity.getEvents().trigger("runStart");
        verify(animator).startAnimation("run_right");

        entity.getEvents().trigger("idleStart");
        // account for start idle animation
        verify(animator, times(2)).startAnimation("idle_right");
    }

    @Test
    void checkDirectionChangeRetriggersAnimation() {
        entity.getEvents().trigger("runStart");
        verify(animator).startAnimation("run_right");

        entity.getEvents().trigger("directionChange", DirectionUtils.LEFT);
        verify(animator).startAnimation("run_left");

        entity.getEvents().trigger("walkStart");
        verify(animator).startAnimation("walk_left");
    }
}
