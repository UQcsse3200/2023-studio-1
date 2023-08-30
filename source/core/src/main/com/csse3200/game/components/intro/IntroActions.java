package com.csse3200.game.components.intro;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Game Screen and does something when one of the
 * events is triggered.
 */
public class IntroActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(IntroActions.class);
  private final GdxGame game;

  public IntroActions(GdxGame game) {
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("skip", this::onSkip);
  }

  /**
   * Swaps to the Main Menu screen.
   */
  private void onSkip() {
    logger.info("Skipping to Main Game screen");
    game.setScreen(GdxGame.ScreenType.MAIN_GAME);
  }
}
