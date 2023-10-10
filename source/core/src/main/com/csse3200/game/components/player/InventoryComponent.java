
package com.csse3200.game.components.player;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.services.FactoryService;
import com.csse3200.game.services.ServiceLocator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;


/**
 * A component intended to be used by the player to track their inventory.
 * Currently untested, but forms the basis for the UI which will be implemented soon:tm:
 */
public class InventoryComponent extends Component {

    /**
     * Logger for InventoryComponent
     */
    private static final Logger logger = LoggerFactory.getLogger(InventoryComponent.class);
    /**
     * HashMap of the String and the count of the item in the inventory
     */
    private HashMap<String, Integer> itemCount = new HashMap<>();

    /**
     * HashMap of the String and Entity of the item in the inventory
     */
    private HashMap<String,Entity> heldItemsEntity = new HashMap<>();

    /**
     * HashMap of the Position and the String of the item in the inventory
     */
    private HashMap<Integer,String> itemPlace = new HashMap<>();

    /**
     * Entity representing the item currently held by the player.
     */
    private Entity heldItem = null;

    /**
     * String representing the event that the inventory has been updated
     */
    public static final String UPDATE_INVENTORY = "updateInventory";

    /**
     * Integer representing the index of the item currently held by the player.
     */
    private int heldIndex = -1;

    /**
     * The maximum size of the inventory.
     */
    private int maxInventorySize = 30; // default size 30

    /**
     * Creates a new InventoryComponent with a given list of items.
     * @param items List of Entities to be added to inventory
     */
  public InventoryComponent(List<Entity> items) {
    setInventory(items);
  }

    /**
     * Creates a new InventoryComponent with a given maximum size.
     */
    public InventoryComponent(){
        newInventory();
    }

    /**
     * Sets the maximum size of the inventory.
     *
     * @param size The new maximum size of the inventory.
     * @return true if the size was set successfully, false otherwise.
     */
    public boolean setInventorySize(int size) {
        if (size > 0) {
            this.maxInventorySize = size;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the maximum size of the inventory.
     *
     * @return The maximum size of the inventory.
     */
    public int getInventorySize() {
        return this.maxInventorySize;
    }

    /**
     * Checks if the inventory is full.
     * Currently holding 30 types of Items
     *
     * @return true if the inventory is full, false otherwise.
     */
    public boolean isFull() {
        return this.itemCount.size() >= this.maxInventorySize;
    }

    /**
     * Returns the HashMap of the String and the count of the item in the inventory
     *
     * @return HashMap of items and their count
     */
    public HashMap <String, Integer> getItemCount() {
        return this.itemCount;
    }

    /**
     * Returns the HashMap of the Position and the String of the item in the inventory
     *
     * @return HashMap of itemPlace
     */
    public HashMap <Integer, String> getItemPlace() {
        return this.itemPlace;
    }

    /**
     * Returns the HashMap of the String and Entity of the item in the inventory
     * Created only for use in ItemSlot at the moment
     *
     * @return HashMap of held items
     */
    public HashMap <String,Entity> getHeldItemsEntity() {
        return this.heldItemsEntity;
    }

    /**
     * Sets the HashMap of the Position and the String of the item in the inventory
     * @param itemPlace HashMap of itemPlace
     */
    public void setItemPlace(HashMap<Integer, String> itemPlace) {
        this.itemPlace = itemPlace;
    }

    /**
     * Sets the HashMap of the String and the count of the item in the inventory
     * @param allCount
     */
    public void setItemCount(HashMap<String, Integer> allCount) {
        this.itemCount = allCount;
    }

    /**
     * Sets the HashMap of the String and Entity of the item in the inventory
     * @param heldItemsEntity
     */
    public void setHeldItemsEntity(HashMap<String,Entity> heldItemsEntity) {
        this.heldItemsEntity = heldItemsEntity;
    }

    /**
     * Returns the count of an item in the inventory
     *
     * @param item Passing the Item name of the item
     * @return integer representation of count
     */
    public int getItemCount(String item) {
        return this.itemCount.getOrDefault(item, 0);
    }

    /**
     * Returns if the player has a certain item or not.
     *
     * @param item Entity to be checked
     * @return boolean representing if the item is on the character
     */
    public Boolean hasItem(Entity item) {
        if(item.getType() != EntityType.ITEM) {
            logger.info("Passed Entity is not an item");
            return false;
        }
        return this.itemCount.containsKey(item.getComponent(ItemComponent.class).getItemName());
    }

    /**
     *  Returns if the player has a certain item or not
     *
     * @param item String to be checked
     * @return boolean representing if the item is on the character
     */
    public Boolean hasItem(String item) {
        return this.itemCount.containsKey(item);
    }

    private void newInventory() {
        this.itemPlace = new HashMap<>();
        this.itemCount = new HashMap<>();
        this.heldItemsEntity = new HashMap<>();
    }

    /**
     * Get the total number of Different Items in the inventory
     *
     * @return integer representing the total number of different items in the inventory
     */
    public int lastItemIndex() {
        return this.itemPlace.size();
    }

    /**
     * Sets the player's inventory to a given list of items.
     * Old inventory is cleared.
     *
     * @param items List of Entities to be added to inventory
     */

  public void setInventory(List<Entity> items) {
    newInventory();
    logger.debug("Setting inventory started");
    for (Entity item : items) {
        if (item.getComponent(ItemComponent.class) == null) {
            logger.info("Not an Item");
            continue;
        }
      // Add to Entity against Item Type for setting Held Item
      this.heldItemsEntity.put(item.getComponent(ItemComponent.class).getItemName(),item);
      // Update the count against Item Type
      if (itemCount.containsKey(item.getComponent(ItemComponent.class).getItemName())) {
        // Item exists in inventory, increase count
        this.itemCount.put(item.getComponent(ItemComponent.class).getItemName(), itemCount.get(item.getComponent(ItemComponent.class).getItemName()) + 1);
      } else {
        // Item does not exist in inventory, add to inventory
        this.itemCount.put(item.getComponent(ItemComponent.class).getItemName(), 1); // Setting initial count as 1
        this.setPosition(item); // Setting position of item to next available position
      }
      logger.debug("Setting inventory Completed");
    }
  }

    /**
     * Function to get the item of a specific position in Inventory.
     * Starts at 0
     *
     * @param position position of the item in inventory
     * @return entity for that position in inventory
     */
    public Entity getItem(int position) {
        return this.heldItemsEntity.get(this.getItemName(position));
    }

    /**
     * Function to get the item of a specific position in Inventory.
     * Starts at 0
     *
     * @param position position of the item in inventory
     * @return entity for that position in inventory
     */
    public String getItemName(int position) {
        return this.itemPlace.get(position);
    }

    /**
     * Function to get the exchange the position of two specific item in Inventory.
     *
     * @param pos1 position of first Inventory Item
     * @param pos2 position of second Inventory Item
     */
    public boolean swapPosition(int pos1, int pos2) {
        if (pos1 >= this.getInventorySize() || pos1 < 0 || pos2 >= this.getInventorySize() || pos2 < 0) {
            logger.info("Swap Position is out of bounds");
            return false;
        } else{
            String temp = this.itemPlace.get(pos1);
            this.itemPlace.put(pos1,this.itemPlace.get(pos2));
            this.itemPlace.put(pos2,temp);
            return true;
        }
    }

    /**
     * add position of an entity into the HashList, with a specific position
     *
     * @param entity entity to be added
     * @param position position of the entity
     * @return boolean representing if the item was added successfully
     */
    public boolean setPosition(Entity entity, int position) {
        if (this.itemPlace.containsValue(entity.getComponent(ItemComponent.class).getItemName())) {
            logger.info("Item already in inventory, Swap position instead");
            return false;
        } else if (position >= this.getInventorySize() || position < 0) {
            logger.info("Set Position is out of bounds");
            return false;
        } else if (this.itemPlace.get(position) != null) {
            logger.info("Set Position is already occupied");
            return false;
        } else {
            this.itemPlace.put(position, entity.getComponent(ItemComponent.class).getItemName());
            return true;
        }
    }

    /**
     * Add Item to the first available position in the inventory
     *
     * @param entity entity to be added
     * @return boolean representing if the item was added successfully
     */
    public boolean setPosition(Entity entity){
        int position = nextAvailablePosition();
        if (position == -1) {
            return false;
        } else if (this.itemPlace.get(position) != null) {
            logger.info("Set Position is already occupied");
            return false;
        } else if (this.itemPlace.containsValue(entity.getComponent(ItemComponent.class).getItemName())) {
            logger.info("Item already in inventory, Swap position instead");
            return false;
        } else {
            this.itemPlace.put(position, entity.getComponent(ItemComponent.class).getItemName());
            return true;
        }
    }

  /**
   * Get the next available position in the inventory
   * @return integer representing the next available position
   */

  private int nextAvailablePosition() {
    for (int i = 0; i < this.getInventorySize(); i++) {
      if (this.itemPlace.get(i) == null) {
        return i;
      }
    }
    return -1;
  }

    /**
     * Adds an item to the Player's inventory
     * @param itemComponent ItemComponent to be added
     * @return boolean representing if the item was added successfully
     */
  public boolean addItem(ItemComponent itemComponent){
    Entity item = new Entity(EntityType.ITEM);
    item.addComponent(itemComponent);
    return addItem(item);
  }

    /**
     * Adds an item to the Player's inventory
     * @param count number of items to be added
     * @param item ItemComponent to be added
     * @return boolean representing if the item was added successfully
     */
  public boolean addMultipleItem(int count, Entity item) {
    for (int i = 0; i < count; i++) {
      addItem(item);
    }
    return true;
  }

    /**
     * Adds an item to the Player's inventory
     *
     * @param item Entity to add
     * @return boolean representing if the item was added successfully
     */
    public boolean addItem(Entity item) {
        if(item.getType() != EntityType.ITEM || item.getComponent(ItemComponent.class) == null) {
            return false;
        }
        if (isFull()) {
            return false;
        } else {
            // Update the count of the Item Type
            this.itemCount.put(item.getComponent(ItemComponent.class).getItemName(), this.itemCount.getOrDefault(item.getComponent(ItemComponent.class).getItemName(), 0) + 1);
            // Add to Entity against Item Type for setting Held Item
            this.heldItemsEntity.put(item.getComponent(ItemComponent.class).getItemName(), item);
            // Add item to next available position
            setPosition(item);
            setHeldItem(getHeldIndex());
            entity.getEvents().trigger(UPDATE_INVENTORY);
            return true;
        }
    }

    /**
     * Removes an item from the Player's Inventory
     *
     * @param item item to be removed
     * @return boolean representing if the item was removed successfully
     */
    public boolean removeItem(Entity item) {
        // Check if it is an Item
        if(item.getType() != EntityType.ITEM) {
            logger.info("To be removed Entity is not an item");
            return false;
        }
        // Check if item is in inventory
        if (!this.itemCount.containsKey(item.getComponent(ItemComponent.class).getItemName())) {
            return false;
        }
        // Decrease item count by 1
        this.itemCount.put(item.getComponent(ItemComponent.class).getItemName(), this.itemCount.get(item.getComponent(ItemComponent.class).getItemName()) - 1);
        // If item count is now 0
        if (this.itemCount.get(item.getComponent(ItemComponent.class).getItemName()).equals(0)) {
            // Remove it from the item count
            this.itemCount.remove(item.getComponent(ItemComponent.class).getItemName());
            // Find the position of the item and remove the item from the position
            for (Map.Entry<Integer, String> placeEntry : itemPlace.entrySet()) {
                if (placeEntry.getValue() == null) {
                    // Ignore the position if a remnant null is left behind after moving an item.
                    continue;
                }
                // Remove the item from item place
                if (placeEntry.getValue().equals(item.getComponent(ItemComponent.class).getItemName())) {
                    itemPlace.remove(placeEntry.getKey());
                    // If it was the held item set held item to null
                    if (this.heldIndex == placeEntry.getKey()) {
                        this.heldItem = null;
                    }
                    setHeldItem(heldIndex);
                    break;
                }
            }
        }
        entity.getEvents().trigger(UPDATE_INVENTORY);
        logger.info("Removing item from inventory - {}, new count {}", item.getComponent(ItemComponent.class).getItemName(),
                this.itemCount.getOrDefault(item.getComponent(ItemComponent.class).getItemName(), 0));
        return true;
    }

    /**
     * Sets the held item for the Player.
     *
     * @param index The index of the item in the inventory to be set as the held item.
     */
    public void setHeldItem(int index) {
        if (index >= 0 && index < 10) {
            this.heldItem = this.heldItemsEntity.get(this.itemPlace.get(index));
            this.heldIndex = index;
        }
    }

    /**
     * Retrieves the held item of the Player.
     *
     * @return The Entity representing the held item.
     */
    public Entity getHeldItem() {
        return this.heldItem;
    }

    /**
     * Retrieves the held item index of the Player.
     *
     * @return The Entity representing the held item.
     */
    public int getHeldIndex() {
        return this.heldIndex;
    }

    /**
     * Returns the count of an item in the inventory
     * @param item Entity that is an item to find the counted of
     * @return integer representation of count
     */
    public int getItemCount(Entity item) {
        return this.itemCount.getOrDefault(item.getComponent(ItemComponent.class).getItemName(), 0);
    }

    /**
     * Returns the count of an item in the inventory
     * @param i position of the item in inventory
     * @return integer representation of count
     */
    public Integer getItemCount(int i) {
        return this.itemCount.getOrDefault(this.itemPlace.get(i), 0);
    }

    /**
     * Returns the count of an item in the inventory
     * @param itemCount HashMap of the String and the count of the item in the inventory
     * @param heldItemsEntity HashMap of the String and Entity of the item in the inventory
     * @param itemPlace HashMap of the Position and the String of the item in the inventory
     *
     */
    public void loadInventory(HashMap<String, Integer> itemCount, HashMap<String, Entity> heldItemsEntity, HashMap<Integer, String> itemPlace) {
        logger.debug("Loading inventory started");
        newInventory();
        this.itemCount = itemCount;
        this.heldItemsEntity = heldItemsEntity;
        this.itemPlace = itemPlace;
        entity.getEvents().trigger(UPDATE_INVENTORY);
        logger.debug("Loading inventory completed");
    }

    /**
     * Writes the InventoryComponent to a Json object.
     * @param json The Json object to write to.
     */
    @Override
    public void write(Json json) {
        json.writeObjectStart(this.getClass().getSimpleName());
        json.writeArrayStart("inventory");
        for (Map.Entry<Integer, String> placeEntry : itemPlace.entrySet()) {
            Integer i = placeEntry.getKey();
            String e = placeEntry.getValue();
            if (e == null) {
                continue;
            }
            json.writeObjectStart();
            heldItemsEntity.get(e).writeItem(json);
            json.writeValue("count", getItemCount(e));
            json.writeValue("place", i);
            json.writeObjectEnd();
        }
        json.writeArrayEnd();
        json.writeObjectEnd();
    }

    /**
     * Reads the InventoryComponent from a Json object.
     * @param json The Json object to read from.
     * @param inv The JsonValue to read from.
     */
    @Override
    public void read(Json json, JsonValue inv) {
        newInventory();
        inv = inv.get("InventoryComponent").get("inventory");
        inv.forEach(jsonValue -> {
            Entity itemEntity = FactoryService.getItemFactories().get(jsonValue.getString("name")).get();
            ServiceLocator.getGameArea().spawnEntity(itemEntity);
            itemEntity.readItem(json, jsonValue.get("components"));
            itemCount.put(jsonValue.getString("name"),  jsonValue.getInt("count"));
            heldItemsEntity.put(jsonValue.getString("name"), itemEntity);
            itemPlace.put(jsonValue.getInt("place"), jsonValue.getString("name"));
        });
    }
}
