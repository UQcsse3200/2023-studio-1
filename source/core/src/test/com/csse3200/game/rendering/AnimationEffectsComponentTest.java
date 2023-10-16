package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class AnimationEffectsComponentTest {
    @BeforeEach
    void setUp() {
        ServiceLocator.registerRenderService(mock(RenderService.class));
    }
    @Test
    void testSetOffset() {
        int numFrames = 5;
        String animName = "test_name";
        float frameTime = 1f;

        // Mock texture atlas
        TextureAtlas atlas = createMockAtlas(animName, numFrames);
        Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions(animName);
        SpriteBatch batch = mock(SpriteBatch.class);

        // Mock game time
        GameTime gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
        when(gameTime.getDeltaTime()).thenReturn(frameTime);

        // Start animation
        AnimationEffectsComponent animator = new AnimationEffectsComponent(atlas);
        Entity entity = new Entity().addComponent(animator);
        entity.create();
        animator.addAnimation(animName, frameTime);
        animator.startAnimation(animName);

        // check default scale is one
        animator.draw(batch);
        verify(batch).draw(
                regions.get(0),
                entity.getPosition().x,
                entity.getPosition().y,
                1f,
                1f
        );

        // check scale changes when set
        animator.setScale(new Vector2(2f, 2f));

        // check getter works
        assertEquals(new Vector2(2f, 2f), animator.getScale());

        animator.draw(batch);
        verify(batch).draw(
                regions.get(1),
                entity.getPosition().x,
                entity.getPosition().y,
                2f,
                2f
        );
    }

    @Test
    void testSetScale() {
        int numFrames = 5;
        String animName = "test_name";
        float frameTime = 1f;

        // Mock texture atlas
        TextureAtlas atlas = createMockAtlas(animName, numFrames);
        Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions(animName);
        SpriteBatch batch = mock(SpriteBatch.class);

        // Mock game time
        GameTime gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
        when(gameTime.getDeltaTime()).thenReturn(frameTime);

        // Start animation
        AnimationEffectsComponent animator = new AnimationEffectsComponent(atlas);
        Entity entity = new Entity().addComponent(animator);
        entity.create();
        animator.addAnimation(animName, frameTime);
        animator.startAnimation(animName);

        // check default offset is 0, and so drawn at entity position
        animator.draw(batch);
        verify(batch).draw(
                regions.get(0),
                entity.getPosition().x,
                entity.getPosition().y,
                1f,
                1f
        );

        // check draw position changes when set
        animator.setOffset(new Vector2(2f, 2f));

        animator.draw(batch);
        verify(batch).draw(
                regions.get(1),
                entity.getPosition().x + 2f,
                entity.getPosition().y + 2f,
                1f,
                1f
        );

        animator.stopAnimation();
        animator.draw(batch);
    }

    static TextureAtlas createMockAtlas(String animationName, int numRegions) {
        TextureAtlas atlas = mock(TextureAtlas.class);
        Array<TextureAtlas.AtlasRegion> regions = new Array<>(numRegions);
        for (int i = 0; i < numRegions; i++) {
            regions.add(mock(TextureAtlas.AtlasRegion.class));
        }
        when(atlas.findRegions(animationName)).thenReturn(regions);
        return atlas;
    }
}
