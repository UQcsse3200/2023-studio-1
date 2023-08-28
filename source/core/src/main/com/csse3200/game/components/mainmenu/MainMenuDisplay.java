
package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.screens.MainMenuScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final long frameDuration =  (long)(800 / fps);

    @Override
    public void create() {
        frame=1;
        super.create();
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
        TextButton startBtn = new TextButton("Start", skin);
        TextButton loadBtn = new TextButton("Controls", skin);
        TextButton settingsBtn = new TextButton("Settings", skin);
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
                        logger.debug("Load button clicked");
                        entity.getEvents().trigger("load");
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

        table.add(title);
        table.row();
        table.add(startBtn).padTop(30f);
        table.row();
        table.add(loadBtn).padTop(15f);
        table.row();
        table.add(settingsBtn).padTop(15f);
        table.row();
        table.add(exitBtn).padTop(15f);
        stage.addActor(title);
        if (frame < MainMenuScreen.frameCount) {
            transitionFrames = new Image(ServiceLocator.getResourceService()
                    .getAsset(MainMenuScreen.transitionTextures[frame], Texture.class));
            transitionFrames.setWidth(Gdx.graphics.getWidth());
            transitionFrames.setHeight(Gdx.graphics.getHeight()/2);
            transitionFrames.setPosition(0, Gdx.graphics.getHeight()/2+15);
            frame++;
            stage.addActor(transitionFrames);
            lastFrameTime = System.currentTimeMillis();
        } else {
            frame=1;
        }
        stage.addActor(table);
    }

    @Override
    public void update() {
        if (System.currentTimeMillis() - lastFrameTime > frameDuration) {
            addActors();
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
