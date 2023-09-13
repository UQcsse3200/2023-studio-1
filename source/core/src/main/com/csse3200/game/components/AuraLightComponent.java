package com.csse3200.game.components;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.csse3200.game.services.ServiceLocator;

public class AuraLightComponent extends Component{

	private static final Color DEFAULT_COLOR = new Color(3,12,33,0.8f);
	private final PointLight light;
	private boolean active;

	/**
	 * Creates a AuraLightComponent which can be attached to an entity to enable it to produce light.
	 */
	public AuraLightComponent() {
		light = new PointLight(ServiceLocator.getLightService().getRayHandler(), 25);
		light.setDistance(4);
		light.setColor(DEFAULT_COLOR);
		light.setStaticLight(true);
		light.setXray(true); // Stops most of the shadows and reduces CPU burden
		light.setSoft(true);
	}

	/**
	 * Creates an AuraLightComponent which can be attached to an entity to enable it to produce light.
	 * @param distance distance/spread of the light
	 */
	public AuraLightComponent(float distance) {
		this();
		setDistance(distance);
	}


	/**
	 *
	 * Creates an AuraLightComponent which can be attached to an entity to enable it to produce light.
	 * @param color color of the light being created
	 */
	public AuraLightComponent(Color color) {
		this();
		setColor(color);
	}

	/**
	 *
	 * Creates an AuraLightComponent which can be attached to an entity to enable it to produce light.
	 * @param distance distance/spread of the light
	 * @param color color of the light being created
	 */
	public AuraLightComponent(float distance, Color color) {
		this();
		setDistance(distance);
		setColor(color);
	}

	/**
	 * Creates the event listener for the light to enable it to be toggled on and off
	 */
	@Override
	public void create() {
		super.create();
		active = false;
		light.setActive(active);
		entity.getEvents().addListener("toggleLight", this::toggleLight);
	}

	/**
	 * Sets the distance/spread of the light
	 * @param distance distance/spread of the light
	 */
	public void setDistance(float distance) {
		light.setDistance(distance);
	}

	/**
	 * Sets the color of the light
	 * @param color light color
	 */
	public void setColor(Color color) {
		light.setColor(color);
	}

	/**
	 * Toggles the light between an on and off state
	 */
	public void toggleLight() {
		active = !active;
		light.setActive(active);
	}

	/**
	 * Determines whether or not the light is currently on
	 * @return state of the light (on/off)
	 */
	public boolean getActive() {
		return light.isActive();
	}

	/**
	 * Called every frame draw to ensure that the position of the light is the same as the entity
	 */
	@Override
	public void update() {
		light.setPosition(entity.getCenterPosition());
	}

	/**
	 * Disposes of the light if the entity is disposed
	 */
	@Override
	public void dispose() {
		light.dispose();
	}
}
