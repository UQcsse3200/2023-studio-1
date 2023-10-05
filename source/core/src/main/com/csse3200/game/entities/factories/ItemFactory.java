package com.csse3200.game.entities.factories;
import com.csse3200.game.components.items.ItemActions;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.components.items.WateringCanLevelComponent;
import com.csse3200.game.components.items.ClueComponent;
import com.csse3200.game.components.items.CoordinatesDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.FactoryService;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * Factory to create an item
 */
public class ItemFactory {

  /**
   * Map of item names to their supplier function.
   *
   * <p>
   * This unmodifiable (i.e., read-only) map provides a means of acquiring the
   * factory method
   * for an item based on its unique item name.
   */
  private static final Map<String, Supplier<Entity>> itemSuppliers = FactoryService.getItemFactories();

  /**
   * Returns the supplier function for the item with the given unique name.
   *
   * @param itemName Name of the item to get the supplier of
   * @return Supplier for the item
   * @throws NoSuchElementException If an item with the given name does not exist
   */
  public static Supplier<Entity> getItemSupplier(String itemName) {
    Supplier<Entity> itemSupplier = itemSuppliers.get(itemName);
    if (itemSupplier == null) {
      throw new NoSuchElementException(String.format("Item with the name '%s' does not exist", itemName));
    }
    return itemSupplier;
  }


  public static Entity createBaseItem() {
    return new Entity(EntityType.ITEM)
            .addComponent(new PhysicsComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ITEM))
            .addComponent(new ItemActions());
  }


  /**
   * Creates a shovel item
   * 
   * @return shovel
   */
  public static Entity createShovel() {
    return createBaseItem()
            .addComponent(new TextureRenderComponent("images/tool_shovel.png"))
            .addComponent(new ItemComponent("shovel", ItemType.SHOVEL, "Shovel for removing items", "images/tool_shovel.png"));
  }

  /**
   * Creates a hoe item
   * 
   * @return hoe
   */
  public static Entity createHoe() {
    return createBaseItem()
            .addComponent(new TextureRenderComponent("images/tool_hoe.png"))
            .addComponent(new ItemComponent("hoe", ItemType.HOE, "images/tool_hoe.png"));
  }


  /**
   * Creates a watering-can item
   * 
   * @return watering can
   */
  public static Entity createWateringcan() {
    return createBaseItem()
            .addComponent(new TextureRenderComponent("images/tool_watering_can.png"))
            .addComponent(new ItemComponent("watering_can", ItemType.WATERING_CAN, "images/tool_watering_can.png"))
            .addComponent(new WateringCanLevelComponent(150));
  }


  /**
   * Creates a scythe item
   * 
   * @return scythe
   */
  public static Entity createScythe() {
    return createBaseItem()
            .addComponent(new TextureRenderComponent("images/tool_scythe.png"))
            .addComponent(new ItemComponent("scythe", ItemType.SCYTHE, "images/tool_scythe.png"));
  }

  /**
   * Creates a sword item
   *
   * @return sword
   */
  public static Entity createSword() {
    return createBaseItem()
            .addComponent(new TextureRenderComponent("images/tool_sword.png"))
            .addComponent(new ItemComponent("sword", ItemType.SWORD, "images/tool_sword.png"));
  }

  /**
   * Creates a gun item
   *
   * @return gun
   */
  public static Entity createGun() {
    return createBaseItem()
            .addComponent(new TextureRenderComponent("images/tool_gun.png"))
            .addComponent(new ItemComponent("gun", ItemType.GUN, "images/tool_gun.png"));
  }

  /**
   * Creates a milk item
   *
   * @return milk item
   */
  public static Entity createMilk() {
    Entity milk = createBaseItem()
            .addComponent(new TextureRenderComponent("images/animals/milk.png"))
            .addComponent(new ItemComponent("milk",
                    ItemType.MILK, "images/animals/milk.png"));
    milk.scaleHeight(0.75f);
    return milk;
  }

  /**
   * Creates an egg item
   *
   * @return egg item
   */
  public static Entity createEgg() {
    Entity egg = createBaseItem()
            .addComponent(new TextureRenderComponent("images/animals/egg.png"))
            .addComponent(new ItemComponent("egg", ItemType.EGG,
                    "images/animals/egg.png"));
    egg.scaleHeight(0.75f);
    return egg;
  }

  /**
   * Creates a golden egg item
   *
   * @return golden egg item
   */
  public static Entity createGoldenEgg() {
    Entity egg = createBaseItem()
            .addComponent(new TextureRenderComponent("images/animals/golden_egg.png"))
            .addComponent(new ItemComponent("golden egg", ItemType.EGG,
                    "images/animals/golden_egg.png"));
    egg.scaleHeight(0.75f);
    return egg;
  }

  /**
   * Creates a Map Clue item
   *
   * @return Map Item
   */
  public static Entity createMapItem() {
    ClueComponent clueComponent = new ClueComponent();
    return createBaseItem()
            .addComponent(new TextureRenderComponent("images/ship/ship_clue.png"))
            .addComponent(new ItemComponent("map", ItemType.CLUE_ITEM, "images/ship/ship_clue.png"))
            .addComponent(clueComponent)
            .addComponent(new CoordinatesDisplay(clueComponent));
  }

   /**
   * Creates a fertiliser item
   *
   * @return fertiliser
   */
  public static Entity createFertiliser() {
return createBaseItem()
            .addComponent(new TextureRenderComponent("images/fertiliser.png"))
            .addComponent(new ItemComponent("fertiliser", ItemType.FERTILISER,
                    "images/fertiliser.png"));
  }

  /**
   * Creates an 'aloe vera seed' item
   *
   * @return aloe vera seed
   */
  public static Entity createAloeVeraSeed() {
return createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/aloe_vera/seedbag.png"))
            .addComponent(new ItemComponent("Aloe Vera Seeds", ItemType.SEED,
                    "Seed of Aloe Vera", "images/plants/aloe_vera/seedbag.png"));

  }

  /**
   * Creates an 'Aloe Vera Leaf' item.
   *
   * @return Aloe Vera Leaf
   */
  public static Entity createAloeVeraLeaf() {
  return createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/aloe_vera/item_drop.png"))
            .addComponent(new ItemComponent("Aloe Vera Leaf", ItemType.FOOD,
                    "The gel oozing from this leaf has mystical healing properties",
                    "images/plants/aloe_vera/item_drop.png"));

  }

  /**
   * Creates an 'atomic algae seed' item
   *
   * @return atomic algae seed
   */
  public static Entity createAtomicAlgaeSeed() {
    return createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/atomic_algae/seedbag.png"))
            .addComponent(new ItemComponent("Atomic Algae Seeds", ItemType.SEED,
                    "Seed of Atomic Algae", "images/plants/atomic_algae/seedbag.png"));

  }

  /**
   * Creates a 'cosmic cob seed' item
   *
   * @return cosmic cob seed
   */
  public static Entity createCosmicCobSeed() {
return createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/cosmic_cob/seedbag.png"))
            .addComponent(new ItemComponent("Cosmic Cob Seeds", ItemType.SEED,
                    "Seed of Cosmic Cob", "images/plants/cosmic_cob/seedbag.png"));

  }

  /**
   * Creates an 'Ear of Cosmic Cob' item.
   *
   * @return Ear of Cosmic Cob
   */
  public static Entity createCosmicCobEar() {
 return createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/cosmic_cob/item_drop.png"))
            .addComponent(new ItemComponent("Ear of Cosmic Cob", ItemType.FOOD,
                    "Nutritious space corn essential for surviving out in space",
                    "images/plants/cosmic_cob/item_drop.png"));

  }

  /**
   * Creates a 'deadly nightshade seed' item
   *
   * @return deadly nightshade seed
   */
  public static Entity createDeadlyNightshadeSeed() {
 return createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/deadly_nightshade/seedbag.png"))
            .addComponent(new ItemComponent("Deadly Nightshade Seeds", ItemType.SEED,
                    "Seed of Deadly Nightshade", "images/plants/deadly_nightshade/seedbag.png"));

  }

  /**
   * Creates a 'Nightshade Berry' item.
   *
   * @return Nightshade Berry
   */
  public static Entity createDeadlyNightshadeBerry() {
return createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/deadly_nightshade/item_drop.png"))
            .addComponent(new ItemComponent("Nightshade Berry", ItemType.FOOD,
                    "Deadly poisonous to humans, but the local wildlife find it delectable",
                    "images/plants/deadly_nightshade/item_drop.png"));

  }

  /**
   * Creates a 'hammer plant seed' item
   *
   * @return hammer plant seed
   */
  public static Entity createHammerPlantSeed() {
return createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/hammer_plant/seedbag.png"))
            .addComponent(new ItemComponent("Hammer Plant Seeds", ItemType.SEED,
                    "Seed of Hammer Plant", "images/plants/hammer_plant/seedbag.png"));

  }

  /**
   * Creates a 'Hammer Flower' item.
   *
   * @return Hammer Flower
   */
  public static Entity createHammerFlower() {
return createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/hammer_plant/item_drop.png"))
            .addComponent(new ItemComponent("Hammer Flower", ItemType.FOOD,
                    "Unusually shaped flower that looks like the tool it is named after",
                    "images/plants/hammer_plant/item_drop.png"));

  }

  /**
   * Creates a 'space snapper seed' item
   *
   * @return space snapper seed
   */
  public static Entity createSpaceSnapperSeed() {
return createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/space_snapper/seedbag.png"))
            .addComponent(new ItemComponent("Space Snapper Seeds", ItemType.SEED,
                    "Seed of Space Snapper", "images/plants/space_snapper/seedbag.png"));

  }

  /**
   * Creates a beef item
   *
   * @return beef item
   */
  public static Entity createBeef() {
return createBaseItem()
            .addComponent(new TextureRenderComponent("images/animals/beef.png"))
            .addComponent(new ItemComponent("Beef", ItemType.FOOD,
                    "Beef", "images/animals/beef.png"));

  }

  /**
   * Creates a chicken item
   *
   * @return chicken item
   */
  public static Entity createChickenMeat() {
    Entity chickenMeat = createBaseItem()
            .addComponent(new TextureRenderComponent("images/animals/chicken_meat.png"))
            .addComponent(new ItemComponent("Chicken", ItemType.FOOD,
                    "Chicken", "images/animals/chicken_meat.png"));
    chickenMeat.scaleHeight(0.6f);
    return chickenMeat;
  }

  /**
   * Creates a fence item that allows the player to place fences
   * @return the fence item
   */
  public static Entity createFenceItem() {
return createBaseItem()
            .addComponent(new TextureRenderComponent("images/placeable/fences/f.png"))
            .addComponent(new ItemComponent("FENCE", ItemType.PLACEABLE,
                    "A fence to keep animals in or out",
                    "images/placeable/fences/f.png"));

  }

  /**
   * Creates a gate item that allows the player to place gates
   * @return the gate item
   */
  public static Entity createGateItem() {
return createBaseItem()
            .addComponent(new TextureRenderComponent("images/placeable/fences/g_r_l.png"))
            .addComponent(new ItemComponent("GATE", ItemType.PLACEABLE,
                    "Allows the player to walk in and out of enclosed areas",
                    "images/placeable/fences/g_r_l.png"));

  }

  /**
   * Creates a sprinkler item that allows the player to place sprinklers
   * @return the sprinkler item
   */
  public static Entity createSprinklerItem() {
return createBaseItem()
            .addComponent(new TextureRenderComponent("images/placeable/sprinkler/pipe_null.png"))
            .addComponent(new ItemComponent("SPRINKLER", ItemType.PLACEABLE,
                    "Waters crops in the surrounding area",
                    "images/placeable/sprinkler/pipe_null.png"));

  }

  /**
   * Creates a Pump item that allows the player to power sprinklers
   * @return the sprinkler item
   */
  public static Entity createPumpItem() {
return createBaseItem()
            .addComponent(new TextureRenderComponent("images/placeable/sprinkler/pump.png"))
            .addComponent(new ItemComponent("PUMP", ItemType.PLACEABLE,
                    "Powers connected sprinklers",
                    "images/placeable/sprinkler/pump.png"));

  }

  /**
   * Creates a chest item that allows the player to place chests
   * @return the chest item
   */
  public static Entity createChestItem() {
return createBaseItem()
            .addComponent(new TextureRenderComponent("images/Temp-Chest.png"))
            .addComponent(new ItemComponent("CHEST", ItemType.PLACEABLE,
                    "A storage container to keep your seeds and goodies",
                    "images/Temp-Chest.png"));

  }

  public static Entity createLightItem() {
return createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/misc/aloe_vera_seed.png"))
            .addComponent(new ItemComponent("LIGHT", ItemType.PLACEABLE,
                    "A quick and easy fix to being scared of the dark!",
                    "images/plants/misc/aloe_vera_seed.png"));

  }

  /**
   * Creates a ship part item that can be used to repair the ship
   * @return ship part
   */
  public static Entity createShipPart() {
return createBaseItem()
            .addComponent(new TextureRenderComponent("images/ship/ship_part.png"))
            .addComponent(new ItemComponent("Ship Part", ItemType.SHIP_PART,
                    "Pieces of scrap metal in surprisingly good condition. Seems like it could be used" +
                            " for ship repairs...",
                    "images/ship/ship_part.png"));
  }
}
