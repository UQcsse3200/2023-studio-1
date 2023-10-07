package com.csse3200.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
    
    /**
     * The table used to display the map.
     */
    Table table = new Table();
    Group group = new Group(); // what is this for? Angus

    /**
     * The map to be displayed.
     */
    private Image map;

    /**
     * Dimmed screen
     */
    private Image transparentRectangle;

    /*
     * The mini-map to be displayed.
     */
    TiledMap tiledMap;

    /**
     * Whether the mini-map is open or not.
     */
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
        tiledMap = ServiceLocator.getGameArea().getMap().getTiledMap();
    }

    /**
     * Dim the screen when the map is open (Thang: should I pause the game too?)
     */
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

    /**
     * Updates the map to display the player's current position.
     */
    public void playerDot() {
        logger.debug("player dot");
        //Following code for making transparent rectangle from
        //https://stackoverflow.com/questions/44260510/is-it-possible-to-draw-a-transparent-layer-without-using-image-libgdx
        Pixmap pixmap = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fillRectangle(0, 0, 1, 1);
        Texture transparentDotTex = new Texture(pixmap);
        pixmap.dispose();
    }

    /**
     * Updates the map to display the player's current position.
     * 
     * @param isOpen Whether the map is open or not.
     */
    public void toggleOpen(Boolean isOpen) {
        this.isOpen = isOpen;
        window.setVisible(isOpen);
        transparentRectangle.setVisible(isOpen);
    }
    
    /**
     * Creates assets used
     */
    public void createAssets() {
        int map_x = 100, map_y = 100; // x length of map
        Table Tmap = new Table();
        tiledMap = ServiceLocator.getGameArea().getMap().getTiledMap();
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        // interate through the layers and add them to the table (not sure if this works)
        for (int i = 0; i < map_x;i ++) {
            for (int j = 0 ; j < map_y; j++) {
                TiledMapTileLayer.Cell cell = layer.getCell(i, j);
                if (cell != null) {
                    TiledMapTile tile = cell.getTile();
                    if (tile != null) {
                        Tmap.add(new Image(tile.getTextureRegion()));
                    }
                }
            }
            table.row(); // new row
        }
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
        /*window.setSize((Gdx.graphics.getHeight() * (map.getWidth() / map.getHeight())),
                Gdx.graphics.getHeight() * (map.getHeight() / map.getWidth()));
        window.padBottom(10f);
        window.setPosition(Gdx.graphics.getWidth()/5, 20f);
        window.setMovable(false);
        window.setResizable(false);
        window.add(map).expandX().width((Gdx.graphics.getHeight() * (map.getWidth() / map.getHeight()))-20f).
                expandY().height(Gdx.graphics.getHeight() * (map.getHeight() / map.getWidth())-20f);*/
        // Add the player's dot to the window's content

        stage.addActor(window);
    }

    /**
     * Draws the actors to the game.
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch) {
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
