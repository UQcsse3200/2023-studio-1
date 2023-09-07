package com.csse3200.game.services;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class LightService {

	private final RayHandler rayHandler;
	private final OrthographicCamera camera;
	public LightService(OrthographicCamera camera) {
		rayHandler = new RayHandler(ServiceLocator.getPhysicsService().getPhysics().getWorld());
		this.camera = camera;
	}

	public void renderLight() {
		rayHandler.setCombinedMatrix(camera);
		rayHandler.updateAndRender();
	}

	public RayHandler getRayHandler() {
		return rayHandler;
	}
}
