package com.csse3200.game.areas.weather;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

/**
 * Displays different images corresponding to the current weather event on the UI.
 */
public class WeatherEventDisplay extends UIComponent {
	private Array<Image> weatherImages;
	private Image weatherImage;
	private Image clockImage;
	private Group group = new Group();
	private Table table = new Table();
	private AcidShower acidShower;
	private boolean isAcidShowerActive = false;

	/**
	 * Initializes the display and listens to the updates in the game hours.
	 */
	@Override
	public void create() {
		super.create();
		ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::updateDisplay);
		updateDisplay();
	}

	public void createTextures() {
		weatherImages = new Array<>();
		weatherImages.add(new Image(ServiceLocator.getResourceService().getAsset(
				"images/weather_event/acid-rain.png", Texture.class)), new Image(ServiceLocator.getResourceService().getAsset(
				"images/weather_event/solar-flare.png", Texture.class)), new Image(ServiceLocator.getResourceService().getAsset(
				"images/time_system_ui/indicator_11.png", Texture.class)));
		clockImage = new Image(ServiceLocator.getResourceService().getAsset(
				"images/weather_event/weather-border.png", Texture.class));
	}

	/**
	 * Updates the displayed image based on the current weather event.
	 */
	public void updateDisplay() {
		if (weatherImages == null) {
			createTextures();
		}
		WeatherEvent currentEvent = ServiceLocator.getGameArea().getClimateController().getCurrentWeatherEvent();

		if (currentEvent instanceof AcidShowerEvent) {
			weatherImage = weatherImages.get(0);
			if (!isAcidShowerActive) {
				if (acidShower == null) {
					acidShower = new AcidShower();
				}
				acidShower.show();
				isAcidShowerActive = true;
			}
		} else {
			weatherImage = (currentEvent instanceof SolarSurgeEvent) ? weatherImages.get(1) : weatherImages.get(2);
			if (isAcidShowerActive && acidShower != null) {
				acidShower.hide();
				isAcidShowerActive = false;
			}
		}
	}

	/**
	 * Draw the weather event display.
	 * @param batch Batch to render to.
	 */
	@Override
	public void draw(SpriteBatch batch) {
		table.clear();
		group.clear();
		table.top().left();
		table.setFillParent(true);
		table.padTop(340f).padLeft(-100f);

		group.addActor(clockImage);
		group.addActor(weatherImage);

		table.add(group).size(200);
		stage.addActor(table);

		if (acidShower != null) {
			acidShower.render(Gdx.graphics.getDeltaTime());
		}
	}

	/**
	 * Cleans up assets.
	 */
	@Override
	public void dispose() {
		super.dispose();
		clockImage.remove();
		weatherImage.remove();
		if (acidShower != null) {
			acidShower.dispose();
		}
	}
}
