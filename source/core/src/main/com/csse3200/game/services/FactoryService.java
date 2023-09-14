package com.csse3200.game.services;

import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.PlaceableFactory;
import com.csse3200.game.entities.factories.PlantFactory;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class FactoryService {
    private static final Map<EntityType, Function<Entity, Entity>> npcFactories = Map.of(EntityType.Chicken, NPCFactory::createChicken,
            EntityType.Cow, NPCFactory::createCow, EntityType.Astrolotl, NPCFactory::createAstrolotl,
            EntityType.OxygenEater, NPCFactory::createOxygenEater);

    private static final Map<String, Function<CropTileComponent, Entity>> plantFactories = Map.of(
            "Cosmic Cob", PlantFactory::createCosmicCob,
            "Aloe Vera", PlantFactory::createAloeVera,
            "Hammer Plant", PlantFactory::createHammerPlant,
            "Venus Fly Trap", PlantFactory::createVenusFlyTrap,
            "Nightshade", PlantFactory::createNightshade,
            "Water Weed", PlantFactory::createWaterWeed
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
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("horticultural heater seed", ItemFactory::createHorticulturalHeaterSeed),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("space snapper seed", ItemFactory::createSpaceSnapperSeed),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("tobacco seed", ItemFactory::createTobaccoSeed),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("COW FOOD", ItemFactory::createCowFood),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("egg", ItemFactory::createEgg),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("milk", ItemFactory::createMilk),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Chest", ItemFactory::createChestItem),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Fence", ItemFactory::createFenceItem),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Gate", ItemFactory::createGateItem),
            new AbstractMap.SimpleEntry<String, Supplier<Entity>>("Sprinkler", ItemFactory::createSprinklerItem));

    private static final Map<EntityType, Supplier<Entity>> placeableFactories = Map.ofEntries(
            new AbstractMap.SimpleEntry<EntityType, Supplier<Entity>>(EntityType.Chest, PlaceableFactory::createChest),
            new AbstractMap.SimpleEntry<EntityType, Supplier<Entity>>(EntityType.Fence, PlaceableFactory::createFence),
            new AbstractMap.SimpleEntry<EntityType, Supplier<Entity>>(EntityType.Gate, PlaceableFactory::createGate),
            new AbstractMap.SimpleEntry<EntityType, Supplier<Entity>>(EntityType.Sprinkler, PlaceableFactory::createSprinkler));

    public static Map<String, Function<CropTileComponent, Entity>> getPlantFactories() {
        return plantFactories;
    }

    public static Map<EntityType, Function<Entity, Entity>> getNpcFactories() {
        return npcFactories;
    }

    public static Map<String, Supplier<Entity>> getItemFactories() {
        return itemFactories;
    }

    public static Map<EntityType, Supplier<Entity>> getPlaceableFactories() {
        return placeableFactories;
    }
}
