package com.csse3200.game.components.items;

import com.csse3200.game.components.Component;
import java.util.UUID;

public class ItemComponent extends Component {
    /**
     * Item class for all items in the game
     * @param itemName user facing name for item
     * @param itemDescription user facing description for item
     * @param price price of item
     * @param sellable is the item sellable. true if sellable false otherwise
     * @param itemId unique id of the item
     * 
     */
    private String itemName; // User facing name for item. can be customised by user. 
    private String itemDescription; // User facing description for item.
    private final String itemId;
    private int price; // Price of item
    private final boolean sellable; // is the item sellable


    /**
     * Constructor for Item
     * @param itemName user facing name for item  
     */
    public ItemComponent(String itemName) {
        this.itemName = itemName; 
        this.itemId = generateUniqueID(); // Generate a unique ID for the item
        this.itemDescription = "" ; //default description
        this.price = 0;
        this.sellable = false; //default sellable 
    }

    /**
     * Constructor for Item
     * @param itemName user facing name 
     * @param price sellable price of the item
     */
    public ItemComponent(String itemName,  int price) {
        this.itemName = itemName; 
        this.itemId = generateUniqueID(); // Generate a unique ID for the item
        this.itemDescription = "" ; //default description
        this.price = price;
        this.sellable = true;
    }

    /**
     * Constructor for Item
     * @param itemName user facing name for item  
     * @param itemDescription user facing description for item
     */
    public ItemComponent(String itemName, String itemDescription) {
        this.itemName = itemName; 
        this.itemId = generateUniqueID(); // Generate a unique ID for the item
        this.itemDescription = itemDescription ; //default description
        this.sellable = false; //default sellable
    }

    /**
     * Constructor for Item
     * @param itemName user facing name for item 
     * @param itemDescription user facing description for item
     * @param price price of item
     */
    public ItemComponent(String itemName, String itemDescription, int price) {
        this.itemName = itemName; 
        this.itemId = generateUniqueID(); // Generate a unique ID for the item
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
     * Sets the price of the item
     * @param price int price of item
     */
    public void setPrice(int price) {
        this.price = price;
    }


    /**
     * Returns selalble bool of item
     * @return true if item is sellable, false otherwise
     */
    public boolean isSellable() {
        return sellable;
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
     * Sets the price of the item
     * @param description string description of the item
     */
    public void setItemDescription(String description) {
        itemDescription = description;
    }

    /**
     * Sets the item name of the item
     * @param name string name of the item
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
