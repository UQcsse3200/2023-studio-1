package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.AnimalAnimationController;
import com.csse3200.game.components.npc.GhostAnimationController;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.npc.TamableComponent;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.FollowTask;
import com.csse3200.game.components.tasks.RunAwayTask;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.configs.BaseAnimalConfig;
import com.csse3200.game.entities.configs.BaseEntityConfig;
import com.csse3200.game.entities.configs.GhostKingConfig;
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

import javax.lang.model.UnknownEntityException;
import java.lang.reflect.Method;
import java.util.HashMap;

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
   * Creates a ghost entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public static Entity createGhost(Entity target) {
    Entity ghost = createBaseNPC(target);
    BaseEntityConfig config = configs.ghost;

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/ghost.atlas", TextureAtlas.class));
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);

    ghost
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
        .addComponent(animator)
        .addComponent(new GhostAnimationController());

    ghost.getComponent(AnimationRenderComponent.class).scaleEntity();

    return ghost;
  }

  /**
   * Creates a ghost king entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public static Entity createGhostKing(Entity target) {
    Entity ghostKing = createBaseNPC(target);
    GhostKingConfig config = configs.ghostKing;

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService()
                .getAsset("images/ghostKing.atlas", TextureAtlas.class));
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);

    ghostKing
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
        .addComponent(animator)
        .addComponent(new GhostAnimationController());

    ghostKing.getComponent(AnimationRenderComponent.class).scaleEntity();
    return ghostKing;
  }

  /**
   * Creates a chicken entity
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

    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
            .addTask(new RunAwayTask(player, 10, 2f, 4f, new Vector2(3f, 3f)));

    chicken
            .addComponent(aiTaskComponent)
            .addComponent(animator)
            .addComponent(new AnimalAnimationController())
            .addComponent(new TamableComponent(player, config.tamingThreshold,
                    config.tamingProbability, config.favouriteFood));

    PhysicsUtils.setScaledCollider(chicken, 0.8f, 0.4f);
    return chicken;
  }

  /**
   * Creates a cow entity
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

    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new WanderTask(new Vector2(2f, 2f), 2f));

    cow
            .addComponent(aiTaskComponent)
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
            .addTask(new WanderTask(new Vector2(2f, 2f), 3f))
            .addTask(new FollowTask(player, 10, 5f, 5f, 2f));

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
