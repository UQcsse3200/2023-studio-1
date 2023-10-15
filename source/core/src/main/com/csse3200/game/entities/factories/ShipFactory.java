package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.components.ship.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class ShipFactory {

  public enum events {
    ADD_PART,
    REMOVE_PART,
    PROGRESS_UPDATED
  }

  /**
   * Creates a ship entity
   *
   * @return ship entity
   */
  public static Entity createShip() {

    AnimationRenderComponent animator = setupShipAnimations();

    Entity ship = new Entity(EntityType.SHIP)
        .addComponent(new PhysicsComponent())
        .addComponent(new PhysicsMovementComponent())
        .addComponent(new ColliderComponent())
        .addComponent(new HitboxComponent())
        .addComponent(new ShipProgressComponent())
        .addComponent(new ShipAnimationController())
        .addComponent(new ShipTimeSkipComponent())
        .addComponent(new ShipLightComponent())
        .addComponent(new ShipDisplay ())
        .addComponent(animator);

    ship.getComponent(AnimationRenderComponent.class).scaleEntity();
    ship.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody); // body type to static body so he won't move
    return ship;
  }

  /**
   * Adds all animations to the AnimationRenderComponent for the ship
   *
   * @return an AnimationRenderComponent for ship animations.
   */
  public static AnimationRenderComponent setupShipAnimations() {
    AnimationRenderComponent animator = new AnimationRenderComponent(
        ServiceLocator.getResourceService().getAsset("images/ship/ship.atlas", TextureAtlas.class),
        16f);
    animator.addAnimation("default", 0.1f, Animation.PlayMode.NORMAL);

    animator.addAnimation("ship_0", 0.1f, Animation.PlayMode.NORMAL);
    animator.addAnimation("ship_1", 0.1f, Animation.PlayMode.NORMAL);
    animator.addAnimation("ship_2", 0.1f, Animation.PlayMode.NORMAL);
    animator.addAnimation("ship_3", 0.1f, Animation.PlayMode.NORMAL);
    animator.addAnimation("ship_4", 0.1f, Animation.PlayMode.NORMAL);
    animator.addAnimation("ship_5", 0.1f, Animation.PlayMode.NORMAL);
    animator.startAnimation("default");
    // this will get updated in a future sprint to include proper animations
    return animator;
  }

}
