package com.csse3200.game.components;

import box2dLight.DirectionalLight;
import com.csse3200.game.services.ServiceLocator;

import com.badlogic.gdx.graphics.Color;

public class DirectionalLightComponent extends Component{

	private static final Color DEFAULT_COLOR = new Color(3,12,33,0.8f);
	private final DirectionalLight light;
	private boolean active;

	public DirectionalLightComponent() {
		light = new DirectionalLight(ServiceLocator.getLightService().getRayHandler(), 100,DEFAULT_COLOR, 90);
		light.setStaticLight(true);
		light.setXray(true);
		light.setSoft(true);
	}

	public DirectionalLightComponent(float direction) {
		this();
		light.setDirection(direction);
	}

	public DirectionalLightComponent(Color color) {
		this();
		light.setColor(color);
	}

	public DirectionalLightComponent(float direction, Color color) {
		this();
		light.setDirection(direction);
		light.setColor(color);
	}

	@Override
	public void create() {
		super.create();
		active = false;
		entity.getEvents().addListener("toggleLight", this::toggleLight);
	}

	public void setDirection(float direction) {
		light.setDirection(direction);
	}

	public void setColor(Color color) {
		light.setColor(color);
	}

	public void toggleLight() {
		active = !active;
		light.setActive(active);
	}

	private boolean getActive() {
		return active;
	}

	@Override
	public void dispose() {
		light.dispose();
	}
}
