package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csse3200.game.entities.EntityService;

import java.util.ArrayList;

public class OpenPauseComponent extends Component {
    private static Logger logger = LoggerFactory.getLogger(OpenPauseComponent.class);
    private Boolean pauseOpen;

    @Override
    public void create() {
        entity.getEvents().addListener("escInput", this::togglePauseMenu);
        pauseOpen = false;

    }

    public Boolean getPauseOpen() { return pauseOpen; }

    private void togglePauseMenu() {
        logger.info("togglepausemenu");
        if (Boolean.TRUE.equals(pauseOpen)) {
            closePauseMenu();
        } else {
            openPauseMenu();
        }
    }

    public void openPauseMenu() {
        logger.info("Opening pause window");
        ServiceLocator.getPauseMenuArea().setPauseMenu();
        pauseOpen = true;
      //  EntityService.pauseGame();
    }

    public void closePauseMenu() {
        logger.info("Closing pause window");
        KeyboardPlayerInputComponent.clearMenuOpening();
        ServiceLocator.getPauseMenuArea().disposePauseMenu();
        pauseOpen = false;
    }
}
