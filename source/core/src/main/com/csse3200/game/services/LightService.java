package com.csse3200.game.services;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

public class LightService {

	private final RayHandler rayHandler;
	private final OrthographicCamera camera;
	public LightService(OrthographicCamera camera) {
		rayHandler = new RayHandler(ServiceLocator.getPhysicsService().getPhysics().getWorld());
		this.camera = camera;
		rayHandler.setCulling(true);
		rayHandler.setAmbientLight(0.8f);
		ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::updateAmbientLighting);
	}

	public void renderLight() {
		rayHandler.setCombinedMatrix(camera);
		rayHandler.updateAndRender();
	}

	public RayHandler getRayHandler() {
		return rayHandler;
	}

	private void updateAmbientLighting() {
		int hour = ServiceLocator.getTimeService().getHour();
		rayHandler.setAmbientLight((float) (MathUtils.sin((float) (Math.PI / 23 * hour)) + 0.1));
	}
}
