package com.csse3200.game.areas.weather;

import com.csse3200.game.events.ScheduledEvent;
import com.csse3200.game.services.ParticleService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.sound.EffectSoundFile;
import com.csse3200.game.services.sound.InvalidSoundFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An acid rain event to occur in game affecting both the temperature and the climate and other surrounding
 * entities.
 */
public class AcidShowerEvent extends WeatherEvent {

    private static final Logger logger = LoggerFactory.getLogger(AcidShowerEvent.class);

    private ScheduledEvent nextAcidBurn;
    private long stormSoundId;

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
        climateControllerEvents.addListener("acidShowerAnimalPanic", this::triggerAcidBurn);
    }

    @Override
    public void startEffect() {
        logger.info("Starting AcidShower effects");

        // Trigger in-game effects
        climateControllerEvents.trigger("startWaterLevelEffect", getDryRate());
        climateControllerEvents.trigger("douseFlames");
        triggerAcidBurn();

        // Add particle effects
        ServiceLocator.getParticleService().startEffect(ParticleService.ParticleEffectType.ACID_RAIN);

        // Add lighting effects
        ServiceLocator.getLightService().setBrightnessMultiplier((1.0f - severity / 1.5f) * 0.2f + 0.75f);

        // Start sound effects
        try {
            stormSoundId = ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.STORM, true);
        } catch (InvalidSoundFileException exception) {
            logger.error("Failed to play storm sound effect", exception);
        }
    }

    @Override
    public void stopEffect() {
        logger.info("Stopping AcidShower effects");

        // Cancel in-game effects
        climateControllerEvents.trigger("stopWaterLevelEffect");
        climateControllerEvents.trigger("stopPlacedLightEffects");
        climateControllerEvents.cancelEvent(nextAcidBurn);

        // Remove particle effects
        ServiceLocator.getParticleService().stopEffect(ParticleService.ParticleEffectType.ACID_RAIN);

        // Remove lighting effects
        ServiceLocator.getLightService().setBrightnessMultiplier(1.0f);

        // Stop sound effects
        try {
            ServiceLocator.getSoundService().getEffectsMusicService().stop(EffectSoundFile.STORM, stormSoundId);
        } catch (InvalidSoundFileException exception) {
            logger.error("Failed to stop storm sound effect", exception);
        }
    }

    private void scheduleNextAcidBurn() {
        nextAcidBurn = climateControllerEvents.scheduleEvent(4.0f - 2.0f * severity / 1.5f, "acidShowerAnimalPanic");
    }

    private void triggerAcidBurn() {
        // Play SFX
        try {
            ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.ACID_BURN);
        } catch (InvalidSoundFileException exception) {
            logger.error("Failed to play acid burning sound effect", exception);
        }
        // Cause animal panic
        climateControllerEvents.trigger("startPanicEffect");
        // Damage the plants
        climateControllerEvents.trigger("damagePlants");
        // Recursively schedule next burn
        scheduleNextAcidBurn();
    }

    private float getDryRate() {
        // Lowest severity: Dry at regular rate
        // Highest severity: Watered at regular dry rate
        return 0.0005f - 0.001f * severity / 1.5f;
    }

}
