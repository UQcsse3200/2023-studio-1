package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.InteractionDetector;
import com.csse3200.game.components.AuraLightComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.npc.*;
import com.csse3200.game.components.tasks.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.configs.BaseAnimalConfig;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.Arrays;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Factory to create non-playable character (NPC) entities with predefined components.
 *
 * <p>Each NPC entity type should have a creation method that returns a corresponding entity.
 * Predefined entity properties can be loaded from configs stored as json files which are defined in
 * "NPCConfigs".
 *
 * <p>If needed, this factory can be separated into more specific factories for entities with
 * similar characteristics.
 */
public class NPCFactory {
  private static final NPCConfigs configs =
      FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

  private static final String[] textureAtlasPaths = new String[]{
          "images/animals/chicken.atlas",
          "images/animals/cow.atlas",
          "images/animals/astrolotl.atlas",
          "images/animals/oxygen_eater.atlas",
          "images/fireflies.atlas"
  };

  static {
    ServiceLocator.getResourceService().loadTextureAtlases(textureAtlasPaths);
    ServiceLocator.getResourceService().loadAll();
  }

  /**
   * Creates a chicken entity
   * @param player player entity
   * @return chicken entity
   */
  public static Entity createChicken(Entity player) {
    Entity chicken = createBaseAnimal(EntityType.Chicken);
    BaseAnimalConfig config = configs.chicken;

    AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset(textureAtlasPaths[0], TextureAtlas.class),
            16f
    );
    animator.addAnimation("idle_left", Float.MAX_VALUE);
    animator.addAnimation("idle_right", Float.MAX_VALUE);
    animator.addAnimation("walk_left", 0.2f, Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation("walk_right", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("run_left", 0.1f, Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation("run_right", 0.1f, Animation.PlayMode.LOOP);

    // Tamed Animations
    animator.addAnimation("idle_left_tamed", Float.MAX_VALUE);
    animator.addAnimation("idle_right_tamed", Float.MAX_VALUE);
    animator.addAnimation("walk_left_tamed", 0.2f, Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation("walk_right_tamed", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("run_left_tamed", 0.1f, Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation("run_right_tamed", 0.1f, Animation.PlayMode.LOOP);

    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
            .addTask(new RunAwayTask(player, 10, 2.25f, 4.25f, new Vector2(3f, 3f)))
            .addTask(new TamedFollowTask(player, 10, 8, 10, 2f, config.favouriteFood));

    chicken
            .addComponent(aiTaskComponent)
            .addComponent(new PassiveDropComponent(ItemFactory::createEgg, 2))
            .addComponent(animator)
            .addComponent(new AnimalAnimationController())
            .addComponent(new TamableComponent(player, config.tamingThreshold,
                    config.tamingProbability, config.favouriteFood));

    PhysicsUtils.setScaledCollider(chicken, 0.8f, 0.4f);

    return chicken;
  }

  /**
   * Creates a cow entity
   * @param player player entity
   * @return cow entity
   */
  public static Entity createCow(Entity player) {
    Entity cow = createBaseAnimal(EntityType.Cow);
    BaseAnimalConfig config = configs.cow;

    AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset(textureAtlasPaths[1], TextureAtlas.class),
            16f
    );
    animator.addAnimation("idle_left", 0.25f, Animation.PlayMode.LOOP);
    animator.addAnimation("idle_right", 0.5f, Animation.PlayMode.LOOP);
    animator.addAnimation("walk_left", 0.25f, Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation("walk_right", 0.25f, Animation.PlayMode.LOOP);

    // Tamed Animations
    animator.addAnimation("idle_left_tamed", Float.MAX_VALUE);
    animator.addAnimation("idle_right_tamed", Float.MAX_VALUE);
    animator.addAnimation("walk_left_tamed", 0.2f, Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation("walk_right_tamed", 0.2f, Animation.PlayMode.LOOP);


    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
            .addTask(new TamedFollowTask(player, 10, 8, 10, 2f, config.favouriteFood));

    cow
            .addComponent(aiTaskComponent)
            .addComponent(new PassiveDropComponent(ItemFactory::createFertiliser, 24))
            .addComponent(animator)
            .addComponent(new AnimalAnimationController())
            .addComponent(new TamableComponent(
                    player, config.tamingThreshold,
                    config.tamingProbability, config.favouriteFood));

    cow.scaleHeight(1.8f);
    PhysicsUtils.setScaledCollider(cow, 0.7f, 0.4f);

    return cow;
  }

  /**
   * Creates an Astrolotl entity
   * @param player player entity
   * @return Astrolotl entity
   */
  public static Entity createAstrolotl(Entity player) {
    Entity astrolotl = createBaseAnimal(EntityType.Astrolotl);
    BaseAnimalConfig config = configs.astrolotl;

    AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset(textureAtlasPaths[2], TextureAtlas.class)
            , 20f);
    animator.addAnimation("idle_left", 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation("idle_right", 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation("walk_left", 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation("walk_right", 0.15f, Animation.PlayMode.LOOP);

    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new WanderTask(new Vector2(1.5f, 1.5f), 5f))
            .addTask(new FollowTask(player, 10, 8, 10, 3f));

    astrolotl
            .addComponent(aiTaskComponent)
            .addComponent(animator)
            .addComponent(new AnimalAnimationController())
            .addComponent(new TamableComponent(
                    player, config.tamingThreshold,
                    config.tamingProbability, config.favouriteFood));


    astrolotl.scaleHeight(1.2f);
    PhysicsUtils.setScaledCollider(astrolotl, 0.9f, 0.4f);
    return astrolotl;
  }

  /**
   * Creates an Oxygen Eater entity.
   * @param player player entity
   * @return Oxygen Eater entity
   */
  public static Entity createOxygenEater(Entity player) {
    Entity oxygenEater = createBaseAnimal(EntityType.OxygenEater);
    BaseAnimalConfig config = configs.oxygenEater;

    AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset(textureAtlasPaths[3], TextureAtlas.class),
            16f
    );

    animator.addAnimation("idle_left", 0.5f, Animation.PlayMode.LOOP);
    animator.addAnimation("walk_left", 0.5f, Animation.PlayMode.LOOP);
    animator.addAnimation("idle_right", 0.5f, Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation("walk_right", 0.5f, Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation("attack_right", 0.1f, Animation.PlayMode.REVERSED);
    animator.addAnimation("attack_left", 0.1f);

    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new WanderTask(new Vector2(2f, 2f), 2f));

    oxygenEater
            .addComponent(aiTaskComponent)
            .addComponent(animator)
            .addComponent(new HostileAnimationController())
            .addComponent(new OxygenEaterAttackPattern())
            .addComponent(new InteractionDetector(5f, new ArrayList<>(Arrays.asList(EntityType.Player)))); // TODO: Do we want it to attack anything

    oxygenEater.scaleHeight(2f);
    oxygenEater.getComponent(ColliderComponent.class).setAsBoxAligned(new Vector2(1f, 1f),
            PhysicsComponent.AlignX.CENTER, PhysicsComponent.AlignY.CENTER);
    oxygenEater.getComponent(HitboxComponent.class).setAsBoxAligned(new Vector2(1f, 1f),
            PhysicsComponent.AlignX.CENTER, PhysicsComponent.AlignY.CENTER);

    return oxygenEater;
  }

  public static Entity createFireFlies(Entity player) {
    SecureRandom random = new SecureRandom();
    AuraLightComponent light = new AuraLightComponent(3f, Color.ORANGE);
    light.toggleLight();

    AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset(textureAtlasPaths[4], TextureAtlas.class),
            16f
    );
    String animation = "default";
    if (random.nextInt(10000) == 0) {
      animation = "DancinInTheMoonlight";
    }
    animator.addAnimation(animation, 0.5f, Animation.PlayMode.LOOP);
    animator.startAnimation(animation);

    Entity fireflies = new Entity(EntityType.FireFlies)
            .addComponent(animator)
            .addComponent(light)
            // Not actually scaring just dying from daylight (named from previous idea for feature)
            .addComponent(new FireflyScareComponent())
            .addComponent(new PhysicsComponent());
    return fireflies;
  }

  /**
   * Creates a generic animal to be used as a base entity by more specific animal creation methods.
   *
   * @return entity
   */
  private static Entity createBaseAnimal(EntityType type) {
    Entity animal = new Entity(type)
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent());

    return animal;
  }


  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */
  private static Entity createBaseNPC(Entity target) {
    AITaskComponent aiComponent =
        new AITaskComponent()
            .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
            .addTask(new ChaseTask(target, 10, 3f, 4f));
    Entity npc =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f))
            .addComponent(aiComponent);

    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    return npc;
  }

  private NPCFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
