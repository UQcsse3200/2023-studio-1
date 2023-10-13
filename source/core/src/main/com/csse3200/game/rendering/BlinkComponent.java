package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.Color;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

public class BlinkComponent extends Component {
    private static final float DAMAGE_BLINK_DURATION = 0.4f;
    private static final float MIN_OPACITY = 0.5f;
    private boolean isBlinking;
    private float playTime;
    private float duration = 0f;
    private final Color color = new Color(Color.WHITE);

    @Override
    public void create() {
        isBlinking = false;
        entity.getEvents().addListener("hit", this::startDamageBlink);
    }

    public void startDamageBlink(Entity entity) {
        playTime = 0f;
        isBlinking = true;
        color.a = 1f;
        duration = DAMAGE_BLINK_DURATION;
    }

    public Color getColor() {
        return color;
    }

    public boolean isBlinking() {
        return isBlinking;
    }

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
