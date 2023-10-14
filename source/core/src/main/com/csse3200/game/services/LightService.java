package com.csse3200.game.services;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

import box2dLight.RayHandler;

public class LightService {
	/**
	 * Box2dLights ray handler that handles all of the rendering of the lights
	 */
	private final RayHandler rayHandler;

	/**
	 * Game camera
	 */
	private final OrthographicCamera camera;

	private Color colourOffset;
	private float brightnessMultiplier;

	/**
	 * Creates a LightService which is accessed via the ServiceLocator and used to create lights
	 */
	public LightService() {
		RayHandler.useDiffuseLight(true);

		rayHandler = new RayHandler(ServiceLocator.getPhysicsService().getPhysics().getWorld());
		this.camera = (OrthographicCamera) ServiceLocator.getCameraComponent().getCamera();

		rayHandler.setCulling(true);
		rayHandler.setAmbientLight(0.3f, 0.3f, 0.7f, 0.1f);

		colourOffset = Color.CLEAR;
		brightnessMultiplier = 1.0f;
	}

	/**
	 * Renders the lighting involved in the game and sets the ambient light to be based on the time of day
	 */
	public void renderLight() {
		float time = ServiceLocator.getTimeService().getHour() + (float) ServiceLocator.getTimeService().getMinute() / 60;
//		float brightness = 1.2f * MathUtils.sin((float) (Math.PI * time / 23 - 0.1f)) - 0.1f;
		rayHandler.setAmbientLight(getAmbientLight(time));
//		rayHandler.setAmbientLight(brightness, brightness, brightness, brightness);
		rayHandler.setCombinedMatrix(camera);
		rayHandler.updateAndRender();
	}

	/**
	 * Getter method for the ray handler
	 * @return ray handler instance used for creating lights
	 */
	public RayHandler getRayHandler() {
		return rayHandler;
	}

	/**
	 * Sets the global ambient colour offset. This offset will be added to the global ambient lighting (after any
	 * brightness multipliers are applied).
	 * @param colourOffset The global ambient light colour offset. Set to {@link Color#CLEAR} for default.
	 */
	public void setColourOffset(Color colourOffset) {
		this.colourOffset = colourOffset;
	}

	/**
	 * Sets the global ambient brightness multiplier. This multiplier will pre-multiply the global brightness (before
	 * any global brightness offsets are added.
	 * @param brightnessMultiplier The global ambient light brightness multiplier. Set to 1 for regular.
	 */
	public void setBrightnessMultiplier(float brightnessMultiplier) {
		this.brightnessMultiplier = brightnessMultiplier;
	}

	private Color getAmbientLight(float timeOfDay) {
		// Calculate expected brightness
		float brightness = MathUtils.sin((float) (Math.PI * timeOfDay / 23.0f - 0.1f));
		brightness = 0.8f * brightness * brightness + 0.2f;
		brightness *= brightness;

		// Apply brightness multiplier
		brightness *= brightnessMultiplier;
		// Apply and return ambient light with colour offset
		return new Color(brightness + colourOffset.r, brightness + colourOffset.g, brightness + colourOffset.b,
				1.0f + colourOffset.a);
	}

}
