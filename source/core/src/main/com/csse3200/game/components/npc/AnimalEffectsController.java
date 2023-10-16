package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.events.ScheduledEvent;
import com.csse3200.game.rendering.AnimationEffectsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * A component responsible for managing animation effects for animal entities in the game.
 */
public class AnimalEffectsController extends Component {
    /** The animator component responsible for playing animation effects. */
    private AnimationEffectsComponent animator;
    /** The currently active animation trigger. */
    private String currentTrigger;
    /** The scheduled event used to stop the animation effect after a specific duration. */
    private ScheduledEvent stopAnimationEvent;
    private static final Logger logger = LoggerFactory.getLogger(AnimalEffectsController.class);


    /**
     * Initializes the AnimalEffectsController and sets up event listeners for managing animation effects.
     */
    @Override
    public void create() {
        animator = entity.getComponent(AnimationEffectsComponent.class);
        currentTrigger = null;


        entity.getEvents().addListener("stopEffect", this::stopEffect);
        entity.getEvents().addListener("startEffect", this::startEffect);
        entity.getEvents().addListener("startTimedEffect", this::startTimedEffect);
    }

    /**
     * Starts a specific animation effect for the animal entity based on the given trigger.
     *
     * @param trigger The trigger for the animation effect, such as "tamed," "fed," "runAwayStart," etc.
     */
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
                    if (Objects.equals(animator.getCurrentAnimation(), "heart") ||
                        Objects.equals(animator.getCurrentAnimation(), "fed")) {
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

    /**
     * Starts a timed animation effect for the animal entity based on the given trigger and duration.
     *
     * @param trigger   The trigger for the animation effect, such as "tamed," "fed," "runAwayStart," etc.
     * @param duration  The duration (in seconds) for which the animation effect should be active.
     */
    public void startTimedEffect(String trigger, float duration) {
        startEffect(trigger);
        stopAnimationEvent = entity.getEvents().scheduleEvent(duration, "stopEffect", trigger);
    }

    /**
     * Stops the currently active animation effect for the animal entity based on the given trigger.
     *
     * @param trigger The trigger associated with the animation effect to be stopped.
     */
    private void stopEffect(String trigger) {
        if (Objects.equals(currentTrigger, trigger)) {
            animator.stopAnimation();
            this.currentTrigger = null;
        }
    }
}
