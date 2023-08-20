package com.csse3200.game.areas.terrain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
public class CropTileComponentTest {

    void shouldKnowWaterLevel() {
        Entity cropTile =
                new Entity()
                        .addComponent(new CropTileComponent(50, 50))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent());
    }

    void shouldKnowSoilQuality() {
        Entity cropTile =
                new Entity()
                        .addComponent(new CropTileComponent(50, 50))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent());
    }

    void shouldFertilise() {
        Entity cropTile =
                new Entity()
                        .addComponent(new CropTileComponent(50, 50))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent());
    }

}
