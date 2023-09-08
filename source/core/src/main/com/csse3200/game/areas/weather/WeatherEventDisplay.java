package com.csse3200.game.areas.weather;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

public class WeatherEventDisplay extends UIComponent {
    private Image raindropImage;

    @Override
    public void create() {
        super.create();
        ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::updateDisplay);
        updateDisplay();
    }

    public void updateDisplay() {
        raindropImage = new Image(ServiceLocator.getResourceService().getAsset("images/weather_event/raindrop.png", Texture.class));
        raindropImage.setPosition(raindropImage.getImageX() + 158f, raindropImage.getImageY() + 147f);
        ClimateController climateController = ServiceLocator.getGameArea().getClimateController();
        raindropImage.setVisible(climateController.getCurrentWeatherEvent() instanceof RainEvent);
        stage.addActor(raindropImage);
    }

    @Override
    public void dispose() {
        super.dispose();
        raindropImage.remove();
    }

    @Override
    protected void draw(SpriteBatch batch) {
        return;
    }
}
