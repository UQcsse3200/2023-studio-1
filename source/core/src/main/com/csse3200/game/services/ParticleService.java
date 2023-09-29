package com.csse3200.game.services;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;

import java.util.HashMap;
import java.util.Map;

public class ParticleService {

	public static final String WEATHER_EVENT = "WEATHER_EVENT";

	private HashMap<ParticleEffectType, ParticleEffect> queuedEffects;
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
		// Creates pools for all the particles
		ParticleEffect effect;
		ServiceLocator.getResourceService().loadParticleEffects(particleNames);
		for (ParticleEffectType effectType : ParticleEffectType.values()) {
			effect = ServiceLocator.getResourceService().getAsset(effectType.effectPath, ParticleEffect.class);
			particleEffectPools.put(effectType, new ParticleEffectPool(effect, effectType.minCapacity, effectType.maxCapacity));
		}
	}
}
