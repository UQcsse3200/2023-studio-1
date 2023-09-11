package com.csse3200.game.files;

import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainCropTileFactory;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.npc.TamableComponent;
import com.csse3200.game.components.plants.PlantComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.tractor.TractorActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.files.SaveGame;
import com.csse3200.game.services.SaveLoadService;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.utils.Array;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.services.TimeService;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaveLoadServiceTest { 
    @Mock
    private SaveGame.GameState mockGameState;
    @Mock
    private Entity mockPlayer;
    @Mock
    private Entity mockTractor;
    @Mock
    private Entity mockEntity;
    @Mock
    private CropTileComponent mockCropTileComponent;
    @Mock
    private PlantComponent mockPlantComponent;
    @Mock
    private GameMap mockGameMap;

    private SaveLoadService saveLoadService;

    @BeforeEach
    void setUp() {
        saveLoadService = new SaveLoadService();
        Files mockFiles = mock(Files.class);
        Gdx.files = mockFiles;
        FileHandle mockFileHandle = mock(FileHandle.class);


        TimeService mockTimeService = mock(TimeService.class);
        when(mockTimeService.getDay()).thenReturn(5);
        when(mockTimeService.getHour()).thenReturn(12);
        ServiceLocator.registerTimeService(mockTimeService);
        
        GameArea mockGameArea = mock(GameArea.class);
        ServiceLocator.registerGameArea(mockGameArea);
        when(mockGameArea.getPlayer()).thenReturn(mockPlayer);
        when(mockGameArea.getTractor()).thenReturn(mockTractor);

        EntityService mockEntityService = mock(EntityService.class);
        Array<Entity> entityArray = new Array<>();
        entityArray.add(mockEntity);
        entityArray.add(mockTractor, mockPlayer, mockEntity);
        when(mockEntityService.getEntities()).thenReturn(entityArray);
        ServiceLocator.registerEntityService(mockEntityService);
    }



    @Test
    void testValidSaveFileWithNoValidSaveFile() {
        assertFalse(saveLoadService.validSaveFile());
    }
}
