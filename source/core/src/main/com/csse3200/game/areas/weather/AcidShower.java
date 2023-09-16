package com.csse3200.game.areas.weather;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.awt.*;
import java.util.Iterator;

public class AcidShower extends ApplicationAdapter {

    private Iterator<Rectangle> iterator;
    private long lastRaindropTime;
    private Rectangle raindrop;
    private Array<Rectangle> raindrops;
    private Texture raindropImage;
    private SpriteBatch spriteBatch;

    @Override
    public void create() {
        raindrops = new Array<>();
        raindropImage = new Texture(Gdx.files.internal("images/weather_event/acid-rain.png"));
        spriteBatch = new SpriteBatch();
        spawnRaindrop();
    }

    private void spawnRaindrop() {
        raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800-64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastRaindropTime = TimeUtils.nanoTime();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        spriteBatch.begin();
        spriteBatch.draw(raindropImage, raindrop.x, raindrop.y);

        for(Rectangle raindrop: raindrops) {
            spriteBatch.draw(raindropImage, raindrop.x, raindrop.y);
        }

        spriteBatch.end();

        if (TimeUtils.nanoTime() - lastRaindropTime > 1000000000) {
            spawnRaindrop();
        }

        for (iterator = raindrops.iterator(); iterator.hasNext(); ) {
            raindrop = iterator.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if(raindrop.y + 64 < 0) {
                iterator.remove();
            }
        }
    }

    @Override
    public void dispose() {
        raindropImage.dispose();
        spriteBatch.dispose();
    }
}
