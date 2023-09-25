package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.components.ship.ShipProgressComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

//TODO change assets to ship assets once they are created, rn the ship looks like a questgiver clone
public class ShipFactory {

  /**
   * Creates a ship entity
   * 
   * @return ship entity
   */
  public static Entity createShip() {

    AnimationRenderComponent animator = setupShipAnimations();

    Entity ship = new Entity(EntityType.Ship)
        .addComponent(new PhysicsComponent())
        .addComponent(new PhysicsMovementComponent())
        .addComponent(new ColliderComponent())
        .addComponent(new ShipProgressComponent())
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
  private static AnimationRenderComponent setupShipAnimations() {
    AnimationRenderComponent animator = new AnimationRenderComponent(
        ServiceLocator.getResourceService().getAsset("images/ship/ship.atlas", TextureAtlas.class),
        16f);
    animator.addAnimation("default", 0.1f, Animation.PlayMode.LOOP);
    animator.startAnimation("default");
    // this will get updated in a future sprint to include proper animations
    return animator;
  }

}
