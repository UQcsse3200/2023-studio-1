package com.csse3200.game.components.ship;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.EntityType;
import org.junit.jupiter.api.BeforeEach;
import com.csse3200.game.rendering.RenderService;
import org.junit.jupiter.api.Test;
import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.areas.GameArea;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.extension.ExtendWith;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.weather.ClimateController;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class ClueComponentTest {
    private ClueComponent clueComponent;
    private Entity entity;
    @Test
    void testGetCurrentLocation() {
        entity = new Entity();
        clueComponent = new ClueComponent();

        entity.addComponent(this.clueComponent);
        entity.create();

        Vector2 expectedLocation = new Vector2(7, 7);

        Vector2 actualLocation = clueComponent.getCurrentLocation();

        assertEquals(expectedLocation.x, actualLocation.x);
        assertEquals(expectedLocation.y, actualLocation.y);
    }
}