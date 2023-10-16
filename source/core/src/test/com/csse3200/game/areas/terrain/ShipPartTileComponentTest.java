package com.csse3200.game.areas.terrain;

import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.components.ship.ShipDebrisComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class ShipPartTileComponentTest {
    @Test
    void destroysShipDebrisFirst() {
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerTimeService(new TimeService());
        ServiceLocator.registerMissionManager(new MissionManager());
        ServiceLocator.registerResourceService(new ResourceService());

        ServiceLocator.registerGameArea(new SpaceGameArea(null));

        EntityService mockEntityService = spy(EntityService.class);
        ServiceLocator.registerEntityService(mockEntityService);

        Entity shipPartTile = new Entity(EntityType.TILE)
                .addComponent(new ShipPartTileComponent());
        mockEntityService.register(shipPartTile);

        Entity shipDebris = new Entity(EntityType.SHIP_DEBRIS)
                .addComponent(new ShipDebrisComponent());

        // should register the shipDebris when added to the shipPartTile
        shipPartTile.getComponent(ShipPartTileComponent.class).addShipDebris(shipDebris);
        verify(mockEntityService, times(1)).register(shipDebris);

        // should only destroy the shipDebris
        shipPartTile.getEvents().trigger("destroy", null);
        verify(mockEntityService, times(1)).unregister(shipDebris);
        verify(mockEntityService, times(0)).unregister(shipPartTile);
    }

    @Test
    void dropsItemOnDestroyAndRemovesSelfFromTerrainTile() {
        EntityService mockEntityService = spy(EntityService.class);
        ServiceLocator.registerEntityService(mockEntityService);

        TerrainTile terrainTile = new TerrainTile(null, TerrainTile.TerrainCategory.GRASS);
        TerrainTile mockTerrainTile = spy(terrainTile);

        Entity shipPartTile = new Entity(EntityType.TILE)
                .addComponent(new ShipPartTileComponent());
        mockEntityService.register(shipPartTile);

        mockTerrainTile.setOccupant(shipPartTile);
        mockTerrainTile.setOccupied();

        Entity mockItem = new Entity(EntityType.ITEM);

        try (MockedStatic<ItemFactory> itemFactory = mockStatic(ItemFactory.class)) {
            itemFactory.when(ItemFactory::createShipPart).thenReturn(mockItem);

            shipPartTile.getEvents().trigger("destroy", mockTerrainTile);

            // shipPartTile should register the dropped item, then unregister self
            verify(mockEntityService, times(1)).register(mockItem);
            verify(mockEntityService, times(1)).unregister(shipPartTile);

            // should also remove self from the terrain tile
            verify(mockTerrainTile, times(1)).removeOccupant();
        }
    }
}
