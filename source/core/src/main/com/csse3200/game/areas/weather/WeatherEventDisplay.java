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
	private Group group;
	private Table table;

	@Override
	public void create() {
		super.create();
		ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::updateDisplay);
		updateDisplay();
	}

	public void updateDisplay() {
		table = new Table();
		group = new Group();
		table.top().left();
		table.setFillParent(true);
		table.padTop(150f).padLeft(-100f);

		WeatherEvent currentEvent = ServiceLocator.getGameArea().getClimateController().getCurrentWeatherEvent();
		if (currentEvent instanceof RainEvent) {
			weatherImage = new Image(ServiceLocator.getResourceService().getAsset(
					"images/weather_event/raindrop.png", Texture.class));
		}
		// TODO Remove this when other events are added
		weatherImage = new Image(ServiceLocator.getResourceService().getAsset(
				"images/weather_event/raindrop.png", Texture.class));
		weatherImage.setPosition(weatherImage.getImageX() + 158f, weatherImage.getImageY() + 147f);
		stage.addActor(weatherImage);

		group.addActor(weatherImage);

		table.add(group).size(200);
		stage.addActor(table);
	}

	@Override
	public void dispose() {
		super.dispose();
		weatherImage.remove();
	}

	@Override
	protected void draw(SpriteBatch batch) {
		return;
	}
}
