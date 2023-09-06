package com.csse3200.game.areas.terrain;

import com.csse3200.game.components.plants.PlantComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class CropTileComponentTest {
    private Entity cropTile1, cropTile2, cropTile3, cropTile4, cropTile5, cropTile6, cropTile7;
    @BeforeEach
    public void init() {
        cropTile1 = new Entity().addComponent(new CropTileComponent(1f, 0.5f));
        cropTile2 = new Entity().addComponent(new CropTileComponent(0.5f, 0.5f));
        cropTile3 = new Entity().addComponent(new CropTileComponent(1f, 1.0f));
        cropTile4 = new Entity().addComponent(new CropTileComponent(0f, 1.0f));
        cropTile5 = new Entity().addComponent(new CropTileComponent(1f, 0.0f));
        cropTile6 = new Entity().addComponent(new CropTileComponent(2.5f, 0.5f));
        cropTile7 = new Entity().addComponent(new CropTileComponent(-1.0f, 0.5f));
        cropTile1.create();
        cropTile2.create();
        cropTile3.create();
        cropTile4.create();
        cropTile5.create();
        cropTile6.create();
        cropTile7.create();
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerTimeService(new TimeService());
        MissionManager Mission = new MissionManager();
        ServiceLocator.registerMissionManager(Mission);
    }

    @Test
    public void shouldGiveExpectedGrowthRate() {
        assertEquals(0.5, cropTile1.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(0.27694, cropTile2.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(1.0, cropTile3.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(-1.0, cropTile4.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
        assertEquals(0, cropTile5.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
    }

    @Test
    public void shouldGiveWateredGrowthRate() {
        cropTile1.getEvents().trigger("water", 0.5f);
        cropTile2.getEvents().trigger("water", 0.5f);
        cropTile3.getEvents().trigger("water", 0.5f);
        cropTile4.getEvents().trigger("water", 0.5f);
        cropTile5.getEvents().trigger("water", 0.5f);
        assertEquals(0.27694, cropTile1.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(0.5, cropTile2.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(0.55387, cropTile3.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(0.55387, cropTile4.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(0, cropTile5.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
        cropTile4.getEvents().trigger("water", 0.5f);
        assertEquals(1.0, cropTile4.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        cropTile4.getEvents().trigger("water", 0.5f);
        assertEquals(0.55387, cropTile4.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        Entity dryTile = new Entity().addComponent(new CropTileComponent(0.0f, 1.0f));
        dryTile.create();
        dryTile.getEvents().trigger("water", 1.0f);
        assertEquals(1.0, dryTile.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        dryTile.getEvents().trigger("water", 1.0f);
        assertEquals(-1.0, dryTile.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
    }

    @Test
    public void shouldGiveFertilisedGrowthRate() {
        cropTile1.getEvents().trigger("fertilise");
        cropTile2.getEvents().trigger("fertilise");
        cropTile3.getEvents().trigger("fertilise");
        cropTile4.getEvents().trigger("fertilise");
        cropTile5.getEvents().trigger("fertilise");
        assertEquals(1, cropTile1.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(0.55387, cropTile2.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(2.0, cropTile3.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(-1.0, cropTile4.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
        assertEquals(0, cropTile5.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
        /* Fertilising twice should not do anything */
        cropTile1.getEvents().trigger("fertilise");
        cropTile2.getEvents().trigger("fertilise");
        cropTile3.getEvents().trigger("fertilise");
        cropTile4.getEvents().trigger("fertilise");
        cropTile5.getEvents().trigger("fertilise");
        assertEquals(1, cropTile1.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(0.55387, cropTile2.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(2.0, cropTile3.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(-1.0, cropTile4.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
        assertEquals(0, cropTile5.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
    }

    @Test
    public void testUpdate() {
        DynamicTextureRenderComponent dynamock = mock(DynamicTextureRenderComponent.class);
        cropTile1 = new Entity().addComponent(new CropTileComponent(1f, 0.5f)).addComponent(dynamock);
        cropTile2 = new Entity().addComponent(new CropTileComponent(0.5f, 0.5f)).addComponent(dynamock);
        cropTile3 = new Entity().addComponent(new CropTileComponent(1f, 1.0f)).addComponent(dynamock);
        cropTile4 = new Entity().addComponent(new CropTileComponent(0f, 1.0f)).addComponent(dynamock);
        cropTile5 = new Entity().addComponent(new CropTileComponent(1f, 0.0f)).addComponent(dynamock);
        cropTile6 = new Entity().addComponent(new CropTileComponent(2.5f, 0.5f)).addComponent(dynamock);
        cropTile7 = new Entity().addComponent(new CropTileComponent(-1.0f, 0.5f)).addComponent(dynamock);
        cropTile1.create();
        cropTile2.create();
        cropTile3.create();
        cropTile4.create();
        cropTile5.create();
        cropTile6.create();
        cropTile7.create();
        GameTime gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
        when(gameTime.getDeltaTime()).thenReturn(400f / 1000);
        cropTile1.update();
        cropTile2.update();
        cropTile3.update();
        cropTile4.update();
        cropTile5.update();
        cropTile6.update();
        cropTile7.update();
        assertEquals(0.49958, cropTile1.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(0.26197, cropTile2.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(0.99916, cropTile3.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(-1.0, cropTile4.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
        assertEquals(0, cropTile5.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
        assertEquals(-1, cropTile6.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
        assertEquals(-1, cropTile7.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
        cropTile1.getEvents().trigger("fertilise");
        cropTile2.getEvents().trigger("fertilise");
        cropTile3.getEvents().trigger("fertilise");
        cropTile4.getEvents().trigger("fertilise");
        cropTile5.getEvents().trigger("fertilise");
        cropTile6.getEvents().trigger("fertilise");
        cropTile7.getEvents().trigger("fertilise");
        //tests whether getTexturePath() goes down the correct way
        cropTile1.update();
        cropTile2.update();
        cropTile3.update();
        cropTile4.update();
        cropTile5.update();
        cropTile6.update();
        cropTile7.update();
        assertEquals(0.99663, cropTile1.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(0.49378, cropTile2.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(1.99326, cropTile3.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        assertEquals(-1.0, cropTile4.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
        assertEquals(0, cropTile5.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
        assertEquals(-1, cropTile6.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
        assertEquals(-1, cropTile7.getComponent(CropTileComponent.class).getGrowthRate(), 0.0f);
    }

    @Test
    public void testSetUnoccupiedWhenUnoccupied() {
        EntityService mockEntityService = mock(EntityService.class);
        ServiceLocator.registerEntityService(mockEntityService);
        cropTile1.getComponent(CropTileComponent.class).setUnoccupied();
        cropTile1.getComponent(CropTileComponent.class).setUnoccupied();
        cropTile1.getComponent(CropTileComponent.class).setUnoccupied();
        verify(mockEntityService, never()).unregister(cropTile1);
        cropTile1.getEvents().trigger("fertilise");
        assertEquals(1, cropTile1.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        cropTile1.getComponent(CropTileComponent.class).setUnoccupied();
        assertEquals(0.5, cropTile1.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
    }

    @Test
    public void testSetUnoccupiedWhenOccupied() {
        EntityService mockEntityService = mock(EntityService.class);
        ServiceLocator.registerEntityService(mockEntityService);
        Entity plant = mock(Entity.class);
        plant.addComponent(new PlantComponent(
                1, "name", "type", "desc", 1, 7, 1, cropTile1.getComponent(CropTileComponent.class)));
        Function<CropTileComponent, Entity> plantFactoryMethod = cropTileComponent -> plant;
        cropTile1.getEvents().trigger("plant", plantFactoryMethod);
        verify(mockEntityService).register(plant);
        cropTile1.getComponent(CropTileComponent.class).setUnoccupied();
        cropTile1.getEvents().trigger("plant", plantFactoryMethod);
        verify(mockEntityService, times(2)).register(plant);
        cropTile1.getComponent(CropTileComponent.class).setUnoccupied();
        cropTile1.getEvents().trigger("plant", plantFactoryMethod);
        cropTile1.getEvents().trigger("fertilise");
        assertEquals(1, cropTile1.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        cropTile1.getComponent(CropTileComponent.class).setUnoccupied();
        assertEquals(0.5, cropTile1.getComponent(CropTileComponent.class).getGrowthRate(), 0.00001);
        verify(mockEntityService, times(3)).register(plant);
    }

    @Test
    public void testPlantCrop() {
        EntityService mockEntityService = mock(EntityService.class);
        ServiceLocator.registerEntityService(mockEntityService);
        Entity plant = mock(Entity.class);
        plant.addComponent(new PlantComponent(
                1, "name", "type", "desc", 1, 7, 1, cropTile1.getComponent(CropTileComponent.class)));
        Function<CropTileComponent, Entity> plantFactoryMethod = cropTileComponent -> plant;
        cropTile1.getEvents().trigger("plant", plantFactoryMethod);
        verify(mockEntityService).register(plant);
        //plant should only register once, despite calling plantCrop() twice
        cropTile1.getEvents().trigger("plant", plantFactoryMethod);
        verify(mockEntityService).register(plant);
    }

    @Test
    public void testDestroyTile() {
        EntityService mockEntityService = mock(EntityService.class);
        ServiceLocator.registerEntityService(mockEntityService);
        Entity plant = new Entity().addComponent(new PlantComponent(
                1, "name", "type", "desc", 1, 7, 1, cropTile1.getComponent(CropTileComponent.class)));
        Function<CropTileComponent, Entity> plantFactoryMethod = cropTileComponent -> plant;
        cropTile1.getEvents().trigger("plant", plantFactoryMethod);
        verify(mockEntityService).register(plant);
        cropTile1.getEvents().trigger("destroy");
        verify(mockEntityService, never()).unregister(cropTile1);
        cropTile1.getEvents().trigger("destroy");
        verify(mockEntityService).unregister(cropTile1);
        cropTile2.getEvents().trigger("destroy");
        verify(mockEntityService).unregister(cropTile2);
    }
}
