package com.csse3200.game.components.items;

import java.util.UUID;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceLocator;


public class ItemComponent extends Component {
	private String itemName; // User facing name for item. can be customised by user.
	private String itemDescription; // User facing description for item.
	private final String itemId;
	private int price; // Price of item
	private final boolean sellable; // is the item sellable
	private final ItemType itemType; // Type of item
	private Texture itemTexture;

	private final boolean perishable; // will the item be consumed on use


	/**
	 * Constructor for Item
	 *
	 * @param itemName user facing name for item
	 * @param itemType the enum for type of item
	 * @param texturePath path for the item texture
	 */
	public ItemComponent(String itemName, ItemType itemType, String texturePath) {
		this(itemName, itemType, "", 0, false, false, texturePath);
	}

	/**
	 * Constructor for Item
	 *
	 * @param itemName user facing name
	 * @param itemType the enum for type of item
	 * @param price    sellable price of the item
	 * @param texturePath path for the item texture
	 */
	public ItemComponent(String itemName, ItemType itemType, int price, String texturePath) {
		this(itemName, itemType, "", price, true, false, texturePath);
	}

	/**
	 * Constructor for Item
	 *
	 * @param itemName        user facing name for item
	 * @param itemType        the enum for type of item
	 * @param itemDescription user facing description for item
	 * @param texturePath path for the item texture
	 */
	public ItemComponent(String itemName, ItemType itemType, String itemDescription, String texturePath) {
		this(itemName, itemType, itemDescription, 0, false, false, texturePath);
	}


	/**
	 * Constructor for Item
	 *
	 * @param itemName        user facing name for item
	 * @param itemType        the enum for type of item
	 * @param itemDescription user facing description for item
	 * @param price           price of item
	 * @param texturePath path for the item texture
	 */
	public ItemComponent(String itemName, ItemType itemType, String itemDescription, int price, String texturePath) {
		this(itemName, itemType, itemDescription, price, true, false, texturePath);
	}

	/**
	 *
	 * @param itemName        user facing name for item
	 * @param itemType        the enum for type of item
	 * @param itemDescription user facing description for item
	 * @param price           price of item
	 * @param sellable        whether the item is sellable
	 * @param perishable      whether the item is perishable
	 * @param texturePath     path for the item texture
	 */
	private ItemComponent(String itemName, ItemType itemType, String itemDescription, int price, boolean sellable, boolean perishable, String texturePath) {
		this.itemTexture = ServiceLocator.getResourceService().getAsset(texturePath, Texture.class);
		this.itemType = itemType;
		this.itemName = itemName;
		this.itemId = generateUniqueID(); // Generate a unique ID for the item
		this.itemDescription = itemDescription; //default description
		this.price = price;
		this.sellable = sellable;
		this.perishable = perishable;
	}

	/**
	 * Returns if the item is consumable
	 *
	 * @return boolean whether the item is perishable or not
	 */
	public boolean isPerishable() {
		return perishable;
	}

	/**
	 * Returns the price of the item
	 *
	 * @return int price of item
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * Sets the price of the item
	 *
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
	 * @param texturePath - path of the new item texture
	 */
	public void setItemTexture(String texturePath) {
		this.itemTexture = ServiceLocator.getResourceService().getAsset(texturePath, Texture.class);
	}

	/**
	 * Returns selalble bool of item
	 *
	 * @return true if item is sellable, false otherwise
	 */
	public boolean isSellable() {
		return sellable;
	}


	/**
	 * Returns the name of the item
	 *
	 * @return int price of item
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * Returns the id of the item
	 *
	 * @return String id of item
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * Returns the description of the item
	 *
	 * @return String description of item
	 */
	public String getItemDescription() {
		return itemDescription;
	}

	/**
	 * Sets the price of the item
	 *
	 * @param description string description of the item
	 */
	public void setItemDescription(String description) {
		itemDescription = description;
	}

	/**
	 * Sets the item name of the item
	 *
	 * @param name string name of the item
	 */
	public void setItemName(String name) {
		itemName = name;
	}

	/**
	 * Returns the type of the item
	 *
	 * @return ItemType enum type of item
	 */
	public ItemType getItemType() {
		return itemType;
	}

	/**
	 * Generates a unique ID for the item
	 *
	 * @return String random unique ID for each item
	 */
	private String generateUniqueID() {
		return UUID.randomUUID().toString();
	}
}