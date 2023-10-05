package com.csse3200.game.components.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.services.ServiceLocator;
import java.util.Objects;

/**
 * A class used to combine all the data necessary to the individual inventory slots
 */
public class ItemSlot extends Stack {
	private static final String SELECTED_PATH = "images/selected.png";
	private static final String ITEM_FRAME_PATH = "images/itemFrame.png";
	private Texture itemTexture;
	private Integer count;
	private final Skin skin = ServiceLocator.getResourceService().getAsset("gardens-of-the-galaxy/gardens-of-the-galaxy.json", Skin.class);
	private Image background;
	private Image frame;
	private boolean selected;
	private Image itemImage;

	private Label label;
	private Stack draggable;

	/**
	 * Construct an itemSlot with a texture, count and selected state
	 *
	 * @param itemTexture texture of item's image
	 * @param count       count of item
	 * @param selected    boolean state of whether item slot is selected
	 */
	public ItemSlot(Texture itemTexture, Integer count, boolean selected) {
		this.itemTexture = itemTexture;
		this.count = count;
		this.background = new Image(ServiceLocator.getResourceService().getAsset(SELECTED_PATH, Texture.class));
		this.frame = new Image(ServiceLocator.getResourceService().getAsset(ITEM_FRAME_PATH, Texture.class));
		this.selected = selected;
		this.createItemSlot();
	}

	/**
	 * Construct an itemSlot with a texture and selected state
	 *
	 * @param itemTexture texture of item's image
	 * @param selected    boolean state of whether item slot is selected
	 */
	public ItemSlot(Texture itemTexture, boolean selected) {
		this.itemTexture = itemTexture;
		this.count = null;
		this.background = new Image(ServiceLocator.getResourceService().getAsset(SELECTED_PATH, Texture.class));
		this.frame = new Image(ServiceLocator.getResourceService().getAsset(ITEM_FRAME_PATH, Texture.class));
		this.selected = selected;
		this.createItemSlot();
	}

	/**
	 * Construct an itemSlot with a selected state
	 *
	 * @param selected boolean state of whether item slot is selected
	 */
	public ItemSlot(boolean selected) {
		this.itemTexture = null;
		this.count = null;
		this.background = new Image(ServiceLocator.getResourceService().getAsset(SELECTED_PATH, Texture.class));
		this.frame = new Image(ServiceLocator.getResourceService().getAsset(ITEM_FRAME_PATH, Texture.class));
		this.selected = selected;
		this.createItemSlot();

	}

	/**
	 * Set the item count
	 *
	 * @param count integer of number of item
	 */
	public void setCount(Integer count) {
		if (Objects.equals(count, this.count)) {
			return;
		}
		this.count = count;
		if (this.count > 1) {
			if (label == null) {
				label = new Label(this.count + " ", this.skin);
				label.setColor(Color.BLACK);
				label.setAlignment(Align.bottomRight);
				draggable.add(label);
			} else {
				label.setText(this.count + " ");
				draggable.add(label);

			}
		} else {
			draggable.removeActor(label);
			if (label != null) {
				this.label = null;

			}
		}
	}

	/**
	 * Get the item count
	 *
	 * @return count integer of number of item
	 */
	public Integer getCount() {
		if (count != null) {
			return count;
		}
		return -1;
	}

	/**
	 * Set the item texture
	 *
	 * @return itemTexture texture of item's image
	 */
	public Texture getItemTexture() {
		return this.itemTexture;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	/**
	 * Creates the itemSlot
	 */
	private void createItemSlot() {
		draggable = new Stack();

		//Add the selection background if necessary
		if (this.selected) {
			this.add(this.background);
		}

		//Add the item frame image to the item slot
		this.add(this.frame);

		//Add the item image to the itemSlot
		if (this.itemTexture != null) {
			itemImage = new Image(this.itemTexture);
			draggable.add(itemImage);
		}

		// Add or update the count label if the number is not 0
		if (this.count != null && this.count > 1) {
			if (label == null) {
				label = new Label(this.count + " ", this.skin);
				label.setColor(Color.BLACK);
				label.setAlignment(Align.bottomRight);
				draggable.add(label);
			} else {
				label.setText(this.count + " ");
			}
		}
		if (this.count != null && this.count <= 1 && label != null) {
			draggable.removeActor(label);
			this.label = null;
		}

		this.add(draggable);

	}

	/**
	 * Get the item image
	 *
	 * @return the item image
	 */
	public Image getItemImage() {
		return itemImage;
	}

	public Stack getDraggable() {
		return draggable;
	}

	public boolean setDraggable(Stack stack) {
		boolean ans = false;
		if (!this.removeActor(this.draggable)) {
			ans = true;
		}

		if (stack != null) {
			this.draggable = stack;
			this.add(stack);
		}
		return ans;
	}

	public void setItemImage(Image image) {
		draggable.removeActor(itemImage);
		if (image != null) {
			draggable.addActorAt(0, image);
			this.itemImage = image;
		}
	}

	/**
	 * Make the slot selected
	 */
	public void setSelected() {
		selected = true;
		this.addActorAt(0, this.background);
	}

	/**
	 * Make the slot unselected
	 */
	public void setUnselected() {
		selected = false;
		this.removeActor(this.background);
	}
}
