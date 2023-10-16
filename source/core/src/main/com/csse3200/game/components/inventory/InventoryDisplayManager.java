package com.csse3200.game.components.inventory;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.sound.EffectSoundFile;
import com.csse3200.game.services.sound.InvalidSoundFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class InventoryDisplayManager {
    private final List<InventoryDisplay> inventoryDisplays;
    private final Stage stage;

    private static final Logger logger = LoggerFactory.getLogger(InventoryDisplayManager.class);

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

        List<InventoryDisplay> openInventoryDisplays = inventoryDisplays.stream()
                .filter(InventoryDisplay::isOpen)
                .toList();

        int displayCount = openInventoryDisplays.size();

        if (displayCount == 1) {
            Window window = openInventoryDisplays.get(0).getWindow();
            window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, stage.getHeight() / 2 - window.getHeight() / 2);
        }
        else if (displayCount == 2){
            InventoryDisplay displayOne = openInventoryDisplays.get(0);
            InventoryDisplay displayTwo = openInventoryDisplays.get(1);
            displayTwo.refreshInventory();
            displayOne.refreshInventory();
            addTarget(displayTwo, displayOne);
            addTarget(displayOne, displayTwo);
            float totalHeight = displayOne.getWindow().getHeight() + displayTwo.getWindow().getHeight();

            float yOne = (stage.getHeight() - totalHeight - 50) / 2;
            float yTwo = yOne + displayOne.getWindow().getHeight() + 50;

            displayOne.getWindow().setPosition(stage.getWidth() / 2 - displayOne.getWindow().getWidth() / 2, yOne);
            displayTwo.getWindow().setPosition(stage.getWidth() / 2 - displayTwo.getWindow().getWidth() / 2, yTwo);
        }
    }

        public void addTarget(InventoryDisplay d1, InventoryDisplay d2) {
            final InventoryComponent slotInventory = d2.getInventory();
            final InventoryComponent sourceInventory = d1.getInventory();
            ArrayList<ItemSlot> slots = d2.getSlots();
            DragAndDrop dnd = d1.getDnd();
            for (ItemSlot slot: slots) {
                dnd.addTarget(new DragAndDrop.Target(slot) {

                    @Override
                    public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                        return true;
                    }

                    @Override
                    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                        Stack sourceActor = (Stack) source.getActor();
                        ItemSlot sourceSlot = d1.getMap().get(sourceActor);
                        d1.getMap().get(sourceActor).setDraggable(slot.getDraggable());
                        slot.setDraggable(sourceActor);
                        Entity item = sourceInventory.getItem(d1.getIndexes().get(d1.getMap().get(sourceActor)));
                        int count = sourceInventory.getItemCount(item);

                        // gets slot inventory's map then puts the dragged item with destination slot
                        slotInventory.getEntity().getComponent(InventoryDisplay.class).getMap().put((Stack) source.getActor(), slot);

                        slotInventory.getEntity().getComponent(InventoryDisplay.class).getMap().remove(slot.getDraggable());
                        sourceInventory.removeAll(item);
                        if (slotInventory.getItem(d2.getIndexes().get(slot)) != null) {
                            Entity destItem = slotInventory.getItem(d2.getIndexes().get(slot));
                            int destItemCount = slotInventory.getItemCount(destItem);
                            int slotNum = d1.getIndexes().get(sourceSlot);

                            sourceInventory.addMultipleItem(destItemCount,destItem,slotNum);
                            slotInventory.removeAll(destItem);
                        }

                        slotInventory.addMultipleItem(count, item, d2.getIndexes().get(slot));

                        d1.refreshInventory();
                        d2.refreshInventory();

                        addTarget(d2, d1);
                        addTarget(d1, d2);

                        try {
                            ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.DROP_ITEM);
                        } catch (InvalidSoundFileException e) {
                            logger.error("sound not loaded");
                        }

                    }
                });
            }
            if (d2.getBin() != null) {
                dnd.addTarget(new DragAndDrop.Target(d2.getBin()) {
                    @Override
                    public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                        ItemSlot itemSlot = d1.getMap().get((Stack) source.getActor());
                        return !InventoryComponent.getForbiddenItems().contains(sourceInventory.getItemPlace().get(d1.getIndexes().get(itemSlot)));
                    }

                    @Override
                    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                        ItemSlot itemSlot = d1.getMap().get((Stack) source.getActor());
                        itemSlot.removeActor(source.getActor());
                        itemSlot.add(source.getActor());
                        sourceInventory.removeItem(sourceInventory.getHeldItemsEntity().get(sourceInventory.getItemPlace().get(d1.getIndexes().get(itemSlot))));
                        d1.refreshInventory();
                        d1.addTooltips();
                        try {
                            ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.DELETE_ITEM);
                        } catch (InvalidSoundFileException e) {
                            logger.error("sound not loaded");
                        }
                    }
                });
            }
        }

        public void addTargets() {
            List<InventoryDisplay> openInventoryDisplays = inventoryDisplays.stream()
                    .filter(InventoryDisplay::isOpen)
                    .toList();

            if (openInventoryDisplays.size() == 2) {
                InventoryDisplay displayOne = openInventoryDisplays.get(0);
                InventoryDisplay displayTwo = openInventoryDisplays.get(1);
                addTarget(displayTwo, displayOne);
                addTarget(displayOne, displayTwo);
            }
        }


}