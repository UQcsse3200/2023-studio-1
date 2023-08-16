package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import java.util.UUID;

public class Item extends Component {
    /**
     * Basic Item Class
     * @param name user facing name for item
     * @param itemType type of item. can be sourced via enum 
     */
    private String itemName; // User facing name for item. can be customised by user. 
    private String itemDescription; // User facing description for item.
    private final String itemId;
    private final ItemType itemType; 

    public enum ItemType {
        TOOL,
        ARMOUR,
        CONSUMABLE,
        PLANT
    }

    /**
     * Constructor for Item
     * @param itemName user facing name for item  
     * @param itemType type of item
     */
    public Item(String itemName, ItemType itemType) {
        this.itemName = itemName; 
        this.itemId = generateUniqueID(); // Generate a unique ID for the item
        this.itemType = itemType; //from enum ItemType
        this.itemDescription = "" ; //default description
    }

    /**
     * Constructor for Item
     * @param itemName user facing name for item  
     * @param itemType type of item
     * @param itemDescription user facing description for item
     */
    public Item(String itemName, ItemType itemType, String itemDescription) {
        this.itemName = itemName; 
        this.itemId = generateUniqueID(); // Generate a unique ID for the item
        this.itemType = itemType; //from enum ItemType
        this.itemDescription = itemDescription ; //default description
    }

    public ItemType getItemType() {
        return itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemName(String name) {
        itemName = name;
    }

    /**
     * Generates a unique ID for the item
     * @return String random unique ID for each item
     */
    private String generateUniqueID() {
        return UUID.randomUUID().toString();
    }
}
