package com.csse3200.game.areas.weather;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class AcidShower implements Screen {
    private ParticleEffect acidShowerParticleEffect;
    private ParticleEffectPool acidShowerParticleEffectPool;
    private SpriteBatch spriteBatch;
    private Array<ParticleEffectPool.PooledEffect> pooledEffectsArray;

    @Override
    public void show() {
        acidShowerParticleEffect = new ParticleEffect();
        acidShowerParticleEffect.load(Gdx.files.internal("particle-effects/acidrain"), Gdx.files.internal("images"));
        acidShowerParticleEffect.setEmittersCleanUpBlendFunction(false);
        acidShowerParticleEffectPool = new ParticleEffectPool(acidShowerParticleEffect, 1, 10);
        spriteBatch = new SpriteBatch();
        pooledEffectsArray = new Array<>();

        addMultipleParticleEffects();
    }

    @Override
    public void render(float delta) {
        spriteBatch.begin();
        if (pooledEffectsArray != null) {
            for (int i = pooledEffectsArray.size - 1; i >= 0; i--) {
                ParticleEffectPool.PooledEffect pooledEffect = pooledEffectsArray.get(i);
                pooledEffect.draw(spriteBatch, delta);
                if (pooledEffect.isComplete()) {
                    pooledEffect.free();
                    pooledEffectsArray.removeIndex(i);
                }
            }
        }
        spriteBatch.end();
        addMultipleParticleEffects();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        clearPooledEffectsArray();
    }

    @Override
    public void dispose() {
        clearPooledEffectsArray();
        if (spriteBatch != null) {
            spriteBatch.dispose();
        }
        if (acidShowerParticleEffect != null) {
            acidShowerParticleEffect.dispose();
        }
    }

    private void addMultipleParticleEffects() {
        if (pooledEffectsArray != null) {
            while (pooledEffectsArray.size < 10) {
                ParticleEffectPool.PooledEffect pooledEffect = acidShowerParticleEffectPool.obtain();
                float randomX = MathUtils.random(0, Gdx.graphics.getWidth());
                float randomY = MathUtils.random(0, Gdx.graphics.getHeight());
                pooledEffect.setPosition(randomX, randomY);
                pooledEffectsArray.add(pooledEffect);
                pooledEffect.start();
            }
        }
    }

    private void clearPooledEffectsArray() {
        if (pooledEffectsArray != null) {
            for (ParticleEffectPool.PooledEffect pooledEffect : pooledEffectsArray) {
                pooledEffect.free();
            }
            pooledEffectsArray.clear();
        }
    }
}
