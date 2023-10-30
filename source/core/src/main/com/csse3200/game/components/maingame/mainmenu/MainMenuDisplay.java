package com.csse3200.game.components.maingame.mainmenu;

import com.badlogic.gdx.utils.Align;
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
    private Image title;
    private long lastFrameTime;
    private static int fps = 15;
    private static final long FRAME_DURATION = (4500 / fps);
    private float titleAspectRatio;
    private float aniAspectRatio = 0;

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
        title =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/title.png", Texture.class));

        titleAspectRatio = title.getHeight() / title.getWidth();
        title.setWidth(0.5f * Gdx.graphics.getWidth());
        title.setHeight(title.getWidth() * titleAspectRatio);
        title.setPosition(
                (float) Gdx.graphics.getWidth() * 0.01f,
                (float) Gdx.graphics.getHeight() * 0.98f,
                Align.topLeft
        );

        TextButton startBtn = new TextButton("New Game", skin,"transparent_orange");
        TextButton loadBtn = new TextButton("Continue", skin, Gdx.files.local("saves/saveFile.json").exists() ? "transparent_orange" : "transparent_grey");
        TextButton controlsBtn = new TextButton("Controls", skin,"transparent_orange");
        TextButton settingsBtn = new TextButton("Settings", skin,"transparent_orange");
        TextButton creditsBtn = new TextButton("Credits", skin,"transparent_orange");
        TextButton exitBtn = new TextButton("Exit", skin,"transparent_orange");

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
        loadBtn.setDisabled(!Gdx.files.local("saves/saveFile.json").exists());

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

        table.row().width(Gdx.graphics.getWidth() * 0.2f);
        table.add(startBtn).fill();
        table.row();
        table.add(loadBtn).padTop(15f).fill();
        table.row();
        table.add(controlsBtn).padTop(15f).fill();
        table.row();
        table.add(settingsBtn).padTop(15f).fill();
        table.row();
        table.add(creditsBtn).padTop(15f).fill();
        table.row();
        table.add(exitBtn).padTop(15f).fill();
        table.align(Align.topLeft);
        table.pad(title.getHeight() * 1.5f, Gdx.graphics.getWidth() * 0.01f, 0, 0);

        updateAnimation();

        stage.addActor(transitionFrames);
        stage.addActor(table);
        stage.addActor(title);
    }

    private void updateAnimation() {
        if (frame < MainMenuScreen.FRAME_COUNT) {
            transitionFrames.setDrawable(MainMenuScreen.getTransitionTextures()[frame]);
            if (aniAspectRatio == 0) {
                // awful
                aniAspectRatio = MainMenuScreen.getTransitionTextures()[frame].getMinWidth() / MainMenuScreen.getTransitionTextures()[frame].getMinHeight();
                System.out.println(MainMenuScreen.getTransitionTextures()[frame].getMinWidth());
            }
            if ((float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight() < aniAspectRatio) {
                transitionFrames.setHeight(Gdx.graphics.getHeight()); //https://rules.sonarsource.com/java/tag/overflow/RSPEC-2184/
                transitionFrames.setWidth(transitionFrames.getHeight() * aniAspectRatio);
            } else {
                transitionFrames.setWidth(Gdx.graphics.getWidth()); //https://rules.sonarsource.com/java/tag/overflow/RSPEC-2184/
                transitionFrames.setHeight(transitionFrames.getWidth() / aniAspectRatio);
            }
            transitionFrames.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Align.center);
            frame++;
            lastFrameTime = System.currentTimeMillis();
        } else {
            frame = 1;
        }

        title.setWidth(0.5f * Gdx.graphics.getWidth());
        title.setHeight(title.getWidth() * titleAspectRatio);
        title.setPosition(
                (float) Gdx.graphics.getWidth() * 0.01f,
                (float) Gdx.graphics.getHeight() * 0.98f,
                Align.topLeft
        );
        table.pad(title.getHeight() * 1.5f, Gdx.graphics.getWidth() * 0.01f, 0, 0);
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
