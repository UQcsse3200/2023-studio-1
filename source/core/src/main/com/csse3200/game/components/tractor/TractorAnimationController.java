package com.csse3200.game.components.tractor;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class TractorAnimationController extends Component {
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
   *
   * @param direction
   * @param tool
   */
  void animateStopMoving(String direction, String tool) {
    String animation = String.format("stop_%s_%s", direction, tool);
    if (!animator.getCurrentAnimation().equals(animation)) {
      animator.startAnimation(animation);
    }
  }

  /**
   *
   * @param direction
   * @param tool
   */
  void animateMoving(String direction, String tool) {
    String animation = String.format("move_%s_%s", direction, tool);
    if (!animator.getCurrentAnimation().equals(animation)) {
      animator.startAnimation(animation);
    }
  }

  /**
   *
   * @param direction
   */
  void animateIdle(String direction) {
    animator.startAnimation(String.format("idle_%s", direction));
  }
}
