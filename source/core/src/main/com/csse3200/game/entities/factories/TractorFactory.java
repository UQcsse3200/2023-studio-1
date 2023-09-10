package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.tractor.KeyboardTractorInputComponent;
import com.csse3200.game.components.tractor.TractorActions;
import com.csse3200.game.components.tractor.TractorAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class TractorFactory {

  /**
   * Creates an Entity and adds all necessary componenets to make it function as a
   * tractor should
   *
   * @param player - a reference to the player that should be allowed to enter and
   *               control it
   *
   * @return a referenece to the tractor entity
   */
  public static Entity createTractor(Entity player) {

    AnimationRenderComponent animator = setupTractorAnimations();
    InputComponent inputComponent = ServiceLocator.getInputService().getInputFactory().createForTractor();

    Entity tractor = new Entity(EntityType.Tractor)
        .addComponent(new PhysicsComponent())
        .addComponent(new TractorAnimationController())
        .addComponent(new ColliderComponent())
        .addComponent(animator)
        .addComponent(inputComponent)
        .addComponent(new TractorActions());

    tractor.getComponent(AnimationRenderComponent.class).scaleEntity();
    tractor.getComponent(TractorActions.class).setPlayer(player);
    tractor.getComponent(KeyboardTractorInputComponent.class).setActions(tractor.getComponent(TractorActions.class));
    tractor.getComponent(ColliderComponent.class).setAsBox(new Vector2(3.5f, 1.5f), new Vector2(3f, 0.75f));
    tractor.getComponent(ColliderComponent.class).setDensity(999);    //prevents entities from pushing the tractor so easily
    return tractor;
  }

  /**
   * Adds all animations to the AnimationRenderComponent for the tractor to use
   *
   * @return an AnimationRenderComponent with the tractors animations added.
   */
  private static AnimationRenderComponent setupTractorAnimations() {
    AnimationRenderComponent animator = new AnimationRenderComponent(
        ServiceLocator.getResourceService().getAsset("images/tractor.atlas", TextureAtlas.class),
        16f);
    animator.addAnimation("move_left_tool", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("move_right_tool", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("move_stop_tool", 0.1f, Animation.PlayMode.LOOP);

    return animator;
  }
}
