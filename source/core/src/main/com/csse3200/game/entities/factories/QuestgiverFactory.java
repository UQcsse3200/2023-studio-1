package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class QuestgiverFactory {

   /**
     * Creates a questgiver entity
     * 
     * @return questgiver entity
     */
  public static Entity createQuestgiver(Entity player) {

    AnimationRenderComponent animator = setupQuestgiverAnimations();

    Entity questgiver = new Entity(EntityType.Questgiver)
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent())
        .addComponent(animator);

    questgiver.getComponent(AnimationRenderComponent.class).scaleEntity();
    return questgiver;
  }

  /**
   * Adds all animations to the AnimationRenderComponent for the questgiver
   *
   * @return an AnimationRenderComponent for questgiver animations.
   */
  private static AnimationRenderComponent setupQuestgiverAnimations() {
    AnimationRenderComponent animator = new AnimationRenderComponent(
        ServiceLocator.getResourceService().getAsset("images/questgiver.atlas", TextureAtlas.class),
        16f);

    return animator;
  }
}
