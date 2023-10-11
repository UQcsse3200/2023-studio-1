package com.csse3200.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToggleableMap extends UIComponent {
    
    private static final Logger logger = LoggerFactory.getLogger(ToggleableMap.class);
    
    /**
     * The table used to display the map.
     */
    Table Tmap = new Table();

    GridPoint2 mapSize = new GridPoint2(0,0);
    GridPoint2 g_pos = new GridPoint2(0,0);
    Boolean Switch = false;
    //Color prev_color; // can not used this =((

    /**
     * Dimmed screen
     */
    private Image transparentRectangle;

    /**
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
        createNewMap();
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
     * Toggles the map open or closed and updates the display.
     * @param isOpen Whether the map is open or not.
     */
    public void toggleOpen(Boolean isOpen) {
        this.isOpen = isOpen;
        window.setVisible(isOpen);
        transparentRectangle.setVisible(isOpen);
        if (!isOpen) {
            recoverExternalUI();
        } else {
            removeExternalUI();
        }
        mapSize = ServiceLocator.getGameArea().getMap().getMapSize();
        if (isOpen) {
            pauseGame();
            // Draw the player's position dot on the mini-map
            Vector2 v_pos = ServiceLocator.getGameArea().getPlayer().getPosition();
            g_pos = ServiceLocator.getGameArea().getMap().vectorToTileCoordinates(v_pos);
            g_pos = new GridPoint2(g_pos.x +1, g_pos.y +1); // because it takes bottom left corner

            // store the player's position in a temporary variable and change that cell's color
            //prev_color = Tmap.getChildren().get(g_pos.x + (mapSize.y - (g_pos.y +1)) * mapSize.x).getColor();
            Tmap.getChildren().get(g_pos.x + (mapSize.y - (g_pos.y +1)) * mapSize.x).setColor(Color.WHITE);
            Tmap.getChildren().get(g_pos.x + (mapSize.y - (g_pos.y +1)) * mapSize.x).setColor(Color.RED);
            Switch = true;
        }
        else {
            if(Switch) {
                //Tmap.getChildren().get(g_pos.x + (mapSize.y - (g_pos.y +1)) * mapSize.x).setColor(prev_color);
                Switch = false;
                createMap(); // re-create the map because the position of player is now updated
                unPauseGame();
            }
        }
    }
    
    /**
     * Creates assets used
     */
    public void createAssets() {
        GridPoint2 mapSize = ServiceLocator.getGameArea().getMap().getMapSize();
        Tmap = new Table();
        tiledMap = ServiceLocator.getGameArea().getMap().getTiledMap();
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        // interate through the layers and add them to the table (not sure if this works)

        for (int yPos = mapSize.x -1; yPos >= 0; yPos --) {
            for (int xPos = 0; xPos <= mapSize.y -1; xPos++) {
                TiledMapTileLayer.Cell cell = layer.getCell(xPos, yPos);
                if (cell != null) {
                    TiledMapTile tile = cell.getTile();
                    if (tile != null) {
                        Tmap.add(new Image(tile.getTextureRegion()));
                    }
                    else logger.info("tile at (" + xPos + ", " + yPos + ") is null");
                }
                else logger.info("Cell at (" + xPos + ", " + yPos + ") is null");
            }
            Tmap.row(); // new row
        }
    }

    /**
     * Create the window used to display the map.
     */
    private void createMap() {
        createAssets();
        window.reset();
        window.getTitleLabel().setText("MAP");
        window.setVisible(isOpen);
        float mapHeight = Tmap.getChild(0).getHeight();
        float mapWidth = Tmap.getChild(0).getWidth();
        window.setSize((Gdx.graphics.getHeight() * (mapWidth / mapHeight)),
                Gdx.graphics.getHeight() * (mapHeight / mapWidth));
        window.padBottom(10f);
        window.setPosition(Gdx.graphics.getWidth()/5, 20f);
        window.setMovable(false);
        window.setResizable(false);
        window.add(Tmap);
        stage.addActor(window);
    }

    private void createNewMap() {
        createAssets();
        dimScreen();
        window.reset();
        window.getTitleLabel().setText("MAP");
        window.setVisible(isOpen);
        float mapHeight = Tmap.getChild(0).getHeight();
        float mapWidth = Tmap.getChild(0).getWidth();
        window.setSize((Gdx.graphics.getHeight() * (mapWidth / mapHeight)),
                Gdx.graphics.getHeight() * (mapHeight / mapWidth));
        window.padBottom(10f);
        window.setPosition(Gdx.graphics.getWidth()/5, 20f);
        window.setMovable(false);
        window.setResizable(false);
        window.add(Tmap);
        //window.add(Tmap).expandX().width((Gdx.graphics.getHeight() * (mapWidth / mapHeight))-20f).
        //        expandY().height(Gdx.graphics.getHeight() * (mapHeight / mapWidth)-20f);
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

    public void pauseGame() {
        logger.info("Setting paused state to: 0");
        ServiceLocator.setCutSceneRunning(true);
        ServiceLocator.getTimeSource().setTimeScale(0);
    }

    /**
     * Unpauses the game
     */
    public void unPauseGame() {
        logger.info("Setting paused state to: 1");
        // 1 is for delta time to run in normal speed
        ServiceLocator.setCutSceneRunning(false);
        ServiceLocator.getTimeSource().setTimeScale(1);
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
    /**
     * Removes the UI components on the screen so that cutscene is not so cluttered
     */
    public void removeExternalUI() {
        ServiceLocator.getPlantInfoService().getEvents().trigger("toggleOpen", false);
        ServiceLocator.getUIService().getEvents().trigger("toggleUI", false);
    }

    /**
     * Recovers the UI components that were removed back onto the screen
     */
    public void recoverExternalUI() {
        ServiceLocator.getPlantInfoService().getEvents().
                trigger("toggleOpen", KeyboardPlayerInputComponent.getShowPlantInfoUI());
        ServiceLocator.getUIService().getEvents().trigger("toggleUI", true);
    }
}
