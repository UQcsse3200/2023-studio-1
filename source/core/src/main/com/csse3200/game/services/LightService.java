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

	/**
	 * Creates a LightService which is accessed via the ServiceLocator and used to create lights
	 */
	public LightService() {
		RayHandler.useDiffuseLight(true);
		rayHandler = new RayHandler(ServiceLocator.getPhysicsService().getPhysics().getWorld());
		this.camera = (OrthographicCamera) ServiceLocator.getCameraComponent().getCamera();
		rayHandler.setCulling(true);
		rayHandler.setAmbientLight(0.3f, 0.3f, 0.7f, 0.1f);
	}

	/**
	 * Renders the lighting involved in the game and sets the ambient light to be based on the time of day
	 */
	public void renderLight() {
		float time = ServiceLocator.getTimeService().getHour() + (float) ServiceLocator.getTimeService().getMinute() / 60;
		float brightness = (float) (MathUtils.sin((float) ((float) Math.PI * (time-2) / 23 + 0.1)));
		rayHandler.setAmbientLight(brightness, brightness, brightness, brightness);
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
}
