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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Display the UI for the toolbar
 */
public class ToolbarDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(ToolbarDisplay.class);
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
        entity.getEvents().addListener("updateToolbar", this::updateToolbar);
        entity.getEvents().addListener("toggleInventory",this::toggleOpen);
        entity.getEvents().addListener("hotkeySelection",this::updateItemSlot);
        inventory = entity.getComponent(InventoryComponent.class);
    }

    /**
     * Updates actors and re-positions them on the stage using a table.
     * @see Table for positioning options
     */

    private void createToolbar(){
        if(!isOpen){
            return;
        }
        table.reset();
        Skin skin = new Skin(Gdx.files.internal("gardens-of-the-galaxy/gardens-of-the-galaxy.json"));
        table = new Table(skin);
        table.defaults().size(64, 64);
        table.pad(40, 5 , 5, 5);
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
            if (inventory.getItem(i) == null){
                item = new ItemSlot(i == selectedSlot);
            } else {
                item = new ItemSlot(
                        inventory.getItemTexture(i), inventory.getItemCount(i),
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
//        stage.addActor(window);
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
//        table.bottom().center();
//        stage.addActor(table);
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
    private void toggleOpen(){
        if (this.isOpen) {
            this.window.setVisible(false);
            this.isOpen = false;
        } else {
            this.window.setVisible(true);
            this.isOpen = true;
        }
    }

    /**
     * Updates the player's inventory toolbar on the ui.
     */
    private void updateToolbar() {
        this.inventory = entity.getComponent(InventoryComponent.class);
        // refresh the ui as per the new inventory.
        createToolbar();
    }

    /**
     * Updates the player's inventory toolbar selected itemSlot.
     * @param slotNum updated slot number
     */
    private void updateItemSlot(int slotNum) {
        this.selectedSlot = inventory.getHeldIndex();
        // refresh ui to reflect new selected slot
        createToolbar();
    }

    /**
     * Dispose of Toolbar
     */
    @Override
    public void dispose() {
        super.dispose();
    }
}
