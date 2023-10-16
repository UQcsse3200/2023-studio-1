package com.csse3200.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Component that renders a highlight on current player cursor position
 * Is bounded by the maximum "reach" of the player.
 * Also recognises when it should be muted via listeners.
 */
public class PlayerHighlightComponent extends Component {
    // currentTexture is the texture that is being rendered
    private DynamicTextureRenderComponent currentTexture;
    // isMuted is a boolean that is true when the player is muted
    private boolean isMuted;

    private static final String  TEXTURE_PATH = "images/yellowSquare.png";

    /**
     * Creates the component and sets the muted boolean to false
     */
    public PlayerHighlightComponent() {
        this.isMuted = false;
    }

    /**
     * Creates the component and adds listeners for when the player enters and exits the tractor
     */
    @Override
    public void create() {
        entity.getEvents().addListener(PlayerActions.events.ENTER_TRACTOR.name(), this::mute);
        entity.getEvents().addListener(PlayerActions.events.EXIT_TRACTOR.name(), this::unMute);
        currentTexture = entity.getComponent(DynamicTextureRenderComponent.class);
    }

    /**
     * Mutes the player highlight (OFF)
     */
    public void mute() {
        isMuted = true;
    }


    /**
     * Unmutes the player highlight (highlight ON)
     */
    public void unMute() {
        isMuted = false;
    }

    /**
     * Returns the muted boolean
     * @return true if playerHighlight is muted (highlight is off)
     */
    public boolean isMuted() {
        return isMuted;
    }

    /**
     * Updates the position of the player highlight to the current player cursor position
     * Handles translating mouse pos to world pos
     * And "locking" it to the closest tile
     * Indicates which tile the player is currently going to act on
     */
    public void updatePosition() {
        GameMap map = ServiceLocator.getGameArea().getMap();
        if (currentTexture != null) {
            String texturePath = this.getTexturePath();
            currentTexture.setTexture(texturePath);
        }
        Vector2 mouseWorldPos = ServiceLocator.getCameraComponent().screenPositionToWorldPosition(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        Vector2 adjustedPosition = new Vector2(
                map.tileCoordinatesToVector(map.vectorToTileCoordinates(new Vector2(mouseWorldPos.x, mouseWorldPos.y))));

        Vector2 playerPosCenter = ServiceLocator.getGameArea().getPlayer().getCenterPosition();
        playerPosCenter.add(0, -1.0f); // Player entity sprite's feet are located -1.0f below the centre of the entity. ty Hunter

        playerPosCenter = map.tileCoordinatesToVector(map.vectorToTileCoordinates(playerPosCenter));

        if (adjustedPosition.x - 0.5 > playerPosCenter.x) {
            playerPosCenter.x += 1;
        } else if (adjustedPosition.x + 0.5 < playerPosCenter.x) {
            playerPosCenter.x -= 1;
        }
        if (adjustedPosition.y - 0.5> playerPosCenter.y) {
            playerPosCenter.y += 1;
        } else if (adjustedPosition.y  + 0.5 < playerPosCenter.y) {
            playerPosCenter.y -= 1;
        }

        entity.setPosition(playerPosCenter);

    }

    /**
     * Updates player highlight
     */
    @Override
    public void update() {
        updatePosition();
    }

    /**
     * Returns the path to the texture
     * @return path to texture
     */
    public String getTexturePath() {
        return TEXTURE_PATH;
    }
}