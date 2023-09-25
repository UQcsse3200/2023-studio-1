package com.csse3200.game.areas.weather;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AcidShower implements Screen {
    private ParticleEffect acidShowerParticleEffect;
    private SpriteBatch spriteBatch;

    @Override
    public void show() {
        acidShowerParticleEffect = new ParticleEffect();
        spriteBatch = new SpriteBatch();
        acidShowerParticleEffect.load(Gdx.files.internal("particle-effects/acidrain"), Gdx.files.internal("images"));
        acidShowerParticleEffect.setPosition((float) Gdx.graphics.getWidth()/2, (float) Gdx.graphics.getHeight()/2);
        acidShowerParticleEffect.start();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        acidShowerParticleEffect.draw(spriteBatch, delta);
        spriteBatch.end();
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

    }
}
