package com.csse3200.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.rendering.AnimationEffectsComponent;

import java.util.List;
import java.util.Objects;

public class AnimalEffectsController extends Component {
    private AnimationEffectsComponent animator;
    private String currentTrigger;

    @Override
    public void create() {
        animator = entity.getComponent(AnimationEffectsComponent.class);
        currentTrigger = null;

        entity.getEvents().addListener("stopEffect", this::stopEffect);
        entity.getEvents().addListener("startEffect", this::startEffect);
    }

    public void startEffect(String trigger) {
        switch (trigger) {
            case "tamed" -> animator.startAnimation("heart");
            case "fed" -> animator.startAnimation("fed");
            case "runAwayStart" -> {
                if (!List.of(EntityType.BAT, EntityType.DRAGONFLY, EntityType.OXYGEN_EATER).contains(entity.getType())) {
                    animator.startAnimation("exclamation");
                }
            }
            case "attack" -> animator.startAnimation("red_exclamation");
        }
        this.currentTrigger = trigger;
    }

    private void stopEffect(String trigger) {
        if (Objects.equals(currentTrigger, trigger)) {
            animator.stopAnimation();
        }
    }
}
