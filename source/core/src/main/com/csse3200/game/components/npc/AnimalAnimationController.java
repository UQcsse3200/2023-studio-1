package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a chicken entity's state and plays the animation when one
 * of the events is triggered.
 */
public class AnimalAnimationController extends Component {
    AnimationRenderComponent animator;
    String direction = "right";
    private static final String WALK_PREFIX = "walk";
    private static final String RUN_PREFIX = "run";
    private static final String IDLE_PREFIX = "idle";

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);

        entity.getEvents().addListener("changeDirection", this::setDirection);
        entity.getEvents().addListener("walk", this::animateWalk);
        entity.getEvents().addListener("run", this::animateRun);
        entity.getEvents().addListener("idle", this::animateIdle);
    }

    void animateWalk() {
        animator.startAnimation(WALK_PREFIX + "_" + direction);
    }

    void animateRun() {
        animator.startAnimation(RUN_PREFIX + "_" + direction);
    }

    void animateIdle() { animator.startAnimation(IDLE_PREFIX + "_" + direction);}

    void setDirection(String direction){
        this.direction = direction;
    }
}
