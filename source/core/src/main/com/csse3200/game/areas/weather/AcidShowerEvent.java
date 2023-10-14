package com.csse3200.game.areas.weather;

import com.csse3200.game.events.ScheduledEvent;
import com.csse3200.game.services.ParticleService;
import com.csse3200.game.services.ServiceLocator;

/**
 * An acid rain event to occur in game affecting both the temperature and the climate and other surrounding
 * entities.
 */
public class AcidShowerEvent extends WeatherEvent {

    private ScheduledEvent nextAcidBurn;

    /**
     * Constructs a AcidShowerEvent with a given countdown, duration, priority and severity.
     *
     * @param numHoursUntil number of in-game hours until the weather event can occur
     * @param duration      number of in-game hours that the event can occur for
     * @param priority      priority of the weather event
     * @param severity      intensity of the weather event, impacting its effects
     */
    public AcidShowerEvent(int numHoursUntil, int duration, int priority, float severity) {
        super(numHoursUntil, duration, priority, severity);
        climateControllerEvents.addListener("acidShowerAnimalPanic", this::triggerAnimalPanic);
    }

    @Override
    public void startEffect() {
        climateControllerEvents.trigger("startWaterLevelEffect", getDryRate());
        triggerAnimalPanic();

        ServiceLocator.getParticleService().startEffect(ParticleService.ParticleEffectType.ACID_RAIN);
    }

    @Override
    public void stopEffect() {
        climateControllerEvents.trigger("stopWaterLevelEffect");
        climateControllerEvents.cancelEvent(nextAcidBurn);

        ServiceLocator.getParticleService().stopEffect(ParticleService.ParticleEffectType.ACID_RAIN);
    }

    private void scheduleNextAcidBurn() {
        nextAcidBurn = climateControllerEvents.scheduleEvent(10.0f, "acidShowerAnimalPanic");
    }

    private void triggerAnimalPanic() {
        climateControllerEvents.trigger("startPanicEffect");
        climateControllerEvents.trigger("damagePlants");
        scheduleNextAcidBurn();
    }

    private float getDryRate() {
        // Lowest severity: Dry at regular rate
        // Highest severity: Watered at regular dry rate
        return 0.0005f - 0.001f * severity / 1.5f;
    }

}
