package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.utils.DirectionUtils;

/**
 * This class listens to events relevant to a chicken entity's state and plays the animation when one
 * of the events is triggered.
 */
public class AnimalAnimationController extends Component {
    /**
     * Render component used to render animals.
     **/
    protected AnimationRenderComponent animator;
    /**
     * Current direction of animal.
     */
    protected String direction;
    /**
     * Current animation playing (excluding direction suffix)
     */
    protected String currentAnimation;
    /**
     * Walk prefix to play walk animation
     */
    protected static final String WALK_PREFIX = "walk";
    /**
     * Run prefix to play run animation
     */
    protected static final String RUN_PREFIX = "run";
    /**
     * Idle prefix to play idle animation
     */
    protected static final String IDLE_PREFIX = "idle";
    /**
     * Tamed suffix to add tamed indicator
     */
    private static final String TAMED_SUFFIX = "_tamed";
    /**
     * Attack prefix to play attack animation
     */
    private static final String ATTACK_PREFIX = "attack";

    /**
     * Returns a suffix if the entity is tamed.
     */
    private String isTamed() {

        TamableComponent tamableComponent = this.entity.getComponent(TamableComponent.class);

        if (tamableComponent != null && tamableComponent.isTamed()) {
            return TAMED_SUFFIX;
        }
        return "";
    }

    /**
     * Create component by retrieving animator, setting start direction and animation, and adding
     * event listeners.
     */
    @Override
    public void create() {
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        this.direction = DirectionUtils.RIGHT;
        this.currentAnimation = IDLE_PREFIX;

        entity.getEvents().addListener("directionChange", this::changeDirection);
        entity.getEvents().addListener("walkStart", this::animateWalk);
        entity.getEvents().addListener("runStart", this::animateRun);
        entity.getEvents().addListener("idleStart", this::animateIdle);
        entity.getEvents().addListener("followStart", this::animateWalk);
        entity.getEvents().addListener("followStop", this::animateIdle);
        entity.getEvents().addListener("attack", this::animateAttack);

        animateIdle();
    }

    /**
     * Play walk animation with current direction
     */
    protected void animateWalk() {
        animator.startAnimation(WALK_PREFIX + "_" + direction + isTamed());
        currentAnimation = WALK_PREFIX;
    }

    /**
     * Play run animation with current direction
     */
    protected void animateRun() {
        animator.startAnimation(RUN_PREFIX + "_" + direction + isTamed());
        currentAnimation = RUN_PREFIX;
    }

    /**
     * Play idle animation with current direction
     */
    protected void animateIdle() {
        animator.startAnimation(IDLE_PREFIX + "_" + direction + isTamed());
        currentAnimation = IDLE_PREFIX;
    }

    /**
     * Set new direction of animal and retrigger current animation with new direction
     *
     * @param direction new direction
     */
    void changeDirection(String direction) {
        this.direction = direction;
        entity.getEvents().trigger(currentAnimation + "Start");
    }

    /**
     * Play attack animation with current direction
     */
    void animateAttack() {
        entity.getEvents().trigger(ATTACK_PREFIX + "_" + direction);
        currentAnimation = ATTACK_PREFIX;
    }
}


