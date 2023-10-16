package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

import java.util.Objects;

public class ShipEaterAnimationController extends Component {
	private boolean isDigging;
	private boolean isHiding;
	private boolean isEating;
	private AnimationRenderComponent animator;
	@Override
	public void create() {
		animator = this.entity.getComponent(AnimationRenderComponent.class);

		entity.getEvents().addListener("eatingUpdated", this::setEating);
		entity.getEvents().addListener("diggingUpdated", this::setDigging);
		entity.getEvents().addListener("hidingUpdated", this::setHiding);
	}
	void setDigging(boolean isDigging) {
		this.isDigging = isDigging;
	}

	void setEating(boolean isEating) {
		this.isEating = isEating;
	}

	void setHiding(boolean isHiding) {
		this.isHiding = isHiding;
	}

	@Override
	public void update() {
		// priorities are hiding > eating > digging > moving
		String animation = "running";
		if (isHiding) {
			animation = "hiding";
		} else if (isEating) {
			animation = "eating";
		} else if (isDigging) {
			animation = "digging";
		}

		if (!Objects.equals(animator.getCurrentAnimation(), animation)) {
			animator.startAnimation(animation);
		}
	}
}
