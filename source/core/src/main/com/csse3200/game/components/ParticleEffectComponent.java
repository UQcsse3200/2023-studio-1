package com.csse3200.game.components;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.services.ParticleService;
import com.csse3200.game.services.ServiceLocator;

public class ParticleEffectComponent extends Component {

	private ParticleEffectPool.PooledEffect effect;
	private ParticleService.ParticleEffectType effectType;
	private boolean isActive;

	public ParticleEffectComponent() {
		isActive = false;
	}

	@Override
	public void create() {
		super.create();
		ServiceLocator.getParticleService().addComponent(this);
	}

	public void render(SpriteBatch batch, float delta) {
		// Don't render if not active
		if (!isActive) {
			return;
		}
		// If effect was active and now is complete
		if (effect.isComplete()) {
			isActive = false;
			effect.free();
			effect = null;
		} else {
			// TODO - Do we want to update position as well
			effect.draw(batch, delta);
		}
	}

	public void startEffect(ParticleService.ParticleEffectType effectType) {
		// If effect is still going on, stop it for new effect
		if (isActive) {
			effect.free();
		}
		isActive = true;
		this.effectType = effectType;
		effect = ServiceLocator.getParticleService().getEffect(effectType);
		effect.start();
	}

	public ParticleService.ParticleEffectType getEffectType() {
		return effectType;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (ServiceLocator.getParticleService() != null) {
			ServiceLocator.getParticleService().removeComponent(this);
		}
	}
}
