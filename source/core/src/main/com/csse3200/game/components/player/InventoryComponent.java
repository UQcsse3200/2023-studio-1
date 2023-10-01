package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

/**
 * A component intended to be used by the player to track their inventory.
 * Currently untested, but forms the basis for the UI which will be implemented soon:tm:
 */
public class InventoryComponent extends Component {
  private static final Logger logger = LoggerFactory.getLogger(InventoryComponent.class);
  //private List<Entity> inventory = new ArrayList<>();
  private HashMap<String, Integer> itemCount = new HashMap<>();

  private HashMap<String,Entity> heldItemsEntity = new HashMap<>();

  private HashMap<Integer,String> itemPlace = new HashMap<>();

  private HashMap<String, Texture> itemTextures = new HashMap<>();

  private Entity heldItem = null;

  private int heldIndex = 0;

  private int maxInventorySize = 30;

  public InventoryComponent(List<Entity> items) {
    setInventory(items);
  }

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
   * Returns the player's inventory.
   *
   * @return player's inventory
   */
//  public List<Entity> getInventory() {
//    return this.inventory;
//  }

  /**
   * Returns the HashMap of the String and the count of the item in the inventory
   *
   * @return HashMap<String, Integer>
   */

  public HashMap<String, Integer> getItemCount() {
    return this.itemCount;
  }

  /**
   * Returns the HashMap of the Position and the String of the item in the inventory
   * @return HashMap<Integer,String>
   */

  public HashMap<Integer, String> getItemPlace() {
    return this.itemPlace;
  }

  /**
   * Returns the HashMap of the String and Entity of the item in the inventory
   * Created only for use in ItemSlot at the moment
   * @return HashMap<String,Entity>
   */

  public HashMap<String,Entity> getHeldItemsEntity() {
    return this.heldItemsEntity;
  }

  public boolean setItemPlace(HashMap<Integer, String> itemPlace) {
    this.itemPlace = itemPlace;
    return true;
  }

  public boolean setItemCount(HashMap<String, Integer> allCount) {
    this.itemCount = allCount;
    return true;
  }

  public boolean setHeldItemsEntity(HashMap<String,Entity> heldItemsEntity) {
    this.heldItemsEntity = heldItemsEntity;
    return true;
  }

  /**
   * Returns the count of an item in the inventory
   *
   * @param item Passing the Entity type of the item
   * @return integer representation of count
   */
  public int getItemCount(String item) {
    return this.itemCount.getOrDefault(item, 0);
  }

  /**
   * Returns if the player has a certain item or not.
   * @param item Entity to be checked
   * @return boolean representing if the item is on the character
   */
  public Boolean hasItem(Entity item) {
    if(item.getType() != EntityType.Item) {
      //logger.info("Entity is not an item");
      return false;
    }
    return this.itemCount.containsKey(item.getComponent(ItemComponent.class).getItemName());
  }

  /**
   *  Returns if the player has a certain item or not
   * @param item String to be checked
   * @return boolean representing if the item is on the character
   */
  public Boolean hasItem(String item) {
    return this.itemCount.containsKey(item);
  }

  private void newInventory() {
    this.itemPlace = new HashMap<>();
    this.itemCount = new HashMap<>();
    //this.inventory = new ArrayList<>();
    this.itemTextures = new HashMap<>();
    this.heldItemsEntity = new HashMap<>();
  }

  /**
   * Get the total number of Different Items in the inventory
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
    //logger.debug("Setting inventory");
    //this.inventory.addAll(items);
    for (Entity item : items) {
      // Add to Entity against Item Type for setting Held Item
      this.heldItemsEntity.put(item.getComponent(ItemComponent.class).getItemName(),item);
      // Add the Texture against Item Type
      this.itemTextures.put(item.getComponent(ItemComponent.class).getItemName(), item.getComponent(ItemComponent.class).getItemTexture());
      // Update the count against Item Type
      if (itemCount.containsKey(item.getComponent(ItemComponent.class).getItemName())) {
        // Item exists in inventory, increase count
        this.itemCount.put(item.getComponent(ItemComponent.class).getItemName(), itemCount.get(item.getComponent(ItemComponent.class).getItemName()) + 1);
      } else {
        // Item does not exist in inventory, add to inventory
        this.itemCount.put(item.getComponent(ItemComponent.class).getItemName(), 1); // Setting initial count as 1
        this.setPosition(item); // Setting position of item to next available position
      }
      //logger.debug("Setting inventory Completed");
    }
  }

  /**
   * Sets the player's inventory to a given HashMap.
   *
   * @param items items to be added to inventory
   */
  public void setInventory(
          HashMap<String, Integer> items,
          HashMap<Integer, String> itemPosition,
          HashMap<String, Texture> itemTextures,
          HashMap<String,Entity> heldItemsEntity) {
    // Clear the old inventory
    newInventory();
    // Add the new inventory
    this.itemPlace.putAll(itemPosition);
    this.itemCount.putAll(items);
    this.itemTextures.putAll(itemTextures);
    this.heldItemsEntity.putAll(heldItemsEntity);
  }

  /**
   * Function to get the item of a specific position in Inventory.
   * Starts an 0
   *
   * @param position position of the item in inventory
   * @return entity for that position in inventory
   */
  public String getItem(int position) {
    return this.itemPlace.get(position);
  }

  /**
   * Get texture of an Item Type
   *
   * @param item Item Type to get texture of
   * @return Texture of the Item Type
   */
  public Texture getItemTexture(String item) {
    return this.itemTextures.get(item);
  }

  /**
   * Get texture of an Item Type
   *
   * @param position position of the item in inventory
   * @return Texture of the Item Type
   */
  public Texture getItemTexture(int position) {
    return this.itemTextures.get(this.itemPlace.get(position));
  }

  /**
   * Function to get the exchange the position of two specific item in Inventory.
   *
   * @param pos1 position of first Inventory Item
   * @param pos2 position of second Inventory Item
   */
  public boolean swapPosition(int pos1, int pos2) {
    if (pos1 >= this.getInventorySize() || pos1 < 0 || pos2 >= this.getInventorySize() || pos2 < 0) {
      //logger.info("Position is out of bounds");
      return false;
    } else{
        String temp = this.itemPlace.get(pos1);
        this.itemPlace.put(pos1,this.itemPlace.get(pos2));
        this.itemPlace.put(pos2,temp);
        itemChange(pos1);
        itemChange(pos2);
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
      //logger.info("Item already in inventory, Swap position instead");
      return false;
    } else if (position >= this.getInventorySize() || position < 0) {
      //logger.info("Position is out of bounds");
      return false;
    } else if (this.itemPlace.get(position) != null) {
      //logger.info("Position is already occupied");
      return false;
    } else {
      this.itemPlace.put(position, entity.getComponent(ItemComponent.class).getItemName());
      itemChange(position);
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
        //logger.info("Position is already occupied");
        return false;
    } else if (this.itemPlace.containsValue(entity.getComponent(ItemComponent.class).getItemName())) {
        //logger.info("Item already in inventory, Swap position instead");
        return false;
    } else {
      this.itemPlace.put(position, entity.getComponent(ItemComponent.class).getItemName());
      itemChange(position);
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
  public boolean addItem(ItemComponent itemComponent){
    Entity item = new Entity(EntityType.Item);
    item.addComponent(itemComponent);
    return addItem(item);
  }
  /**
   * Adds an item to the Player's inventory
   * @param item Entity to add
   * @return boolean representing if the item was added successfully
   */
  public boolean addItem(Entity item) {
    if(item.getType() != EntityType.Item || item.getComponent(ItemComponent.class) == null) {
      //logger.info("Entity is not an item");
      //logger.info(entity.toString());
      return false;
    }
    if (isFull()) {
      return false;
    } else {
      //logger.info("Adding item to inventory - " + item.getComponent(ItemComponent.class).getItemName() + ",old count " + this.itemCount.getOrDefault(item.getComponent(ItemComponent.class).getItemName(), 0));
      // Update the count of the Item Type
      this.itemCount.put(item.getComponent(ItemComponent.class).getItemName(), this.itemCount.getOrDefault(item.getComponent(ItemComponent.class).getItemName(), 0) + 1);
      // Update the Texture against Item Type list
      this.itemTextures.put(item.getComponent(ItemComponent.class).getItemName(), item.getComponent(ItemComponent.class).getItemTexture());
      // Add to Entity against Item Type for setting Held Item
      this.heldItemsEntity.put(item.getComponent(ItemComponent.class).getItemName(), item);
      // Add item to next available position
      setPosition(item);
      entity.getEvents().trigger("updateToolbar");
      return true;
    }
  }

  /**
   * Removes an item from the Player's Inventory
   * @param item item to be removed
   * @return boolean representing if the item was removed successfully
   */
  public boolean removeItem(Entity item) {
    // check if item is in inventory
    if(item.getType() != EntityType.Item) {
      //logger.info("Entity is not an item");
      return false;
    }
    if (!this.itemCount.containsKey(item.getComponent(ItemComponent.class).getItemName())) {
      return false;
    } else {
      this.itemCount.put(item.getComponent(ItemComponent.class).getItemName(), this.itemCount.get(item.getComponent(ItemComponent.class).getItemName()) - 1);
        if (this.itemCount.get(item.getComponent(ItemComponent.class).getItemName()) == 0) {
          this.itemCount.remove(item.getComponent(ItemComponent.class).getItemName());
            // find the position of the item and remove the item from the position
            for (int i = 0; i < this.itemPlace.size(); i++) {
                if (this.itemPlace.get(i) == item.getComponent(ItemComponent.class).getItemName()) {
                  this.itemPlace.remove(i);
                  itemChange(i);
                    break;
                }
            }
        }
        entity.getEvents().trigger("updateToolbar");
        //logger.info("Removing item from inventory - " + item.getComponent(ItemComponent.class).getItemName() + ",new count " + this.itemCount.getOrDefault(item.getComponent(ItemComponent.class).getItemName(), 0));
        return true;
    }
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
      //Create a new item from the String in itemPlace.get(index)
//      this.heldItem = new Entity(this.itemPlace.get(index));
//      this.heldIndex = index;
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

//  @Override
//  public void write(Json json) {
//    json.writeObjectStart(this.getClass().getSimpleName());
//    json.writeObjectStart("inventory");
//    for (Entity e : inventory) {
//      json.writeObjectStart("item");
//      e.writeItem(json);
//      json.writeValue("count", getItemCount(e));
//      json.writeValue("X", getItemPosition(e).x);
//      json.writeValue("Y", getItemPosition(e).y);
//      json.writeObjectEnd();
//    }
//    json.writeObjectEnd();
//    json.writeObjectEnd();
//  }

  public Integer getItemCount(int i) {
    return this.itemCount.getOrDefault(this.itemPlace.get(i), 0);
  }

  /**
   * Function to trigger the update of ItemSlot at the pass index.
   * @param index
   */

  public void itemChange(int index) {
    if(index >= 0 && index < 10) {
      // Only update toolbar

    } else {
      // update both toolbar and inventory

    }
  }
}
