package com.csse3200.game.areas.weather;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.services.ServiceLocator;

public class ParticleService {

    private final Array<ParticleEffect> particleEffectArray;
    private SpriteBatch spriteBatch;

    //TODO
    public ParticleService() {
        particleEffectArray = new Array<>();
        ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::updateParticleEffect);
//        for (ParticleEffect particleEffect : particleEffectArray) {
//            if (particleEffect instanceof acidShowerParticleEffect) {
//                updateAcidShowerParticleEffect();
//            } else if (particleEffect instanceof solarSurgeParticleEffect) {
//                updateSolarSurgeParticleEffect();
//            }
//        }
    }

    public void addParticleEffect(ParticleEffect particleEffect) {
        particleEffectArray.add(particleEffect);
    }

    public void updateParticleEffect() {
        WeatherEvent currentWeatherEvent = ServiceLocator.getGameArea().getClimateController().getCurrentWeatherEvent();
        if (currentWeatherEvent instanceof AcidShowerEvent) {
            //TODO
        } else if (currentWeatherEvent instanceof SolarSurgeEvent) {
            //TODO
        }
    }

    public void renderParticleEffect() {
        for (ParticleEffect particleEffect : particleEffectArray) {
            particleEffect.draw(spriteBatch);
        }
    }

    //TODO
    private void updateAcidShowerParticleEffect() {
    }

    //TODO
    private void updateSolarSurgeParticleEffect() {
    }

    public void dispose() {
        for (ParticleEffect particleEffect : particleEffectArray) {
            particleEffect.dispose();
        }
    }
}
