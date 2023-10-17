package com.csse3200.game.components.player;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

import com.csse3200.game.services.TimeService;
import com.csse3200.game.services.sound.SoundService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class OpenPauseComponentTest {

    Entity player;
    OpenPauseComponent openPauseComponent;

    String[] texturePaths = {"images/PauseMenu/Pause_Overlay.jpg", "images/PauseMenu/Pausenew.jpg"};
    @BeforeEach
    void init() {
        AssetManager assetManager = spy(AssetManager.class);
        ResourceService resourceService = new ResourceService(assetManager);
        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.getResourceService().loadTextures(texturePaths);
        ServiceLocator.getResourceService().loadAll();
        TimeService timeService = new TimeService();
        ServiceLocator.registerTimeService(timeService);
        GameTime gameTime = new GameTime();
        ServiceLocator.registerTimeSource(gameTime);
        GameArea gameArea = spy(GameArea.class);
        RenderService renderService = new RenderService();
        renderService.setStage(mock(Stage.class));
        GameAreaDisplay playerGuidArea = new GameAreaDisplay("");
        ServiceLocator.registerGameArea(gameArea);
        ServiceLocator.registerRenderService(renderService);
        playerGuidArea.create();
        SoundService soundService = new SoundService();
        ServiceLocator.registerSoundService(soundService);
        player = new Entity();
        openPauseComponent = new OpenPauseComponent();
        player.addComponent(openPauseComponent);
        player.create();
        gameArea.setPlayer(player);
    }

    @Test
    void shouldAllBeClosed() {

        assertFalse(openPauseComponent.getPauseOpen());
    }

    @Test
    void shouldOpenPauseMenu() {

        player.getEvents().trigger(PlayerActions.events.ESC_INPUT.name());
        assertTrue(openPauseComponent.getPauseOpen());
    }

    @Test
    void shouldOpenPauseMenuInventoryToggle() {

        player.getEvents().trigger(PlayerActions.events.ESC_INPUT.name());
        assertTrue(openPauseComponent.getPauseOpen());
    }

    @Test
    void shouldClosePauseMenu() {

        player.getEvents().trigger(PlayerActions.events.ESC_INPUT.name());
        player.getEvents().trigger(PlayerActions.events.ESC_INPUT.name());
        assertFalse(openPauseComponent.getPauseOpen());
    }

    @Test
    void shouldClosePauseMenuInventoryToggle() {

        player.getEvents().trigger(PlayerActions.events.ESC_INPUT.name());
        player.getEvents().trigger(PlayerActions.events.ESC_INPUT.name());
        assertFalse(openPauseComponent.getPauseOpen());
    }
}