package com.csse3200.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

//TODO commenting in here is a bit of a mess, clean it up.and junit for this

/**
 * Component that renders a highlight on current player cursor position
 * Is bounded by the maximum "reach" of the player.
 *
 */
public class PlayerHighlightComponent extends Component {
    private DynamicTextureRenderComponent currentTexture;
    private boolean isMuted;

    @Override
    public void create() {
		entity.getEvents().addListener("enterTractor", this::mute);
		entity.getEvents().addListener("exitTractor", this::unMute);
        currentTexture = entity.getComponent(DynamicTextureRenderComponent.class);
    }

    public void mute() {
        isMuted = true;
    }

    private void setMuted(boolean muted) {
        this.isMuted = muted;
    }
    public void unMute() {
        isMuted = false;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public PlayerHighlightComponent() {
        this.isMuted = false;
    }

    public void updateMuted() {
        boolean isMuted = ServiceLocator.getGameArea().getPlayer().getComponent(PlayerActions.class).isMuted();
        setMuted(isMuted);
    }

    public void updatePosition() {
        GameMap map = ServiceLocator.getGameArea().getMap();
        if (currentTexture != null) {
            String texturePath = this.getTexturePath();
            currentTexture.setTexture(texturePath);
        }
        Vector2 playerPos = ServiceLocator.getGameArea().getPlayer().getPosition();
        Vector2 mouseWorldPos = ServiceLocator.getCameraComponent().screenPositionToWorldPosition(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        Vector2 adjustedPosition = new Vector2(
                map.tileCoordinatesToVector(map.vectorToTileCoordinates(new Vector2(mouseWorldPos.x, mouseWorldPos.y))));

        Vector2 playerPosCenter = ServiceLocator.getGameArea().getPlayer().getCenterPosition();
        playerPosCenter.add(0, -1.0f); // Player entity sprite's feet are located -1.0f below the centre of the entity. ty Hunter

        playerPosCenter = map.tileCoordinatesToVector(map.vectorToTileCoordinates(playerPosCenter));
        ;
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

    @Override
    public void update() {
        updateMuted();
        updatePosition();
    }


    private String getTexturePath() {
        //TODO implement the other circle thing here as well.
        return "images/yellowSquare.png";
    }
}
