package com.csse3200.game.components.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToolbarDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ToolbarDisplay.class);
    private Table table;
    private Window window;
    private boolean isOpen;

    private InventoryComponent inventory;

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    public ToolbarDisplay() {
    }


    private int selectedSlot = -1;

    /**
     * Creates reusable ui styles and adds actors to the stage.
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
     * Creates actors and positions them on the stage using a table.
     * @see Table for positioning options
     */

    private void resetToolbar(){
        //logger.info("Reset Toolbar..........................................");
        //window.remove();
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
            Label label = new Label(String.valueOf(idx) + " ", skin); //please please please work
            label.setColor(Color.DARK_GRAY);
            label.setAlignment(Align.topLeft);
            //stack.add(new Image(new Texture("images/itemFrame.png")));
            ItemSlot item;
            if (inventory.getItemPos(i) == null){
                //logger.info("Null Item at "+i );
                item = new ItemSlot(i == selectedSlot);
                //stack.add(new Image(new Texture("images/itemFrame.png")));
            } else {
                item = new ItemSlot(
                        inventory.getItemPos(i).getComponent(ItemComponent.class).getItemTexture(),
                        i == selectedSlot);

                //stack.add(new Image(inventory.getItemPos(i).getComponent(ItemComponent.class).getItemTexture()));
            }
            item.add(label);
            table.add(item).pad(10, 10, 10, 10).fill();
            //table.add(stack).pad(10, 10, 10, 10).fill();
        }
        //window = new Window("", skin);
        window.pad(40, 5 , 5, 5); // Add padding to with so that the text doesn't go offscreen
        window.add(table); //Add the table to the window
        window.pack(); // Pack the window to the size
        window.setMovable(false);
        window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, 0); // Clip to the bottom of the window on the stage
        window.setVisible(isOpen);
        // Add the window to the stage
        stage.addActor(window);
    }
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
            Label label = new Label(String.valueOf(idx) + " ", skin); //please please please work
            label.setColor(Color.DARK_GRAY);
            label.setAlignment(Align.topLeft);

            ItemSlot item = new ItemSlot(i == selectedSlot);
            //item.setDebug(true);
            //Create the itemslot, check if it is the active slot
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
    /*
    public void setDragItems(ArrayList<Actor> actors, Map<Image,ItemSlot> map) {
        for (Actor item : actors) {
            if (item != null) {
                dnd.addSource(new DragAndDrop.Source(item) {
                    DragAndDrop.Payload payload = new DragAndDrop.Payload();

                    @Override
                    public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                        payload.setObject(getActor());
                        payload.setDragActor(getActor());
                        stage.addActor(getActor());

                        return payload;
                    }

                    @Override
                    public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                        if (target == null) {
                            ItemSlot itemSlot = map.get(getActor());
                            itemSlot.removeActor(getActor());
                            itemSlot.add(getActor());
                        }
                    }
                });
            }

            for (Cell<ItemSlot> targetItem : table.getCells()) {
                dnd.addTarget(new DragAndDrop.Target(targetItem.getActor()) {
                    ItemSlot slot = targetItem.getActor();

                    @Override
                    public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {

                        return payload.getDragActor() != slot.getItemImage();
                    }

                    @Override
                    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                        // called when left click is touched up


                        ItemSlot sourceSlot = map.get((source.getActor()));
                        inventory.setHeldItem(indexes.get(sourceSlot));
                        inventory.swapPosition(indexes.get(sourceSlot), indexes.get(slot));
                        map.put(slot.getItemImage(), sourceSlot);
                        sourceSlot.setItemImage(slot.getItemImage());
                        map.put((Image) payload.getDragActor(),slot);
                        slot.setItemImage((Image)payload.getDragActor());
                        updateItemSlot(1);


                    }
                });

            }
        }
    } */

    /**
     * Draw stage for render
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch)  {
        // draw is handled by the stage
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
