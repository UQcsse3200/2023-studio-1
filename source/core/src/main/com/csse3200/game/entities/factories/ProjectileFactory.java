package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.combat.ProjectileAnimationController;
import com.csse3200.game.components.combat.ProjectileComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class ProjectileFactory {

    public static Entity createOxygenEaterProjectile() {
        Entity projectile = createBaseProjectile();

        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/projectiles/oxygen_eater_projectile.atlas",
                        TextureAtlas.class),
                16f
        );

        animator.addAnimation("flight", 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation("impact", 0.15f);

        projectile
                .addComponent(new ProjectileComponent(2f))
                .addComponent(animator);

        return projectile;
    }

    public static Entity createBaseProjectile() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent())
                .addComponent(new ProjectileAnimationController());
    }
}
