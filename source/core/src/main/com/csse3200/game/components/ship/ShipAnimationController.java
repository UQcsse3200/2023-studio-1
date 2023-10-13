package com.csse3200.game.components.ship;

import java.util.Set;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.ship.ShipProgressComponent.Feature;
import com.csse3200.game.entities.factories.ShipFactory;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * the ShipAnimationController
 */
public class ShipAnimationController extends Component {
	/**
	 * The animation Component of the Ship entity
	 */
	AnimationRenderComponent animator;

	@Override
	/**
	 * Create the ShipAnimationController
	 */
	public void create() {
		super.create();
		animator = this.entity.getComponent(AnimationRenderComponent.class);
		entity.getEvents().addListener(ShipFactory.events.PROGRESS_UPDATED.name(), this::animateShipStage);
	}

	/**
	 * Changes the stage of the ship to match progress, progress from
	 * ShipProgressComponent
	 */
	private void animateShipStage(int progress, Set<Feature> unlockedFeatures) {
		String animation = "default";

		//TODO change states to match final amount of progress
		if (progress >= 0 && progress < 3) {
			animation = "ship_0";
		}

		if (progress >= 3 && progress < 5) {
			animation = "ship_1";
		}

		if (progress >= 5 && progress < 8) {
			animation = "ship_2";
		}

		if (progress >= 8 && progress < 15) {
			animation = "ship_3";
		}

		if (progress >= 15 && progress < 20) {
			animation = "ship_4";
		}

		if (progress >= 20) {
			animation = "ship_5";
		}

		if (!animator.getCurrentAnimation().equals(animation)) {
			animator.startAnimation(animation);
		}
	}


	/**
	 * Store the current animation of the ship in the passed-in json object.
	 *
	 * @param json Json object to write to
	 *             Code edited from ShipProgressComponent by Team 2
	 */
	@Override
	public void write(Json json) {
		json.writeObjectStart(this.getClass().getSimpleName());
		json.writeValue("animation", animator.getCurrentAnimation());
		json.writeObjectEnd();
	}

	/**
	 * Update the entity based on the read data.
	 *
	 * @param json    which is a valid Json that is read from
	 * @param jsonMap which is a valid JsonValue that is read from
	 *                Code edited from ShipProgressComponent by Team 2
	 */
	@Override
	public void read(Json json, JsonValue jsonMap) {
		animator.startAnimation(jsonMap.getString("animation"));
	}

}
