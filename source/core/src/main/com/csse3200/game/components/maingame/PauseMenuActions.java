package com.csse3200.game.components.maingame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;

public class PauseMenuActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(PauseMenuActions.class);
    private GdxGame game;

    private static boolean quitGame = false;

    @Override
    public void create() {
        entity.getEvents().addListener("exit pressed", this::onExit);
    }

    private void onExit() {
        logger.info("Exiting main game screen");
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

    public static void setQuitGameStatus() {
        quitGame = !quitGame;
    }

    public static Boolean getQuitGameStatus() {
        return quitGame;
    }
}
