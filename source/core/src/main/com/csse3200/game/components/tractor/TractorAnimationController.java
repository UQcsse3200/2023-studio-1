package com.csse3200.game.components.tractor;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This way of controlling the animations was adopted from PlayerActions and PlayerAnimationController which
 * was written by Team 2
 */
public class TractorAnimationController extends Component {
  /**
   * The animation Component of the Tractor entity
   */
  AnimationRenderComponent animator;

  @Override
  /**
   * Creates the tractor animation controller component.
   */
  public void create() {
    super.create();
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("stopMoving", this::animateStopMoving);
    entity.getEvents().addListener("startMoving", this::animateMoving);
    entity.getEvents().addListener("idle", this::animateIdle);
    animator.startAnimation("idle_right");
  }

  /**
   * Plays a Stop animation of the tractor based off direction and tool, does not do anything if that
   * animation is already playing
   * @param direction The direction as a String (values from getDirection() in TractorActions) of the Tractor
   * @param tool The tool the tractor is currently using
   */
  void animateStopMoving(String direction, String tool) {
    String animation = String.format("stop_%s_%s", direction, tool.toLowerCase());
    if (!animator.getCurrentAnimation().equals(animation)) {
      animator.startAnimation(animation);
    }
  }

  /**
   * Plays a Move animation of the tractor based off direction and tool, does not do anything if that
   * animation is already playing
   * @param direction The direction as a String (values from getDirection() in TractorActions) of the Tractor
   * @param tool The tool the tractor is currently using
   */
  void animateMoving(String direction, String tool) {
    String animation = String.format("move_%s_%s", direction, tool.toLowerCase());
    if (!animator.getCurrentAnimation().equals(animation)) {
      animator.startAnimation(animation);
    }
  }

  /**
   * Plays a Idle animation of the tractor based off direction and tool, does not do anything if that
   * animation is already playing. An Idle animation is played when the tractor is not in use, i.e. the player
   * is not inside the tractor.
   * @param direction The direction as a String (values from getDirection() in TractorActions) of the Tractor
   */
  void animateIdle(String direction) {
    animator.startAnimation(String.format("idle_%s", direction));
  }
}
