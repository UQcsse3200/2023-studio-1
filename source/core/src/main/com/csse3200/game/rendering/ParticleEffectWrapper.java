package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;

/**
 * Wrapper for the particle effect so that pooled effects can have a type and a category
 */
public class ParticleEffectWrapper {

	/**
	 * Pooled particle effect that is grabbed from the pool
	 */
	private final PooledEffect effect;

	/**
	 * Category of the particle effect (eg: Weather event)
	 */
	private final String category;

	/**
	 * Type of particle effect (eg: Acid Shower)
	 */
	private final String type;


	/**
	 * Wraps a pooled particle effect with a category and a type
	 * @param effect pooled effect
	 * @param category category of the particle effect
	 * @param type type of particle effect
	 */
	public ParticleEffectWrapper(PooledEffect effect, String category, String type) {
		this.effect = effect;
		this.category = category;
		this.type = type;
	}

	/**
	 * Gets the pooled effect that is contained in the wrapper
	 * @return pooled effect
	 */
	public PooledEffect getPooledEffect() {
		return effect;
	}

	/**
	 * Gets the wrappers category
	 * @return category of the wrapper
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Gets the type of particle effect in the wrapper
	 * @return type of particle effect
	 */
	public String getType() {
		return type;
	}
}
