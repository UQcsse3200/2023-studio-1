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
  void animateStopMoving(float direction, String tool) {
    String animation = String.format("stop_%s_%s", getDirection(direction), tool);
    if (!animator.getCurrentAnimation().equals(animation)) {
      animator.startAnimation(animation);
    }
  }

  /**
   * Animates the tractor to start moving.
   */
  void animateMoving(float direction, String tool) {
    String animation = String.format("move_%s_%s", getDirection(direction), tool);
    if (!animator.getCurrentAnimation().equals(animation)) {
      animator.startAnimation(animation);
    }
  }

  void animateIdle(float direction) {
    animator.startAnimation(String.format("idle_%s", getDirection(direction)));
  }

  private String getDirection(float direction) {
    if (direction < 45) {
      return "right";
    } else if (direction < 135) {
      return "up";
    } else if (direction < 225) {
      return "left";
    } else if (direction < 315) {
      return "down";
    }
    // TODO add logger to provide error here?
    return "right";
  }
}
