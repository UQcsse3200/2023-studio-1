package com.csse3200.game.components.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.ui.UIComponent;

/**
 * Display the UI for the toolbar
 */
public class ToolbarDisplay extends UIComponent {
    private Table table;
    private Window window;
    private boolean isOpen;
    private InventoryComponent inventory;
    private int selectedSlot = -1;

    /**
     * Creates the event listeners, ui, and gets the UI.
     */
    @Override
    public void create() {
        super.create();
        addActors();
        isOpen = true;
        entity.getEvents().addListener("updateInventory", this::updateInventory);
        entity.getEvents().addListener("toggleInventory",this::toggleOpen);
        entity.getEvents().addListener("hotkeySelection",this::updateItemSlot);
        inventory = entity.getComponent(InventoryDisplay.class).getInventory();
    }

    /**
     * Updates actors and re-positions them on the stage using a table.
     * @see Table for positioning options
     */

    private void resetToolbar(){
        window.reset();
        Skin skin = new Skin(Gdx.files.internal("gardens-of-the-galaxy/gardens-of-the-galaxy.json"));
        table = new Table(skin);
        table.defaults().size(64, 64);
        table.pad(10);

        for (int i = 0; i < 10; i++){
            int idx = i + 1;
            if (idx == 10) {
                idx = 0;
            }
            Label label = new Label(" " + String.valueOf(idx), skin); //please please please work
            label.setColor(Color.BLUE);
            label.setAlignment(Align.topLeft);
            ItemSlot item;
            if (inventory.getItemPos(i) == null){
                item = new ItemSlot(i == selectedSlot);
            } else {
                item = new ItemSlot(
                        inventory.getItemPos(i).getComponent(ItemComponent.class).getItemTexture(),
                        i == selectedSlot);

            }
            item.add(label);
            table.add(item).pad(10, 10, 10, 10).fill();
        }
        window.pad(40, 5 , 5, 5); // Add padding to with so that the text doesn't go offscreen
        window.add(table); //Add the table to the window
        window.pack(); // Pack the window to the size
        window.setMovable(false);
        window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, 0); // Clip to the bottom of the window on the stage
        window.setVisible(isOpen);
        // Add the window to the stage
        stage.addActor(window);
    }

    /**
     *  Creates actors and positions them on the stage using a table.
     *  @see Table for positioning options
     */
    private void addActors() {
        Skin skin = new Skin(Gdx.files.internal("gardens-of-the-galaxy/gardens-of-the-galaxy.json"));
        table = new Table(skin);
        table.defaults().size(64, 64);
        for (int i = 0; i < 10; i++) {
            //Set the indexes for the toolbar
            int idx = i + 1;
            if (idx == 10) {
                idx = 0;
            }
            //Create the label for the item slot
            Label label = new Label(" " + String.valueOf(idx), skin); //please please please work
            label.setColor(Color.BLUE);
            label.setAlignment(Align.topLeft);

            //Create the itemslot, check if it is the active slot
            ItemSlot item = new ItemSlot(i == selectedSlot);
            item.add(label);
            table.add(item).pad(10, 10, 10, 10).fill();
        }

        // Create a window for the inventory using the skin
        window = new Window("", skin);
        window.pad(40, 5, 5, 5); // Add padding to with so that the text doesn't go offscreen
        window.add(table); //Add the table to the window
        window.pack(); // Pack the window to the size
        window.setMovable(false);
        window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, 0); // Clip to the bottom of the window on the stage
        window.setVisible(true);
        // Add the window to the stage
        stage.addActor(window);
    }

    /**
     * Draw stage for render
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch)  {
    }

    /**
     * Toggle Toolbar to open state
     */
    public void toggleOpen(){
        if (isOpen) {
            window.setVisible(false);
            isOpen = false;
        } else {
            window.setVisible(true);
            isOpen = true;
        }
    }

    /**
     * Updates the player's inventory toolbar on the ui.
     */
    public void updateInventory() {
        inventory = entity.getComponent(InventoryDisplay.class).getInventory();
        // refresh the ui as per the new inventory.
        resetToolbar();
    }

    /**
     * Updates the player's inventory toolbar selected itemSlot.
     * @param slotNum updated slot number
     */
    public void updateItemSlot(int slotNum) {
        this.selectedSlot = inventory.getHeldIndex();
        // refresh ui to reflect new selected slot
        resetToolbar();
    }

    /**
     * Dispose of Toolbar
     */
    @Override
    public void dispose() {
        super.dispose();
    }
}
