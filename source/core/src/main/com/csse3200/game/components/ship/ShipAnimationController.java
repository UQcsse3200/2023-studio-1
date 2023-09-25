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

        //TODO change stages to match final stages
        if (progress >= 3 && progress < 5) {
            animation = "default";
        }

        if (progress >= 5 && progress < 8) {
            animation = "default";
        }

        if (progress >= 8 && progress < 15) {
            animation = "default";
        }

        if (progress >= 15 && progress < 20) {
            animation = "default";
        }

        if (progress >= 25) {
            animation = "default";
        }

        if (!animator.getCurrentAnimation().equals(animation)) {
            animator.startAnimation(animation);
        }
    }

}
