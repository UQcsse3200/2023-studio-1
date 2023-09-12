package com.csse3200.game.components.losescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.GdxGame.ScreenType;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoseScreenDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(LoseScreenDisplay.class);
    private static final float Z_INDEX = 2f;
    private Image background;
    private Table table;
    private final GdxGame game;

    public LoseScreenDisplay(GdxGame game) {
        super();
        this.game = game;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.setFillParent(true);
        background = new Image(
                ServiceLocator.getResourceService().getAsset("images/lose_temp.png", Texture.class));
        background.setWidth(Gdx.graphics.getWidth());
        background.setHeight(Gdx.graphics.getHeight());
        background.setPosition(0, 0);

        TextButton exitBtn = new TextButton("Return to Main Menu", skin);

        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        exit();
                    }
                });

        table.add(exitBtn).padTop(30f);

        stage.addActor(background);
        stage.addActor(table);
    }

    private void exit() {
        game.setScreen(ScreenType.MAIN_MENU);
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
        background.clear();
        table.clear();
        super.dispose();
    }

}