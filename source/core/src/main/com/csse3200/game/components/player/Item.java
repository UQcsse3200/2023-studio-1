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
    private int price; // Price of item
    private final boolean sellable; // is the item sellable

    public enum ItemType {
        TOOL,
        ARMOUR,
        CONSUMABLE
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
        this.price = 0;
        this.sellable = false; //default sellable 
    }

    /**
     * Constructor for Item
     * @param itemName user facing name 
     * @param itemType type of the item
     * @param price sellable price of the item
     */
    public Item(String itemName, ItemType itemType, int price) {
        this.itemName = itemName; 
        this.itemId = generateUniqueID(); // Generate a unique ID for the item
        this.itemType = itemType; //from enum ItemType
        this.itemDescription = "" ; //default description
        this.price = price;
        this.sellable = true;
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
        this.sellable = false; //default sellable
    }

    /**
     * Constructor for Item
     * @param itemName user facing name for item
     * @param itemType type of item 
     * @param itemDescription user facing description for item
     * @param price price of item
     */
    public Item(String itemName, ItemType itemType, String itemDescription, int price) {
        this.itemName = itemName; 
        this.itemId = generateUniqueID(); // Generate a unique ID for the item
        this.itemType = itemType; //from enum ItemType
        this.itemDescription = itemDescription ; //default description
        this.price = price;
        this.sellable = true;
    }


    /** 
     * Returns the price of the item
     * @return int price of item
     */
    public int getPrice() {
        return price;
    }


    /**
     * Returns selalble bool of item
     * @return true if item is sellable, false otherwise
     */
    public boolean isSellable() {
        return sellable;
    }

    /**
     * Returns the type of the item
     * @return ItemType type of item
     */
    public ItemType getItemType() {
        return itemType;
    }

    
    /**
     * Returns the name of the item
     * @return int price of item
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Returns the id of the item
     * @return String id of item
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * Returns the description of the item
     * @return String description of item
     */
    public String getItemDescription() {
        return itemDescription;
    }

    /**
     * Sets the item name of the item
     * @param String name of the item
     */
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
