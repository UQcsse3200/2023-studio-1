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
    private Label countLabel;
    private Label toolbarLabel;

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
    public void setCount(Integer count) {  // Add this method to update the count label
        this.count = count;
        if (count != null && count > 0) {
            countLabel.setText(count + " ");
        } else {
            countLabel.setText("");
        }
    }
    /**
     * Get the item count
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

        // Add or update the count countLabel if the number is not 0
        if (this.count != null && this.count > 0) {
            if (countLabel == null) {
                countLabel = new Label(this.count + " ", this.skin);
                countLabel.setColor(Color.BLACK);
                countLabel.setAlignment(Align.bottomRight);
                this.add(countLabel);
            } else {
                countLabel.setText(this.count + " ");
            }
        }
        else {
            if (countLabel == null) {
                countLabel = new Label(" ", this.skin);
                countLabel.setColor(Color.BLACK);
                countLabel.setAlignment(Align.bottomRight);
                this.add(countLabel);
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
