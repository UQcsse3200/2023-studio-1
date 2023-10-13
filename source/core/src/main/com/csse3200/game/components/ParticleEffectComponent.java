package com.csse3200.game.components;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.services.ParticleService;
import com.csse3200.game.services.ServiceLocator;

public class ParticleEffectComponent extends Component {

	private ParticleEffectPool.PooledEffect effect;
	private ParticleService.ParticleEffectType effectType;
	private boolean active;

	public ParticleEffectComponent() {
		active = false;
	}

	@Override
	public void create() {
		super.create();
		ServiceLocator.getParticleService().addComponent(this);
		entity.getEvents().addListener("startEffect", this::startEffect);
		entity.getEvents().addListener("stopEffect", this::stopEffect);
	}

	public void render(SpriteBatch batch, float delta) {
		// Don't render if not active
		if (!isActive()) {
			return;
		}
		// If effect was active and now is complete
		if (effect.isComplete()) {
			active = false;
			effect.free();
			effect = null;
		} else {
			effect.draw(batch, delta);
		}
	}

	public void startEffect(ParticleService.ParticleEffectType effectType) {
		// If effect is still going on, stop it for new effect
		if (isActive()) {
			effect.free();
		}
		active = true;
		this.effectType = effectType;
		effect = ServiceLocator.getParticleService().getEffect(effectType);
		effect.setPosition(entity.getCenterPosition().x, entity.getCenterPosition().y);
		effect.scaleEffect(0.1f);
		effect.start();
	}

	public void stopEffect(ParticleService.ParticleEffectType effectType) {
		if (!isActive() || this.effectType != effectType) {
			return;
		}
		active = false;
		effect.free();
		effect = null;
	}

	public void stopAllEffects() {
		if (!isActive() || effect == null) {
			return;
		}
		active = false;
		effect.free();
		effect = null;
	}

	public boolean isActive() {
		return active;
	}

	public ParticleService.ParticleEffectType getEffectType() {
		return effectType;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (effect != null) {
			active = false;
			effect.free();
			effect = null;
		}
		if (ServiceLocator.getParticleService() != null) {
			ServiceLocator.getParticleService().removeComponent(this);
		}
	}
}
