package com.csse3200.game.components.gamearea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

/**
 * Displays performance stats about the game for debugging purposes.
 */
public class PerformanceDisplay extends UIComponent {
	private static final float Z_INDEX = 5f;
	private Label profileLabel;

	@Override
	public void create() {
		super.create();
		addActors();
	}

	private void addActors() {
		profileLabel = new Label(getStats(), skin, "small");
		stage.addActor(profileLabel);
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (ServiceLocator.getRenderService().getDebug().getActive()) {
			profileLabel.setVisible(true);
			profileLabel.setText(getStats());

			int screenHeight = stage.getViewport().getScreenHeight();
			float offsetX = 5f;
			float offsetY = 600f;
			profileLabel.setPosition(offsetX, screenHeight - offsetY);
		} else {
			profileLabel.setVisible(false);
		}
	}

	private String getStats() {
		Vector2 position = ServiceLocator.getGameArea().getPlayer().getPosition();
		String message = "Debug\n";
		message =
				message
						.concat(String.format("FPS: %d fps%n", Gdx.graphics.getFramesPerSecond()))
						.concat(String.format("RAM: %d MB%n", Gdx.app.getJavaHeap() / 1000000))
						.concat(String.format("PLAYER: (%.2f, %.2f)%n", position.x, position.y))
						.concat(String.format("DAY: %d%n", ServiceLocator.getTimeService().getDay()))
						.concat(String.format("TIME: %d: %d%n", ServiceLocator.getTimeService().getHour(),
                                ServiceLocator.getTimeService().getMinute()));
		return message;
	}

	@Override
	public float getZIndex() {
		return Z_INDEX;
	}

	@Override
	public void dispose() {
		super.dispose();
		profileLabel.remove();
	}
}
