package com.csse3200.game.services;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.ParticleEffectWrapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParticleServiceTest {


	ParticleEffect mockEffect;
	ResourceService resourceService;

	@BeforeEach
	public void setUp() {
		ServiceLocator.clear();

		resourceService = mock(ResourceService.class);
		ServiceLocator.registerResourceService(resourceService);
		mockEffect = mock(ParticleEffect.class);
	}

	@AfterEach
	public void cleanUp() {
		ServiceLocator.clear();
	}

	@Test
	public void testConstructor() throws IllegalAccessException {
		ParticleService particleService = new ParticleService();
		// Tests whether they were all loaded
		verify(resourceService, times(1)).loadParticleEffects(any());
		verify(resourceService, times(1)).loadAll();
		// Tests if assets were retrieved after they were loaded
		verify(resourceService, times(ParticleService.ParticleEffectType.values().length)).getAsset(anyString(), any());
	}

	@Test
	public void testRender() throws IllegalAccessException {
		@SuppressWarnings("unchecked")
		ArrayList<ParticleEffectWrapper> mockQueuedEffects = mock(ArrayList.class);

		ParticleService particleService = new ParticleService();
		Field queuedField = ReflectionUtils.findFields(ParticleService.class, f -> f.getName().equals("queuedEffects"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);

		queuedField.setAccessible(true);
		queuedField.set(particleService, mockQueuedEffects);

		ParticleEffectWrapper wrapper = mock(ParticleEffectWrapper.class);
		ParticleEffectPool.PooledEffect pooledEffect = mock(ParticleEffectPool.PooledEffect.class);
		when(wrapper.getPooledEffect()).thenReturn(pooledEffect);

		@SuppressWarnings("unchecked")
		Iterator<ParticleEffectWrapper> iter = mock(Iterator.class);
		when(mockQueuedEffects.iterator()).thenReturn(iter);
		when(iter.hasNext()).thenReturn(true, false);
		when(iter.next()).thenReturn(wrapper);

		GameArea gameArea = mock(GameArea.class);
		Entity player = mock(Entity.class);
		when(player.getCenterPosition()).thenReturn(new Vector2(0, 0));
		when(gameArea.getPlayer()).thenReturn(player);
		ServiceLocator.registerGameArea(gameArea);

		particleService.render(mock(SpriteBatch.class), 0f);

		verify(pooledEffect, times(1)).draw(any(SpriteBatch.class), anyFloat());
		verify(pooledEffect, times(1)).setPosition(anyFloat(), anyFloat());
	}

	@Test
	public void testStartEffect() throws IllegalAccessException {
		@SuppressWarnings("unchecked")
		HashMap<ParticleService.ParticleEffectType, ParticleEffectPool> mockPools = mock(HashMap.class);

		@SuppressWarnings("unchecked")
		ArrayList<ParticleEffectWrapper> mockQueuedEffects = mock(ArrayList.class);

		ParticleService particleService = new ParticleService();
		Field poolsField = ReflectionUtils.findFields(ParticleService.class, f -> f.getName().equals("particleEffectPools"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
		Field queuedField = ReflectionUtils.findFields(ParticleService.class, f -> f.getName().equals("queuedEffects"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);

		poolsField.setAccessible(true);
		queuedField.setAccessible(true);
		poolsField.set(particleService, mockPools);
		queuedField.set(particleService, mockQueuedEffects);

		ParticleEffectPool pool = mock(ParticleEffectPool.class);
		ParticleEffectPool.PooledEffect pooledEffect = mock(ParticleEffectPool.PooledEffect.class);
		when(mockPools.get(any(ParticleService.ParticleEffectType.class))).thenReturn(pool);
		when(pool.obtain()).thenReturn(pooledEffect);

		particleService.startEffect(ParticleService.ParticleEffectType.ACID_RAIN);

		verify(mockQueuedEffects, times(1)).add(any(ParticleEffectWrapper.class));
		verify(pooledEffect, times(1)).start();
	}

	@Test
	public void testStopEffect() throws IllegalAccessException {
		// Injecting mock into private field
		@SuppressWarnings("unchecked")
		HashMap<ParticleService.ParticleEffectType, ParticleEffectPool> mockPools = mock(HashMap.class);
		@SuppressWarnings("unchecked")
		ArrayList<ParticleEffectWrapper> mockQueuedEffects = mock(ArrayList.class);
		ParticleService particleService = new ParticleService();

		Field poolsField = ReflectionUtils.findFields(ParticleService.class, f -> f.getName().equals("particleEffectPools"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
		Field queuedField = ReflectionUtils.findFields(ParticleService.class, f -> f.getName().equals("queuedEffects"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);

		poolsField.setAccessible(true);
		queuedField.setAccessible(true);
		poolsField.set(particleService, mockPools);
		queuedField.set(particleService, mockQueuedEffects);

		ParticleEffectPool pool = mock(ParticleEffectPool.class);
		ParticleEffectPool.PooledEffect pooledEffect = mock(ParticleEffectPool.PooledEffect.class);
		when(mockPools.get(any(ParticleService.ParticleEffectType.class))).thenReturn(pool);
		when(pool.obtain()).thenReturn(pooledEffect);

		particleService.startEffect(ParticleService.ParticleEffectType.ACID_RAIN);
		assertFalse(mockQueuedEffects.isEmpty());


		particleService.stopEffect(ParticleService.ParticleEffectType.ACID_RAIN);
		assertEquals(0, mockQueuedEffects.size());
	}

	@Test
	public void testStopEffectCategory() throws IllegalAccessException {
		// Injecting mock into private field
		@SuppressWarnings("unchecked")
		HashMap<ParticleService.ParticleEffectType, ParticleEffectPool> mockPools = mock(HashMap.class);
		@SuppressWarnings("unchecked")
		ArrayList<ParticleEffectWrapper> mockQueuedEffects = mock(ArrayList.class);
		ParticleService particleService = new ParticleService();

		Field poolsField = ReflectionUtils.findFields(ParticleService.class, f -> f.getName().equals("particleEffectPools"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
		Field queuedField = ReflectionUtils.findFields(ParticleService.class, f -> f.getName().equals("queuedEffects"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);

		poolsField.setAccessible(true);
		queuedField.setAccessible(true);
		poolsField.set(particleService, mockPools);
		queuedField.set(particleService, mockQueuedEffects);

		ParticleEffectPool pool = mock(ParticleEffectPool.class);
		ParticleEffectPool.PooledEffect pooledEffect = mock(ParticleEffectPool.PooledEffect.class);
		when(mockPools.get(any(ParticleService.ParticleEffectType.class))).thenReturn(pool);
		when(pool.obtain()).thenReturn(pooledEffect);

		particleService.startEffect(ParticleService.ParticleEffectType.ACID_RAIN);
		assertFalse(mockQueuedEffects.isEmpty());


		particleService.stopEffectCategory(ParticleService.WEATHER_EVENT);
		assertEquals(0, mockQueuedEffects.size());
	}

	@Test
	public void testGetCategory() {
		ParticleService.ParticleEffectType particleEffectType = ParticleService.ParticleEffectType.ACID_RAIN;
		assertEquals(ParticleService.WEATHER_EVENT, particleEffectType.getCategory());
	}
}
