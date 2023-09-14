package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.npc.AnimalAnimationController;
import com.csse3200.game.components.npc.PassiveDropComponent;
import com.csse3200.game.components.npc.TamableComponent;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.FollowTask;
import com.csse3200.game.components.tasks.RunAwayTask;
import com.csse3200.game.components.tasks.WanderTask;
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


  /**
   * Creates a chicken entity
   * @param player player entity
   * @return chicken entity
   */
  public static Entity createChicken(Entity player) {
    Entity chicken = createBaseAnimal(EntityType.Chicken);
    BaseAnimalConfig config = configs.chicken;

    AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/animals/chicken.atlas", TextureAtlas.class),
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
            .addTask(new RunAwayTask(player, 10, 2.25f, 4.25f, new Vector2(3f, 3f)));

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
            ServiceLocator.getResourceService().getAsset("images/animals/cow.atlas", TextureAtlas.class),
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
            .addTask(new WanderTask(new Vector2(2f, 2f), 2f));

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
            ServiceLocator.getResourceService().getAsset("images/animals/astrolotl.atlas", TextureAtlas.class)
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
            ServiceLocator.getResourceService().getAsset("images/animals/oxygen_eater.atlas",
                    TextureAtlas.class),
            16f
    );

    animator.addAnimation("idle_left", 1f, Animation.PlayMode.LOOP);
    animator.addAnimation("idle_right", 1f, Animation.PlayMode.LOOP);
    animator.addAnimation("walk_left", 1f, Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation("walk_right", 1f, Animation.PlayMode.LOOP);



//    animator.addAnimation("idle", 1f, Animation.PlayMode.LOOP);
  // animator.addAnimation("consume", 1f, Animation.PlayMode.LOOP);

    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new WanderTask(new Vector2(2f, 2f), 2f));

    oxygenEater
            .addComponent(aiTaskComponent)
            .addComponent(animator)
            .addComponent(new AnimalAnimationController());


    oxygenEater.scaleHeight(3f);
    PhysicsUtils.setScaledCollider(oxygenEater, 0.7f, 0.4f);


    return oxygenEater;
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
            .addComponent(new ColliderComponent());

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
