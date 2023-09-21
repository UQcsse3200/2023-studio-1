package com.csse3200.game.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.components.plants.PlantComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.files.SaveGame;

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
