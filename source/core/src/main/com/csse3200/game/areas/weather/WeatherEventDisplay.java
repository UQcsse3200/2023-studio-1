package com.csse3200.game.areas.weather;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

/**
 * Displays different images corresponding to the current weather event on the UI.
 */
public class WeatherEventDisplay extends UIComponent {
	private Image weatherImage;
	private Image clockImage;
	private Group group;
	private Table table;

	/**
	 * Initializes the display and listens to the updates in the game hours.
	 */
	@Override
	public void create() {
		super.create();
		ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::updateDisplay);
		updateDisplay();
	}

	/**
	 * Updates the displayed image based on the current weather event.
	 */
	public void updateDisplay() {
		WeatherEvent currentEvent = ServiceLocator.getGameArea().getClimateController().getCurrentWeatherEvent();
		clockImage = new Image(ServiceLocator.getResourceService().getAsset(
				"images/time_system_ui/clock_frame.png", Texture.class));
		if (currentEvent instanceof AcidShowerEvent) {
			weatherImage = new Image(ServiceLocator.getResourceService().getAsset(
					"images/weather_event/acid-rain.png", Texture.class));
		} else if (currentEvent instanceof SolarSurgeEvent) {
			weatherImage = new Image(ServiceLocator.getResourceService().getAsset(
					"images/weather_event/solar-flare.png", Texture.class));
		} else {
			weatherImage = new Image(ServiceLocator.getResourceService().getAsset(
					"images/time_system_ui/indicator_11.png", Texture.class));
		}
	}

	/**
	 * Draw the weather event display.
	 * @param batch Batch to render to.
	 */
	@Override
	public void draw(SpriteBatch batch) {
		table = new Table();
		group = new Group();
		table.top().left();
		table.setFillParent(true);
		table.padTop(340f).padLeft(-100f);

		group.addActor(clockImage);
		group.addActor(weatherImage);

		table.add(group).size(200);
		stage.addActor(table);
	}

	/**
	 * Cleans up assets.
	 */
	@Override
	public void dispose() {
		super.dispose();
		clockImage.remove();
		weatherImage.remove();
	}
}
