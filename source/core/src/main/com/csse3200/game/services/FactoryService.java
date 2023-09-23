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
import com.csse3200.game.missions.quests.Quest;
import com.csse3200.game.missions.quests.QuestFactory;

public class FactoryService {
    private static final Map<EntityType, Function<Entity, Entity>> npcFactories = Map.of(EntityType.Chicken, NPCFactory::createChicken,
            EntityType.Cow, NPCFactory::createCow, EntityType.Astrolotl, NPCFactory::createAstrolotl,
            EntityType.OxygenEater, NPCFactory::createOxygenEater);

    private static final Map<String, Function<CropTileComponent, Entity>> plantFactories = Map.of(
            "Cosmic Cob", PlantFactory::createCosmicCob,
            "Aloe Vera", PlantFactory::createAloeVera,
            "Hammer Plant", PlantFactory::createHammerPlant,
            "Space Snapper", PlantFactory::createSpaceSnapper,
            "Deadly Nightshade", PlantFactory::createDeadlyNightshade,
            "Atomic Algae", PlantFactory::createAtomicAlgae
    );

    private static final Map<String, Supplier<Entity>> itemFactories = Map.ofEntries(
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("shovel", ItemFactory::createShovel),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("hoe", ItemFactory::createHoe),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("scythe", ItemFactory::createScythe),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("watering_can", ItemFactory::createWateringcan),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("fertiliser", ItemFactory::createFertiliser),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("aloe vera seed", ItemFactory::createAloeVeraSeed),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("atomic algae seed", ItemFactory::createAtomicAlgaeSeed),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("cosmic cob seed", ItemFactory::createCosmicCobSeed),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("deadly nightshade seed", ItemFactory::createDeadlyNightshadeSeed),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("hammer plant seed", ItemFactory::createHammerPlantSeed),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("space snapper seed", ItemFactory::createSpaceSnapperSeed),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("COW FOOD", ItemFactory::createCowFood),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("egg", ItemFactory::createEgg),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("milk", ItemFactory::createMilk),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Chest", ItemFactory::createChestItem),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Fence", ItemFactory::createFenceItem),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Gate", ItemFactory::createGateItem),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Sprinkler", ItemFactory::createSprinklerItem));

    private static final Map<String, Supplier<Entity>> placeableFactories = Map.ofEntries(
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Chest", PlaceableFactory::createChest),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Fence", PlaceableFactory::createFence),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Gate", PlaceableFactory::createGate),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Sprinkler", PlaceableFactory::createSprinkler));

    private static final Map<String, Supplier<Quest>> questFactories = Map.ofEntries(
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>("Haber Hobbyist", QuestFactory::createHaberHobbyist),
            new AbstractMap.SimpleEntry<String, Supplier<Quest>>("Fertiliser Fanatic", QuestFactory::createFertiliserFanatic));

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
