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

        animator.startAnimation("Tractor_Idle_Tool");
    }

    /**
     * Animates the tractor to stop moving.
     *
     * @param tool tool to animate
     */
    void animateStopMoving(String tool) {
        animator.startAnimation(String.format("Tractor_Idle_%s",tool));
    }

    /**
     * Animates the tractor to start moving.
     *
     * @param direction direction to animate
     * @param tool tool to animate
     */
    void animateMoving(String direction, String tool) {
        animator.startAnimation(String.format("Tractor_Move_%s_%s", direction, tool));
    }
}
