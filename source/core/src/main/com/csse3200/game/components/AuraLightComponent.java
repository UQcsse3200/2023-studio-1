package com.csse3200.game.components;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;

public class AuraLightComponent extends Component{

	private final PointLight light;

	public AuraLightComponent() {
		light = new PointLight();
		light.setDistance(100);
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
}
