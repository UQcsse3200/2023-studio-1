package com.csse3200.game.components;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.csse3200.game.services.ServiceLocator;

public class AuraLightComponent extends Component{

	private static final Color DEFAULT_COLOR = new Color(3,12,33,0.8f);
	private final PointLight light;
	private boolean active;

	public AuraLightComponent() {
		light = new PointLight(ServiceLocator.getLightService().getRayHandler(), 10);
		light.setDistance(4);
		light.setColor(DEFAULT_COLOR);
		light.setStaticLight(true);
		light.setXray(true); // Stops most of shadows and reduces CPU burden
		light.setSoft(true);
	}

	@Override
	public void create() {
		super.create();
		active = false;
		entity.getEvents().addListener("toggleLight", this::toggleLight);
	}

	public AuraLightComponent(float distance) {
		this();
		setDistance(distance);
	}

	public AuraLightComponent(Color color) {
		this();
		setColor(color);
	}

	public AuraLightComponent(float distance, Color color) {
		this();
		setDistance(distance);
		setColor(color);
	}

	public void setDistance(float distance) {
		light.setDistance(distance);
	}

	public void setColor(Color color) {
		light.setColor(color);
	}

	public void toggleLight() {
		active = !active;
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
