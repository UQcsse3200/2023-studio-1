package com.csse3200.game.entities.factories;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.AuraLightComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.InteractionDetector;
import com.csse3200.game.components.inventory.InventoryDisplay;
import com.csse3200.game.components.inventory.ToolbarDisplay;
import com.csse3200.game.components.maingame.PauseMenuActions;
import com.csse3200.game.components.player.*;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.player.ItemPickupComponent;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.components.player.OpenPauseComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.player.PlayerAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

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
    //InventoryComponent playerInventory = new InventoryComponent(new ArrayList<>());

    Entity player =
        new Entity(EntityType.Player)
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
            .addComponent(new PlayerActions())
            .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack))
            .addComponent(new InventoryComponent())
            .addComponent(new HungerComponent(30))
            .addComponent(inputComponent)
            .addComponent(animator)
            .addComponent(new OpenPauseComponent())
            .addComponent(new PlayerAnimationController())
            .addComponent(new ItemPickupComponent())
            .addComponent(new InteractionDetector(2f, new ArrayList<EntityType>(Arrays.asList(EntityType.Questgiver, EntityType.Gate, EntityType.Chest, EntityType.Chicken,
                    EntityType.Cow, EntityType.Astrolotl, EntityType.OxygenEater, EntityType.ShipDebris, EntityType.Ship))))
            .addComponent(new ToolbarDisplay())
	        .addComponent(new AuraLightComponent(6f))
            .addComponent(new InventoryDisplay("updateInventory", "toggleInventory", 30, 10, true))
            .addComponent(new PauseMenuActions());

    player.getComponent(ColliderComponent.class).setDensity(1.5f);
    player.getComponent(ColliderComponent.class).setAsBox(new Vector2(0.9f, 0.9f), new Vector2(1.5f, 1f));
    player.getComponent(HitboxComponent.class).setAsBox(new Vector2(1f, 2f), new Vector2(1.5f, 1.5f));
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
    animator.addAnimation("blink_down", 0.5f, Animation.PlayMode.NORMAL);
    animator.addAnimation("yawn_down", 0.5f, Animation.PlayMode.NORMAL);
    animator.addAnimation("snooze_down", 0.5f, Animation.PlayMode.NORMAL);
    animator.addAnimation("blink_left", 0.5f, Animation.PlayMode.NORMAL);
    animator.addAnimation("yawn_left", 0.5f, Animation.PlayMode.NORMAL);
    animator.addAnimation("snooze_left", 0.5f, Animation.PlayMode.NORMAL);
    animator.addAnimation("blink_up", 0.5f, Animation.PlayMode.NORMAL);
    animator.addAnimation("yawn_up", 0.5f, Animation.PlayMode.NORMAL);
    animator.addAnimation("snooze_up", 0.5f, Animation.PlayMode.NORMAL);
    animator.addAnimation("blink_right", 0.5f, Animation.PlayMode.NORMAL);
    animator.addAnimation("yawn_right", 0.5f, Animation.PlayMode.NORMAL);
    animator.addAnimation("snooze_right", 0.5f, Animation.PlayMode.NORMAL);
    animator.addAnimation("default", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("interact_down", 0.13f, Animation.PlayMode.NORMAL);
    animator.addAnimation("interact_up", 0.13f, Animation.PlayMode.NORMAL);
    animator.addAnimation("interact_right", 0.1f, Animation.PlayMode.NORMAL);
    animator.addAnimation("interact_left", 0.1f, Animation.PlayMode.NORMAL);

    animator.addAnimation("hoe_up",0.1f,Animation.PlayMode.NORMAL);
    animator.addAnimation("hoe_left",0.1f,Animation.PlayMode.NORMAL);
    animator.addAnimation("hoe_right",0.1f,Animation.PlayMode.NORMAL);
    animator.addAnimation("hoe_down",0.1f,Animation.PlayMode.NORMAL);
    animator.addAnimation("shovel_up",0.1f,Animation.PlayMode.NORMAL);
    animator.addAnimation("shovel_left",0.1f,Animation.PlayMode.NORMAL);
    animator.addAnimation("shovel_right",0.1f,Animation.PlayMode.NORMAL);
    animator.addAnimation("shovel_down",0.1f,Animation.PlayMode.NORMAL);
    animator.addAnimation("watering_can_up",0.1f,Animation.PlayMode.NORMAL);
    animator.addAnimation("watering_can_left",0.1f,Animation.PlayMode.NORMAL);
    animator.addAnimation("watering_can_right",0.1f,Animation.PlayMode.NORMAL);
    animator.addAnimation("watering_can_down",0.1f,Animation.PlayMode.NORMAL);
    animator.addAnimation("scythe_up",0.1f,Animation.PlayMode.NORMAL);
    animator.addAnimation("scythe_left",0.1f,Animation.PlayMode.NORMAL);
    animator.addAnimation("scythe_right",0.1f,Animation.PlayMode.NORMAL);
    animator.addAnimation("scythe_down",0.1f,Animation.PlayMode.NORMAL);
    animator.addAnimation("sword_up",0.1f,Animation.PlayMode.NORMAL);
    animator.addAnimation("sword_left",0.1f,Animation.PlayMode.NORMAL);
    animator.addAnimation("sword_right",0.1f,Animation.PlayMode.NORMAL);
    animator.addAnimation("sword_down",0.1f,Animation.PlayMode.NORMAL);
  }

  private PlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }

}
