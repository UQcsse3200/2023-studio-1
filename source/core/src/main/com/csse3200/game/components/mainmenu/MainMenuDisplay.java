package com.csse3200.game.components.mainmenu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.screens.MainMenuScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

/**
 * A ui component for displaying the Main menu.
 */
public class MainMenuDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    public static int frame;
    private Image transitionFrames;
    private long lastFrameTime;
    private int fps = 15;
    private final long frameDuration =  (long)(400 / fps);

    @Override
    public void create() {
        frame=1;
        super.create();
        transitionFrames = new Image();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.setFillParent(true);
        TextButton startBtn = new TextButton("New Game", skin);
        TextButton loadBtn = new TextButton("Continue", skin);
        TextButton controlsBtn = new TextButton("Controls", skin);
        TextButton settingsBtn = new TextButton("Settings", skin);
        TextButton exitBtn = new TextButton("Exit", skin);

        startBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Start button clicked");
                        entity.getEvents().trigger("start");
                    }
                });

        loadBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("load button clicked");
                        entity.getEvents().trigger("load");
                    }
                });

        controlsBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("control button clicked");
                        entity.getEvents().trigger("control");
                    }
                });

        settingsBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Settings button clicked");
                        entity.getEvents().trigger("settings");
                    }
                });

        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });

        table.row();
        table.add(startBtn).padTop(80f);
        table.row();
        table.add(loadBtn).padTop(15f);
        table.row();
        table.add(controlsBtn).padTop(15f);
        table.row();
        table.add(settingsBtn).padTop(15f);
        table.row();
        table.add(exitBtn).padTop(15f);
        updateAnimation();
        stage.addActor(transitionFrames);
        stage.addActor(table);
    }


    private void updateAnimation() {
        if (frame < MainMenuScreen.frameCount) {
            transitionFrames.setDrawable(new TextureRegionDrawable(new TextureRegion(ServiceLocator.getResourceService()
                    .getAsset(MainMenuScreen.transitionTextures[frame], Texture.class))));
        } else {
            int descendingFrame = MainMenuScreen.frameCount * 2 - 1 - frame;
            transitionFrames.setDrawable(new TextureRegionDrawable(new TextureRegion(ServiceLocator.getResourceService()
                    .getAsset(MainMenuScreen.transitionTextures[descendingFrame], Texture.class))));
        }
        transitionFrames.setWidth(Gdx.graphics.getWidth());
        transitionFrames.setHeight(Gdx.graphics.getHeight());
        frame++;
        if (frame >= MainMenuScreen.frameCount * 2) {
            frame = 0;
        }
        lastFrameTime = System.currentTimeMillis();
    }

    @Override
    public void update() {
        if (System.currentTimeMillis() - lastFrameTime > frameDuration) {
            updateAnimation();
        }
    }
    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }
}