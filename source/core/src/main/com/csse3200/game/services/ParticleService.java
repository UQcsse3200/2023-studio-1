package com.csse3200.game.services;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.csse3200.game.components.ParticleEffectComponent;
import com.csse3200.game.rendering.ParticleEffectWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;

public class ParticleService {
	private static final Logger logger = LoggerFactory.getLogger(ParticleService.class);

	public static final String WEATHER_EVENT = "WEATHER_EVENT";
	public static final String ENTITY_EFFECT = "ENTITY_EFFECT";

	/**
	 * All the effects that will be rendered
	 */
	private final ArrayList<ParticleEffectWrapper> queuedEffects;
	private final ArrayList<ParticleEffectWrapper> positionalEffects;

	private final ArrayList<ParticleEffectComponent> effectComponents;

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
		RAIN(WEATHER_EVENT, "particle-effects/acidRain.p", 1, 10),
		BLIZZARD(WEATHER_EVENT, "particle-effects/snowEffect.p", 1, 10),
		SUCCESS_EFFECT(ENTITY_EFFECT, "particle-effects/successEffect.p", 1, 10),
		ATTACK_EFFECT(ENTITY_EFFECT, "particle-effects/attackEffect.p", 1, 10),
		FEED_EFFECT(ENTITY_EFFECT, "particle-effects/feedEffect.p", 1, 10),
		DIRT_EFFECT(ENTITY_EFFECT, "particle-effects/dirtEffect.p", 1, 10);

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

		effectComponents = new ArrayList<>();
		positionalEffects = new ArrayList<>();
	}

	/**
	 * Renders the queued particle effects
	 *
	 * @param batch sprite batch used to render effects
	 * @param delta delta time value used to update the particle effects
	 */
	public void render(SpriteBatch batch, float delta) {
		Vector3 position = ServiceLocator.getCameraComponent().getCamera().position;
		for (ParticleEffectWrapper wrapper : queuedEffects) {
			wrapper.getPooledEffect().setPosition(position.x, position.y);
			wrapper.getPooledEffect().draw(batch, delta);
			if (wrapper.getPooledEffect().isComplete()) {
				wrapper.getPooledEffect().reset();
			}
		}

		// Render the particle effects attached to specific components
		for (ParticleEffectComponent component : effectComponents) {
			component.render(batch, delta);
		}

		// Render the particles at a certain position
		Iterator<ParticleEffectWrapper> itr = positionalEffects.iterator();
		while (itr.hasNext()) {
			ParticleEffectWrapper wrapper = itr.next();
			// If effect is complete, don't render and free effect
			if (wrapper.getPooledEffect().isComplete()) {
				wrapper.getPooledEffect().free();
				itr.remove();
			} else {
				wrapper.getPooledEffect().draw(batch, delta);
			}
		}
	}

	/**
	 * Starts a particle effect, creating an effect wrapper and adding it to the queue
	 *
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
	 *
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
	 *
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

	/**
	 * Obtains the pooled particle effect for a given {@link ParticleEffectType}
	 *
	 * @param effectType The {@link ParticleEffectType} of the
	 *                   {@link com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect} to be obtained
	 * @return The {@link com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect} that is used to render
	 * the effect
	 */
	public ParticleEffectPool.PooledEffect getEffect(ParticleEffectType effectType) {
		logger.debug("Obtaining effect for type - {}", effectType.name());
		return particleEffectPools.get(effectType).obtain();
	}

	/**
	 * Starts rendering a {@link com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect} at a given
	 * {@link Vector2} position
	 *
	 * @param effectType The {@link ParticleEffectType} of the particle to be rendered
	 * @param position   position to render the particle effect at
	 */
	public void startEffectAtPosition(ParticleEffectType effectType, Vector2 position) {
		// Grabs the effect from the effect pool using the enum
		ParticleEffectWrapper effectWrapper = new ParticleEffectWrapper(particleEffectPools.get(effectType).
				obtain(), effectType.category, effectType.name());
		// Adds the effect to the queued effects so the particle service knows to draw it
		positionalEffects.add(effectWrapper);
		effectWrapper.getPooledEffect().scaleEffect(0.1f);
		effectWrapper.getPooledEffect().setPosition(position.x, position.y);
		effectWrapper.getPooledEffect().start();

	}

	/**
	 * Adds a {@link ParticleEffectComponent} to the {@link List} of {@link ParticleEffectComponent}s tracked by the
	 * {@link ParticleService} so that it can render each individual effect.
	 *
	 * @param component component to be added
	 */
	public void addComponent(ParticleEffectComponent component) {
		effectComponents.add(component);
	}

	/**
	 * Removes a {@link ParticleEffectComponent} to the {@link List} of {@link ParticleEffectComponent}s tracked by the
	 * {@link ParticleService} so that it can render each individual effect.
	 *
	 * @param component component to be removed
	 */
	public void removeComponent(ParticleEffectComponent component) {
		effectComponents.remove(component);
	}
}
