package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.Color;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

public class BlinkComponent extends Component {
    /** The duration of the damage blink effect. */
    private static final float DAMAGE_BLINK_DURATION = 0.4f;
    /** The minimum opacity (alpha) value for the entity during the blink. */
    private static final float MIN_OPACITY = 0.5f;
    /** Flag indicating whether the entity is currently blinking. */
    private boolean isBlinking;
    /** Flag indicating whether the entity is currently blinking. */
    private float playTime;
    /** The total duration of the blink effect. */
    private float duration = 0f;
    /** The color used for rendering the entity. */
    private final Color color = new Color(Color.WHITE);

    /**
     * Initializes the BlinkComponent and adds an event listener for handling "hit" events.
     */
    @Override
    public void create() {
        isBlinking = false;
        entity.getEvents().addListener("hit", this::startDamageBlink);
    }

    /**
     * Starts the damage blink effect for the entity.
     *
     * @param entity The entity to apply the damage blink effect to.
     */
    private void startDamageBlink(Entity entity) {
        playTime = 0f;
        isBlinking = true;
        color.a = 1f;
        duration = DAMAGE_BLINK_DURATION;
    }

    /**
     * Gets the color used for rendering the entity during the blink effect.
     *
     * @return The Color object representing the entity's color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Checks whether the entity is currently blinking.
     *
     * @return {@code true} if the entity is blinking; {@code false} otherwise.
     */
    public boolean isBlinking() {
        return isBlinking;
    }

    /**
     * Updates the entity's blinking effect over time, interpolating color opacity.
     */
    @Override
    public void update() {
        if (!isBlinking) {
            return;
        }

        if (playTime > duration) {
            playTime = 0f;
            isBlinking = false;
            duration = 0f;
            return;
        }

        float halfDuration = duration / 2;

        float alpha;
        if (playTime < halfDuration) {
            alpha = 1f - ((1 - MIN_OPACITY) / halfDuration) * playTime;
        } else {
            alpha = MIN_OPACITY + ((1 - MIN_OPACITY) / halfDuration) * (playTime - halfDuration);
        }

        color.a = alpha;

        playTime += ServiceLocator.getTimeSource().getDeltaTime();
    }
}
