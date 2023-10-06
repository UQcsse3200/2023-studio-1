package com.csse3200.game.components;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.LightService;
import com.csse3200.game.services.ServiceLocator;

import box2dLight.ConeLight;
import box2dLight.RayHandler;

@ExtendWith(GameExtension.class)
class ConeLightComponentTest {


	private ConeLight light;

	private RayHandler rayHandler;

	private LightService lightService;

	@BeforeEach
	public void setUp() {
		rayHandler = mock(RayHandler.class);
		lightService = mock(LightService.class);
		when(lightService.getRayHandler()).thenReturn(rayHandler);
		ServiceLocator.registerLightService(lightService);
	}

	@AfterEach
	public void clear() {
		ServiceLocator.clear();
	}

	@Test
	void testSetDistance() {
		try (MockedConstruction<ConeLight> mock = mockConstruction(ConeLight.class)) {
			ConeLightComponent component = new ConeLightComponent();
			light = mock.constructed().get(0);

			component.setDistance(10);
			verify(light, times(1)).setDistance(10);
		}
	}

	@Test
	void testToggle() {
		try (MockedConstruction<ConeLight> mock = mockConstruction(ConeLight.class)) {
			Entity entity = createEntity();
			ConeLightComponent component = entity.getComponent(ConeLightComponent.class);
			light = mock.constructed().get(0);

			component.getEntity().getEvents().trigger("toggleLight");
			verify(light, times(1)).setActive(true);
		}
	}

	@Test
	void testCreate() {
		try (MockedConstruction<ConeLight> mock = mockConstruction(ConeLight.class)) {
			Entity entity = createEntity();
			ConeLightComponent component = entity.getComponent(ConeLightComponent.class);
			light = mock.constructed().get(0);

			verify(light, times(1)).setActive(false);
		}
	}

	@Test
	void testSetColor() {
		try (MockedConstruction<ConeLight> mock = mockConstruction(ConeLight.class)) {
			ConeLightComponent component = new ConeLightComponent();
			light = mock.constructed().get(0);

			Color color = Color.LIGHT_GRAY;
			component.setColor(color);
			verify(light, times(1)).setColor(color);
		}
	}

	@Test
	void testGetActive() {
		try (MockedConstruction<ConeLight> mock = mockConstruction(ConeLight.class)) {
			ConeLightComponent component = new ConeLightComponent();
			light = mock.constructed().get(0);

			assertFalse(component.getActive());
		}
	}

	@Test
	void testUpdate() {
		try (MockedConstruction<ConeLight> mock = mockConstruction(ConeLight.class)) {
			Entity entity = createEntity();
			ConeLightComponent component = entity.getComponent(ConeLightComponent.class);
			light = mock.constructed().get(0);

			Vector2 position = new Vector2(0, 0);
			Vector2 centrePosition = new Vector2(0.5f, 0.5f);

			entity.setPosition(position);
			component.update();

			// Once on create, once on update
			verify(light, times(2)).setPosition(centrePosition);
		}
	}

	@Test
	void testDispose() {
		try (MockedConstruction<ConeLight> mock = mockConstruction(ConeLight.class)) {
			Entity entity = createEntity();
			ConeLightComponent component = entity.getComponent(ConeLightComponent.class);
			light = mock.constructed().get(0);
			component.dispose();
			verify(light, times(1)).remove();
		}
	}

	@Test
	void testSetDirection() {
		try (MockedConstruction<ConeLight> mock = mockConstruction(ConeLight.class)) {
			Entity entity = createEntity();
			ConeLightComponent component = entity.getComponent(ConeLightComponent.class);
			light = mock.constructed().get(0);
			component.setDirection(90);
			verify(light, times(1)).setDirection(90);
		}
	}


	Entity createEntity() {
		Entity entity = new Entity().addComponent(new ConeLightComponent());
		entity.create();
		return entity;
	}

}