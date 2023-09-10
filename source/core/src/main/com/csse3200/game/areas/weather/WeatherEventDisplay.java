package com.csse3200.game.areas.weather;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

public class WeatherEventDisplay extends UIComponent {
	private Image weatherImage;
	private Image clockImage;
	private Group group;
	private Table table;

	@Override
	public void create() {
		super.create();
		ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::updateDisplay);
		updateDisplay();
	}

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

	@Override
	public void dispose() {
		super.dispose();
		clockImage.remove();
		weatherImage.remove();
	}
}
