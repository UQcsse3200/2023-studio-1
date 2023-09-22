package com.csse3200.game.components.plants;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.components.HitboxComponent;

import java.util.ArrayList;
import java.util.List;

public class PlantProximityComponent extends HitboxComponent {
    private float radius;
    private CircleShape shape = new CircleShape();

    public PlantProximityComponent() {
        this.radius = 5f;
    }

    @Override
    public void create() {
        shape.setRadius(radius);
        shape.setPosition(entity.getComponent(PlantComponent.class).getCropTile().getEntity().getScale().scl(0.5f).add(0, -0.5f));
        setShape(shape);
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);

        super.create();
    }

    private void onCollisionStart(Fixture me, Fixture other) {
        if (getFixture() != me) {
            return;
        }

        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;

        if (target.getType() == EntityType.Player) {
            entity.getComponent(PlantComponent.class).setPlayerInProximity(true);
        }

    }

    private void onCollisionEnd(Fixture me, Fixture other) {
        if (getFixture() != me) {
            return;
        }

        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        if (target.getType() == EntityType.Player) {
            entity.getComponent(PlantComponent.class).setPlayerInProximity(false);
        }
    }

}
