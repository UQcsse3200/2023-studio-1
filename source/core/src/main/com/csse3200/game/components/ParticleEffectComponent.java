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

public class ParticleEffectComponent extends Component {

	private final ArrayList<ParticleEffectWrapper> effects;

	public ParticleEffectComponent() {
		effects = new ArrayList<>();
	}

	@Override
	public void create() {
		super.create();
		ServiceLocator.getParticleService().addComponent(this);
		entity.getEvents().addListener("startEffect", this::startEffect);
		entity.getEvents().addListener("stopEffect", this::stopEffect);
	}

	public void render(SpriteBatch batch, float delta) {
		Iterator<ParticleEffectWrapper> itr = effects.iterator();
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

	public void startEffect(ParticleService.ParticleEffectType effectType) {
		// Wrap the pooled effect with the wrapper
		ParticleEffectPool.PooledEffect effect = ServiceLocator.getParticleService().getEffect(effectType);
		ParticleEffectWrapper wrapper = new ParticleEffectWrapper(effect, effectType.getCategory(), effectType.name());
		// Add the effect to the effects arraylist
		effects.add(wrapper);

		effect.setPosition(entity.getCenterPosition().x, entity.getPosition().y);
		effect.scaleEffect(0.1f);
		effect.start();
	}

	public void stopEffect(ParticleService.ParticleEffectType effectType) {
		Predicate<ParticleEffectWrapper> predicate = effectWrapper -> effectWrapper.getType().equals(effectType.name());
		List<ParticleEffectWrapper> wrappers = effects.stream().filter(predicate).toList();
		for (ParticleEffectWrapper wrapper : wrappers) {
			wrapper.getPooledEffect().free();
		}
		effects.removeIf(predicate);
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

	public void stopAllEffects() {
		for (ParticleEffectWrapper wrapper : effects) {
			wrapper.getPooledEffect().free();
		}
		effects.clear();
	}

	public int getNumEffects() {
		return effects.size();
	}

	public boolean effectExists(ParticleService.ParticleEffectType effectType) {
		Predicate<ParticleEffectWrapper> predicate = effectWrapper -> effectWrapper.getType().equals(effectType.name());
		return !effects.stream().filter(predicate).toList().isEmpty();
	}

	@Override
	public void dispose() {
		super.dispose();
		stopAllEffects();
		if (ServiceLocator.getParticleService() != null) {
			ServiceLocator.getParticleService().removeComponent(this);
		}
	}
}
