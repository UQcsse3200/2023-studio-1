package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import com.csse3200.game.components.combat.CombatStatsComponent;
import com.csse3200.game.components.combat.TouchAttackComponent;
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
     * Constant for the flight event.
     */
    private static final String FLIGHTEVENT = "flight";

    private static final String IMPACTEVENT = "impact";

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

        animator.addAnimation(FLIGHTEVENT, 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation(IMPACTEVENT, 0.1f);

        projectile
                .addComponent(new CombatStatsComponent(1, 1))
                .addComponent(new ProjectileComponent(2f))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 10f, 0.25f))
                .addComponent(animator);

        projectile.getComponent(HitboxComponent.class).setAsBoxAligned(
            new Vector2(0.4f, 0.4f),
            PhysicsComponent.AlignX.CENTER,
            PhysicsComponent.AlignY.CENTER);

        projectile.getComponent(ProjectileComponent.class).setSpeed(new Vector2(4f, 4f));
        projectile.getComponent(ProjectileComponent.class).setDestroyOnImpact(true);
        projectile.getComponent(ProjectileComponent.class).setConstantVelocity(true);

        return projectile;
    }

    public static Entity createDragonflyProjectile() {
        Entity projectile = createBaseProjectile();

        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/projectiles/dragon_fly_projectile.atlas",
                        TextureAtlas.class),
                16f
        );

        animator.addAnimation(FLIGHTEVENT, 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation(IMPACTEVENT, 0.1f);

        projectile
                .addComponent(new CombatStatsComponent(1, 1))
                .addComponent(new ProjectileComponent(3f))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 5f, 0.05f))
                .addComponent(animator);

        projectile.getComponent(HitboxComponent.class).setAsBoxAligned(
                new Vector2(0.4f, 0.4f),
                PhysicsComponent.AlignX.CENTER,
                PhysicsComponent.AlignY.CENTER);

        projectile.getComponent(ProjectileComponent.class).setDestroyOnImpact(true);

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
                ServiceLocator.getResourceService().getAsset("images/projectiles/gun_projectile" +
                                ".atlas",
                        TextureAtlas.class),
                16f
        );

        animator.addAnimation(FLIGHTEVENT, 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation(IMPACTEVENT, 0.1f);

        projectile
                .addComponent(new ProjectileComponent(2f))
                .addComponent(new TouchAttackComponent(PhysicsLayer.NPC, 10f, 0.3f))
                .addComponent(new CombatStatsComponent(1, 10))
                .addComponent(animator);

        projectile.getComponent(HitboxComponent.class).setAsBoxAligned(
                new Vector2(0.4f, 0.4f),
                PhysicsComponent.AlignX.CENTER,
                PhysicsComponent.AlignY.CENTER);

        projectile.getComponent(ProjectileComponent.class).setDestroyOnImpact(true);
        projectile.getComponent(ProjectileComponent.class).setConstantVelocity(true);

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
