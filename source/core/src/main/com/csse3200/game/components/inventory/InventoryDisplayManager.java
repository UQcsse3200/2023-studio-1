package com.csse3200.game.components.inventory;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

public class InventoryDisplayManager {
    private final List<InventoryDisplay> inventoryDisplays;
    private final Stage stage;

    private static final Logger logger = LoggerFactory.getLogger(InventoryDisplayManager.class);
    private DragAndDrop dnd;

    /**
     * Initialise the Inventory Display Manager
     * @param stage to place display on
     */
    public InventoryDisplayManager(Stage stage) {
        this.inventoryDisplays = new ArrayList<>();
        this.stage = stage;
    }

    /**
     * Add an inventoryDisplay to the manager
     * @param inventoryDisplay display to be added
     */
    public void addInventoryDisplay(InventoryDisplay inventoryDisplay) {
        this.inventoryDisplays.add(inventoryDisplay);
    }

    /**
     * Remove an inventoryDisplay to the manager
     * @param inventoryDisplay display to be removed
     */
    public void removeInventoryDisplay(InventoryDisplay inventoryDisplay) {
        logger.info("Removing inventory display");
        this.inventoryDisplays.remove(inventoryDisplay);
    }

    /**
     * Remove an inventoryDisplay to the manager
     */
    public List<InventoryDisplay> getInventoryDisplays() {
        return this.inventoryDisplays;
    }

    /**
     * Update the position of the displays
     */
    public void updateDisplays() {
        dnd = new DragAndDrop();
        List<InventoryDisplay> openInventoryDisplays = inventoryDisplays.stream()
                .filter(InventoryDisplay::isOpen)
                .toList();

        int displayCount = openInventoryDisplays.size();

        if (displayCount == 1) {
            Window window = (Window) openInventoryDisplays.get(0).getWindow();
            window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, stage.getHeight() / 2 - window.getHeight() / 2);
        }
        else if (displayCount == 2){
            InventoryDisplay displayOne = openInventoryDisplays.get(0);
            InventoryDisplay displayTwo = openInventoryDisplays.get(1);

            float totalHeight = displayOne.getWindow().getHeight() + displayTwo.getWindow().getHeight();

            float yOne = (stage.getHeight() - totalHeight - 50) / 2;
            float yTwo = yOne + displayOne.getWindow().getHeight() + 50;

            displayOne.getWindow().setPosition(stage.getWidth() / 2 - displayOne.getWindow().getWidth() / 2, yOne);
            displayTwo.getWindow().setPosition(stage.getWidth() / 2 - displayTwo.getWindow().getWidth() / 2, yTwo);
        }
    }

    public void setDragAndDrop(InventoryDisplay d1, InventoryDisplay d2) {
        d1.getDnd().clear();
        d2.getDnd().clear();
        System.out.println(((Table) (d2.getWindow().getCells().get(0).getActor())).getCells());
        for (Cell cell: ((Table)(d2.getWindow().getCells().get(0).getActor())).getCells()) {
            if (cell.getActor() instanceof ItemSlot) {
                dnd.addSource(new DragAndDrop.Source(cell.getActor()) {
                    final DragAndDrop.Payload payload = new DragAndDrop.Payload();
                    @Override
                    public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                        payload.setObject(getActor());
                        payload.setDragActor(getActor());
                        stage.addActor(getActor());
                        dnd.setDragActorPosition(50, -getActor().getHeight() / 2);

                        return payload;
                    }
                });
                dnd.addTarget(new DragAndDrop.Target(cell.getActor()) {
                    final ItemSlot slot = (ItemSlot) cell.getActor();

                    @Override
                    public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                        return true;
                    }

                    @Override
                    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                        //ItemSlot sourceSlot = map.get((source.getActor()));

                        //inventory.swapPosition(indexes.get(sourceSlot), indexes.get(slot));
                        //map.put(slot.getDraggable(), sourceSlot);

                        //map.put((Stack) payload.getDragActor(), slot);

                        //sourceSlot.setDraggable(slot.getDraggable());
                        slot.setDraggable((Stack) source.getActor());

                        //entity.getEvents().trigger("updateToolbar");
                        //displayTwo.getInventory().setHeldItem(displayTwo.getInventory().getHeldIndex());
                    }
                });
            }
        }
    }

}