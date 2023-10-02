package com.csse3200.game.missions.quests;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.TractorFactory;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QuestFactoryTest {

    @Mock
    GameTime mockGameTime;
    @Mock
    TimeService mockTimeService;
    @Mock
    MissionManager mockMissionManager;
    @Mock
    ResourceService mockResourceService;
    @Mock
    Texture mockTexture;
    @Mock
    Entity mockEntity;
    @Mock
    SpaceGameArea mockGameArea;
    @Mock
    TextureRenderComponent mockRenderComponent;

    @BeforeEach
    public void init() {
        Gdx.files = mock(Files.class);
        Gdx.gl = mock(GL20.class);
        MockitoAnnotations.openMocks(this);
        ServiceLocator.registerTimeSource(mockGameTime);
        ServiceLocator.registerTimeService(mockTimeService);
        ServiceLocator.registerMissionManager(mockMissionManager);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerResourceService(mockResourceService);
        ServiceLocator.registerGameArea(mockGameArea);

        when(mockResourceService.getAsset(any(), any())).thenReturn(mockTexture);

        TextureData mockTextureData = mock(TextureData.class);
        doNothing().when(mockTexture).load(mockTextureData);
    }
    @AfterEach
    public void reset() {
        ServiceLocator.clear();
    }

    @Test
    void testCreateFirstContactQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createShipPart).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createLightItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHoe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobEar).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createChestItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAtomicAlgaeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createBaseItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerPlantSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraLeaf).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createMilk).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createWateringcan).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createGateItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createScythe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCowFood).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createEgg).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeBerry).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerFlower).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createShovel).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSpaceSnapperSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFertiliser).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFenceItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSprinklerItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createPumpItem).thenReturn(mockEntity);
            try (MockedStatic<NPCFactory> npcFactoryMockedStatic = mockStatic(NPCFactory.class)) {
                npcFactoryMockedStatic.when(() -> NPCFactory.createOxygenEater(any())).thenReturn(mockEntity);
                Quest quest = QuestFactory.createFirstContactQuest();
                assertEquals(quest.getName(), QuestFactory.FIRST_CONTACT_QUEST_NAME);
                assertTrue(quest.isCompleted());
            }
        }
    }

    @Test
    void testCreateClearingYourMessQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createShipPart).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createLightItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHoe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobEar).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createChestItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAtomicAlgaeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createBaseItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerPlantSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraLeaf).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createMilk).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createWateringcan).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createGateItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createScythe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCowFood).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createEgg).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeBerry).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerFlower).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createShovel).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSpaceSnapperSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFertiliser).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFenceItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSprinklerItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createPumpItem).thenReturn(mockEntity);
            try (MockedStatic<NPCFactory> npcFactoryMockedStatic = mockStatic(NPCFactory.class)) {
                npcFactoryMockedStatic.when(() -> NPCFactory.createOxygenEater(any())).thenReturn(mockEntity);
                Quest quest = QuestFactory.createClearingYourMessQuest();
                assertEquals(quest.getName(), QuestFactory.CLEARING_YOUR_MESS_QUEST_NAME);
                assertFalse(quest.isCompleted());
            }
        }
    }

    @Test
    void testCreateSowingYourFirstSeedsQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createShipPart).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createLightItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHoe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobEar).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createChestItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAtomicAlgaeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createBaseItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerPlantSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraLeaf).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createMilk).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createWateringcan).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createGateItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createScythe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCowFood).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createEgg).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeBerry).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerFlower).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createShovel).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSpaceSnapperSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFertiliser).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFenceItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSprinklerItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createPumpItem).thenReturn(mockEntity);
            try (MockedStatic<NPCFactory> npcFactoryMockedStatic = mockStatic(NPCFactory.class)) {
                npcFactoryMockedStatic.when(() -> NPCFactory.createOxygenEater(any())).thenReturn(mockEntity);
                Quest quest = QuestFactory.createSowingYourFirstSeedsQuest();
                assertEquals(quest.getName(), QuestFactory.SOWING_YOUR_FIRST_SEEDS_QUEST_NAME);
                assertFalse(quest.isCompleted());
            }
        }
    }

    @Test
    void testCreateReapingYourRewardsQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createShipPart).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createLightItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHoe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobEar).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createChestItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAtomicAlgaeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createBaseItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerPlantSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraLeaf).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createMilk).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createWateringcan).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createGateItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createScythe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCowFood).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createEgg).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeBerry).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerFlower).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createShovel).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSpaceSnapperSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFertiliser).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFenceItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSprinklerItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createPumpItem).thenReturn(mockEntity);
            try (MockedStatic<NPCFactory> npcFactoryMockedStatic = mockStatic(NPCFactory.class)) {
                npcFactoryMockedStatic.when(() -> NPCFactory.createOxygenEater(any())).thenReturn(mockEntity);
                Quest quest = QuestFactory.createReapingYourRewardsQuest();
                assertEquals(quest.getName(), QuestFactory.REAPING_YOUR_REWARDS_QUEST_NAME);
                assertFalse(quest.isCompleted());
            }
        }
    }

    @Test
    void testCreateTractorQuest() {
        try (MockedStatic<TractorFactory> tractorFactoryMockedStatic = mockStatic(TractorFactory.class)) {
            tractorFactoryMockedStatic.when(() -> TractorFactory.createTractor(any())).thenReturn(mockEntity);
            MissionCompleteQuest quest = QuestFactory.createTractorQuest();
            assertEquals(quest.getName(), QuestFactory.TRACTOR_GO_BRRRRRR);
            assertFalse(quest.isCompleted());
            }
    }

    @Test
    void testCreateMakingFriendsQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createShipPart).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createLightItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHoe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobEar).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createChestItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAtomicAlgaeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createBaseItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerPlantSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraLeaf).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createMilk).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createWateringcan).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createGateItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createScythe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCowFood).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createEgg).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeBerry).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerFlower).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createShovel).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSpaceSnapperSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFertiliser).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFenceItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSprinklerItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createPumpItem).thenReturn(mockEntity);
            try (MockedStatic<NPCFactory> npcFactoryMockedStatic = mockStatic(NPCFactory.class)) {
                npcFactoryMockedStatic.when(() -> NPCFactory.createOxygenEater(any())).thenReturn(mockEntity);
                Quest quest = QuestFactory.createMakingFriendsQuest();
                assertEquals(quest.getName(), QuestFactory.MAKING_FRIENDS_QUEST_NAME);
                assertFalse(quest.isCompleted());
            }
        }
    }

    @Test
    void testCreateFertilisingFiestaQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createShipPart).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createLightItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHoe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobEar).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createChestItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAtomicAlgaeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createBaseItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerPlantSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraLeaf).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createMilk).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createWateringcan).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createGateItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createScythe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCowFood).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createEgg).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeBerry).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerFlower).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createShovel).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSpaceSnapperSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFertiliser).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFenceItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSprinklerItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createPumpItem).thenReturn(mockEntity);
            try (MockedStatic<NPCFactory> npcFactoryMockedStatic = mockStatic(NPCFactory.class)) {
                npcFactoryMockedStatic.when(() -> NPCFactory.createOxygenEater(any())).thenReturn(mockEntity);
                Quest quest = QuestFactory.createFertilisingFiestaQuest();
                assertEquals(quest.getName(), QuestFactory.FERTILISING_FIESTA_QUEST_NAME);
                assertFalse(quest.isCompleted());
            }
        }
    }

    @Test
    void testCreateAliensAttackQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createShipPart).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createLightItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHoe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobEar).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createChestItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAtomicAlgaeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createBaseItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerPlantSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraLeaf).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createMilk).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createWateringcan).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createGateItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createScythe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCowFood).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createEgg).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeBerry).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerFlower).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createShovel).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSpaceSnapperSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFertiliser).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFenceItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSprinklerItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createPumpItem).thenReturn(mockEntity);
            try (MockedStatic<NPCFactory> npcFactoryMockedStatic = mockStatic(NPCFactory.class)) {
                npcFactoryMockedStatic.when(() -> NPCFactory.createOxygenEater(any())).thenReturn(mockEntity);
                Quest quest = QuestFactory.createAliensAttackQuest();
                assertEquals(quest.getName(), QuestFactory.ALIENS_ATTACK_QUEST_NAME);
                assertFalse(quest.isCompleted());
            }
        }
    }

    @Test
    void testCreateActIMainQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createShipPart).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createLightItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHoe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobEar).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createChestItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAtomicAlgaeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createBaseItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerPlantSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraLeaf).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createMilk).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createWateringcan).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createGateItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createScythe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCowFood).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createEgg).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeBerry).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerFlower).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createShovel).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSpaceSnapperSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFertiliser).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFenceItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSprinklerItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createPumpItem).thenReturn(mockEntity);
            try (MockedStatic<NPCFactory> npcFactoryMockedStatic = mockStatic(NPCFactory.class)) {
                npcFactoryMockedStatic.when(() -> NPCFactory.createOxygenEater(any())).thenReturn(mockEntity);
                Quest quest = QuestFactory.createActIMainQuest();
                assertEquals(quest.getName(), QuestFactory.ACT_I_MAIN_QUEST_NAME);
                assertFalse(quest.isCompleted());
            }
        }
    }

    @Test
    void testCreateConnectionQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createShipPart).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createLightItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHoe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobEar).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createChestItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAtomicAlgaeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createBaseItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerPlantSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraLeaf).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createMilk).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createWateringcan).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createGateItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createScythe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCowFood).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createEgg).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeBerry).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerFlower).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createShovel).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSpaceSnapperSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFertiliser).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFenceItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSprinklerItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createPumpItem).thenReturn(mockEntity);
            try (MockedStatic<NPCFactory> npcFactoryMockedStatic = mockStatic(NPCFactory.class)) {
                npcFactoryMockedStatic.when(() -> NPCFactory.createOxygenEater(any())).thenReturn(mockEntity);
                Quest quest = QuestFactory.createConnectionQuest();
                assertEquals(quest.getName(), QuestFactory.CONNECTION_QUEST_NAME);
                assertTrue(quest.isCompleted());
            }
        }
    }

    @Test
    void testCreateHomeSickQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createShipPart).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createLightItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHoe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobEar).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createChestItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAtomicAlgaeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createBaseItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerPlantSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraLeaf).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createMilk).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createWateringcan).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createGateItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createScythe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCowFood).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createEgg).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeBerry).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerFlower).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createShovel).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSpaceSnapperSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFertiliser).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFenceItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSprinklerItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createPumpItem).thenReturn(mockEntity);
            try (MockedStatic<NPCFactory> npcFactoryMockedStatic = mockStatic(NPCFactory.class)) {
                npcFactoryMockedStatic.when(() -> NPCFactory.createOxygenEater(any())).thenReturn(mockEntity);
                Quest quest = QuestFactory.createHomeSickQuest();
                assertEquals(quest.getName(), QuestFactory.HOME_SICK_QUEST_NAME);
                assertFalse(quest.isCompleted());
            }
        }
    }

    @Test
    void testCreateShipRepairsQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createShipPart).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createLightItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHoe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobEar).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createChestItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAtomicAlgaeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createBaseItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerPlantSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraLeaf).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createMilk).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createWateringcan).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createGateItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createScythe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCowFood).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createEgg).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeBerry).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerFlower).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createShovel).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSpaceSnapperSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFertiliser).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFenceItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSprinklerItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createPumpItem).thenReturn(mockEntity);
            try (MockedStatic<NPCFactory> npcFactoryMockedStatic = mockStatic(NPCFactory.class)) {
                npcFactoryMockedStatic.when(() -> NPCFactory.createOxygenEater(any())).thenReturn(mockEntity);
                Quest quest = QuestFactory.createShipRepairsQuest();
                assertEquals(quest.getName(), QuestFactory.SHIP_REPAIRS_QUEST_NAME);
                assertFalse(quest.isCompleted());
            }
        }
    }

    @Test
    void testCreateBringingItAllTogetherQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createShipPart).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createLightItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHoe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobEar).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createChestItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAtomicAlgaeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createBaseItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerPlantSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraLeaf).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createMilk).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createWateringcan).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createGateItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createScythe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCowFood).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createEgg).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeBerry).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerFlower).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createShovel).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSpaceSnapperSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFertiliser).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFenceItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSprinklerItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createPumpItem).thenReturn(mockEntity);
            try (MockedStatic<NPCFactory> npcFactoryMockedStatic = mockStatic(NPCFactory.class)) {
                npcFactoryMockedStatic.when(() -> NPCFactory.createOxygenEater(any())).thenReturn(mockEntity);
                Quest quest = QuestFactory.createBringingItAllTogetherQuest();
                assertEquals(quest.getName(), QuestFactory.BRINGING_IT_ALL_TOGETHER_QUEST_NAME);
                assertFalse(quest.isCompleted());
            }
        }
    }

    @Test
    void testCreateActIIMainQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createShipPart).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createLightItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHoe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobEar).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createChestItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAtomicAlgaeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createBaseItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerPlantSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraLeaf).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createMilk).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createWateringcan).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createGateItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createScythe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCowFood).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createEgg).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeBerry).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerFlower).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createShovel).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSpaceSnapperSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFertiliser).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFenceItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSprinklerItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createPumpItem).thenReturn(mockEntity);
            try (MockedStatic<NPCFactory> npcFactoryMockedStatic = mockStatic(NPCFactory.class)) {
                npcFactoryMockedStatic.when(() -> NPCFactory.createOxygenEater(any())).thenReturn(mockEntity);
                Quest quest = QuestFactory.createActIIMainQuest();
                assertEquals(quest.getName(), QuestFactory.ACT_II_MAIN_QUEST_NAME);
                assertFalse(quest.isCompleted());
            }
        }
    }

    @Test
    void testCreateAnImminentThreatQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createShipPart).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createLightItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHoe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobEar).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createChestItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAtomicAlgaeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createBaseItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerPlantSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraLeaf).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createMilk).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createWateringcan).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createGateItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createScythe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCowFood).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createEgg).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeBerry).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerFlower).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createShovel).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSpaceSnapperSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFertiliser).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFenceItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSprinklerItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createPumpItem).thenReturn(mockEntity);
            try (MockedStatic<NPCFactory> npcFactoryMockedStatic = mockStatic(NPCFactory.class)) {
                npcFactoryMockedStatic.when(() -> NPCFactory.createOxygenEater(any())).thenReturn(mockEntity);
                Quest quest = QuestFactory.createAnImminentThreatQuest();
                assertEquals(quest.getName(), QuestFactory.AN_IMMINENT_THREAT_QUEST_NAME);
                assertTrue(quest.isCompleted());
            }
        }
    }

    @Test
    void testCreateAirAndAlgaeQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createShipPart).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createLightItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHoe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobEar).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createChestItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAtomicAlgaeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createBaseItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerPlantSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraLeaf).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createMilk).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createWateringcan).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createGateItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createScythe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCowFood).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createEgg).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeBerry).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerFlower).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createShovel).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSpaceSnapperSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFertiliser).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFenceItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSprinklerItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createPumpItem).thenReturn(mockEntity);
            try (MockedStatic<NPCFactory> npcFactoryMockedStatic = mockStatic(NPCFactory.class)) {
                npcFactoryMockedStatic.when(() -> NPCFactory.createOxygenEater(any())).thenReturn(mockEntity);
                Quest quest = QuestFactory.createAirAndAlgaeQuest();
                assertEquals(quest.getName(), QuestFactory.AIR_AND_ALGAE_QUEST_NAME);
                assertFalse(quest.isCompleted());
            }
        }
    }

    @Test
    void testCreateStratosphericSentinelQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createShipPart).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createLightItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHoe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobEar).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createChestItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAtomicAlgaeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createBaseItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerPlantSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraLeaf).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createMilk).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createWateringcan).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createGateItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createScythe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCowFood).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createEgg).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeBerry).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerFlower).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createShovel).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSpaceSnapperSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFertiliser).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFenceItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSprinklerItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createPumpItem).thenReturn(mockEntity);
            try (MockedStatic<NPCFactory> npcFactoryMockedStatic = mockStatic(NPCFactory.class)) {
                npcFactoryMockedStatic.when(() -> NPCFactory.createOxygenEater(any())).thenReturn(mockEntity);
                Quest quest = QuestFactory.createStratosphericSentinelQuest();
                assertEquals(quest.getName(), QuestFactory.STRATOSPHERIC_SENTINEL_QUEST_NAME);
                assertFalse(quest.isCompleted());
            }
        }
    }

    @Test
    void testCreateActIIIMainQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createShipPart).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createLightItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHoe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobEar).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createChestItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAtomicAlgaeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createBaseItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerPlantSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraLeaf).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createMilk).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createWateringcan).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createGateItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createScythe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCowFood).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createEgg).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeBerry).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerFlower).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createShovel).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSpaceSnapperSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFertiliser).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFenceItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSprinklerItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createPumpItem).thenReturn(mockEntity);
            try (MockedStatic<NPCFactory> npcFactoryMockedStatic = mockStatic(NPCFactory.class)) {
                npcFactoryMockedStatic.when(() -> NPCFactory.createOxygenEater(any())).thenReturn(mockEntity);
                Quest quest = QuestFactory.createActIIIMainQuest();
                assertEquals(quest.getName(), QuestFactory.ACT_III_MAIN_QUEST_NAME);
                assertFalse(quest.isCompleted());
            }
        }
    }

    @Test
    void testCreateHaberHobbyist() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createShipPart).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createLightItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHoe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobEar).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createChestItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAtomicAlgaeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createBaseItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerPlantSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraLeaf).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createMilk).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createWateringcan).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createGateItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createScythe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCowFood).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createEgg).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeBerry).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerFlower).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createShovel).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSpaceSnapperSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFertiliser).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFenceItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSprinklerItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createPumpItem).thenReturn(mockEntity);
            try (MockedStatic<NPCFactory> npcFactoryMockedStatic = mockStatic(NPCFactory.class)) {
                npcFactoryMockedStatic.when(() -> NPCFactory.createOxygenEater(any())).thenReturn(mockEntity);
                Quest quest = QuestFactory.createHaberHobbyist();
                assertEquals(quest.getName(), "Haber Hobbyist");
                assertFalse(quest.isCompleted());
            }
        }
    }

    @Test
    void testCreateFertiliserFanatic() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createShipPart).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createLightItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHoe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobEar).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createChestItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAtomicAlgaeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createBaseItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerPlantSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraLeaf).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createMilk).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createWateringcan).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createGateItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createScythe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createCowFood).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createEgg).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeBerry).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerFlower).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createShovel).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSpaceSnapperSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFertiliser).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFenceItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSprinklerItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createPumpItem).thenReturn(mockEntity);
            try (MockedStatic<NPCFactory> npcFactoryMockedStatic = mockStatic(NPCFactory.class)) {
                npcFactoryMockedStatic.when(() -> NPCFactory.createOxygenEater(any())).thenReturn(mockEntity);
                Quest quest = QuestFactory.createFertiliserFanatic();
                assertEquals(quest.getName(), "Fertiliser Fanatic");
                assertFalse(quest.isCompleted());
            }
        }
    }
}