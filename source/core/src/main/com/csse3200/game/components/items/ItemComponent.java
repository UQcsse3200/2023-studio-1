package com.csse3200.game.components.items;

import java.util.UUID;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceLocator;


public class ItemComponent extends Component {
    /**
     * Item class for all items in the game
     * @param itemName user facing name for item
     * @param itemDescription user facing description for item
     * @param price price of item
     * @param sellable is the item sellable. true if sellable false otherwise
     * @param itemId unique id of the item
     * @param itemType type of item
     *
     */
    private String itemName; // User facing name for item. can be customised by user.
    private String itemDescription; // User facing description for item.
    private final String itemId;
    private int price; // Price of item
    private final boolean sellable; // is the item sellable
    private final ItemType itemType; // Type of item
    private Texture itemTexture;



    /**
     * Constructor for Item
     * @param itemName user facing name for item
     * @param itemType the enum for type of item
     */
    public ItemComponent(String itemName, ItemType itemType, String texturePath) {
        this.itemTexture = ServiceLocator.getResourceService().getAsset(texturePath, Texture.class);
        this.itemType = itemType;
        this.itemName = itemName;
        this.itemId = generateUniqueID(); // Generate a unique ID for the item
        this.itemDescription = "" ; //default description
        this.price = 0;
        this.sellable = false; //default sellable
    }

    /**
     * Constructor for Item
     * @param itemName user facing name
     * @param itemType the enum for type of item
     * @param price sellable price of the item
     */
    public ItemComponent(String itemName, ItemType itemType, int price, String texturePath) {
        this.itemTexture = ServiceLocator.getResourceService().getAsset(texturePath, Texture.class);
        this.itemType = itemType;
        this.itemName = itemName;
        this.itemId = generateUniqueID(); // Generate a unique ID for the item
        this.itemDescription = "" ; //default description
        this.price = price;
        this.sellable = true;
    }

    /**
     * Constructor for Item
     * @param itemName user facing name for item
     * @param itemType the enum for type of item
     * @param itemDescription user facing description for item
     */
    public ItemComponent(String itemName, ItemType itemType, String itemDescription, String texturePath) {
        this.itemTexture = ServiceLocator.getResourceService().getAsset(texturePath, Texture.class);
        this.itemType = itemType;
        this.itemName = itemName;
        this.itemId = generateUniqueID(); // Generate a unique ID for the item
        this.itemDescription = itemDescription ; //default description
        this.sellable = false; //default sellable
    }

    /**
     * Constructor for Item
     * @param itemName user facing name for item
     * @param itemType the enum for type of item
     * @param itemDescription user facing description for item
     * @param price price of item
     */
    public ItemComponent(String itemName, ItemType itemType, String itemDescription, int price, String texturePath) {
        this.itemTexture = ServiceLocator.getResourceService().getAsset(texturePath, Texture.class);
        this.itemType = itemType;
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
     * @return the texture of the item (Primarily used to the inventory display)
     */
    public Texture getItemTexture() {
        return this.itemTexture;
    }

    /**
     * Sets an Item's texture component to a given Texture
     *
     * @param itemTexture - The texture to set the item to.
     */
    public void setItemTexture(Texture itemTexture) {
        this.itemTexture = itemTexture;
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
     * Returns the type of the item
     * @return ItemType enum type of item
     */
    public ItemType getItemType() {
        return itemType;
    }

    /**
     * Generates a unique ID for the item
     * @return String random unique ID for each item
     */
    private String generateUniqueID() {
        return UUID.randomUUID().toString();
    }
}