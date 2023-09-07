package com.csse3200.game.physics;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Provides a global access point to the physics engine. This is necessary for physics-based
 * entities to add or remove themselves from the world, as well as update their position each frame.
 */
public class PhysicsService {
  private final PhysicsEngine engine;
  private final RayHandler rayHandler;

  public PhysicsService() {
    this(new PhysicsEngine());
  }

  public PhysicsService(PhysicsEngine engine) {
    this.engine = engine;
    rayHandler = new RayHandler(engine.getWorld());
  }

  public PhysicsEngine getPhysics() {
    return engine;
  }

  public RayHandler getRayHandler() {
    return rayHandler;
  }

  public void renderLight(OrthographicCamera camera) {
    rayHandler.setCombinedMatrix(camera);
    rayHandler.updateAndRender();
  }

}
