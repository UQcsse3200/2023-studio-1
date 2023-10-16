package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.utils.math.Vector2Utils;

/**
 * A component responsible for rendering animation effects using a TextureAtlas.
 */
public class AnimationEffectsComponent extends AnimationRenderComponent {
    /** The offset to adjust the position of the rendered animation. */
    private Vector2 offset;
    /** The scale to adjust the size of the rendered animation. */
    private Vector2 scale;

    /**
     * Initializes the AnimationEffectsComponent with the given TextureAtlas.
     *
     * @param atlas The TextureAtlas containing animation frames.
     */
    public AnimationEffectsComponent(TextureAtlas atlas) {
        super(atlas);
        offset = Vector2.Zero;
        scale = Vector2Utils.ONE;
    }

    /**
     * Sets the offset for adjusting the position of the rendered animation.
     *
     * @param offset The Vector2 representing the offset in (x, y) coordinates.
     */
    public void setOffset(Vector2 offset) {
        this.offset = offset;
    }

    /**
     * Sets the scale for adjusting the size of the rendered animation.
     *
     * @param scale The Vector2 representing the scale in (x, y) dimensions.
     */
    public void setScale(Vector2 scale) {
        this.scale = scale;
    }

    /**
     * Draws the current frame of the animation using the provided SpriteBatch.
     *
     * @param batch The SpriteBatch used for rendering.
     */
    @Override
    protected void draw(SpriteBatch batch) {
        if (currentAnimation == null) {
            return;
        }

        TextureRegion region = currentAnimation.getKeyFrame(animationPlayTime);
        Vector2 pos = entity.getPosition().add(offset);

        batch.draw(region, pos.x, pos.y, scale.x, scale.y);

        animationPlayTime += timeSource.getDeltaTime();
    }

    /**
     * Gets the scale used for rendering the animation.
     *
     * @return A copy of the Vector2 representing the scale.
     */
    public Vector2 getScale() {
        return scale.cpy();
    }
}
