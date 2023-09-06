package com.csse3200.game.rendering;

import box2dLight.DirectionalLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.Ray;
import com.csse3200.game.services.ServiceLocator;

public class LightingController {

	private OrthographicCamera camera;
	private PointLight playerLight;
	private PointLight sunLight;
	private RayHandler rayHandler;
	private float test = 0.001f;

	public LightingController(OrthographicCamera camera) {
		this.camera = camera;

		// Setup rayhandler
		RayHandler.useDiffuseLight(true);
		rayHandler = new RayHandler(ServiceLocator.getPhysicsService().getPhysics().getWorld());
		rayHandler.setCulling(true); // Saves CPU and GPU time
		rayHandler.setCombinedMatrix(camera);
		rayHandler.setAmbientLight(1);

		// Sun Light
		sunLight = new PointLight(rayHandler, 200);
		Vector2 sunPosition = ServiceLocator.getGameArea().getPlayer().getCenterPosition();
		sunPosition.sub(20, 20);
		sunLight.setPosition(sunPosition);
		sunLight.setDistance(250);
		sunLight.setColor(3, 12, 33, 0.5f);

		// Player light
		playerLight = new PointLight(rayHandler, 200);
		playerLight.setPosition(ServiceLocator.getGameArea().getPlayer().getCenterPosition());
		playerLight.setDistance(5);
		playerLight.setColor(3, 12, 33, 0.7f);



	}

	public void render() {
		// Adjust intensity of the sun
		// Change position of player light

		test += 0.0001f;
		if (test > 1) {
			test = 0.001f;
		}
		rayHandler.setCombinedMatrix(camera);
		playerLight.setPosition(ServiceLocator.getGameArea().getPlayer().getCenterPosition());

		Vector2 sunPosition = ServiceLocator.getGameArea().getPlayer().getCenterPosition();
		sunPosition.sub(20, 20);
		sunLight.setColor(3, 12, 33, 1);
		sunLight.setPosition(sunPosition);
		rayHandler.updateAndRender();
	}
}
