package com.csse3200.game.components.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.sound.EffectSoundFile;
import com.csse3200.game.services.sound.InvalidSoundFileException;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Display the UI for the toolbar
 */
public class ToolbarDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ToolbarDisplay.class);
    private final Skin skin = ServiceLocator.getResourceService().getAsset("gardens-of-the-galaxy/gardens-of-the-galaxy.json", Skin.class);
    private final Table table = new Table(skin);
    private final Window window = new Window("", skin);
    private boolean isOpen;
    private InventoryComponent inventory;
    private int selectedSlot = -1;
    private final ArrayList<ItemSlot> slots = new ArrayList<>();

    /**
     * Creates the event listeners, ui, and gets the UI.
     */
    @Override
    public void create() {
        super.create();
        initialiseToolbar();
        isOpen = true;
        entity.getEvents().addListener("updateToolbar", this::updateInventory);
        entity.getEvents().addListener("toggleInventory",this::toggleOpen);
        entity.getEvents().addListener("hotkeySelection",this::updateItemSlot);
        inventory = entity.getComponent(InventoryComponent.class);
    }

    /**
     * Updates actors and re-positions them on the stage using a table.
     * @see Table for positioning options
     */

    private void updateToolbar(){
        for (int i = 0; i < 10; i++){
            int idx = i + 1;
            if (idx == 10) {
                idx = 0;
            }
            Label label = new Label(" " + idx, skin);
            label.setColor(Color.BLUE);
            label.setAlignment(Align.topLeft);

            ItemComponent item;
            int itemCount;
            Texture itemTexture;

            if (inventory != null && inventory.getItem(i) != null) {
                // Since the item isn't null, we want to make sure that the itemSlot at that position is modified
                item = inventory.getItem(i).getComponent(ItemComponent.class);
                itemCount = inventory.getItemCount(item.getEntity());
                itemTexture = item.getItemTexture();
                ItemSlot curSlot = slots.get(i);
                curSlot.setItemImage(new Image(itemTexture));
                curSlot.setCount(itemCount);


                curSlot.add(label);

                // Update slots array
                slots.set(i, curSlot);
            }
            else {
                ItemSlot curSlot = slots.get(i);
                curSlot.setItemImage(null);
                curSlot.setCount(0);
                slots.set(i, curSlot);
            }
        }
    }

    /**
     *  Creates actors and positions them on the stage using a table.
     *  @see Table for positioning options
     */
    private void initialiseToolbar() {
        logger.debug("Toolbar being made");
        table.defaults().size(64, 64);

        for (int i = 0; i < 10; i++) {
            //Set the indexes for the toolbar
            int idx = i + 1;
            if (idx == 10) {
                idx = 0;
            }
            // Create the label for the item slot
            Label label = new Label(" " + idx, skin); //please please please work
            label.setColor(Color.BLUE);
            label.setAlignment(Align.topLeft);

            // Check if slot is selected
            ItemSlot item = new ItemSlot(i == selectedSlot);
            item.add(label);
            int finalI = i;
            item.addListener(new InputListener() {
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    inventory.setHeldItem(finalI);
                    updateItemSlot(finalI);
                    return true;
                }
            });

            table.add(item).pad(10, 10, 10, 10).fill();
            slots.add(item);
        }

        // Customise window to ensure it meets functionality
        window.pad(40, 5, 5, 5);
        window.add(table);
        window.pack();
        window.setMovable(false);
        window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, 0);
        window.setVisible(true);
        stage.addActor(window);
    }

    /**
     * Draw stage for render
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch)  {
        // Handled else where
    }

    /**
     * Toggle Toolbar to open state
     */
    public void toggleOpen(){
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
    public void updateInventory() {
        this.inventory = entity.getComponent(InventoryComponent.class);
        updateToolbar();
    }

    /**
     * Updates the player's inventory toolbar selected itemSlot.
     * @param slotNum updated slot number
     */
    private void updateItemSlot(int slotNum) {
        this.selectedSlot = inventory.getHeldIndex();
        // refresh ui to reflect new selected slot

        for (int i = 0; i < 10; i++) {
            ItemSlot curSlot = slots.get(i);
            if (i != slotNum) {
                curSlot.setUnselected();
            }
            else {
                curSlot.setSelected();
            }
            slots.set(i, curSlot);
        }

        //Play the item select sound
        try {
            ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.HOTKEY_SELECT);
        } catch (InvalidSoundFileException e) {
            logger.info("Hotkey sound not loaded");
        }
    }
}
