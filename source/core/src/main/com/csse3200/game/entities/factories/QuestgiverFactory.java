package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.npc.QuestIndicatorComponent;
import com.csse3200.game.components.questgiver.MissionDisplay;
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
  public static Entity createQuestgiver() {

    AnimationRenderComponent animator = setupQuestgiverAnimations();

    Entity questgiver = new Entity(EntityType.Questgiver)
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent())
        .addComponent(new MissionDisplay())
        .addComponent(animator);

    questgiver.getComponent(AnimationRenderComponent.class).scaleEntity();
    questgiver.getComponent(ColliderComponent.class).setDensity(10000); //set density so the questgiver doesn't get pushed around
    return questgiver;
  }

  /**
   * Create a questgiver indicator entity (i.e. the animation that indicates status changes)
   *
   * @return questgiver indicator entity
   */
  public static Entity createQuestgiverIndicator(Entity questgiver) {

    AnimationRenderComponent animator = setupMissionAnimations();

    Entity questgiverIndicator = new Entity(EntityType.QuestgiverIndicator)
            .addComponent(new QuestIndicatorComponent())
            .addComponent(animator);

    questgiverIndicator.getComponent(AnimationRenderComponent.class).scaleEntity();
    return questgiverIndicator;
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
    animator.addAnimation("default", 0.1f, Animation.PlayMode.LOOP);
    animator.startAnimation("default");
    // this will get updated in a future sprint to include proper animations
    return animator;
  }

  /**
   * Load animations for the questgiver indicator animation
   *
   * @return an AnimationRenderComponent for the questgiver indicator animations
   */
  private static AnimationRenderComponent setupMissionAnimations() {
    AnimationRenderComponent animator = new AnimationRenderComponent(
        ServiceLocator.getResourceService().getAsset("images/missionStatus.atlas", TextureAtlas.class),
        16f);

    animator.addAnimation("reward_available", 100);
    animator.addAnimation("empty", 100);
    animator.addAnimation("mission_available", 100);
    animator.addAnimation("out_of_time", 100);
    return animator;
  }

}
