package com.csse3200.game.components.npc;

import com.csse3200.game.rendering.AnimationEffectsComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.utils.DirectionUtils;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class AnimalEffectsControllerTest {
    private Entity entity;
    private AnimalEffectsController controller;
    private AnimationEffectsComponent animator;
    private GameTime gameTime;

    @BeforeEach
    void beforeEach() {
        gameTime = mock(GameTime.class);
        when(gameTime.getTime()).thenReturn(0L);
        ServiceLocator.registerTimeSource(gameTime);

        controller = spy(new AnimalEffectsController());
        animator = mock(AnimationEffectsComponent.class);

        entity = spy(new Entity())
                .addComponent(controller)
                .addComponent(animator);
        entity.create();


    }

    @Test
    void checkTriggersAnimations() {
        entity.getEvents().trigger("startEffect", "tamed");
        verify(animator).startAnimation("heart");

        entity.getEvents().trigger("startEffect", "fed");
        verify(animator).startAnimation("fed");

        entity.getEvents().trigger("startEffect", "runAwayStart");
        verify(animator).startAnimation("exclamation");

        entity.getEvents().trigger("startEffect", "panicStart");
        verify(animator, times(2)).startAnimation("exclamation");

        entity.getEvents().trigger("startEffect", "attack");
        verify(animator).startAnimation("red_exclamation");
    }

    @Test
    void testTimedEffect() {
        entity.getEvents().trigger("startTimedEffect", "x", 1f);
        // check effect starts
        verify(controller, times(1)).startEffect("x");

        // check effect stops after 1 sec
        when(gameTime.getTime()).thenReturn(1000L);
        entity.update();

        verify(animator).stopAnimation();
    }

    @Test
    void testStopEffect() {
        entity.getEvents().trigger("startEffect", "x");
        entity.getEvents().trigger("startEffect", "y");

        // check since new animation, stop effect will not work with initial trigger
        entity.getEvents().trigger("stopEffect", "x");
        verify(animator, times(0)).stopAnimation();

        // check stop effect with current trigger stops effect
        entity.getEvents().trigger("stopEffect", "y");
        verify(animator, times(1)).stopAnimation();
    }
}
