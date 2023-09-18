package com.csse3200.game.services;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class LightServiceTest {

	RayHandler rayHandler;

	@BeforeEach
	public void setUp() {
		PhysicsService physicsService = mock(PhysicsService.class);
		PhysicsEngine physicsEngine = mock(PhysicsEngine.class);
		when(physicsService.getPhysics()).thenReturn(physicsEngine);
		ServiceLocator.registerPhysicsService(physicsService);

		OrthographicCamera camera = mock(OrthographicCamera.class);
		CameraComponent cameraComponent = mock(CameraComponent.class);
		when(cameraComponent.getCamera()).thenReturn(camera);
		ServiceLocator.registerCameraComponent(cameraComponent);

		GameTime gameTime = mock(GameTime.class);
		ServiceLocator.registerTimeSource(gameTime);

		TimeService timeService = mock(TimeService.class);
		when(timeService.getHour()).thenReturn(1);
		when(timeService.getMinute()).thenReturn(0);
		ServiceLocator.registerTimeService(timeService);
	}

	@AfterEach
	public void clear() {
		ServiceLocator.clear();
	}

	@Test
	public void testRenderLight() {
		try (MockedConstruction<RayHandler> mock = mockConstruction(RayHandler.class)) {
			LightService lightService = new LightService();
			rayHandler = mock.constructed().get(0);
			verify(rayHandler, times(1)).setAmbientLight(0.8f);

			lightService.renderLight();
			verify(rayHandler, times(1)).setAmbientLight(0.23629053f);
			verify(rayHandler, times(1)).setCombinedMatrix((OrthographicCamera) any());
			verify(rayHandler).updateAndRender();
		}
	}

	@Test
	public void testGetRayHandler() {
		try (MockedConstruction<RayHandler> mock = mockConstruction(RayHandler.class)) {
			LightService lightService = new LightService();
			rayHandler = mock.constructed().get(0);
			assertEquals(rayHandler, lightService.getRayHandler());
		}
	}


}