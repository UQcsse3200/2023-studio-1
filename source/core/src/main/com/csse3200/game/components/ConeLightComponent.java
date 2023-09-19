package com.csse3200.game.components;

import com.badlogic.gdx.graphics.Color;
import com.csse3200.game.services.ServiceLocator;

import box2dLight.ConeLight;

public class ConeLightComponent extends Component {

	/**
	 * Cone light from box2dlights that gets rendered by the ray handler
	 */
	private final ConeLight light;

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
	 * Default size of the light cone in degrees
	 */
	private static final float DEFAULT_CONE_DEGREE = 30f;

	/**
	 * Default direction that the cone light is pointing in. Value of 0 is right, degrees goes anticlockwise
	 */
	private static final float DEFAULT_DIRECTION_DEGREE = 0f;


	/**
	 * Creates a ConeLightComponent which can be attached to an entity to enable it to produce light.
	 */
	public ConeLightComponent() {
		this(DEFAULT_DISTANCE, DEFAULT_COLOR, DEFAULT_DIRECTION_DEGREE, DEFAULT_CONE_DEGREE);
	}

	/**
	 * Creates an ConeLightComponent which can be attached to an entity to enable it to produce light.
	 *
	 * @param distance distance/spread of the light
	 */
	public ConeLightComponent(float distance) {
		this(distance, DEFAULT_COLOR, DEFAULT_DIRECTION_DEGREE, DEFAULT_CONE_DEGREE);
	}


	/**
	 * Creates an ConeLightComponent which can be attached to an entity to enable it to produce light.
	 *
	 * @param color color of the light being created
	 */
	public ConeLightComponent(Color color) {
		this(DEFAULT_DISTANCE, color, DEFAULT_DIRECTION_DEGREE, DEFAULT_CONE_DEGREE);
	}

	/**
	 * Creates an ConeLightComponent which can be attached to an entity to enable it to produce light.
	 *
	 * @param distance distance/spread of the light
	 * @param color    color of the light being created
	 */
	public ConeLightComponent(float distance, Color color) {
		this(distance, color, DEFAULT_DIRECTION_DEGREE, DEFAULT_CONE_DEGREE);
	}

	/**
	 * Creates an ConeLightComponent which can be attached to an entity to enable it to produce light.
	 *
	 * @param distance        distance/spread of the light
	 * @param color           color of the light being created
	 * @param directionDegree direction that the light is pointing in
	 */
	public ConeLightComponent(float distance, Color color, float directionDegree) {
		this(distance, color, directionDegree, DEFAULT_CONE_DEGREE);
	}

	/**
	 * Creates an ConeLightComponent which can be attached to an entity to enable it to produce light.
	 *
	 * @param distance        distance/spread of the light
	 * @param color           color of the light being created
	 * @param directionDegree direction that the light is pointing in
	 * @param coneDegree      size of the cone in degrees
	 */
	public ConeLightComponent(float distance, Color color, float directionDegree, float coneDegree) {
		light = new ConeLight(ServiceLocator.getLightService().getRayHandler(), 25, color, distance, 0, 0, directionDegree, coneDegree);
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
		light.setPosition(entity.getCenterPosition());
		entity.getEvents().addListener("toggleLight", this::toggleLight);
	}

	/**
	 * Sets the distance/spread of the light
	 *
	 * @param distance distance/spread of the light
	 */
	public void setDistance(float distance) {
		light.setDistance(distance);
	}

	/**
	 * Sets the color of the light
	 *
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
	 *
	 * @return state of the light (on/off)
	 */
	public boolean getActive() {
		return light.isActive();
	}


	/**
	 * Changes the direction of the cone light
	 *
	 * @param direction direction of the light
	 */
	public void setDirection(float direction) {
		light.setDirection(direction);
	}

	/**
	 * Changes the cone degree of the light
	 * @param coneDegree cone degree of the light
	 */
	public void setConeDegree(float coneDegree) {
		light.setConeDegree(coneDegree);
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