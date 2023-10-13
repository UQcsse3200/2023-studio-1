package com.csse3200.game.components;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.rendering.ParticleEffectWrapper;
import com.csse3200.game.services.ParticleService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParticleEffectComponentTest {

	private ParticleService particleService;

	@BeforeEach
	void setUp() {
		particleService = mock(ParticleService.class);
		ServiceLocator.registerEntityService(new EntityService());
		ServiceLocator.registerParticleService(particleService);
	}

	@AfterEach
	void clear() {
		ServiceLocator.clear();
	}

	@Test
	void testCreate() {
		Entity entity = mock(Entity.class);
		EventHandler eventHandler = mock(EventHandler.class);
		when(entity.getEvents()).thenReturn(eventHandler);
		ParticleEffectComponent component = new ParticleEffectComponent();
		entity.addComponent(component);
		component.setEntity(entity);
		component.create();
		verify(particleService).addComponent(component);
	}

	@Test
	void testNonActiveRender() throws IllegalAccessException {
		SpriteBatch batch = mock(SpriteBatch.class);
		float delta = 0;

		ParticleEffectComponent component = new ParticleEffectComponent();

		ParticleEffectPool.PooledEffect effect = mock(ParticleEffectPool.PooledEffect.class);

		Field effectField = ReflectionUtils.findFields(ParticleEffectComponent.class, f -> f.getName().equals("effect"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);

		effectField.setAccessible(true);
		effectField.set(component, effect);

		component.render(batch, delta);
		verify(effect, times(0)).draw(batch, delta);
	}


	@Test
	void testRenderEffect() {
		SpriteBatch batch = mock(SpriteBatch.class);
		float delta = 0;

		ParticleEffectComponent component = new ParticleEffectComponent();
		Entity entity = mock(Entity.class);
		Vector2 position = new Vector2(0, 0);
		when(entity.getCenterPosition()).thenReturn(position);
		entity.addComponent(component);
		component.setEntity(entity);

		ParticleEffectPool.PooledEffect effect = mock(ParticleEffectPool.PooledEffect.class);
		when(particleService.getEffect(any())).thenReturn(effect);

		component.startEffect(any());


		when(effect.isComplete()).thenReturn(false);
		component.render(batch, delta);
		verify(effect).draw(batch, delta);

		when(effect.isComplete()).thenReturn(true);
		component.render(batch, delta);
		assertFalse(component.isActive());
		verify(effect, times(1)).free();
	}

	@Test
	void testStartEffect() {
		ParticleEffectComponent component = new ParticleEffectComponent();
		Entity entity = mock(Entity.class);
		Vector2 position = new Vector2(0, 0);
		when(entity.getCenterPosition()).thenReturn(position);
		entity.addComponent(component);
		component.setEntity(entity);

		ParticleEffectPool.PooledEffect effect = mock(ParticleEffectPool.PooledEffect.class);
		when(particleService.getEffect(any())).thenReturn(effect);

		component.startEffect(ParticleService.ParticleEffectType.ACID_RAIN);

		verify(effect, times(1)).setPosition(position.x, position.y);
		verify(effect, times(1)).start();
	}

	@Test
	void testStopEffect() {
		ParticleEffectComponent component = new ParticleEffectComponent();
		Entity entity = mock(Entity.class);
		Vector2 position = new Vector2(0, 0);
		when(entity.getCenterPosition()).thenReturn(position);
		entity.addComponent(component);
		component.setEntity(entity);

		ParticleEffectPool.PooledEffect effect = mock(ParticleEffectPool.PooledEffect.class);
		when(particleService.getEffect(any())).thenReturn(effect);

		component.startEffect(ParticleService.ParticleEffectType.ACID_RAIN);

		component.stopEffect(ParticleService.ParticleEffectType.ACID_RAIN);

		verify(effect, times(1)).free();
		assertFalse(component.isActive());
	}

	@Test
	void getEffectType() {
		ParticleEffectComponent component = new ParticleEffectComponent();
		Entity entity = mock(Entity.class);
		Vector2 position = new Vector2(0, 0);
		when(entity.getCenterPosition()).thenReturn(position);
		entity.addComponent(component);
		component.setEntity(entity);

		ParticleEffectPool.PooledEffect effect = mock(ParticleEffectPool.PooledEffect.class);
		when(particleService.getEffect(any())).thenReturn(effect);

		component.startEffect(ParticleService.ParticleEffectType.ACID_RAIN);
		assertEquals(ParticleService.ParticleEffectType.ACID_RAIN, component.getEffectType());
	}

	@Test
	void testDispose() throws IllegalAccessException {
		ParticleEffectComponent component = new ParticleEffectComponent();

		ParticleEffectPool.PooledEffect effect = mock(ParticleEffectPool.PooledEffect.class);

		Field effectField = ReflectionUtils.findFields(ParticleEffectComponent.class, f -> f.getName().equals("effect"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);

		effectField.setAccessible(true);
		effectField.set(component, effect);

		component.dispose();

		verify(effect, times(1)).free();
		verify(particleService, times(1)).removeComponent(component);


	}
}