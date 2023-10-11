package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.events.ScheduledEvent;
import com.csse3200.game.rendering.AnimationEffectsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class AnimalEffectsController extends Component {
    private AnimationEffectsComponent animator;
    private String currentTrigger;
    private ScheduledEvent stopAnimationEvent;
    private static final Logger logger = LoggerFactory.getLogger(AnimalEffectsController.class);


    @Override
    public void create() {
        animator = entity.getComponent(AnimationEffectsComponent.class);
        currentTrigger = null;


        entity.getEvents().addListener("stopEffect", this::stopEffect);
        entity.getEvents().addListener("startEffect", this::startEffect);
        entity.getEvents().addListener("startTimedEffect", this::startTimedEffect);
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
            case "panicStart" -> animator.startAnimation("exclamation");
            case "followStart" -> {
                if (entity.getType() == EntityType.BAT) {
                    animator.startAnimation("red_exclamation");
                } else if (entity.getType() == EntityType.CHICKEN || entity.getType() == EntityType.COW) {
                    if (Objects.equals(animator.getCurrentAnimation(), "heart")) {
                        entity.getEvents().scheduleEvent(0.25f, "startEffect", "followStart");
                        return;
                    } else {
                        animator.startAnimation("question");
                    }
                }
            }
            case "attack" -> animator.startAnimation("red_exclamation");
            default -> logger.error("unrecognised effect trigger");
        }

        entity.getEvents().cancelEvent(stopAnimationEvent);
        this.currentTrigger = trigger;
    }

    public void startTimedEffect(String trigger, float duration) {
        startEffect(trigger);
        stopAnimationEvent = entity.getEvents().scheduleEvent(duration, "stopEffect", trigger);
    }

    private void stopEffect(String trigger) {
        if (Objects.equals(currentTrigger, trigger)) {
            animator.stopAnimation();
            this.currentTrigger = null;
        }
    }
}
