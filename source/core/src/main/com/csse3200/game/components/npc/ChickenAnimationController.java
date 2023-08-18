package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a chicken entity's state and plays the animation when one
 * of the events is triggered.
 * TODO: figure out if we need only AnimalAnimationController instead of each animal
 */
public class ChickenAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);



        entity.getEvents().addListener("wanderStart", this::animateWander);
        entity.getEvents().addListener("idle", this::animateIdle);
    }

    void animateWander() {
        animator.startAnimation("moveRight");
    }

    void animateIdle() { animator.startAnimation("idleRight");}
}
