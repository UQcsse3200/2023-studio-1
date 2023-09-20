package com.csse3200.game.components;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.LightService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertFalse;


import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class AuraLightComponentTest {


	private PointLight light;

	private RayHandler rayHandler;

	private LightService lightService;

	@BeforeEach
	public void setUp() {
		rayHandler = mock(RayHandler.class);
		lightService = mock(LightService.class);
		when(lightService.getRayHandler()).thenReturn(rayHandler);
		ServiceLocator.registerLightService(lightService);

	}

	@Test
	public void testSetDistance() {
		try (MockedConstruction<PointLight> mock = mockConstruction(PointLight.class)) {
			AuraLightComponent component = new AuraLightComponent();
			light = mock.constructed().get(0);

			component.setDistance(10);
			verify(light, times(1)).setDistance(10);
		}
	}

	@Test
	public void testToggle() {
		try (MockedConstruction<PointLight> mock = mockConstruction(PointLight.class)) {
			Entity entity = createEntity();
			AuraLightComponent component = entity.getComponent(AuraLightComponent.class);
			light = mock.constructed().get(0);

			component.getEntity().getEvents().trigger("toggleLight");
			verify(light, times(1)).setActive(true);
		}
	}

	@Test
	public void testCreate() {
		try (MockedConstruction<PointLight> mock = mockConstruction(PointLight.class)) {
			Entity entity = createEntity();
			AuraLightComponent component = entity.getComponent(AuraLightComponent.class);
			light = mock.constructed().get(0);

			verify(light, times(1)).setActive(false);
		}
	}

	@Test
	public void testSetColor() {
		try (MockedConstruction<PointLight> mock = mockConstruction(PointLight.class)) {
			AuraLightComponent component = new AuraLightComponent();
			light = mock.constructed().get(0);

			Color color = Color.LIGHT_GRAY;
			component.setColor(color);
			verify(light, times(1)).setColor(color);
		}
	}

	@Test
	public void testGetActive() {
		try (MockedConstruction<PointLight> mock = mockConstruction(PointLight.class)) {
			AuraLightComponent component = new AuraLightComponent();
			light = mock.constructed().get(0);

			assertFalse(component.getActive());
		}
	}

	@Test
	public void testUpdate() {
		try (MockedConstruction<PointLight> mock = mockConstruction(PointLight.class)) {
			Entity entity = createEntity();
			AuraLightComponent component = entity.getComponent(AuraLightComponent.class);
			light = mock.constructed().get(0);

			Vector2 position = new Vector2(0, 0);
			Vector2 centrePosition = new Vector2(0.5f, 0.5f);

			entity.setPosition(position);
			component.update();

			// Once when created, once when updated
			verify(light, times(2)).setPosition(centrePosition);
		}
	}

	@Test
	public void testDispose() {
		try (MockedConstruction<PointLight> mock = mockConstruction(PointLight.class)) {
			Entity entity = createEntity();
			AuraLightComponent component = entity.getComponent(AuraLightComponent.class);
			light = mock.constructed().get(0);
			component.dispose();
			verify(light, times(1)).dispose();
		}
	}

	Entity createEntity() {
		Entity entity = new Entity().addComponent(new AuraLightComponent());
		entity.create();
		return entity;
	}

}