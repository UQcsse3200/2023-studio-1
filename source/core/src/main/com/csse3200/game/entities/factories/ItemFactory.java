package com.csse3200.game.entities.factories;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.items.ItemActions;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.components.items.WateringCanLevelComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.FactoryService;

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
    Entity item = new Entity(EntityType.Item)
        .addComponent(new PhysicsComponent())
        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ITEM))
        .addComponent(new ItemActions());
    return item;
  }

  /**
   * Creates a shovel item
   * 
   * @return shovel
   */
  public static Entity createShovel() {
    Entity shovel = createBaseItem()
        .addComponent(new TextureRenderComponent("images/tool_shovel.png"))
        .addComponent(new ItemComponent("shovel", ItemType.SHOVEL, "Shovel for removing items",
            new Texture("images/tool_shovel.png")));
    return shovel;
  }

  /**
   * Creates a hoe item
   * 
   * @return hoe
   */
  public static Entity createHoe() {
    Entity hoe = createBaseItem()
        .addComponent(new TextureRenderComponent("images/tool_hoe.png"))
        .addComponent(new ItemComponent("hoe", ItemType.HOE, new Texture("images/tool_hoe.png")));
    return hoe;
  }

  /**
   * Creates a watering-can item
   * 
   * @return watering can
   */
  public static Entity createWateringcan() {
    Entity watering_can = createBaseItem()
        .addComponent(new TextureRenderComponent("images/tool_watering_can.png"))
        .addComponent(
            new ItemComponent("watering_can", ItemType.WATERING_CAN, new Texture("images/tool_watering_can.png")))
        .addComponent(new WateringCanLevelComponent(150));
    return watering_can;
  }

  /**
   * Creates a scythe item
   * 
   * @return scythe
   */
  public static Entity createScythe() {
    Entity scythe = createBaseItem()
        .addComponent(new TextureRenderComponent("images/tool_scythe.png"))
        .addComponent(new ItemComponent("scythe", ItemType.SCYTHE, new Texture("images/tool_scythe.png")));
    return scythe;
  }

  /**
   * Creates a milk item
   *
   * @return milk item
   */
  public static Entity createMilk() {
    Entity milk = createBaseItem()
            .addComponent(new TextureRenderComponent("images/milk.png"))
            .addComponent(new ItemComponent("milk",
                    ItemType.MILK, new Texture("images/milk.png")));
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
            .addComponent(new TextureRenderComponent("images/egg.png"))
            .addComponent(new ItemComponent("egg", ItemType.EGG,
                    new Texture("images/egg.png")));
    egg.scaleHeight(0.75f);
    return egg;
  }

   /**
   * Creates a fertiliser item
   *
   * @return fertiliser
   */
  public static Entity createFertiliser() {
    Entity fertiliser = createBaseItem()
            .addComponent(new TextureRenderComponent("images/fertiliser.png"))
            .addComponent(new ItemComponent("fertiliser", ItemType.FERTILISER,
                    new Texture("images/fertiliser.png")));
    return fertiliser;
  }

  /**
   * Creates an 'aloe vera seed' item
   *
   * @return aloe vera seed
   */
  public static Entity createAloeVeraSeed() {
    Entity seed = createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/misc/aloe_vera_seed.png"))
            .addComponent(new ItemComponent("aloe vera seed", ItemType.SEED,
                    "Seed of Aloe Vera", new Texture("images/plants/misc/aloe_vera_seed.png")));
    return seed;
  }

  /**
   * Creates an 'atomic algae seed' item
   *
   * @return atomic algae seed
   */
  public static Entity createAtomicAlgaeSeed() {
    Entity seed = createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/atomic_algae/1_seedling.png"))
            .addComponent(new ItemComponent("atomic algae seed", ItemType.SEED,
                    "Seed of Atomic Algae", new Texture("images/plants/atomic_algae/1_seedling.png")));
    return seed;
  }

  /**
   * Creates a 'cosmic cob seed' item
   *
   * @return cosmic cob seed
   */
  public static Entity createCosmicCobSeed() {
    Entity seed = createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/misc/cosmic_cob_seed.png"))
            .addComponent(new ItemComponent("cosmic cob seed", ItemType.SEED,
                    "Seed of Cosmic Cob", new Texture("images/plants/misc/cosmic_cob_seed.png")));
    return seed;
  }

  /**
   * Creates a 'deadly nightshade seed' item
   *
   * @return deadly nightshade seed
   */
  public static Entity createDeadlyNightshadeSeed() {
    Entity seed = createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/misc/deadly_nightshade_seed.png"))
            .addComponent(new ItemComponent("deadly nightshade seed", ItemType.SEED,
                    "Seed of Deadly Nightshade", new Texture("images/plants/misc/deadly_nightshade_seed.png")));
    return seed;
  }

  /**
   * Creates a 'hammer plant seed' item
   *
   * @return hammer plant seed
   */
  public static Entity createHammerPlantSeed() {
    Entity seed = createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/misc/hammer_plant_seed.png"))
            .addComponent(new ItemComponent("hammer plant seed", ItemType.SEED,
                    "Seed of Hammer Plant", new Texture("images/plants/misc/hammer_plant_seed.png")));
    return seed;
  }

  /**
   * Creates a 'space snapper seed' item
   *
   * @return space snapper seed
   */
  public static Entity createSpaceSnapperSeed() {
    Entity seed = createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/misc/space_snapper_seed.png"))
            .addComponent(new ItemComponent("space snapper seed", ItemType.SEED,
                    "Seed of Space Snapper", new Texture("images/plants/misc/space_snapper_seed.png")));
    return seed;
  }

  /**
   * Creates a 'Cow food' item
   *
   * @return tobacco seed
   */
  public static Entity createCowFood() {
    Entity animalFood = createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/cosmic_cob/1_seedling.png"))
            .addComponent(new ItemComponent("COW FOOD", ItemType.FOOD,
                    "Cow food", new Texture("images/plants/cosmic_cob/1_seedling.png")));
    return animalFood;
  }

  /**
   * Creates a fence item that allows the player to place fences
   * @return the fence item
   */
  public static Entity createFenceItem() {
    Entity fence = createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/misc/aloe_vera_seed.png"))
            .addComponent(new ItemComponent("Fence", ItemType.PLACEABLE,
                    "A fence to keep animals in or out",
                    new Texture("images/plants/misc/aloe_vera_seed.png")));
    return fence;
  }

  /**
   * Creates a gate item that allows the player to place gates
   * @return the gate item
   */
  public static Entity createGateItem() {
    Entity gate = createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/misc/aloe_vera_seed.png"))
            .addComponent(new ItemComponent("Gate", ItemType.PLACEABLE,
                    "Allows the player to walk in and out of enclosed areas",
                    new Texture("images/plants/misc/aloe_vera_seed.png")));
    return gate;
  }

  /**
   * Creates a sprinkler item that allows the player to place sprinklers
   * @return the sprinkler item
   */
  public static Entity createSprinklerItem() {
    Entity fence = createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/misc/aloe_vera_seed.png"))
            .addComponent(new ItemComponent("Sprinkler", ItemType.PLACEABLE,
                    "Waters crops in the surrounding area",
                    new Texture("images/plants/misc/aloe_vera_seed.png")));
    return fence;
  }

  /**
   * Creates a chest item that allows the player to place chests
   * @return the chest item
   */
  public static Entity createChestItem() {
    Entity fence = createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/misc/aloe_vera_seed.png"))
            .addComponent(new ItemComponent("Chest", ItemType.PLACEABLE,
                    "A storage container to keep your seeds and goodies",
                    new Texture("images/plants/misc/aloe_vera_seed.png")));
    return fence;
  }

}
