package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;

public class ParticleEffectWrapper {
	private ParticleEffectPool.PooledEffect pooledEffect;
	private String category;
	private String type;


	public ParticleEffectWrapper(ParticleEffectPool.PooledEffect effect, String category, String type) {
		this.pooledEffect = effect;
		this.category = category;
		this.type = type;
	}

	public ParticleEffectPool.PooledEffect getPooledEffect() {
		return pooledEffect;
	}

	public String getCategory() {
		return category;
	}

	public String getType() {
		return type;
	}
}
