package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.InteractionDetector;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.sound.EffectSoundFile;
import com.csse3200.game.services.sound.InvalidSoundFileException;

public class ShipEaterScareComponent extends Component {

	/** Entity's interaction detector */
	protected InteractionDetector interactionDetector;
	protected boolean isHiding;
	@Override
	public void create() {
		interactionDetector = entity.getComponent(InteractionDetector.class);
		interactionDetector.notifyOnDetection(true);

		isHiding = false;

		entity.getEvents().addListener("entityDetected", this::hide);
		entity.getEvents().addListener("entityExitDetected", this::stopHiding);
	}

	private void hide(Entity entity) {
		if (entity.getType() != EntityType.PLAYER) {
			return;
		}

		isHiding = true;
		this.entity.getEvents().trigger("hidingUpdated", true);

		try {
			ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.SHIP_EATER_HIDE);
		} catch (InvalidSoundFileException ignored) {

		}
	}

	private void stopHiding(Entity entity) {
		if (isHiding && entity.getType() == EntityType.PLAYER) {
			isHiding = false;
			this.entity.getEvents().trigger("hidingUpdated", false);
		}
	}
}
