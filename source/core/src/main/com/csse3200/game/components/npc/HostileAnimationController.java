package com.csse3200.game.components.npc;

import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.utils.DirectionUtils;

public class HostileAnimationController extends AnimalAnimationController {
    boolean activeAction = false;

    private static final String ATTACK_PREFIX = "attack";
    @Override
    public void create() {
        super.create();

        entity.getEvents().addListener("attackStart", this::animateAttack);
    }

    private void animateAttack() {
        animator.startAnimation(ATTACK_PREFIX + "_" + direction);
        activeAction = true;
    }

    @Override
    protected void animateIdle() {
        if (!activeAction) {
            super.animateIdle();
        }
    }

    @Override
    protected void animateWalk() {
        if (!activeAction) {
            super.animateWalk();
        }
    }

    @Override
    protected void animateRun() {
        if (!activeAction) {
            super.animateRun();
        }
    }

    @Override
    public void update() {
        if (activeAction && animator.isFinished()) {
            activeAction = false;
            entity.getEvents().trigger(currentAnimation + "Start");
        }
    }
}
