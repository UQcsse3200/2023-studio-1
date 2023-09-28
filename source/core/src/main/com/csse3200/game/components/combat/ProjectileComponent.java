package com.csse3200.game.components.combat;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.Component;
import com.csse3200.game.events.ScheduledEvent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * The ProjectileComponent class defines the behavior of a projectile entity in the game.
 * It handles movement, duration, impact, and destruction of the projectile.
 */
public class ProjectileComponent extends Component {
    /** Speed of projectile */
    private Vector2 speed;
    /** Duration for which projectile remains active before being destroyed */
    private final float duration;
    /** Indicates whether projectile has impacted */
    private boolean hasImpact;
    /** Physics component of projectile */
    private PhysicsComponent physicsComponent;
    /** The scheduled event to handle projectile expiration. */
    private ScheduledEvent bulletExpiredEvent;
    /** Velocity vector of projectile */
    private Vector2 velocity;
    private boolean constantVelocity = false;
    private boolean destroyOnImpact = false;

    /**
     * Constructs a ProjectileComponent with a specified duration.
     *
     * @param duration The duration for which the projectile remains active before self-destruction.
     */
    public ProjectileComponent(float duration) {
        this.duration = duration;
        speed = new Vector2(1f, 1f);
    }

    /**
     * Initializes the ProjectileComponent when it is created.
     * Sets up physics properties, event listeners, and schedules the projectile's destruction.
     */
    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        physicsComponent.getBody().setBullet(true);
        entity.getEvents().addListener("destroyProjectile", this::destroyProjectile);
        entity.getEvents().addListener("impactStart", this::impact);

        // Schedule an event to destroy the projectile after the specified duration.
        bulletExpiredEvent = entity.getEvents().scheduleEvent(duration, "destroyProjectile");
    }

    /**
     * Sets the target direction for the projectile and applies velocity to its physics body.
     *
     * @param target The target position to which the projectile should be directed.
     */
    public void setTargetDirection(Vector2 target) {
        Vector2 directionVector = target.cpy().sub(entity.getCenterPosition()).nor();
        velocity = directionVector.scl(speed);

        Body body = physicsComponent.getBody();
        if (constantVelocity) {
            body.setAngularDamping(0f);
            body.setLinearDamping(0f);
        }

        body.applyLinearImpulse(velocity, body.getWorldCenter(), true);
    }

    public void setConstantVelocity(boolean constantVelocity) {
        this.constantVelocity = constantVelocity;
    }

    /**
     * Sets the speed of the projectile.
     *
     * @param speed The new speed vector for the projectile.
     */
    public void setSpeed(Vector2 speed) {
        this.speed = speed;
    }

    /**
     * Destroys the projectile entity by removing it from the game area.
     */
    private void destroyProjectile() {
        ServiceLocator.getGameArea().removeEntity(entity);
    }

    /**
     * Handles the impact event, cancelling the bullet expiration event and disabling touch attack functionality.
     */
    private void impact() {
        if (destroyOnImpact) {
            entity.getEvents().cancelEvent(bulletExpiredEvent);
            physicsComponent.getBody().setLinearVelocity(0,  0);
            entity.getComponent(TouchAttackComponent.class).setEnabled(false);
        }

        hasImpact = true;
    }

    /**
     * Updates the projectile's behavior, destroying it once its impact animation is finished.
     */
    @Override
    public void update() {
        if (hasImpact && destroyOnImpact) {
            if (entity.getComponent(AnimationRenderComponent.class).isFinished()) {
                destroyProjectile();
            }
        }
    }

    public void setDestroyOnImpact(boolean destroyOnImpact) {
        this.destroyOnImpact = destroyOnImpact;
    }
    /**
     * Gets the velocity vector of the projectile.
     *
     * @return The velocity vector of the projectile.
     */
    public Vector2 getVelocity() {
        return velocity;
    }
}
