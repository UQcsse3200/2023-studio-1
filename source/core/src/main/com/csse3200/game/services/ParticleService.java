package com.csse3200.game.services;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.rendering.ParticleEffectWrapper;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Predicate;

public class ParticleService {

	public static final String WEATHER_EVENT = "WEATHER_EVENT";

	/**
	 * All the effects that will be rendered
	 */
	private final ArrayList<ParticleEffectWrapper> queuedEffects;

	/**
	 * Hashmap mapping the particle effect to the ParticleEffectType enum. This particle effect is needed in the
	 * creation of the particle effect pool and a reference to the effect needs to remain to ensure the pool functions
	 * properly.
	 */
	private final EnumMap<ParticleEffectType, ParticleEffect> particleEffects;

	/**
	 * Hashmap containing particle effect pools for each particle effect type.
	 */
	private final EnumMap<ParticleEffectType, ParticleEffectPool> particleEffectPools;

	/**
	 * Enum for each type of particle effect known to the particle system
	 */
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

		public String getCategory() {
			return category;
		}
	}

	/**
	 * Creates a particle service, loading in all particle effect assets and creating pools for those particle effects.
	 */
	public ParticleService() {
		queuedEffects = new ArrayList<>();
		particleEffectPools = new EnumMap<>(ParticleEffectType.class);
		particleEffects = new EnumMap<>(ParticleEffectType.class);

		String[] particleNames = new String[ParticleEffectType.values().length];
		int i = 0;
		// Creates store of particle effect paths to give to the resource service
		for (ParticleEffectType effectType : ParticleEffectType.values()) {
			particleNames[i] = effectType.effectPath;
			i++;
		}

		// Load all of the particle effects
		ServiceLocator.getResourceService().loadParticleEffects(particleNames);

		// Block until all particle effects are loaded
		ServiceLocator.getResourceService().loadAll();

		// Creates pools for all the particles
		for (ParticleEffectType effectType : ParticleEffectType.values()) {
			particleEffects.put(effectType, ServiceLocator.getResourceService().getAsset(effectType.effectPath, ParticleEffect.class));
			particleEffectPools.put(effectType, new ParticleEffectPool(particleEffects.get(effectType), effectType.minCapacity, effectType.maxCapacity));
		}
	}

	/**
	 * Renders the queued particle effects
	 * @param batch sprite batch used to render effects
	 * @param delta delta value used to update each particle effect
	 */
	public void render (SpriteBatch batch, float delta) {
		Vector2 playerPosition = ServiceLocator.getGameArea().getPlayer().getCenterPosition();
		for (ParticleEffectWrapper wrapper : queuedEffects) {
			wrapper.getPooledEffect().setPosition(playerPosition.x,playerPosition.y);
			wrapper.getPooledEffect().draw(batch, delta);
			if (wrapper.getPooledEffect().isComplete()) {
				wrapper.getPooledEffect().reset();
			}
		}
	}

	/**
	 * Starts a particle effect, creating an effect wrapper and adding it to the queue
	 * @param effectType type of effect to start
	 */
	public void startEffect(ParticleEffectType effectType) {
		// Grabs the effect from the effect pool using the enum
		ParticleEffectWrapper effectWrapper = new ParticleEffectWrapper(particleEffectPools.get(effectType).obtain(), effectType.category, effectType.name());
		// Adds the effect to the queued effects so the particle service knows to draw it
		queuedEffects.add(effectWrapper);
		effectWrapper.getPooledEffect().scaleEffect(0.1f);
		effectWrapper.getPooledEffect().start();
	}

	/**
	 * Stops a particle effect, freeing the effect and removing it from the queue
	 * @param effectType type of effect to stop
	 */
	public void stopEffect(ParticleEffectType effectType) {
		Predicate<ParticleEffectWrapper> predicate = effectWrapper -> effectWrapper.getType().equals(effectType.name());
		List<ParticleEffectWrapper> wrappers = queuedEffects.stream().filter(predicate).toList();
		for (ParticleEffectWrapper wrapper : wrappers) {
			wrapper.getPooledEffect().free();
		}
		queuedEffects.removeIf(predicate);
	}

	/**
	 * Stops all particle effects by category in the render queue
	 * @param category category of particle effects
	 */
	public void stopEffectCategory(String category) {
		Predicate<ParticleEffectWrapper> predicate = effectWrapper -> effectWrapper.getCategory().equals(category);
		List<ParticleEffectWrapper> wrappers = queuedEffects.stream().filter(predicate).toList();
		for (ParticleEffectWrapper wrapper : wrappers) {
			wrapper.getPooledEffect().free();
		}
		queuedEffects.removeIf(predicate);
	}
}
