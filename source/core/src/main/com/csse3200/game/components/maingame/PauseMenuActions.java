package com.csse3200.game.components.maingame;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PauseMenuActions extends Component {
	private static final Logger logger = LoggerFactory.getLogger(PauseMenuActions.class);
	private static boolean quitGame = false;
	private GdxGame game;

	public static void setQuitGameStatus() {
		quitGame = !quitGame;
	}

	public static Boolean getQuitGameStatus() {
		return quitGame;
	}

	@Override
	public void create() {
		entity.getEvents().addListener("exit pressed", this::onExit);
	}

	private void onExit() {
		logger.info("Exiting main game screen");
		game.setScreen(GdxGame.ScreenType.MAIN_MENU);
	}
}
