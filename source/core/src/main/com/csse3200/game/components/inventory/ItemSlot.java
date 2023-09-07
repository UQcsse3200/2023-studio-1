package com.csse3200.game.components.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.ui.UIComponent;
import net.dermetfan.gdx.physics.box2d.PositionController;

import java.util.ArrayList;

public class ItemSlot extends Stack {
    private Texture itemTexture;
    private Integer count;
    private Skin skin;

    private Image background;
    private Image frame;
    public ItemSlot(Texture itemTexture, Integer count) {
        this.itemTexture = itemTexture;
        this.count = count;
        this.skin = new Skin(Gdx.files.internal("gardens-of-the-galaxy/gardens-of-the-galaxy.json"));
        this.background = new Image(new Texture(Gdx.files.internal("images/selected.png")));
        this.frame = new Image(new Texture(Gdx.files.internal("images/itemFrame.png")));
        this.createItemSlot();
    }

    public ItemSlot(Texture itemTexture) {
        this.itemTexture = itemTexture;
        this.count = null;
        this.skin = new Skin(Gdx.files.internal("gardens-of-the-galaxy/gardens-of-the-galaxy.json"));
        this.background = new Image(new Texture(Gdx.files.internal("images/selected.png")));
        this.frame = new Image(new Texture(Gdx.files.internal("images/itemFrame.png")));
        this.createItemSlot();
    }

    public ItemSlot() {
        this.itemTexture = null;
        this.count = null;
        this.skin = new Skin(Gdx.files.internal("gardens-of-the-galaxy/gardens-of-the-galaxy.json"));
        this.background = new Image(new Texture(Gdx.files.internal("images/selected.png")));
        this.frame = new Image(new Texture(Gdx.files.internal("images/itemFrame.png")));
        this.createItemSlot();
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setTexture(Texture itemTexture) {
        this.itemTexture = itemTexture;
    }

    /**
     * Creates actors and positions them on the stage using a table.
     * @see Table for positioning options
     */
    private void createItemSlot() {
        Label label = new Label(String.valueOf(this.count) + " ", this.skin); //please please please work
        label.setColor(Color.BLACK);
        label.setAlignment(Align.bottomRight);

        this.add(this.frame);

        if (this.itemTexture != null) {
            this.add(new Image(this.itemTexture));
        }

        if (this.count != null && this.count > 0) {
            this.add(label);
        }
    }

}
