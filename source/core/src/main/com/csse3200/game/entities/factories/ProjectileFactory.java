package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.combat.ProjectileAnimationController;
import com.csse3200.game.components.combat.ProjectileComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * The ProjectileFactory class is responsible for creating different types of projectile entities
 * used in the game.
 */
public class ProjectileFactory {

    /**
     * Creates an oxygen eater projectile entity.
     *
     * @return The created oxygen eater projectile entity.
     */
    public static Entity createOxygenEaterProjectile() {
        Entity projectile = createBaseProjectile();

        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/projectiles/oxygen_eater_projectile.atlas",
                        TextureAtlas.class),
                16f
        );

        animator.addAnimation("flight", 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation("impact", 0.1f);

        projectile
                .addComponent(new ProjectileComponent(2f))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 10f))
                .addComponent(animator);

        projectile.getComponent(HitboxComponent.class).setAsBoxAligned(
            new Vector2(0.4f, 0.4f),
            PhysicsComponent.AlignX.CENTER,
            PhysicsComponent.AlignY.CENTER);

        return projectile;
    }

    /**
     * Creates a player projectile entity.
     *
     * @return The created player projectile entity.
     */
    public static Entity createPlayerProjectile() {
        Entity projectile = createBaseProjectile();

        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/projectiles/oxygen_eater_projectile.atlas",
                        TextureAtlas.class),
                16f
        );

        animator.addAnimation("flight", 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation("impact", 0.1f);

        projectile
                .addComponent(new ProjectileComponent(2f))
                .addComponent(new TouchAttackComponent(PhysicsLayer.NPC, 10f))
                .addComponent(new CombatStatsComponent(1, 10))
                .addComponent(animator);

        projectile.getComponent(HitboxComponent.class).setAsBoxAligned(
                new Vector2(0.4f, 0.4f),
                PhysicsComponent.AlignX.CENTER,
                PhysicsComponent.AlignY.CENTER);

        return projectile;
    }

    /**
     * Creates an oxygen eater projectile entity.
     *
     * @return The created oxygen eater projectile entity.
     */
    public static Entity createBaseProjectile() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent())
                .addComponent(new ProjectileAnimationController());
    }
}
