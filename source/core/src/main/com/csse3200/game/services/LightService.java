package com.csse3200.game.services;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

public class LightService {

	private final RayHandler rayHandler;
	private final OrthographicCamera camera;
	public LightService() {
		rayHandler = new RayHandler(ServiceLocator.getPhysicsService().getPhysics().getWorld());
		this.camera = (OrthographicCamera) ServiceLocator.getCameraComponent().getCamera();
		rayHandler.setCulling(true);
		rayHandler.setAmbientLight(0.8f);
	}

	public void renderLight() {
		float time = ServiceLocator.getTimeService().getHour() + (float) ServiceLocator.getTimeService().getMinute() / 60;
		rayHandler.setAmbientLight((float) (MathUtils.sin((float) (Math.PI / 23 * time)) + 0.1));
		rayHandler.setCombinedMatrix(camera);
		rayHandler.updateAndRender();
	}

	public RayHandler getRayHandler() {
		return rayHandler;
	}
}
