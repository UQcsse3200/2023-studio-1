package com.csse3200.game.components.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;


public class ItemSlot extends Stack {
    private Texture itemTexture;
    private Integer count;
    private Skin skin;

    private Image background;
    private Image frame;

    private boolean selected;
    private Image itemImage;

    public ItemSlot(Texture itemTexture, Integer count, boolean selected) {
        this.itemTexture = itemTexture;
        this.count = count;
        this.skin = new Skin(Gdx.files.internal("gardens-of-the-galaxy/gardens-of-the-galaxy.json"));
        this.background = new Image(new Texture(Gdx.files.internal("images/selected.png")));
        this.frame = new Image(new Texture(Gdx.files.internal("images/itemFrame.png")));
        this.selected = selected;
        this.createItemSlot();
    }

    public ItemSlot(Texture itemTexture, boolean selected) {
        this.itemTexture = itemTexture;
        this.count = null;
        this.skin = new Skin(Gdx.files.internal("gardens-of-the-galaxy/gardens-of-the-galaxy.json"));
        this.background = new Image(new Texture(Gdx.files.internal("images/selected.png")));
        this.frame = new Image(new Texture(Gdx.files.internal("images/itemFrame.png")));
        this.selected = selected;
        this.createItemSlot();
    }

    public ItemSlot(boolean selected) {
        this.itemTexture = null;
        this.count = null;
        this.skin = new Skin(Gdx.files.internal("gardens-of-the-galaxy/gardens-of-the-galaxy.json"));
        this.background = new Image(new Texture(Gdx.files.internal("images/selected.png")));
        this.frame = new Image(new Texture(Gdx.files.internal("images/itemFrame.png")));
        this.selected = selected;
        this.createItemSlot();

    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setTexture(Texture itemTexture) {
        this.itemTexture = itemTexture;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Creates actors and positions them on the stage using a table.
     * @see Table for positioning options
     */
    private void createItemSlot() {
        Label label = new Label(String.valueOf(this.count) + " ", this.skin); //please please please work
        label.setColor(Color.BLACK);
        label.setAlignment(Align.bottomRight);
        this.setTouchable(Touchable.childrenOnly);

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

        //Add the count label if the number is not 0
        if (this.count != null && this.count > 0) {
            this.add(label);
        }
    }

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
}
