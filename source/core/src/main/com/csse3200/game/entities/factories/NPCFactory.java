package com.csse3200.game.entities.factories;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.AuraLightComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.combat.TouchAttackComponent;
import com.csse3200.game.components.combat.attackpatterns.BatAttackPattern;
import com.csse3200.game.components.combat.attackpatterns.DragonflyAttackPattern;
import com.csse3200.game.components.combat.attackpatterns.OxygenEaterAttackPattern;
import com.csse3200.game.components.npc.*;
import com.csse3200.game.components.npc.AnimalAnimationController;
import com.csse3200.game.components.npc.FireflyScareComponent;
import com.csse3200.game.components.npc.TamableComponent;
import com.csse3200.game.components.tasks.*;
import com.csse3200.game.components.InteractionDetector;
import com.csse3200.game.components.npc.HostileAnimationController;
import com.csse3200.game.components.tasks.TamedFollowTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityIndicator;
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
import com.csse3200.game.rendering.BlinkComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.Vector2Utils;

import java.util.List;

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

  private static final String IDLE_LEFT = "idle_left";
  private static final String IDLE_RIGHT = "idle_right";
  private static final String WALK_LEFT = "walk_left";
  private static final String WALK_RIGHT = "walk_right";
  private static final String RUN_LEFT = "run_left";
  private static final String RUN_RIGHT = "run_right";
  private static final String ATTACK_LEFT = "attack_left";
  private static final String ATTACK_RIGHT = "attack_right";
  private static final String IDLE_LEFT_TAMED = "idle_left_tamed";
  private static final String IDLE_RIGHT_TAMED = "idle_right_tamed";
  private static final String WALK_LEFT_TAMED = "walk_left_tamed";
    private static final String WALK_RIGHT_TAMED = "walk_right_tamed";

  private static final NPCConfigs configs =
      FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");


  /**
   * Creates a chicken entity
   *
   * @return chicken entity
   */
  public static Entity createChicken() {
    Entity chicken = createBaseAnimal(EntityType.CHICKEN);
    BaseAnimalConfig config = configs.chicken;

    AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/animals/chicken.atlas", TextureAtlas.class),
            16f
    );

    animator.addAnimation(IDLE_LEFT, Float.MAX_VALUE);
    animator.addAnimation(IDLE_RIGHT, Float.MAX_VALUE);
    animator.addAnimation(WALK_LEFT, 0.2f, Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation(WALK_RIGHT, 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation(RUN_LEFT, 0.1f, Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation(RUN_RIGHT, 0.1f, Animation.PlayMode.LOOP);

    // Tamed Animations
    animator.addAnimation(IDLE_LEFT_TAMED, Float.MAX_VALUE);
    animator.addAnimation(IDLE_RIGHT_TAMED, Float.MAX_VALUE);
    animator.addAnimation(WALK_LEFT_TAMED, 0.2f, Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation(WALK_RIGHT_TAMED, 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("run_left_tamed", 0.1f, Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation("run_right_tamed", 0.1f, Animation.PlayMode.LOOP);

    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
            .addTask(new RunAwayTask(ServiceLocator.getGameArea().getPlayer(), 10, 2.25f, 4.25f, new Vector2(3f, 3f)))
            .addTask(new PanicTask("panicStart", 10f, 20, new Vector2(3f, 3f), new Vector2(3f, 3f)))
            .addTask(new TamedFollowTask(ServiceLocator.getGameArea().getPlayer(), 11, 8, 10, 2f, config.favouriteFood, Vector2Utils.ONE));

    List<SingleDropHandler> singleDropHandlers = new ArrayList<>();
    MultiDropComponent multiDropComponent = new MultiDropComponent(singleDropHandlers, true);
    //Chickens untamed drop eggs
    singleDropHandlers.add(new SingleDropHandler(ItemFactory::createEgg, 24,
            ServiceLocator.getTimeService().getEvents()::addListener, "hourUpdate", false));
    //Once tamed, chickens drop one extra egg
    singleDropHandlers.add(new SingleDropHandler(ItemFactory::createEgg, 24,
            ServiceLocator.getTimeService().getEvents()::addListener, "hourUpdate", true));
    //Once tamed, chickens can be fed to drop golden eggs
    singleDropHandlers.add(new SingleDropHandler(ItemFactory::createGoldenEgg, 3,
            chicken.getEvents()::addListener, "feed", true));
    //Drop chicken on death
    singleDropHandlers.add(new SingleDropHandler(ItemFactory::createChickenMeat, 1,
            chicken.getEvents()::addListener, "death", false));

    chicken
            .addComponent(aiTaskComponent)
            .addComponent(multiDropComponent)
            .addComponent(animator)
            .addComponent(new AnimalAnimationController())
            .addComponent(new CombatStatsComponent(10, 0))
            .addComponent(new TamableComponent(ServiceLocator.getGameArea().getPlayer(), config.tamingThreshold,
                    config.tamingProbability, config.favouriteFood));

    PhysicsUtils.setScaledCollider(chicken, 0.8f, 0.4f);

    return chicken;
  }

  /**
   * Creates a cow entity
   *
   * @return cow entity
   */
  public static Entity createCow() {
    Entity cow = createBaseAnimal(EntityType.COW);
    BaseAnimalConfig config = configs.cow;

    AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/animals/cow.atlas", TextureAtlas.class),
            16f
    );
    animator.addAnimation(IDLE_LEFT, 0.25f, Animation.PlayMode.LOOP);
    animator.addAnimation(IDLE_RIGHT, 0.5f, Animation.PlayMode.LOOP);
    animator.addAnimation(WALK_LEFT, 0.25f, Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation(WALK_RIGHT, 0.25f, Animation.PlayMode.LOOP);

    // Tamed Animations
    animator.addAnimation(IDLE_LEFT_TAMED, Float.MAX_VALUE);
    animator.addAnimation(IDLE_RIGHT_TAMED, Float.MAX_VALUE);
    animator.addAnimation(WALK_LEFT_TAMED, 0.2f, Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation(WALK_RIGHT_TAMED, 0.2f, Animation.PlayMode.LOOP);


    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
            .addTask(new TamedFollowTask(ServiceLocator.getGameArea().getPlayer(), 10, 8, 10, 2f, config.favouriteFood, Vector2Utils.ONE));

    List<SingleDropHandler> singleDropHandlers = new ArrayList<>();
    MultiDropComponent multiDropComponent = new MultiDropComponent(singleDropHandlers, true);
    //Cows untamed drop fertiliser
    singleDropHandlers.add(new SingleDropHandler(ItemFactory::createFertiliser, 24,
            ServiceLocator.getTimeService().getEvents()::addListener, "hourUpdate", false));
    //Once tamed, cows drop one extra fertiliser
    singleDropHandlers.add(new SingleDropHandler(ItemFactory::createFertiliser, 24,
            ServiceLocator.getTimeService().getEvents()::addListener, "hourUpdate", true));
    //Once tamed, cows can be fed to drop milk
    singleDropHandlers.add(new SingleDropHandler(ItemFactory::createMilk, 2,
            cow.getEvents()::addListener, "feed", true));
    //Drop beef on death
    singleDropHandlers.add(new SingleDropHandler(ItemFactory::createBeef, 1,
            cow.getEvents()::addListener, "death", false));
    cow
            .addComponent(aiTaskComponent)
            .addComponent(multiDropComponent)
            .addComponent(animator)
            .addComponent(new CombatStatsComponent(20, 0))
            .addComponent(new AnimalAnimationController())
            .addComponent(new TamableComponent(
                    ServiceLocator.getGameArea().getPlayer(), config.tamingThreshold,
                    config.tamingProbability, config.favouriteFood));

    cow.scaleHeight(1.8f);
    PhysicsUtils.setScaledCollider(cow, 0.7f, 0.4f);

    return cow;
  }

  /**
   * Creates an Astrolotl entity
   *
   * @return Astrolotl entity
   */
  public static Entity createAstrolotl() {
    Entity astrolotl = createBaseAnimal(EntityType.ASTROLOTL);
    BaseAnimalConfig config = configs.astrolotl;

    AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/animals/astrolotl.atlas", TextureAtlas.class)
            , 20f);
    animator.addAnimation(IDLE_LEFT, 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation(IDLE_RIGHT, 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation(WALK_LEFT, 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation(WALK_RIGHT, 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation(IDLE_LEFT_TAMED, 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation(IDLE_RIGHT_TAMED, 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation(WALK_LEFT_TAMED, 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation(WALK_RIGHT_TAMED, 0.15f, Animation.PlayMode.LOOP);

    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new WanderTask(new Vector2(1.5f, 1.5f), 5f))
            .addTask(new FollowTask(ServiceLocator.getGameArea().getPlayer(), 10, 8, 10, 3f, new Vector2(3f, 3f)));

    astrolotl
            .addComponent(aiTaskComponent)
            .addComponent(animator)
            .addComponent(new CombatStatsComponent(80, 0))
            .addComponent(new AnimalAnimationController())
            .addComponent(new TamableComponent(
                    ServiceLocator.getGameArea().getPlayer(), config.tamingThreshold,
                    config.tamingProbability, config.favouriteFood));


    astrolotl.scaleHeight(1.2f);
    PhysicsUtils.setScaledCollider(astrolotl, 0.9f, 0.4f);
    return astrolotl;
  }

  /**
   * Creates an Oxygen Eater entity.
   *
   * @return Oxygen Eater entity
   */
  public static Entity createOxygenEater() {
    Entity oxygenEater = createBaseAnimal(EntityType.OXYGEN_EATER);


    AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/animals/oxygen_eater.atlas",
                    TextureAtlas.class),
            16f
    );

    animator.addAnimation(IDLE_LEFT, 0.5f, Animation.PlayMode.LOOP);
    animator.addAnimation(WALK_LEFT, 0.5f, Animation.PlayMode.LOOP);
    animator.addAnimation(IDLE_RIGHT, 0.5f, Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation(WALK_RIGHT, 0.5f, Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation(ATTACK_RIGHT, 0.1f, Animation.PlayMode.REVERSED);
    animator.addAnimation(ATTACK_LEFT, 0.1f);

    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new WanderTask(new Vector2(2f, 2f), 2f));

    oxygenEater
            .addComponent(aiTaskComponent)
            .addComponent(animator)
            .addComponent(new EntityIndicator(oxygenEater))
            .addComponent(new CombatStatsComponent(10, 0))
            .addComponent(new HostileAnimationController())
            .addComponent(new OxygenEaterAttackPattern(1.5f, ProjectileFactory::createOxygenEaterProjectile))
            .addComponent(new InteractionDetector(5f, new ArrayList<>(Arrays.asList(EntityType.PLAYER)))); // TODO: Do we want it to attack anything

    oxygenEater.scaleHeight(2f);
    oxygenEater.getComponent(ColliderComponent.class).setAsBoxAligned(new Vector2(1f, 1f),
            PhysicsComponent.AlignX.CENTER, PhysicsComponent.AlignY.CENTER);
    oxygenEater.getComponent(HitboxComponent.class).setAsBoxAligned(new Vector2(1f, 1f),
            PhysicsComponent.AlignX.CENTER, PhysicsComponent.AlignY.CENTER);

    return oxygenEater;
  }


  /**
   * Creates a Fire Fly entity.
   *
   * @return Fire Fly entity
   */
  public static Entity createFireFlies() {
    SecureRandom random = new SecureRandom();
    AuraLightComponent light = new AuraLightComponent(3f, Color.ORANGE);
    light.toggleLight();

    AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/fireflies.atlas", TextureAtlas.class),
            16f
    );
    String animation = "default";
    if (random.nextInt(10000) == 0) {
      animation = "DancinInTheMoonlight";
    }
    animator.addAnimation(animation, 0.5f, Animation.PlayMode.LOOP);
    animator.startAnimation(animation);

    return new Entity(EntityType.FIRE_FLIES)
            .addComponent(animator)
            .addComponent(light)
            // Not actually scaring just dying from daylight (named from previous idea for feature)
            .addComponent(new FireflyScareComponent())
            .addComponent(new PhysicsComponent());
  }

  /**
   * Creates a Dragonfly entity
   *
   * @return Dragonfly entity
   */
  public static Entity createDragonfly() {
    Entity dragonfly = createBaseAnimal(EntityType.DRAGONFLY);
    BaseAnimalConfig config = configs.dragonfly;

    AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/animals/dragonfly.atlas",
                    TextureAtlas.class)
            , 20f);
    animator.addAnimation(ATTACK_LEFT, 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation(ATTACK_RIGHT, 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation(WALK_LEFT, 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation(WALK_RIGHT, 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation(IDLE_LEFT, 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation(IDLE_RIGHT, 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation(RUN_LEFT, 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation(RUN_RIGHT, 0.15f, Animation.PlayMode.LOOP);

    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new WanderTask(new Vector2(1.5f, 1.5f), 5f))
            .addTask(new MoveToPlantTask(5, new Vector2(2f, 2f), 0.5f))
            .addTask(new RunAwayTask(ServiceLocator.getGameArea().getPlayer(), 10, 5f, 5f, new Vector2(2f, 2f), false));


    dragonfly
            .addComponent(aiTaskComponent)
            .addComponent(animator)
            .addComponent(new HostileAnimationController())
            .addComponent(new EntityIndicator(dragonfly))
            .addComponent(new DragonflyAttackPattern(1.5f, ProjectileFactory::createDragonflyProjectile))
            .addComponent(new InteractionDetector(5f,
                    new ArrayList<>(Arrays.asList((EntityType.PLAYER), (EntityType.PLANT)))))
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack));


    dragonfly.scaleHeight(1.2f);
    return dragonfly;
  }

  /**
   * Creates a Bat entity
   *
   * @return Bat entity
   */
  public static Entity createBat() {
    Entity bat = createBaseAnimal(EntityType.BAT);
    BaseAnimalConfig config = configs.bat;

    AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/animals/bat.atlas",
                    TextureAtlas.class)
            , 20f);
    animator.addAnimation(ATTACK_LEFT, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(ATTACK_RIGHT, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(WALK_LEFT, 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation(WALK_RIGHT, 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation(IDLE_LEFT, 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation(IDLE_RIGHT, 0.15f, Animation.PlayMode.LOOP);


    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new WanderTask(new Vector2(1.5f, 1.5f), 5f))
            .addTask(new FollowTask(ServiceLocator.getGameArea().getPlayer(), 10, 8, 10, 1.5f, new Vector2(3f, 3f), false));

    bat
            .addComponent(aiTaskComponent)
            .addComponent(animator)
            .addComponent(new HostileAnimationController())
            .addComponent(new BatAttackPattern(1.5f))
            .addComponent(new EntityIndicator(bat))
            .addComponent(new InteractionDetector(1.5f,
                    new ArrayList<>(Arrays.asList(EntityType.PLAYER))))
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack));



    bat.scaleHeight(1.2f);
    return bat;
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
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new BlinkComponent());

    if (type != EntityType.DRAGONFLY && type != EntityType.BAT) {
      animal.addComponent(new ColliderComponent());
    }

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
            .addTask(new ChaseTask(target, 10, 3f, 4f, Vector2Utils.ONE));
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
