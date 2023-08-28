package com.csse3200.game.components.tractor;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class TractorAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    /**
     * Creates the tractor animation controller component.
     */
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("stopMoving", this::animateStopMoving);
        entity.getEvents().addListener("startMoving", this::animateMoving);

        animator.startAnimation("move_right_tool");
    }

    /**
     * Animates the tractor to stop moving.
     *
     * @param tool tool to animate
     */
    void animateStopMoving(String tool) {
        animator.startAnimation(String.format("move_stop_tool"));
    }

    /**
     * Animates the tractor to start moving.
     */
    void animateMoving() {
        animator.startAnimation(String.format("move_right_tool"));
    }
}
