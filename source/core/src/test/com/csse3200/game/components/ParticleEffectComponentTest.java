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
import java.util.Iterator;

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
	void testRender() throws IllegalAccessException {
		SpriteBatch batch = mock(SpriteBatch.class);
		float delta = 0;

		@SuppressWarnings("unchecked")
		ArrayList<ParticleEffectWrapper> effects = mock(ArrayList.class);

		ParticleEffectComponent component = new ParticleEffectComponent();
		Field effectsField = ReflectionUtils.findFields(ParticleEffectComponent.class, f -> f.getName().equals("effects"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
		effectsField.setAccessible(true);
		effectsField.set(component, effects);

		ParticleEffectWrapper wrapper = mock(ParticleEffectWrapper.class);
		ParticleEffectPool.PooledEffect pooledEffect = mock(ParticleEffectPool.PooledEffect.class);
		when(wrapper.getPooledEffect()).thenReturn(pooledEffect);

		@SuppressWarnings("unchecked")
		Iterator<ParticleEffectWrapper> iter = mock(Iterator.class);
		when(effects.iterator()).thenReturn(iter);
		when(iter.hasNext()).thenReturn(true, false);
		when(iter.next()).thenReturn(wrapper);

		Entity entity = mock(Entity.class);
		when(entity.getCenterPosition()).thenReturn(new Vector2(1,1));
		when(entity.getPosition()).thenReturn(new Vector2(0,0));
		entity.addComponent(component);
		component.setEntity(entity);
		component.render(batch, delta);

		verify(pooledEffect, times(1)).draw(batch, delta);
	}


	@Test
	void testStartEffect() {
		ParticleEffectComponent component = new ParticleEffectComponent();
		Entity entity = mock(Entity.class);
		Vector2 position = new Vector2(1, 1);
		Vector2 centerPosition = new Vector2(0, 0);
		when(entity.getCenterPosition()).thenReturn(centerPosition);
		when(entity.getPosition()).thenReturn(position);
		entity.addComponent(component);
		component.setEntity(entity);

		ParticleEffectPool.PooledEffect effect = mock(ParticleEffectPool.PooledEffect.class);
		when(particleService.getEffect(any())).thenReturn(effect);

		component.startEffect(ParticleService.ParticleEffectType.ACID_RAIN);

		verify(effect, times(1)).setPosition(centerPosition.x, position.y);
		verify(effect, times(1)).start();
	}

	@Test
	void testStopEffect() {
		ParticleEffectComponent component = new ParticleEffectComponent();
		Entity entity = mock(Entity.class);
		when(entity.getCenterPosition()).thenReturn(new Vector2(0, 0));
		when(entity.getPosition()).thenReturn(new Vector2(0, 0));
		entity.addComponent(component);
		component.setEntity(entity);

		ParticleEffectPool.PooledEffect effect = mock(ParticleEffectPool.PooledEffect.class);
		when(particleService.getEffect(any())).thenReturn(effect);

		component.startEffect(ParticleService.ParticleEffectType.ACID_RAIN);

		component.stopEffect(ParticleService.ParticleEffectType.ACID_RAIN);

		verify(effect, times(1)).free();
	}

	@Test
	void testStopAllEffects() {
		ParticleEffectComponent component = new ParticleEffectComponent();
		Entity entity = mock(Entity.class);
		when(entity.getCenterPosition()).thenReturn(new Vector2(0, 0));
		when(entity.getPosition()).thenReturn(new Vector2(0, 0));
		entity.addComponent(component);
		component.setEntity(entity);

		ParticleEffectPool.PooledEffect effect = mock(ParticleEffectPool.PooledEffect.class);
		when(particleService.getEffect(any())).thenReturn(effect);

		component.startEffect(ParticleService.ParticleEffectType.ACID_RAIN);

		component.stopAllEffects();

		verify(effect, times(1)).free();
	}

	@Test
	void testStopEffectCategory() throws IllegalAccessException {
		ParticleEffectComponent component = new ParticleEffectComponent();
		Entity entity = mock(Entity.class);
		when(entity.getCenterPosition()).thenReturn(new Vector2(0, 0));
		when(entity.getPosition()).thenReturn(new Vector2(0, 0));
		entity.addComponent(component);
		component.setEntity(entity);

		ParticleEffectPool.PooledEffect effect = mock(ParticleEffectPool.PooledEffect.class);
		when(particleService.getEffect(any())).thenReturn(effect);

		component.startEffect(ParticleService.ParticleEffectType.ACID_RAIN);
		component.stopEffectCategory(ParticleService.ENTITY_EFFECT);
		verify(effect, times(0)).free();


		component.stopEffectCategory(ParticleService.WEATHER_EVENT);
		verify(effect, times(1)).free();
	}


	@Test
	void getEffectType() {
		ParticleEffectComponent component = new ParticleEffectComponent();
		Entity entity = mock(Entity.class);
		when(entity.getCenterPosition()).thenReturn(new Vector2(0, 0));
		when(entity.getPosition()).thenReturn(new Vector2(0, 0));
		entity.addComponent(component);
		component.setEntity(entity);

		ParticleEffectPool.PooledEffect effect = mock(ParticleEffectPool.PooledEffect.class);
		when(particleService.getEffect(any())).thenReturn(effect);

		component.startEffect(ParticleService.ParticleEffectType.ACID_RAIN);
	}

	@Test
	void testExists() {
		ParticleEffectComponent component = new ParticleEffectComponent();
		assertFalse(component.effectExists(ParticleService.ParticleEffectType.SUCCESS_EFFECT));
	}

	@Test
	void testDispose() throws IllegalAccessException {
		ParticleEffectComponent component = new ParticleEffectComponent();

		ParticleEffectPool.PooledEffect effect = mock(ParticleEffectPool.PooledEffect.class);
		Field effectField = ReflectionUtils.findFields(ParticleEffectComponent.class, f -> f.getName().equals("effects"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
		@SuppressWarnings("unchecked")
		ArrayList<ParticleEffectWrapper> effects = mock(ArrayList.class);
		ParticleEffectWrapper wrapper = mock(ParticleEffectWrapper.class);

		when(wrapper.getPooledEffect()).thenReturn(effect);

		@SuppressWarnings("unchecked")
		Iterator<ParticleEffectWrapper> iter = mock(Iterator.class);
		when(effects.iterator()).thenReturn(iter);
		when(iter.hasNext()).thenReturn(true, false);
		when(iter.next()).thenReturn(wrapper);

		effectField.setAccessible(true);
		effectField.set(component, effects);

		component.dispose();

		verify(effect, times(1)).free();
		verify(particleService, times(1)).removeComponent(component);
	}
}