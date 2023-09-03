package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.inventory.ToolbarDisplay;
import com.csse3200.game.components.player.*;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.player.PlayerAnimationController;
import com.csse3200.game.components.inventory.InventoryDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;


import java.util.ArrayList;

/**
 * Factory to create a player entity.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */
public class PlayerFactory {
  private static final PlayerConfig stats =
      FileLoader.readClass(PlayerConfig.class, "configs/player.json");

  /**
   * Create a player entity.
   * @return entity
   */
  public static Entity createPlayer() {
    InputComponent inputComponent =
        ServiceLocator.getInputService().getInputFactory().createForPlayer();

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/player.atlas", TextureAtlas.class),
                    16f
            );

    setupPlayerAnimator(animator);
    InventoryComponent playerInventory = new InventoryComponent(new ArrayList<>());

    Entity player =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
            .addComponent(new PlayerActions())
            .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack))
                .addComponent(playerInventory)
            .addComponent(inputComponent)
            .addComponent(animator)
            .addComponent(new PlayerAnimationController())
            .addComponent(new ItemPickupComponent())
                .addComponent(new InventoryDisplay(playerInventory))
            .addComponent(new ToolbarDisplay());

    PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
    player.getComponent(ColliderComponent.class).setDensity(1.5f);
    player.getComponent(AnimationRenderComponent.class).scaleEntity();
    player.getComponent(KeyboardPlayerInputComponent.class).setActions(player.getComponent(PlayerActions.class));
    return player;
  }

  /**
   * Registers player animations to the AnimationRenderComponent.
   */
  public static void setupPlayerAnimator(AnimationRenderComponent animator) {
    animator.addAnimation("walk_left", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("walk_right", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("walk_down", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("walk_up", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("run_left", 0.05f, Animation.PlayMode.LOOP);
    animator.addAnimation("run_right", 0.05f, Animation.PlayMode.LOOP);
    animator.addAnimation("run_down", 0.05f, Animation.PlayMode.LOOP);
    animator.addAnimation("run_up", 0.05f, Animation.PlayMode.LOOP);
    animator.addAnimation("idle_down", 0.5f, Animation.PlayMode.LOOP_RANDOM);
    animator.addAnimation("idle_left", 0.5f, Animation.PlayMode.LOOP_RANDOM);
    animator.addAnimation("idle_up", 0.5f, Animation.PlayMode.LOOP_RANDOM);
    animator.addAnimation("idle_right", 0.5f, Animation.PlayMode.LOOP_RANDOM);
    animator.addAnimation("default", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("interact_down", 0.13f, Animation.PlayMode.NORMAL);
    animator.addAnimation("interact_up", 0.13f, Animation.PlayMode.NORMAL);
    animator.addAnimation("interact_right", 0.1f, Animation.PlayMode.NORMAL);
    animator.addAnimation("interact_left", 0.1f, Animation.PlayMode.NORMAL);
  }

  private PlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
