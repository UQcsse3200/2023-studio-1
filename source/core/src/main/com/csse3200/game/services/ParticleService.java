package com.csse3200.game.services;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.rendering.ParticleEffectWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ParticleService {

	public static final String WEATHER_EVENT = "WEATHER_EVENT";

	ArrayList<ParticleEffectWrapper> queuedEffects;
	private HashMap<ParticleEffectType, ParticleEffectPool> particleEffectPools;

	public enum ParticleEffectType {
		ACID_RAIN(WEATHER_EVENT, "particle-effects/acid_rain.p", 1, 10);

		private final String category;
		private final String effectPath;
		private final int minCapacity;
		private final int maxCapacity;

		ParticleEffectType(String category, String effectPath, int minCapacity, int maxCapacity) {
			this.category = category;
			this.effectPath = effectPath;
			this.minCapacity = minCapacity;
			this.maxCapacity = maxCapacity;
		}
	}

	public ParticleService() {
		String[] particleNames = new String[ParticleEffectType.values().length];
		int i = 0;
		// Loads the particles
		for (ParticleEffectType effectType : ParticleEffectType.values()) {
			particleNames[i] = effectType.effectPath;
			i++;
		}
		ServiceLocator.getResourceService().loadParticleEffects(particleNames);

		// Block until all particle effects are loaded
		ServiceLocator.getResourceService().loadAll();

		// Creates pools for all the particles
		ParticleEffect effect;
		for (ParticleEffectType effectType : ParticleEffectType.values()) {
			effect = ServiceLocator.getResourceService().getAsset(effectType.effectPath, ParticleEffect.class);
			particleEffectPools.put(effectType, new ParticleEffectPool(effect, effectType.minCapacity, effectType.maxCapacity));
		}
	}

	public void render (SpriteBatch batch, float delta) {
		for (ParticleEffectWrapper wrapper : queuedEffects) {
			if (wrapper.getPooledEffect().isComplete()) {
				wrapper.getPooledEffect().reset();
			}
			wrapper.getPooledEffect().draw(batch, delta);
		}
	}

	public void startEffect(ParticleEffectType effectType) {
		ParticleEffectWrapper effectWrapper = new ParticleEffectWrapper(particleEffectPools.get(effectType).obtain(), effectType.category, effectType.name());
		queuedEffects.add(effectWrapper);
		effectWrapper.getPooledEffect().start();
	}

	public void stopEffect(String type) {
		Predicate<ParticleEffectWrapper> predicate = effectWrapper -> effectWrapper.getType().equals(type);
		List<ParticleEffectWrapper> wrappers = queuedEffects.stream().filter(predicate).toList();
		for (ParticleEffectWrapper wrapper : wrappers) {
			wrapper.getPooledEffect().free();
		}
		queuedEffects.removeIf(predicate);
	}

	public void stopEffectCategory(String category) {
		Predicate<ParticleEffectWrapper> predicate = effectWrapper -> effectWrapper.getCategory().equals(category);
		List<ParticleEffectWrapper> wrappers = queuedEffects.stream().filter(predicate).toList();
		for (ParticleEffectWrapper wrapper : wrappers) {
			wrapper.getPooledEffect().free();
		}
		queuedEffects.removeIf(predicate);
	}
}
