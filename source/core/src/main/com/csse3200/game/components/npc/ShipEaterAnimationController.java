package com.csse3200.game.components.npc;

import java.util.Objects;

public class ShipEaterAnimationController extends AnimalAnimationController {
	@Override
	public void create() {
		super.create();

		entity.getEvents().addListener("attackStart", this::animateAttack);
		entity.getEvents().addListener("digging", this::digging);
	}
	void digging() {
		if (!Objects.equals(animator.getCurrentAnimation(), "digging")) {
			animator.startAnimation("digging");
		}
	}

	void animateAttack() {
		if (!Objects.equals(animator.getCurrentAnimation(), "eating")) {
			animator.startAnimation("eating");
		}
	}

	@Override
	protected void animateRun() {
		if (!Objects.equals(animator.getCurrentAnimation(), "hiding")) {
			animator.startAnimation("hiding");
		}
	}

	@Override
	protected void animateWalk() {
		if (!Objects.equals(animator.getCurrentAnimation(), "running")) {
			animator.startAnimation("running");
		}
	}
	@Override
	protected void animateIdle() {
		if (!Objects.equals(animator.getCurrentAnimation(), "running")) {
			animator.startAnimation("running");
		}
	}
}
