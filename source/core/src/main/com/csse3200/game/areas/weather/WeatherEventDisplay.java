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
	}

	public void updateDisplay() {
		table = new Table();
		group = new Group();
		table.top().left();
		table.setFillParent(true);
		table.padTop(520f).padLeft(-260f);

		WeatherEvent currentEvent = ServiceLocator.getGameArea().getClimateController().getCurrentWeatherEvent();
		clockImage = new Image(ServiceLocator.getResourceService().getAsset(
				"images/time_system_ui/clock_frame.png", Texture.class));
		if (currentEvent instanceof AcidShowerEvent) {
			weatherImage = new Image(ServiceLocator.getResourceService().getAsset(
					"images/weather_event/raindrop.png", Texture.class));
		} else if (currentEvent instanceof SolarSurgeEvent) {
			weatherImage = new Image(ServiceLocator.getResourceService().getAsset(
					"images/time_system_ui/indicator_1.png", Texture.class));
		} else {
			weatherImage = new Image(ServiceLocator.getResourceService().getAsset(
					"images/time_system_ui/indicator_1.png", Texture.class));
		}
		clockImage.setPosition(clockImage.getImageX() + 158f, clockImage.getImageY() + 189f);
		weatherImage.setPosition(weatherImage.getImageX() + 158f, weatherImage.getImageY() + 189f);
		stage.addActor(weatherImage);

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

	@Override
	protected void draw(SpriteBatch batch) {
		updateDisplay();
		return;
	}
}
