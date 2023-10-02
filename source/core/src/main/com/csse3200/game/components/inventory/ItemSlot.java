package com.csse3200.game.components.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;

/**
 * A class used to combine all the data necessary to the individual inventory slots
 */
public class ItemSlot extends Stack {
    private Texture itemTexture;
    private Integer count;
    private final Skin skin = new Skin(Gdx.files.internal("gardens-of-the-galaxy/gardens-of-the-galaxy.json"));

    private Image background;
    private Image frame;

    private boolean selected;
    private Image itemImage;

    private Label label;

    /**
     * Construct an itemSlot with a texture, count and selected state
     * @param itemTexture texture of item's image
     * @param count count of item
     * @param selected boolean state of whether item slot is selected
     */
    public ItemSlot(Texture itemTexture, Integer count, boolean selected) {
        this.itemTexture = itemTexture;
        this.count = count;
        this.background = new Image(new Texture(Gdx.files.internal("images/selected.png")));
        this.frame = new Image(new Texture(Gdx.files.internal("images/itemFrame.png")));
        this.selected = selected;
        this.createItemSlot();
    }

    /**
     * Construct an itemSlot with a texture and selected state
     * @param itemTexture texture of item's image
     * @param selected boolean state of whether item slot is selected
     */
    public ItemSlot(Texture itemTexture, boolean selected) {
        this.itemTexture = itemTexture;
        this.count = null;
        this.background = new Image(new Texture(Gdx.files.internal("images/selected.png")));
        this.frame = new Image(new Texture(Gdx.files.internal("images/itemFrame.png")));
        this.selected = selected;
        this.createItemSlot();
    }

    /**
     * Construct an itemSlot with a selected state
     * @param selected boolean state of whether item slot is selected
     */
    public ItemSlot(boolean selected) {
        this.itemTexture = null;
        this.count = null;
        this.background = new Image(new Texture(Gdx.files.internal("images/selected.png")));
        this.frame = new Image(new Texture(Gdx.files.internal("images/itemFrame.png")));
        this.selected = selected;
        this.createItemSlot();

    }

    /**
     * Set the item count
     * @param count integer of number of item
     */
    public void setCount(Integer count) {
        this.count = count;
        createItemSlot();
    }

    /**
     * Get the item count
     * @return count integer of number of item
     */
    public Integer getCount() {
        return count;
    }

    /**
     * Set the item texture
     * @return  itemTexture texture of item's image
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

        //Add the selection background if necessary
        if (this.selected) {
            this.add(this.background);
        }

        //Add the item frame image to the item slot
        this.add(this.frame);

        //Add the item image to the itemSlot
        if (this.itemTexture != null) {
            itemImage = new Image(this.itemTexture);
            this.add(itemImage);
        }

        // Add or update the count label if the number is not 0
        if (this.count != null && this.count > 0) {
            if (label == null) {
                label = new Label(this.count + " ", this.skin);
                label.setColor(Color.BLACK);
                label.setAlignment(Align.bottomRight);
                this.add(label);
            } else {
                label.setText(this.count + " ");
            }
        }
    }

    /**
     * Get the item image
     * @return the item image
     */
    public Image getItemImage() {
        return itemImage;
    }
    public void setItemImage(Image image) {
        this.removeActor(itemImage);
        itemImage = null;
        if (image != null) {
            this.add(image);
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
