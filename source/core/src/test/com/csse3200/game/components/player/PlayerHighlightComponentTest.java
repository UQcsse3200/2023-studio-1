package com.csse3200.game.components.player;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.*;

//TODO commenting here
@ExtendWith(GameExtension.class)
public class PlayerHighlightComponentTest {
    private Entity playerHighlight;


    @Test
    public void isMuted() {
        playerHighlight = new Entity().addComponent(new PlayerHighlightComponent());
        playerHighlight.create();
        assertFalse(playerHighlight.getComponent(PlayerHighlightComponent.class).isMuted());
        playerHighlight.getComponent(PlayerHighlightComponent.class).mute();
        assertTrue(playerHighlight.getComponent(PlayerHighlightComponent.class).isMuted());
        playerHighlight.getComponent(PlayerHighlightComponent.class).unMute();
        assertFalse(playerHighlight.getComponent(PlayerHighlightComponent.class).isMuted());
    }
}
