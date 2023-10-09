package com.csse3200.game.services;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.PlaceableFactory;
import com.csse3200.game.entities.factories.PlantFactory;
import com.csse3200.game.entities.factories.ShipDebrisFactory;
import com.csse3200.game.missions.quests.Quest;
import com.csse3200.game.missions.quests.QuestFactory;

public class FactoryService {
    private static final Map<EntityType, Supplier<Entity>> npcFactories = Map.ofEntries(
            new AbstractMap.SimpleEntry<EntityType, Supplier<Entity>>(EntityType.CHICKEN, NPCFactory::createChicken),
            new AbstractMap.SimpleEntry<EntityType, Supplier<Entity>>(EntityType.COW, NPCFactory::createCow),
            new AbstractMap.SimpleEntry<EntityType, Supplier<Entity>>(EntityType.ASTROLOTL, NPCFactory::createAstrolotl),
            new AbstractMap.SimpleEntry<EntityType, Supplier<Entity>>(EntityType.BAT, NPCFactory::createBat),
            new AbstractMap.SimpleEntry<EntityType, Supplier<Entity>>(EntityType.OXYGEN_EATER, NPCFactory::createOxygenEater),
            new AbstractMap.SimpleEntry<EntityType, Supplier<Entity>>(EntityType.SHIP_DEBRIS, ShipDebrisFactory::createShipDebris),
            new AbstractMap.SimpleEntry<EntityType, Supplier<Entity>>(EntityType.FIRE_FLIES, NPCFactory::createFireFlies),
            new AbstractMap.SimpleEntry<EntityType, Supplier<Entity>>(EntityType.DRAGONFLY, NPCFactory::createDragonfly));

    private static final Map<String, Function<CropTileComponent, Entity>> plantFactories = Map.of(
            "Cosmic Cob", PlantFactory::createCosmicCob,
            "Aloe Vera", PlantFactory::createAloeVera,
            "Hammer Plant", PlantFactory::createHammerPlant,
            "Space Snapper", PlantFactory::createSpaceSnapper,
            "Deadly Nightshade", PlantFactory::createDeadlyNightshade,
            "Atomic Algae", PlantFactory::createAtomicAlgae,
            "Test", PlantFactory::createTest
    );

    private static final Map<String, Supplier<Entity>> itemFactories = Map.ofEntries(
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("shovel", ItemFactory::createShovel),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("hoe", ItemFactory::createHoe),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("scythe", ItemFactory::createScythe),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("watering_can", ItemFactory::createWateringcan),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("fertiliser", ItemFactory::createFertiliser),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Aloe Vera Seeds", ItemFactory::createAloeVeraSeed),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Aloe Vera Leaf", ItemFactory::createAloeVeraLeaf),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Atomic Algae Seeds", ItemFactory::createAtomicAlgaeSeed),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Cosmic Cob Seeds", ItemFactory::createCosmicCobSeed),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Ear of Cosmic Cob", ItemFactory::createCosmicCobEar),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Deadly Nightshade Seeds", ItemFactory::createDeadlyNightshadeSeed),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Nightshade Berry", ItemFactory::createDeadlyNightshadeBerry),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Hammer Plant Seeds", ItemFactory::createHammerPlantSeed),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Hammer Flower", ItemFactory::createHammerFlower),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Space Snapper Seeds", ItemFactory::createSpaceSnapperSeed),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("egg", ItemFactory::createEgg),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("milk", ItemFactory::createMilk),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("CHEST", ItemFactory::createChestItem),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("FENCE", ItemFactory::createFenceItem),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("GATE", ItemFactory::createGateItem),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("LIGHT", ItemFactory::createLightItem),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("SPRINKLER", ItemFactory::createSprinklerItem),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("gun", ItemFactory::createGun),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("sword", ItemFactory::createSword),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("PUMP", ItemFactory::createPumpItem),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Fishing Rod", ItemFactory::createFishingRod),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Lava Eel", ItemFactory::createLavaEel),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Salmon", ItemFactory::createSalmon),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("map", ItemFactory::createMapItem),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Netty", ItemFactory::createNetty),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Yak3", ItemFactory::createYak3),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Lola", ItemFactory::createLola),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Larry", ItemFactory::createLarry),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Braydan", ItemFactory::createBraydan),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Harry", ItemFactory::createHarry),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Mr Krabs", ItemFactory::createMrKrabs),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Phar Lap", ItemFactory::createPharLap),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Bryton", ItemFactory::createBryton),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Sanders", ItemFactory::createSanders),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Churchill", ItemFactory::createChurchill),
		    new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Golden Fish", ItemFactory::createGoldenFish)
            );

    private static final Map<String, Supplier<Entity>> placeableFactories = Map.ofEntries(
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("CHEST", PlaceableFactory::createChest),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("FENCE", PlaceableFactory::createFence),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("GATE", PlaceableFactory::createGate),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("LIGHT", PlaceableFactory::createLight),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("SPRINKLER", PlaceableFactory::createSprinkler),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("PUMP", PlaceableFactory::createPump));

    private static final Map<String, Supplier<Quest>> questFactories = Map.ofEntries(
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.FIRST_CONTACT_QUEST_NAME, QuestFactory::createFirstContactQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.CLEARING_YOUR_MESS_QUEST_NAME, QuestFactory::createClearingYourMessQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.SOWING_YOUR_FIRST_SEEDS_QUEST_NAME, QuestFactory::createSowingYourFirstSeedsQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.REAPING_YOUR_REWARDS_QUEST_NAME, QuestFactory::createReapingYourRewardsQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.MAKING_FRIENDS_QUEST_NAME, QuestFactory::createMakingFriendsQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.FERTILISING_FIESTA_QUEST_NAME, QuestFactory::createFertilisingFiestaQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.ALIENS_ATTACK_QUEST_NAME, QuestFactory::createAliensAttackQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.ACT_I_MAIN_QUEST_NAME, QuestFactory::createActIMainQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.CONNECTION_QUEST_NAME, QuestFactory::createConnectionQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.HOME_SICK_QUEST_NAME, QuestFactory::createHomeSickQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.SHIP_REPAIRS_QUEST_NAME, QuestFactory::createShipRepairsQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.BRINGING_IT_ALL_TOGETHER_QUEST_NAME, QuestFactory::createBringingItAllTogetherQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.ACT_II_MAIN_QUEST_NAME, QuestFactory::createActIIMainQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.AN_IMMINENT_THREAT_QUEST_NAME, QuestFactory::createAnImminentThreatQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.AIR_AND_ALGAE_QUEST_NAME, QuestFactory::createAirAndAlgaeQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.STRATOSPHERIC_SENTINEL_QUEST_NAME, QuestFactory::createStratosphericSentinelQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.ACT_III_MAIN_QUEST_NAME, QuestFactory::createActIIIMainQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>("Haber Hobbyist", QuestFactory::createHaberHobbyist),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>("Fertiliser Fanatic", QuestFactory::createFertiliserFanatic),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.FISHING_QUEST, QuestFactory::createFishingQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.TRACTOR_GO_BRRRRRR, QuestFactory::createTractorQuest));

    public static Map<String, Function<CropTileComponent, Entity>> getPlantFactories() {
        return plantFactories;
    }

    public static Map<EntityType, Supplier<Entity>> getNpcFactories() {
        return npcFactories;
    }

    public static Map<String, Supplier<Entity>> getItemFactories() {
        return itemFactories;
    }

    public static Map<String, Supplier<Entity>> getPlaceableFactories() {
        return placeableFactories;
    }

    public static Map<String, Supplier<Quest>> getQuests() {
        return questFactories;
    }
}
