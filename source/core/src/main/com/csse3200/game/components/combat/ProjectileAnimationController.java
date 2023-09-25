package com.csse3200.game.components.combat;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class ProjectileAnimationController extends Component {
    protected AnimationRenderComponent animator;

    @Override
    public void create() {
        animator = this.entity.getComponent(AnimationRenderComponent.class);

        entity.getEvents().addListener("flightStart", this::animateFlight);
        entity.getEvents().addListener("impactStart", this::animateImpact);

        animateFlight();
    }

    private void animateFlight() {

        animator.startAnimation("flight");
    }

    private void animateImpact() {
        animator.startAnimation("impact");
    }
}
