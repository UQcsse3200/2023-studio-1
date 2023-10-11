package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.utils.math.Vector2Utils;

public class AnimationEffectsComponent extends AnimationRenderComponent {
    private Vector2 offset;
    private Vector2 scale;
    public AnimationEffectsComponent(TextureAtlas atlas) {
        super(atlas);
        offset = Vector2.Zero;
        scale = Vector2Utils.ONE;
    }

    public void setOffset(Vector2 offset) {
        this.offset = offset;
    }

    public void setScale(Vector2 scale) {
        this.scale = scale;
    }

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

    public Vector2 getScale() {
        return scale.cpy();
    }
}
