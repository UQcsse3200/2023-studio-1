package com.csse3200.game.components.plants;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.components.HitboxComponent;

/**
 * Component to inform the plant when the player is close enough for plant sounds to be played.
 * This will prevent the game from being clogged up with plant sounds.
 */
public class PlantProximityComponent extends HitboxComponent {
    /**
     * The radius of the hitbox area.
     */
    private final float radius;

    /**
     * The shape of the hitbox area.
     */
    private final CircleShape shape = new CircleShape();

    /**
     * Constructor for the plant proximity component. Just used to setting the
     * radius of the area. The radius will be the same for all plants.
     */
    public PlantProximityComponent() {
        this.radius = 8f;
    }

    /**
     * Sets up the radius for the collider and listen for relevant services.
     */
    @Override
    public void create() {
        shape.setRadius(radius);
        shape.setPosition(entity.getComponent(PlantComponent.class).getCropTile().getEntity().getScale().scl(0.5f).add(0, -0.5f));
        setShape(shape);
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);

        super.create();
    }

    /**
     * Checks if the entity is the player on collision start. If the entity is the player
     * then tell the plant that the player is in range.
     *
     * @param me        The fixture of this component.
     * @param other     The fixture of the colliding entity.
     */
    private void onCollisionStart(Fixture me, Fixture other) {
        if (getFixture() != me) {
            return;
        }

        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;

        if (target.getType() == EntityType.PLAYER) {
            entity.getComponent(PlantComponent.class).setPlayerInProximity(true);
        }

    }

    /**
     * Checks if the entity is the player on collision end. If the entity is the player
     * then tell the plant that the player is no longer in range.
     * @param me        The fixture of this component.
     * @param other     The fixture of the colliding entity.
     */
    private void onCollisionEnd(Fixture me, Fixture other) {
        if (getFixture() != me) {
            return;
        }

        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        if (target.getType() == EntityType.PLAYER) {
            entity.getComponent(PlantComponent.class).setPlayerInProximity(false);
        }
    }

}
