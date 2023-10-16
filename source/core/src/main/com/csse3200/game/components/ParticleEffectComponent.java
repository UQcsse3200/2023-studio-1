package com.csse3200.game.components;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.rendering.ParticleEffectWrapper;
import com.csse3200.game.services.ParticleService;
import com.csse3200.game.services.ServiceLocator;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Component which can be added to entities allowing them to have {@link ParticleEffect} rendering on them and attached
 * to their body. This also enables the {@link ParticleService} to keep track of these entities, rendering their effects
 * in the game loop.
 *
 * This component also allows multiple particle effects to be rendered on a single entity ct the one time.
 */
public class ParticleEffectComponent extends Component {

	/**
	 * Store of all the particle effects that will be rendered on an entity at the one time. These are wrapped in the
	 * {@link ParticleEffectWrapper} so game-related metadata can be added to these effects, allowing them to be more
	 * easily controlled.
	 */
	private final ArrayList<ParticleEffectWrapper> effects;

	/**
	 * Creates a new component with an empty list of effects to render.
	 */
	public ParticleEffectComponent() {
		effects = new ArrayList<>();
	}

	/**
	 * Adds event listeners to the component, allowing for effects to be stopped and started.
	 */
	@Override
	public void create() {
		super.create();
		ServiceLocator.getParticleService().addComponent(this);
		entity.getEvents().addListener("startVisualEffect", this::startEffect);
		entity.getEvents().addListener("stopVisualEffect", this::stopEffect);
	}

	/**
	 * Renders all the effects that are being managed by the component for this entity.
	 * @param batch sprite batch to use while rendering
	 * @param delta delta time value used to update the particle effects
	 */
	public void render(SpriteBatch batch, float delta) {
		Iterator<ParticleEffectWrapper> itr = effects.iterator();
		while (itr.hasNext()) {
			ParticleEffectWrapper wrapper = itr.next();
			// If effect is complete, don't render and free effect
			if (wrapper.getPooledEffect().isComplete()) {
				wrapper.getPooledEffect().free();
				itr.remove();
			} else {
				// If the effect isn't complete, update its position to be attached to the player, then render it
				wrapper.getPooledEffect().setPosition(entity.getCenterPosition().x, entity.getPosition().y);
				wrapper.getPooledEffect().draw(batch, delta);
			}
		}
	}

	/**
	 * Starts a {@link ParticleEffect} on an entity, enabling this component to control it throughout its lifecycle
	 * @param effectType type of {@link ParticleEffect} to be added to the entity
	 */
	public void startEffect(ParticleService.ParticleEffectType effectType) {
		// If that effect is already added to the entity, don't add it again to avoid overlap
		if (effectExists(effectType)) {
			return;
		}
		// Wrap the pooled effect with the wrapper
		ParticleEffectPool.PooledEffect effect = ServiceLocator.getParticleService().getEffect(effectType);
		ParticleEffectWrapper wrapper = new ParticleEffectWrapper(effect, effectType.getCategory(), effectType.name());
		// Add the effect to the effects arraylist
		effects.add(wrapper);

		// Attach the effect to the entity
		effect.setPosition(entity.getCenterPosition().x, entity.getPosition().y);
		effect.scaleEffect(0.1f);
		effect.start();
	}

	/**
	 * Stops rendering an effect on the entity, freeing the effect and giving it back to the {@link ParticleEffectPool}.
	 * This will also remove the effect from the {@link List} of effects to be controlled by this component.
	 * @param effectType type of {@link ParticleEffect} to be stopped
	 */
	public void stopEffect(ParticleService.ParticleEffectType effectType) {
		// Attempt to get the wrapper for the given effect
		ParticleEffectWrapper wrapper = effects.stream().filter(effectWrapper -> effectWrapper.getType()
				.equals(effectType.name())).findFirst().orElse(null);
		// If that effect isn't being tracked by this component
		if (wrapper == null) {
			return;
		}
		// Free the effect and remove it from the effect getting tracked.
		wrapper.getPooledEffect().free();
		effects.remove(wrapper);
	}

	/**
	 * Stops all particle effects by category in the entity's list of effects
	 * @param category category of particle effects
	 */
	public void stopEffectCategory(String category) {
		Predicate<ParticleEffectWrapper> predicate = effectWrapper -> effectWrapper.getCategory().equals(category);
		List<ParticleEffectWrapper> wrappers = effects.stream().filter(predicate).toList();
		for (ParticleEffectWrapper wrapper : wrappers) {
			wrapper.getPooledEffect().free();
		}
		effects.removeIf(predicate);
	}

	/**
	 * Stops all of the particle effects for this entity, freeing the effects back to their {@link ParticleEffectPool}s
	 * and removing them from the {@link List} of effects being tracked.
	 */
	public void stopAllEffects() {
		for (ParticleEffectWrapper wrapper : effects) {
			wrapper.getPooledEffect().free();
		}
		effects.clear();
	}

	/**
	 * Gets the number of effects that are currently being tracked by the component.
	 * @return number of effects being tracked by this component.
	 */
	public int getNumEffects() {
		return effects.size();
	}

	/**
	 * Determines whether a {@link ParticleEffect} is being tracked by this component with the same type as the given
	 * {@link com.csse3200.game.services.ParticleService.ParticleEffectType}.
	 * @param effectType type of particle effect
	 * @return whether that particle is being tracked
	 */
	public boolean effectExists(ParticleService.ParticleEffectType effectType) {
		Predicate<ParticleEffectWrapper> predicate = effectWrapper -> effectWrapper.getType().equals(effectType.name());
		return !effects.stream().filter(predicate).toList().isEmpty();
	}

	/**
	 * Disposes of the component, freeing all effects and removing the component from the entity.
	 */
	@Override
	public void dispose() {
		super.dispose();
		stopAllEffects();
		if (ServiceLocator.getParticleService() != null) {
			ServiceLocator.getParticleService().removeComponent(this);
		}
	}
}
