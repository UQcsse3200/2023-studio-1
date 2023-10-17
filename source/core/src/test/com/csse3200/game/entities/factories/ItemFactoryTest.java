package com.csse3200.game.entities.factories;

import com.csse3200.game.components.inventory.InventoryDisplayManager;
import com.csse3200.game.components.items.ItemActions;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemFactoryTest {

    @BeforeEach
    void setUp() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerInventoryDisplayManager(new InventoryDisplayManager(null));

        ResourceService mockResourceService = mock(ResourceService.class);
        when(mockResourceService.getAsset(anyString(), any())).thenReturn(null);
        ServiceLocator.registerResourceService(mockResourceService);
    }

    @Test
    void createBaseItem() {
        PhysicsService physicsService = new PhysicsService();
        ServiceLocator.registerPhysicsService(physicsService);
        Entity baseItem = ItemFactory.createBaseItem();
        assertNotNull(baseItem);
        assertNotNull(baseItem.getComponent(ItemActions.class));
        assertNotNull(baseItem.getComponent(HitboxComponent.class));
        assertNotNull(baseItem.getComponent(PhysicsComponent.class));
    }

    void baseComponentsAssertion(Entity e) {
        assertNotNull(e.getComponent(PhysicsComponent.class));
        assertNotNull(e.getComponent(HitboxComponent.class));
        assertNotNull(e.getComponent(ItemActions.class));
    }

    @Test
    void createShovel() {
        Entity shovel = ItemFactory.createShovel();
        assertEquals(ItemType.SHOVEL, shovel.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(shovel);
    }

    @Test
    void createHoe() {
        Entity hoe = ItemFactory.createHoe();
        assertEquals(ItemType.HOE, hoe.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(hoe);
    }

    @Test
    void createWateringCan() {
        Entity wateringCan = ItemFactory.createWateringcan();
        assertEquals(ItemType.WATERING_CAN, wateringCan.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(wateringCan);
    }

    @Test
    void createScythe() {
        Entity scythe = ItemFactory.createScythe();
        assertEquals(ItemType.SCYTHE, scythe.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(scythe);
    }

    @Test
    void createSword() {
        Entity sword = ItemFactory.createSword();
        assertEquals(ItemType.SWORD, sword.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(sword);
    }

    @Test
    void createGun() {
        Entity gun = ItemFactory.createGun();
        assertEquals(ItemType.GUN, gun.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(gun);
    }

    @Test
    void createMilk() {
        Entity milk = ItemFactory.createMilk();
        assertEquals(ItemType.MILK, milk.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(milk);
    }

    @Test
    void createEgg() {
        Entity egg = ItemFactory.createEgg();
        assertEquals(ItemType.EGG, egg.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(egg);
    }

    @Test
    void createGoldenEgg() {
        Entity goldenEgg = ItemFactory.createGoldenEgg();
        assertEquals(ItemType.EGG, goldenEgg.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(goldenEgg);
    }

    @Test
    void createFertiliser() {
        Entity fertilizer = ItemFactory.createFertiliser();
        assertEquals(ItemType.FERTILISER, fertilizer.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(fertilizer);
    }

    @Test
    void createAloeVeraSeed() {
        Entity aloeVeraSeed = ItemFactory.createAloeVeraSeed();
        assertEquals(ItemType.SEED, aloeVeraSeed.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(aloeVeraSeed);
    }

    @Test
    void createAloeVeraLeaf() {
        Entity aloeVeraLeaf = ItemFactory.createAloeVeraLeaf();
        assertEquals(ItemType.FOOD, aloeVeraLeaf.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(aloeVeraLeaf);
    }

    @Test
    void atomicAlgaeSeed() {
        Entity atomicAlgaeSeed = ItemFactory.createAtomicAlgaeSeed();
        assertEquals(ItemType.SEED, atomicAlgaeSeed.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(atomicAlgaeSeed);
    }

    @Test
    void createCosmicCobSeed() {
        Entity cosmicCobSeed = ItemFactory.createCosmicCobSeed();
        assertEquals(ItemType.SEED, cosmicCobSeed.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(cosmicCobSeed);
    }

    @Test
    void createCosmicCobEar() {
        Entity cosmicCobEar = ItemFactory.createCosmicCobEar();
        assertEquals(ItemType.FOOD, cosmicCobEar.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(cosmicCobEar);
    }

    @Test
    void createDeadlyNightshadeSeed() {
        Entity deadlyNightshadeSeed = ItemFactory.createDeadlyNightshadeSeed();
        assertEquals(ItemType.SEED, deadlyNightshadeSeed.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(deadlyNightshadeSeed);
    }

    @Test
    void createDeadlyNightshadeBerry() {
        Entity deadlyNightshadeBerry = ItemFactory.createDeadlyNightshadeBerry();
        assertEquals(ItemType.FOOD, deadlyNightshadeBerry.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(deadlyNightshadeBerry);
    }

    @Test
    void createHammerPlantSeed() {
        Entity hammerPlantSeed = ItemFactory.createHammerPlantSeed();
        assertEquals(ItemType.SEED, hammerPlantSeed.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(hammerPlantSeed);
    }

    @Test
    void createHammerFlower() {
        Entity hammerFlower = ItemFactory.createHammerFlower();
        assertEquals(ItemType.FOOD, hammerFlower.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(hammerFlower);
    }

    @Test
    void createSpaceSnapperSeed() {
        Entity spaceSnapperSeed = ItemFactory.createSpaceSnapperSeed();
        assertEquals(ItemType.SEED, spaceSnapperSeed.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(spaceSnapperSeed);
    }

    @Test
    void createBeef() {
        Entity beef = ItemFactory.createBeef();
        assertEquals(ItemType.FOOD, beef.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(beef);
    }

    @Test
    void createChickenMeat() {
        Entity chickenMeat = ItemFactory.createChickenMeat();
        assertEquals(ItemType.FOOD, chickenMeat.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(chickenMeat);
    }

    @Test
    void createFenceItem() {
        Entity fence = ItemFactory.createFenceItem();
        assertEquals(ItemType.PLACEABLE, fence.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(fence);
    }

    @Test
    void createGateItem() {
        Entity gate = ItemFactory.createGateItem();
        assertEquals(ItemType.PLACEABLE, gate.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(gate);
    }

    @Test
    void createSprinklerItem() {
        Entity sprinkler = ItemFactory.createSprinklerItem();
        assertEquals(ItemType.PLACEABLE, sprinkler.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(sprinkler);
    }

    @Test
    void createPumpItem() {
        Entity pump = ItemFactory.createPumpItem();
        assertEquals(ItemType.PLACEABLE, pump.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(pump);
    }

    @Test
    void createChestItem() {
        Entity tractor = ItemFactory.createChestItem();
        assertEquals(ItemType.PLACEABLE, tractor.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(tractor);
    }

    @Test
    void createLightItem() {
        Entity light = ItemFactory.createLightItem();
        assertEquals(ItemType.PLACEABLE, light.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(light);
    }

    @Test
    void createShipPartItem() {
        Entity shipPart = ItemFactory.createShipPart();
        assertEquals(ItemType.SHIP_PART, shipPart.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(shipPart);
    }

    @Test
    void createFishingRodItem() {
        Entity fishingRod = ItemFactory.createFishingRod();
        assertEquals(ItemType.FISHING_ROD, fishingRod.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(fishingRod);
    }

    @Test
    void createLavaEelItem() {
        Entity lavaEel = ItemFactory.createLavaEel();
        assertEquals(ItemType.FOOD, lavaEel.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(lavaEel);
    }

    @Test
    void createSalmonItem() {
        Entity salmon = ItemFactory.createSalmon();
        assertEquals(ItemType.FOOD, salmon.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(salmon);
    }

    @Test
    void createYak3Item() {
        Entity yak3 = ItemFactory.createYak3();
        assertEquals(ItemType.FOOD, yak3.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(yak3);
    }

    @Test
    void createNettyItem() {
        Entity netty = ItemFactory.createNetty();
        assertEquals(ItemType.FOOD, netty.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(netty);
    }

    @Test
    void createLola() {
        Entity lola = ItemFactory.createLola();
        assertEquals(ItemType.FOOD, lola.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(lola);
    }

    @Test
    void createLarry() {
        Entity larry = ItemFactory.createLarry();
        assertEquals(ItemType.FOOD, larry.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(larry);
    }

    @Test
    void createBraydan() {
        Entity brayden = ItemFactory.createBraydan();
        assertEquals(ItemType.FOOD, brayden.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(brayden);
    }

    @Test
    void createHarriet() {
        Entity harriet = ItemFactory.createHarry();
        assertEquals(ItemType.FOOD, harriet.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(harriet);
    }

    @Test
    void createMrKrabs() {
        Entity mrKrabs = ItemFactory.createMrKrabs();
        assertEquals(ItemType.FOOD, mrKrabs.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(mrKrabs);
    }

    @Test
    void createPharLap() {
        Entity pharLap = ItemFactory.createPharLap();
        assertEquals(ItemType.FOOD, pharLap.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(pharLap);
    }

    @Test
    void createBryton() {
        Entity bryton = ItemFactory.createBryton();
        assertEquals(ItemType.FOOD, bryton.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(bryton);
    }

    @Test
    void createChurchill() {
        Entity churchill = ItemFactory.createChurchill();
        assertEquals(ItemType.FOOD, churchill.getComponent(ItemComponent.class).getItemType());
        baseComponentsAssertion(churchill);
    }
}
