package com.csse3200.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.ui.UIComponent;
import net.dermetfan.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Vector;

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
            createMap();
        } else {
            if(Boolean.TRUE.equals(mapRunning)) { // if
                mapRunning = false;
                unPauseGame();
            }
        }
    }

    /**
     * Check if the given position is in the list of player's position
     * @param listplayerPos list of player's position
     * @param gpPos position to check
     * @return true if the given position is in the list of player's position
     */
    public boolean inPlayerPos(Vector<GridPoint2> listplayerPos, GridPoint2 gpPos) {
        for (GridPoint2 pos : listplayerPos) {
            if (pos.equals(gpPos)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the list of position around the given position
     * @param gpPos position to get around
     * @return list of position around the given position
     */
    private Vector<GridPoint2> cell_around(GridPoint2 gpPos) {
        Vector<GridPoint2> listplayerPos = new Vector<GridPoint2>();
        listplayerPos.add(gpPos);
        listplayerPos.add(new GridPoint2(gpPos.x +1, gpPos.y)); // right
        //listplayerPos.add(new GridPoint2(gpPos.x -1, gpPos.y)); // left add this to get 3*3
        listplayerPos.add(new GridPoint2(gpPos.x, gpPos.y +1)); // up
        //listplayerPos.add(new GridPoint2(gpPos.x, gpPos.y -1)); // down add this to get 3*3
        listplayerPos.add(new GridPoint2(gpPos.x +1, gpPos.y +1)); // right up
        //listplayerPos.add(new GridPoint2(gpPos.x -1, gpPos.y -1)); // left down add this to get 3*3
        //listplayerPos.add(new GridPoint2(gpPos.x +1, gpPos.y -1)); // right down add this to get 3*3
        //listplayerPos.add(new GridPoint2(gpPos.x -1, gpPos.y +1)); // left up add this to get 3*3
        return listplayerPos;
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

        Vector2 v_pos = ServiceLocator.getGameArea().getPlayer().getPosition();
        gpPos = ServiceLocator.getGameArea().getMap().vectorToTileCoordinates(v_pos);
        gpPos = new GridPoint2(gpPos.x +1, gpPos.y +1);
        // create array of player's position and all positions around it
        Vector<GridPoint2> listPlayerPos = cell_around(gpPos);

        Array<Entity> entityArray = ServiceLocator.getEntityService().getEntities();
        // create an (position, type) pair array
        Vector<Pair<GridPoint2, EntityType>> listEntityPosType = new Vector<Pair<GridPoint2, EntityType>>();
        for (Entity entity : entityArray) {
            Vector2 v_pos_entity = entity.getPosition();
            GridPoint2 gpPos_entity = ServiceLocator.getGameArea().getMap().vectorToTileCoordinates(v_pos_entity);
            //gpPos_entity = new GridPoint2(gpPos_entity.x +1, gpPos_entity.y +1);
            listEntityPosType.add(new Pair<GridPoint2, EntityType>(gpPos_entity, entity.getType()));
        }

        for (int yPos = mapSize.x -1; yPos >= 0; yPos --) {
            for (int xPos = 0; xPos <= mapSize.y -1; xPos++) {
                TiledMapTileLayer.Cell cell = layer.getCell(xPos, yPos);
                if (cell != null) {
                    TiledMapTile tile = cell.getTile();
                    if (tile != null) {
                        // check if the tile is in the list of player's position
                        if (inPlayerPos(listPlayerPos, new GridPoint2(xPos, yPos))) {
                            // create new image from "assets/wiki/placeables/fences/f.png"
                            tableMap.add(new Image(new TextureRegion(ServiceLocator.getResourceService().getAsset("images/miniMap/playerIcon.png", Texture.class))));
                            logger.info("tile at ({}, {}) is an entity", xPos, yPos);
                        } else {
                            // check if the tile is in the list of entity's position
                            boolean isAdded = false;
                            for (Pair<GridPoint2, EntityType> pair : listEntityPosType) {
                                if (pair.getKey().equals(new GridPoint2(xPos, yPos))) {
                                    if (pair.getValue() == null) {
                                        break;
                                    }
                                    switch (pair.getValue()) {
                                        case PLANT:
                                            tableMap.add(new Image(new TextureRegion(ServiceLocator.getResourceService().getAsset("images/miniMap/plantIcon.png", Texture.class))));
                                            logger.info("tile at ({}, {}) is an entity", xPos, yPos);
                                            isAdded = true;
                                            break;
                                        case QUESTGIVER:
                                            tableMap.add(new Image(new TextureRegion(ServiceLocator.getResourceService().getAsset("images/miniMap/questGiverIcon.png", Texture.class))));
                                            logger.info("tile at ({}, {}) is an entity", xPos, yPos);
                                            isAdded = true;
                                            break;
                                        case SHIP:
                                            // create new image from "assets/wiki/placeables/fences/f.png"
                                            tableMap.add(new Image(new TextureRegion(ServiceLocator.getResourceService().getAsset("images/miniMap/shipIcon.png", Texture.class))));
                                            logger.info("tile at ({}, {}) is an entity", xPos, yPos);
                                            isAdded = true;
                                            break;
                                        default:
                                            tableMap.add(new Image(tile.getTextureRegion()));
                                            isAdded = true;
                                    }
                                    break;
                                }
                            }
                            if (!isAdded) {
                                tableMap.add(new Image(tile.getTextureRegion()));
                            } else {
                                isAdded = false;
                            }
                        }
                    }
                    else logger.info("Cell at ({},{}) is null", xPos, yPos);
                }
                else logger.info("Cell at ({},{}) is null", xPos, yPos);
            }
            tableMap.row(); // new row
        }
    }

    /**
     * Create the window used to display the map.
     */
    private void createMap() {
        pauseGame();

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

        // store the player's position in a temporary variable and change that cell's color
        // tableMap.getChildren().get(gpPos.x + (mapSize.y - (gpPos.y +1)) * mapSize.x).setColor(Color.RED);
        mapRunning = true;
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
