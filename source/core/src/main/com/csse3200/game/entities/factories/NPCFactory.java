package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.AnimalAnimationController;
import com.csse3200.game.components.npc.GhostAnimationController;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.RunAwayTask;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.entities.Entity;
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
  public static Entity createChicken(Entity target) {
    Entity chicken = createBaseAnimal();
    BaseAnimalConfig config = configs.cow;

    AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/animals/chicken.atlas", TextureAtlas.class));
    animator.addAnimation("idle_left", Float.MAX_VALUE);
    animator.addAnimation("idle_right", Float.MAX_VALUE);
    animator.addAnimation("walk_left", 0.2f, Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation("walk_right", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("run_left", 0.1f, Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation("run_right", 0.1f, Animation.PlayMode.LOOP);

    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
            .addTask(new RunAwayTask(target, 10, 2f, 4f));

    chicken
            .addComponent(aiTaskComponent)
            .addComponent(animator)
            .addComponent(new AnimalAnimationController());

    PhysicsUtils.setScaledCollider(chicken, 0.8f, 0.4f);
    return chicken;
  }

  /**
   * Creates a cow entity
   * @return cow entity
   */
  public static Entity createCow() {
    Entity cow = createBaseAnimal();
    BaseAnimalConfig config = configs.cow;

    AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/animals/cow.atlas", TextureAtlas.class));
    animator.addAnimation("idle_left", 0.25f, Animation.PlayMode.LOOP);
    animator.addAnimation("idle_right", 0.5f, Animation.PlayMode.LOOP);
    animator.addAnimation("walk_left", 0.25f, Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation("walk_right", 0.25f, Animation.PlayMode.LOOP);

    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new WanderTask(new Vector2(2f, 2f), 2f));

    cow
            .addComponent(aiTaskComponent)
            .addComponent(animator)
            .addComponent(new AnimalAnimationController());

//    cow.scaleHeight(1.2f);//TODO make this bigger and make the hitbox grow accordingly
    return cow;
  }

  /**
   * Creates a generic animal to be used as a base entity by more specific animal creation methods.
   *
   * @return entity
   */
  //TODO: flesh this out. Need to add certain components in a certain order (i think).
  private static Entity createBaseAnimal() {
    Entity animal = new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent());

        PhysicsUtils.setScaledCollider(animal, 0.9f, 0.4f);
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
