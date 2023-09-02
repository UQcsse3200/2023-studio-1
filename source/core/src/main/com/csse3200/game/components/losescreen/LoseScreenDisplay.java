package com.csse3200.game.components.losescreen;

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
import com.csse3200.game.screens.ControlsScreen;
import com.csse3200.game.screens.MainMenuScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.GdxGame;

public class LoseScreenDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(LoseScreenDisplay.class);
    private static final float Z_INDEX = 2f;
    private Image background;
    private final GdxGame game;

    public LoseScreenDisplay(GdxGame game) {
        super();
        // Initialise the animation with a blank image
        this.game = game;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        background = new Image(
                ServiceLocator.getResourceService().getAsset("images/lose_temp.png", Texture.class));
        background.setWidth(Gdx.graphics.getWidth());
        background.setHeight(Gdx.graphics.getHeight());
        background.setPosition(0, 0);
        stage.addActor(background);
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
        super.dispose();
    }

}