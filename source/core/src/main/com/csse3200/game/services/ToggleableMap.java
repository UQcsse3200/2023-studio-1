package com.csse3200.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToggleableMap extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ToggleableMap.class);
    Table table = new Table();
    Group group = new Group();
    private Image map;

    private Image transparentRectangle;
    private boolean isOpen = false;
    /**
     * The window used to display the map.
     */
    private Window window;

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        logger.debug("Adding listener to toggleOpen event");
        ServiceLocator.getPlayerMapService().getEvents().addListener("toggleOpen", this::toggleOpen);
        window = new Window("", skin);
        createMap();
        //starts closed so no updateDisplay()
    }

    public void dimScreen() {
        logger.debug("Screen dimmed");
        //Following code for making transparent rectangle from
        //https://stackoverflow.com/questions/44260510/is-it-possible-to-draw-a-transparent-layer-without-using-image-libgdx
        Pixmap pixmap = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fillRectangle(0, 0, 1, 1);
        Texture transparentRecTex = new Texture(pixmap);
        pixmap.dispose();
        transparentRectangle = new Image(transparentRecTex);
        transparentRectangle.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        transparentRectangle.getColor().a = 0.3f;
        stage.addActor(transparentRectangle);
        transparentRectangle.setVisible(isOpen);
    }

    public void toggleOpen(Boolean isOpen) {
        this.isOpen = isOpen;
        window.setVisible(isOpen);
        transparentRectangle.setVisible(isOpen);
    }
    /**
     * Creates assets used
     */
    public void createAssets() {
        map = new Image(ServiceLocator.getResourceService().getAsset("images/desert_2.png", Texture.class));
        map.setPosition(0, 0);
        // Scale the height of the background to maintain the original aspect ratio of the image
        // This prevents distortion of the image.
        float scaledWidth = (Gdx.graphics.getHeight() * (map.getWidth() / map.getHeight()));
        float scaledHeight = Gdx.graphics.getHeight() * (map.getHeight() / map.getWidth());
        map.setHeight(scaledHeight);
        map.setWidth(scaledWidth);
    }

    /**
     * Create the window used to display the map.
     */
    private void createMap() {
        createAssets();
        dimScreen();
        window.reset();
        window.getTitleLabel().setText("MAP");
        window.setVisible(isOpen);
        window.setSize((Gdx.graphics.getHeight() * (map.getWidth() / map.getHeight())),
                Gdx.graphics.getHeight() * (map.getHeight() / map.getWidth()));
        window.padBottom(10f);
        window.setPosition(Gdx.graphics.getWidth()/5, 20f);
        window.setMovable(false);
        window.setResizable(false);
        window.add(map).expandX().width((Gdx.graphics.getHeight() * (map.getWidth() / map.getHeight()))-20f).
                expandY().height(Gdx.graphics.getHeight() * (map.getHeight() / map.getWidth())-20f);
        stage.addActor(window);
    }

    /**
     * Draws the actors to the game.
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch) {
        //
    }

    /**
     * Destroys the UI objects
     */
    @Override
    public void dispose() {
        super.dispose();
        window.clear();
        //
    }
}
