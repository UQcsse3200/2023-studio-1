package com.csse3200.game.areas.weather;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

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
        acidShowerParticleEffectPool = new ParticleEffectPool(acidShowerParticleEffect, 1, 2);
        spriteBatch = new SpriteBatch();
        pooledEffectsArray = new Array<>();

        addMultipleParticleEffects();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        for(int i = pooledEffectsArray.size - 1; i >= 0; i--) {
            ParticleEffectPool.PooledEffect pooledEffect = pooledEffectsArray.get(i);
            pooledEffect.draw(spriteBatch, delta);
            if(pooledEffect.isComplete()) {
                pooledEffect.free();
                pooledEffectsArray.removeIndex(i);
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
        dispose();
    }

    @Override
    public void dispose() {
        for (ParticleEffectPool.PooledEffect pooledEffect : pooledEffectsArray) {
            pooledEffect.free();
        }
        pooledEffectsArray.clear();
        spriteBatch.dispose();
        acidShowerParticleEffect.dispose();
    }

    private void addMultipleParticleEffects() {
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
