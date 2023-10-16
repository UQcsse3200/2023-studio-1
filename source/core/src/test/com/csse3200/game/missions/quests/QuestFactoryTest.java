package com.csse3200.game.missions.quests;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.components.player.InventoryComponent;
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
        ServiceLocator.registerTimeService(new TimeService());
        ServiceLocator.registerMissionManager(new MissionManager());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerResourceService(mockResourceService);
        ServiceLocator.registerGameArea(mockGameArea);

        ClimateController climateController = new ClimateController();
        when(mockGameArea.getClimateController()).thenReturn(climateController);

        when(mockResourceService.getAsset(any(), any())).thenReturn(mockTexture);

        Entity playerMock = mock(Entity.class);
        InventoryComponent playerInventoryMock = mock(InventoryComponent.class);
        when(playerInventoryMock.getItemCount(anyString())).thenReturn(0);
        when(playerMock.getComponent(InventoryComponent.class)).thenReturn(playerInventoryMock);
        when(mockGameArea.getPlayer()).thenReturn(playerMock);

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
            itemFactoryMockedStatic.when(ItemFactory::createShovel).thenReturn(mockEntity);

            Quest quest = QuestFactory.createFirstContactQuest();
            assertEquals(QuestFactory.FIRST_CONTACT_QUEST_NAME, quest.getName());
            assertFalse(quest.isCompleted());
        } catch (Exception ignored) {
            fail();
        }
    }

    @Test
    void testCreateClearingYourMessQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createCosmicCobSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHoe).thenReturn(mockEntity);

            Quest quest = QuestFactory.createClearingYourMessQuest();
            assertEquals(QuestFactory.CLEARING_YOUR_MESS_QUEST_NAME, quest.getName());
            assertFalse(quest.isCompleted());
        } catch (Exception ignored) {
            fail();
        }
    }

    @Test
    void testCreateSowingYourFirstSeedsQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createScythe).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createWateringcan).thenReturn(mockEntity);

            Quest quest = QuestFactory.createSowingYourFirstSeedsQuest();
            assertEquals(QuestFactory.SOWING_YOUR_FIRST_SEEDS_QUEST_NAME, quest.getName());
            assertFalse(quest.isCompleted());
        } catch (Exception ignored) {
            fail();
        }
    }

    @Test
    void testCreateReapingYourRewardsQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createSprinklerItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createPumpItem).thenReturn(mockEntity);

            Quest quest = QuestFactory.createReapingYourRewardsQuest();
            assertEquals(QuestFactory.REAPING_YOUR_REWARDS_QUEST_NAME, quest.getName());
            assertFalse(quest.isCompleted());
        } catch (Exception ignored) {
            fail();
        }
    }

    @Test
    void testCreateTractorQuest() {
        try (MockedStatic<TractorFactory> tractorFactoryMockedStatic = mockStatic(TractorFactory.class)) {
            tractorFactoryMockedStatic.when(TractorFactory::createTractor).thenReturn(mockEntity);

            MissionCompleteQuest quest = QuestFactory.createTractorQuest();
            assertEquals(QuestFactory.TRACTOR_GO_BRRRRRR, quest.getName());
            assertFalse(quest.isCompleted());
        } catch (Exception ignored) {
            fail();
        }
    }

    @Test
    void testCreateMakingFriendsQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createFishingRod).thenReturn(mockEntity);

            Quest quest = QuestFactory.createMakingFriendsQuest();
            assertEquals(QuestFactory.MAKING_FRIENDS_QUEST_NAME, quest.getName());
            assertFalse(quest.isCompleted());
        } catch (Exception ignored) {
            fail();
        }
    }

    @Test
    void testCreateFertilisingFiestaQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createSword).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSpaceSnapperSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createFenceItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createGateItem).thenReturn(mockEntity);

            try (MockedStatic<NPCFactory> npcFactoryMockedStatic = mockStatic(NPCFactory.class)) {
                npcFactoryMockedStatic.when(NPCFactory::createOxygenEater).thenReturn(mockEntity);
                npcFactoryMockedStatic.when(NPCFactory::createBat).thenReturn(mockEntity);
                npcFactoryMockedStatic.when(NPCFactory::createDragonfly).thenReturn(mockEntity);

                Quest quest = QuestFactory.createFertilisingFiestaQuest();
                assertEquals(QuestFactory.FERTILISING_FIESTA_QUEST_NAME, quest.getName());
                assertFalse(quest.isCompleted());
            } catch (Exception ignored) {
                fail();
            }
        } catch (Exception ignored) {
            fail();
        }
    }

    @Test
    void testCreateAliensAttackQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createAloeVeraSeed).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createGun).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createHammerPlantSeed).thenReturn(mockEntity);

            Quest quest = QuestFactory.createAliensAttackQuest();
            assertEquals(QuestFactory.ALIENS_ATTACK_QUEST_NAME, quest.getName());
            assertFalse(quest.isCompleted());
        } catch (Exception ignored) {
            fail();
        }
    }

    @Test
    void testCreateActIMainQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createChestItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createLightItem).thenReturn(mockEntity);

            Quest quest = QuestFactory.createActIMainQuest();
            assertEquals(QuestFactory.ACT_I_MAIN_QUEST_NAME, quest.getName());
            assertFalse(quest.isCompleted());
        } catch (Exception ignored) {
            fail();
        }
    }

    @Test
    void testCreateConnectionQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createDeadlyNightshadeSeed).thenReturn(mockEntity);

            Quest quest = QuestFactory.createConnectionQuest();
            assertEquals(QuestFactory.CONNECTION_QUEST_NAME, quest.getName());
            assertFalse(quest.isCompleted());
        } catch (Exception ignored) {
            fail();
        }
    }

    @Test
    void testCreateHomeSickQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createShipPart).thenReturn(mockEntity);

            Quest quest = QuestFactory.createHomeSickQuest();
            assertEquals(QuestFactory.HOME_SICK_QUEST_NAME, quest.getName());
            assertFalse(quest.isCompleted());
        } catch (Exception ignored) {
            fail();
        }
    }

    @Test
    void testCreateShipRepairsQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createClueItem).thenReturn(mockEntity);

            Quest quest = QuestFactory.createShipRepairsQuest();
            assertEquals(QuestFactory.SHIP_REPAIRS_QUEST_NAME, quest.getName());
            assertFalse(quest.isCompleted());
        } catch (Exception ignored) {
            fail();
        }
    }

    @Test
    void testCreateBringingItAllTogetherQuest() {
        Quest quest = QuestFactory.createBringingItAllTogetherQuest();
        assertEquals(QuestFactory.BRINGING_IT_ALL_TOGETHER_QUEST_NAME, quest.getName());
        assertFalse(quest.isCompleted());
    }

    @Test
    void testCreateActIIMainQuest() {
        Quest quest = QuestFactory.createActIIMainQuest();
        assertEquals(QuestFactory.ACT_II_MAIN_QUEST_NAME, quest.getName());
        assertFalse(quest.isCompleted());
    }

    @Test
    void testCreateAnImminentThreatQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createAtomicAlgaeSeed).thenReturn(mockEntity);

            Quest quest = QuestFactory.createAnImminentThreatQuest();
            assertEquals(QuestFactory.AN_IMMINENT_THREAT_QUEST_NAME, quest.getName());
            assertFalse(quest.isCompleted());
        } catch (Exception ignored) {
            fail();
        }
    }

    @Test
    void testCreateAirAndAlgaeQuest() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createAtomicAlgaeSeed).thenReturn(mockEntity);

            Quest quest = QuestFactory.createAirAndAlgaeQuest();
            assertEquals(QuestFactory.AIR_AND_ALGAE_QUEST_NAME, quest.getName());
            assertFalse(quest.isCompleted());
        } catch (Exception ignored) {
            fail();
        }
    }

    @Test
    void testCreateStratosphericSentinelQuest() {
        Quest quest = QuestFactory.createStratosphericSentinelQuest();
        assertEquals(QuestFactory.STRATOSPHERIC_SENTINEL_QUEST_NAME, quest.getName());
        assertFalse(quest.isCompleted());
    }

    @Test
    void testCreateActIIIMainQuest() {
        Quest quest = QuestFactory.createActIIIMainQuest();
        assertEquals(QuestFactory.ACT_III_MAIN_QUEST_NAME, quest.getName());
        assertFalse(quest.isCompleted());
    }

    @Test
    void testCreateHaberHobbyist() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createFertiliser).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSprinklerItem).thenReturn(mockEntity);

            Quest quest = QuestFactory.createHaberHobbyist();
            assertEquals(QuestFactory.HABER_HOBBYIST_QUEST_NAME, quest.getName());
            assertFalse(quest.isCompleted());
        }
    }

    @Test
    void testCreateFertiliserFanatic() {
        try (MockedStatic<ItemFactory> itemFactoryMockedStatic = mockStatic(ItemFactory.class)) {
            itemFactoryMockedStatic.when(ItemFactory::createFertiliser).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createSprinklerItem).thenReturn(mockEntity);
            itemFactoryMockedStatic.when(ItemFactory::createAtomicAlgaeSeed).thenReturn(mockEntity);

            Quest quest = QuestFactory.createFertiliserFanatic();
            assertEquals(QuestFactory.FERTILISER_FANATIC_QUEST_NAME, quest.getName());
            assertFalse(quest.isCompleted());
        }
    }

}