package com.csse3200.game.components.maingame.mainmenu;

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
    private int frame;
    private Image transitionFrames;
    private long lastFrameTime;
    private static int fps = 15;
    private static final long FRAME_DURATION = (800 / fps);

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
        Image title =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/galaxy_home_still.png", Texture.class));

        title.setWidth(Gdx.graphics.getWidth());
        title.setHeight(Gdx.graphics.getHeight());
        title.setPosition(0, 0);
        TextButton startBtn = new TextButton("New Game", skin);
        TextButton loadBtn = new TextButton("Continue", skin);
        TextButton controlsBtn = new TextButton("Controls", skin);
        TextButton settingsBtn = new TextButton("Settings", skin);
        TextButton creditsBtn = new TextButton("Credits", skin);
        TextButton exitBtn = new TextButton("Exit", skin);

        // Triggers an event when the button is pressed
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

        creditsBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        logger.debug("Credits button clicked");
                        entity.getEvents().trigger("credits");
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

        table.add(title);
        table.row();
        table.add(startBtn).padTop(80f);
        table.row();
        table.add(loadBtn).padTop(15f);
        table.row();
        table.add(controlsBtn).padTop(15f);
        table.row();
        table.add(settingsBtn).padTop(15f);
        table.row();
        table.add(creditsBtn).padTop(15f);
        table.row();
        table.add(exitBtn).padTop(15f);
        stage.addActor(title);

        updateAnimation();

        stage.addActor(transitionFrames);
        stage.addActor(table);
    }

    private void updateAnimation() {
        if (frame < MainMenuScreen.FRAME_COUNT) {
            transitionFrames.setDrawable(new TextureRegionDrawable(new TextureRegion(ServiceLocator.getResourceService()
                    .getAsset(MainMenuScreen.getTransitionTextures()[frame], Texture.class))));
            transitionFrames.setWidth(Gdx.graphics.getWidth());
            transitionFrames.setHeight(Gdx.graphics.getHeight() / (float)2); //https://rules.sonarsource.com/java/tag/overflow/RSPEC-2184/
            transitionFrames.setPosition(0, Gdx.graphics.getHeight() / (float)2 + 15); //https://rules.sonarsource.com/java/tag/overflow/RSPEC-2184/
            frame++;
            lastFrameTime = System.currentTimeMillis();
        } else {
            frame = 1;
        }
    }

    @Override
    public void update() {
        if (System.currentTimeMillis() - lastFrameTime > FRAME_DURATION) {
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
