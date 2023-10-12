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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToggleableMap extends UIComponent {

    private String toggleOpen = "toggleOpen";
    private static final Logger logger = LoggerFactory.getLogger(ToggleableMap.class);
    
    /**
     * The table used to display the map.
     */
    Table tableMap = new Table();

    GridPoint2 mapSize = new GridPoint2(0,0);
    GridPoint2 gpPos = new GridPoint2(0,0);
    Boolean mapRunning = false;
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
        ServiceLocator.getPlayerMapService().getEvents().addListener(toggleOpen, this::toggleOpen);
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
        if (!Boolean.TRUE.equals(isOpen)) {
            recoverExternalUI();
        } else {
            removeExternalUI();
        }
        mapSize = ServiceLocator.getGameArea().getMap().getMapSize();
        if (Boolean.TRUE.equals(isOpen)) {
            pauseGame();
            // Draw the player's position dot on the mini-map
            Vector2 v_pos = ServiceLocator.getGameArea().getPlayer().getPosition();
            gpPos = ServiceLocator.getGameArea().getMap().vectorToTileCoordinates(v_pos);
            gpPos = new GridPoint2(gpPos.x +1, gpPos.y +1); // because it takes bottom left corner

            // store the player's position in a temporary variable and change that cell's color
            tableMap.getChildren().get(gpPos.x + (mapSize.y - (gpPos.y +1)) * mapSize.x).setColor(Color.RED);
            mapRunning = true;
        } else {
            if(Boolean.TRUE.equals(mapRunning)) { // if
                mapRunning = false;
                createMap(); // re-create the map because the position of player is now updated
                unPauseGame();
            }
        }
    }
    
    /**
     * Creates assets used
     */
    public void createAssets() {
        mapSize = ServiceLocator.getGameArea().getMap().getMapSize();
        tableMap = new Table();
        tiledMap = ServiceLocator.getGameArea().getMap().getTiledMap();
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        // interate through the layers and add them to the table (not sure if this works)

        for (int yPos = mapSize.x -1; yPos >= 0; yPos --) {
            for (int xPos = 0; xPos <= mapSize.y -1; xPos++) {
                TiledMapTileLayer.Cell cell = layer.getCell(xPos, yPos);
                if (cell != null) {
                    TiledMapTile tile = cell.getTile();
                    if (tile != null) {
                        tableMap.add(new Image(tile.getTextureRegion()));
                        // change this part
                    }
                    else logger.info("tile at (%d, %d) is null", xPos, yPos);
                }
                else logger.info("Cell at (%d,%d) is null",xPos,yPos);
            }
            tableMap.row(); // new row
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
        float mapHeight = tableMap.getChild(0).getHeight();
        float mapWidth = tableMap.getChild(0).getWidth();
        window.setSize((Gdx.graphics.getHeight() * (mapWidth / mapHeight)),
                Gdx.graphics.getHeight() * (mapHeight / mapWidth));
        window.padBottom(10f);
        window.setPosition(((float) Gdx.graphics.getWidth())/5f, 20f);
        window.setMovable(false);
        window.setResizable(false);
        window.add(tableMap);
        stage.addActor(window);
    }

    private void createNewMap() {
        createAssets();
        dimScreen();
        window.reset();
        window.getTitleLabel().setText("MAP");
        window.setVisible(isOpen);
        float mapHeight = tableMap.getChild(0).getHeight();
        float mapWidth = tableMap.getChild(0).getWidth();
        window.setSize((Gdx.graphics.getHeight() * (mapWidth / mapHeight)),
                Gdx.graphics.getHeight() * (mapHeight / mapWidth));
        window.padBottom(10f);
        window.setPosition(((float) Gdx.graphics.getWidth())/5f, 20f);
        window.setMovable(false);
        window.setResizable(false);
        window.add(tableMap);

        stage.addActor(window);
    }


    /**
     * Draws the actors to the game.
     * @param batch Batch to render to.
     */
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
        ServiceLocator.getPlantInfoService().getEvents().trigger(toggleOpen, false);
        ServiceLocator.getUIService().getEvents().trigger("toggleUI", false);
    }

    /**
     * Recovers the UI components that were removed back onto the screen
     */
    public void recoverExternalUI() {
        ServiceLocator.getPlantInfoService().getEvents().
                trigger(toggleOpen, KeyboardPlayerInputComponent.getShowPlantInfoUI());
        ServiceLocator.getUIService().getEvents().trigger("toggleUI", true);
    }
}
