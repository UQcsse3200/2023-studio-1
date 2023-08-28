package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.utils.DirectionUtils;

/**
 * This class listens to events relevant to a chicken entity's state and plays the animation when one
 * of the events is triggered.
 */
public class AnimalAnimationController extends Component {
    AnimationRenderComponent animator;
    private String direction;
    private String currentAnimation;
    private static final String WALK_PREFIX = "walk";
    private static final String RUN_PREFIX = "run";
    private static final String IDLE_PREFIX = "idle";

    @Override
    public void create() {
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        this.direction = DirectionUtils.RIGHT;
        this.currentAnimation = IDLE_PREFIX;

        entity.getEvents().addListener("directionChange", this::changeDirection);
        entity.getEvents().addListener("walkStart", this::animateWalk);
        entity.getEvents().addListener("runStart", this::animateRun);
        entity.getEvents().addListener("idleStart", this::animateIdle);
        entity.getEvents().addListener("followStart", this::animateWalk);
        entity.getEvents().addListener("followStop", this::animateIdle);

        animateIdle();
    }

    void animateWalk() {
        animator.startAnimation(WALK_PREFIX + "_" + direction);
        currentAnimation = WALK_PREFIX;
    }

    void animateRun() {
        animator.startAnimation(RUN_PREFIX + "_" + direction);
        currentAnimation = RUN_PREFIX;
    }

    void animateIdle() {
        animator.startAnimation(IDLE_PREFIX + "_" + direction);
        currentAnimation = IDLE_PREFIX;
    }

    void changeDirection(String direction){
        this.direction = direction;
        entity.getEvents().trigger(currentAnimation + "Start");
    }

}
