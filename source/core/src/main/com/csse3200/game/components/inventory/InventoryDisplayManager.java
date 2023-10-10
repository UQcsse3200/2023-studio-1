package com.csse3200.game.components.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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
            displayTwo.refreshInventory();
            displayOne.refreshInventory();
            setDragAndDrop(displayOne,displayTwo);

            float totalHeight = displayOne.getWindow().getHeight() + displayTwo.getWindow().getHeight();

            float yOne = (stage.getHeight() - totalHeight - 50) / 2;
            float yTwo = yOne + displayOne.getWindow().getHeight() + 50;

            displayOne.getWindow().setPosition(stage.getWidth() / 2 - displayOne.getWindow().getWidth() / 2, yOne);
            displayTwo.getWindow().setPosition(stage.getWidth() / 2 - displayTwo.getWindow().getWidth() / 2, yTwo);
        }
    }

    public void setDragAndDrop(InventoryDisplay d1, InventoryDisplay d2) {
        //System.out.println(d1.getIndexes().size());
        d1.getDnd().clear();
        d2.getDnd().clear();
        //System.out.println(((Table) (d2.getWindow().getCells().get(0).getActor())).getCells());
        ArrayList<Actor> allActors = new ArrayList<>();
        allActors.addAll(d1.getActors());
        allActors.addAll(d2.getActors());

        Map<Stack, ItemSlot> map = new HashMap<>();
        map.putAll(d1.getMap());
        map.putAll(d2.getMap());
        Map<ItemSlot,Integer> indexes = new HashMap<>();
        indexes.putAll(d1.getIndexes());
        indexes.putAll(d2.getIndexes());
        //System.out.println(allActors);

        addDragActors(allActors, map);

        ArrayList<ItemSlot> slots = new ArrayList<>();
        slots.addAll(d1.getSlots());
        slots.addAll(d2.getSlots());

        int i = 0;
        for (ItemSlot slot: slots)  {
                dnd.addTarget(new DragAndDrop.Target(slot) {
                    final InventoryComponent slotInventory = d1.getIndexes().get(slot) == null ? d2.getInventory() : d1.getInventory();
                    Stack s;

                    @Override
                    public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                        //System.out.println("Dragg");
                        return true;
                    }

                    @Override
                    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                        System.out.println(d1.getMap().size());
                        System.out.println(d2.getMap().size());
                        System.out.println(map.get(source.getActor()));
                        InventoryDisplay sourceInventory = d1.getMap().get((Stack) source.getActor()) == null ? d2 : d1;
                        System.out.println(sourceInventory == d1 ? "d1":"d2");
                        ItemSlot sourceSlot = map.get(( (Stack) source.getActor()));
                        sourceSlot.setDraggable(slot.getDraggable());
                        slot.setDraggable((Stack) source.getActor());
                        /*Actor[] stackors = ((Stack) payload.getDragActor()).getChildren().toArray();
                        Stack s = new Stack();
                        Arrays.stream(stackors).forEach(s::add);*/


                        if (slotInventory == sourceInventory.getInventory()) {
                            slotInventory.swapPosition(indexes.get(sourceSlot), indexes.get(slot));
                            System.out.println(slotInventory.getItemPlace());

                            sourceInventory.getMap().put(slot.getDraggable(), sourceSlot);
                            sourceInventory.getMap().put((Stack) payload.getDragActor(), slot);

                            sourceSlot.setDraggable(slot.getDraggable());
                            slot.setDraggable((Stack) source.getActor());

                            sourceInventory.getEntity().getEvents().trigger("updateToolbar");
                            slotInventory.setHeldItem(slotInventory.getHeldIndex());
                        }
                        else {
                            System.out.println(sourceInventory.getMap().get((Stack) source.getActor()));
                            System.out.println(d2.getMap().get((Stack) source.getActor()));
                            Entity item = sourceInventory.getInventory()
                                    .getHeldItemsEntity()
                                    .get(sourceInventory
                                            .getInventory()
                                            .getItemPlace()
                                            .get(sourceInventory.getIndexes()
                                                    .get(map
                                                            .get((Stack) source.getActor()))));
                            int count = sourceInventory.getInventory().getItemCount(item);
                            s = (Stack) source.getActor();

                            slotInventory.getEntity().getComponent(InventoryDisplay.class).getMap().put((Stack) source.getActor(), slot);
                            System.out.println(d2.getMap().get((Stack) source.getActor()));
                            slotInventory.getEntity().getComponent(InventoryDisplay.class).getMap().remove(slot.getDraggable());
                            sourceInventory.getMap().put(slot.getDraggable(), sourceSlot);
                            sourceInventory.getMap().remove((Stack) source.getActor());
                            map.put(slot.getDraggable(), sourceSlot);
                            map.put((Stack) source.getActor(), slot);
                            sourceInventory.getInventory().removeAll(item);
                            if (slotInventory.getItem(indexes.get(slot)) != null) {
                                Entity destItem = slotInventory.getItem(indexes.get(slot));
                                int destItemCount = slotInventory.getItemCount(destItem);
                                int slotNum = indexes.get(sourceSlot);
                                sourceInventory.getInventory().addMultipleItem(destItemCount,destItem,slotNum);
                            }
                            slotInventory.addMultipleItem(count, item,indexes.get(slot));
                            System.out.println(slotInventory.getItemPlace());


                        }
                    }
                });
            }
        }

        public void addDragActors(ArrayList<Actor> allActors, Map<Stack, ItemSlot> map){
            for (Actor actor: allActors) {
                dnd.addSource(new DragAndDrop.Source(actor) {
                    final DragAndDrop.Payload payload = new DragAndDrop.Payload();
                    @Override
                    public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                        payload.setObject(getActor());
                        payload.setDragActor(getActor());
                        stage.addActor(getActor());
                        dnd.setDragActorPosition(50, -getActor().getHeight() / 2);

                        return payload;
                    }
                    @Override
                    public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                        if (target == null) {
                            ItemSlot itemSlot = map.get((Stack) getActor());
                            itemSlot.add(getActor());
                        }
                    }
                });
            }
        }

}