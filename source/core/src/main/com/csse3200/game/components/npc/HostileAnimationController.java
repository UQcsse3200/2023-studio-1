package com.csse3200.game.components.npc;

import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.utils.DirectionUtils;

/**
 * The HostileAnimationController class is responsible for controlling the animations of a hostile NPC entity.
 * It extends the behavior of the AnimalAnimationController and includes functionality for playing attack animations.
 */
public class HostileAnimationController extends AnimalAnimationController {
    /** A flag indicating whether the hostile NPC is currently performing an active action, such as an attack. */
    boolean activeAction = false;
    /** Prefix for attack animation */
    private static final String ATTACK_PREFIX = "attack";

    /**
     * Initializes the HostileAnimationController when it is created.
     * Sets up event listeners to trigger animations when event occurs.
     */
    @Override
    public void create() {
        super.create();

        entity.getEvents().addListener("attackStart", this::animateAttack);
    }

    /**
     * Starts the attack animation.
     */
    private void animateAttack() {
        animator.startAnimation(ATTACK_PREFIX + "_" + direction);
        activeAction = true;
    }

    /**
     * Play idle animation when no action animation is currently playing
     */
    @Override
    protected void animateIdle() {
        if (!activeAction) {
            super.animateIdle();
        }
    }

    /**
     * Play walk animation when no action animation is currently playing
     */
    @Override
    protected void animateWalk() {
        if (!activeAction) {
            super.animateWalk();
        }
    }

    /**
     * Play run animation when no action animation is currently playing
     */
    @Override
    protected void animateRun() {
        if (!activeAction) {
            super.animateRun();
        }
    }

    /**
     * Trigger animation when active animation is finished
     */
    @Override
    public void update() {
        if (activeAction && animator.isFinished()) {
            activeAction = false;
            entity.getEvents().trigger(currentAnimation + "Start");
        }
    }
}
