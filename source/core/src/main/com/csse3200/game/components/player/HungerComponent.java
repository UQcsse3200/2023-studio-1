package com.csse3200.game.components.player;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.combat.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;

public class HungerComponent extends Component {

	private int hungerLevel;

	public HungerComponent(int initialHungerLevel) {
		hungerLevel = initialHungerLevel;
	}

	@Override
	public void create() {
		super.create();
		ServiceLocator.getTimeService().getEvents().addListener("minuteUpdate", this::minuteUpdate);
	}

	private void minuteUpdate() {
		int min = ServiceLocator.getTimeService().getMinute();
		if (min % 20 == 0) {
			increaseHungerLevel(1);
		}
		if (checkIfStarving() && min % 10 == 0) {
			entity.getComponent(CombatStatsComponent.class).addHealth(-5);
		}
		checkIfStarving();
	}

	public boolean checkIfStarving() {
		return hungerLevel >= 100;
	}

	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		// Inform the PlayerHungerService that the hunger has been updated
		ServiceLocator.getPlayerHungerService().getEvents().trigger("hungerUpdate", hungerLevel);
	}

	public void increaseHungerLevel(int num) {
		hungerLevel += num;

		if (hungerLevel <= 0) {
			hungerLevel = 0;
		} else if (hungerLevel >= 100) {
			hungerLevel = 100;
		}
		// Inform the PlayerHungerService that the hunger has been updated
		ServiceLocator.getPlayerHungerService().getEvents().trigger("hungerUpdate", hungerLevel);
	}

	@Override
	public void write(Json json) {
		json.writeObjectStart(this.getClass().getSimpleName());
		json.writeValue("hunger", this.hungerLevel);
		json.writeObjectEnd();
	}


	public void read(Json json, JsonValue jsonValue) {
		jsonValue = jsonValue.get(this.getClass().getSimpleName());
		int hunger = jsonValue.getInt("hunger");
		setHungerLevel(hunger);
	}

}
