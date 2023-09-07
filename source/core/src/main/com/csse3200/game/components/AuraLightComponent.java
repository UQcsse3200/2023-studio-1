package com.csse3200.game.components;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.csse3200.game.services.ServiceLocator;

public class AuraLightComponent extends Component{

	private static final Color DEFAULT_COLOR = new Color(3,12,33,0.7f);
	private final PointLight light;

	public AuraLightComponent() {
		light = new PointLight(ServiceLocator.getLightService().getRayHandler(), 50);
		light.setDistance(10);
		light.setColor(DEFAULT_COLOR);
	}

	public AuraLightComponent(float distance) {
		this();
		light.setDistance(distance);
	}

	public AuraLightComponent(Color color) {
		this();
		light.setColor(color);
	}

	public AuraLightComponent(float distance, Color color) {
		this();
		light.setDistance(distance);
		light.setColor(color);
	}

	public void setDistance(float distance) {
		light.setDistance(distance);
	}

	public void setColor(Color color) {
		light.setColor(color);
	}

	public void setActive(boolean active) {
		light.setActive(active);
	}

	public boolean getActive() {
		return light.isActive();
	}

	@Override
	public void update() {
		light.setPosition(entity.getCenterPosition());
	}

	@Override
	public void dispose() {
		light.dispose();
	}
}
