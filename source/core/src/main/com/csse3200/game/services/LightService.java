package com.csse3200.game.services;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

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
		rayHandler = new RayHandler(ServiceLocator.getPhysicsService().getPhysics().getWorld());
		this.camera = (OrthographicCamera) ServiceLocator.getCameraComponent().getCamera();
		rayHandler.setCulling(true);
		rayHandler.setAmbientLight(0.8f);
	}

	/**
	 * Renders the lighting involved in the game and sets the ambient light to be based on the time of day
	 */
	public void renderLight() {
		float time = ServiceLocator.getTimeService().getHour() + (float) ServiceLocator.getTimeService().getMinute() / 60;
		rayHandler.setAmbientLight((float) (MathUtils.sin((float) (Math.PI * time / 23)) + 0.1));
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
