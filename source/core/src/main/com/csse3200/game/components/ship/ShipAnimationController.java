package com.csse3200.game.components.ship;

import java.util.Set;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.ship.ShipProgressComponent.Feature;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * the ShipAnimationController
 */
public class ShipAnimationController extends Component {
    /**
     * The animation Component of the Ship entity
     */
    AnimationRenderComponent animator;

    @Override
    /**
     * Create the ShipAnimationController
     */
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("progressUpdated", this::animateShipStage);
    }

    /**
     * Changes the stage of the ship to match progress, progress from
     * ShipProgressComponent
     */
    private void animateShipStage(int progress, Set<Feature> unlocked_features) {
        String animation = "default";

        //TODO change states to match final amount of progress
        if (progress >= 0 && progress < 3) {
            animation = "ship_0";
        }

        if (progress >= 3 && progress < 5) {
            animation = "ship_1";
        }

        if (progress >= 5 && progress < 8) {
            animation = "ship_2";
        }

        if (progress >= 8 && progress < 15) {
            animation = "ship_3";
        }

        if (progress >= 15 && progress < 20) {
            animation = "ship_4";
        }

        if (progress >= 25) {
            animation = "ship_5";
        }

        if (!animator.getCurrentAnimation().equals(animation)) {
            animator.startAnimation(animation);
        }
    }

}
