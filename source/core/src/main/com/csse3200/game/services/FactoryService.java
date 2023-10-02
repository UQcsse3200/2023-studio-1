package com.csse3200.game.services;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.missions.quests.Quest;
import com.csse3200.game.missions.quests.QuestFactory;

public class FactoryService {
    private static final Map<EntityType, Function<Entity, Entity>> npcFactories = Map.of(EntityType.Chicken, NPCFactory::createChicken,
            EntityType.Cow, NPCFactory::createCow, EntityType.Astrolotl, NPCFactory::createAstrolotl,
            EntityType.OxygenEater, NPCFactory::createOxygenEater, EntityType.ShipDebris, ShipDebrisFactory::createShipDebris,
            EntityType.FireFlies, NPCFactory::createFireFlies);

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
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("COW FOOD", ItemFactory::createCowFood),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("egg", ItemFactory::createEgg),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("milk", ItemFactory::createMilk),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Chest", ItemFactory::createChestItem),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Fence", ItemFactory::createFenceItem),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Gate", ItemFactory::createGateItem),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Light", ItemFactory::createLightItem),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Sprinkler", ItemFactory::createSprinklerItem));

    private static final Map<String, Supplier<Entity>> placeableFactories = Map.ofEntries(
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Chest", PlaceableFactory::createChest),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Fence", PlaceableFactory::createFence),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Gate", PlaceableFactory::createGate),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Light", PlaceableFactory::createLight),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Sprinkler", PlaceableFactory::createSprinkler),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Pump", PlaceableFactory::createPump));

    private static final Map<String, Supplier<Quest>> questFactories = Map.ofEntries(
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.firstContactQuestName, QuestFactory::createFirstContactQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.clearingYourMessQuestName, QuestFactory::createClearingYourMessQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.sowingYourFirstSeedsQuestName, QuestFactory::createSowingYourFirstSeedsQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.reapingYourRewardsQuestName, QuestFactory::createReapingYourRewardsQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.makingFriendsQuestName, QuestFactory::createMakingFriendsQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.fertilisingFiestaQuestName, QuestFactory::createFertilisingFiestaQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.aliensAttackQuestName, QuestFactory::createAliensAttackQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.actIMainQuestName, QuestFactory::createActIMainQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.connectionQuestName, QuestFactory::createConnectionQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.homeSickQuestName, QuestFactory::createHomeSickQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.shipRepairsQuestName, QuestFactory::createShipRepairsQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.bringingItAllTogether, QuestFactory::createBringingItAllTogetherQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.actIIMainQuestName, QuestFactory::createActIIMainQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.anImminentThreatQuestName, QuestFactory::createAnImminentThreatQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.airAndAlgaeQuestName, QuestFactory::createAirAndAlgaeQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.stratosphericSentinel, QuestFactory::createStratosphericSentinelQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>(QuestFactory.actIIIMainQuestName, QuestFactory::createActIIIMainQuest),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>("Haber Hobbyist", QuestFactory::createHaberHobbyist),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>("Fertiliser Fanatic", QuestFactory::createFertiliserFanatic),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>("Tractor Go BRRRRRR", QuestFactory::createTractorQuest));

    public static Map<String, Function<CropTileComponent, Entity>> getPlantFactories() {
        return plantFactories;
    }

    public static Map<EntityType, Function<Entity, Entity>> getNpcFactories() {
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
