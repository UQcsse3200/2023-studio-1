package com.csse3200.game.components;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.csse3200.game.services.ServiceLocator;

public class AuraLightComponent extends Component{

	/**
	 * Point light from box2dlights that gets rendered by the ray handler
	 */
	private final PointLight light;

	/**
	 * State of the light (on/off)
	 */
	private boolean active;

	/**
	 * Default light distance used if nothing is provided in the constructor
	 */
	private static final float DEFAULT_DISTANCE = 8f;

	/**
	 * Default light colour if nothing is provided in the constructor
	 */
	private static final Color DEFAULT_COLOR = Color.LIGHT_GRAY;

	/**
	 * Creates a AuraLightComponent which can be attached to an entity to enable it to produce light.
	 */
	public AuraLightComponent() {
		this(DEFAULT_DISTANCE, DEFAULT_COLOR);
	}

	/**
	 * Creates an AuraLightComponent which can be attached to an entity to enable it to produce light.
	 * @param distance distance/spread of the light
	 */
	public AuraLightComponent(float distance) {
		this(distance, DEFAULT_COLOR);
	}


	/**
	 *
	 * Creates an AuraLightComponent which can be attached to an entity to enable it to produce light.
	 * @param color color of the light being created
	 */
	public AuraLightComponent(Color color) {
		this(DEFAULT_DISTANCE, color);
	}

	/**
	 *
	 * Creates an AuraLightComponent which can be attached to an entity to enable it to produce light.
	 * @param distance distance/spread of the light
	 * @param color color of the light being created
	 */
	public AuraLightComponent(float distance, Color color) {
		light = new PointLight(ServiceLocator.getLightService().getRayHandler(), 25, color, distance, 0, 0);
		light.setStaticLight(true);
		light.setXray(true); // Stops most of the shadows and reduces CPU burden
		light.setSoft(true);
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
	 * Determines whether the light is currently on
	 * @return state of the light (on/off)
	 */
	public boolean getActive() {
		return light.isActive();
	}

	/**
	 * Returns the Box2dLights light
	 * @return point light
	 */
	public PointLight getLight() {
		return light;
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
