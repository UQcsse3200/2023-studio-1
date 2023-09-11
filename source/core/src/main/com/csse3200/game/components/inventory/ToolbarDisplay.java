package com.csse3200.game.components.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Align;

import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ToolbarDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ToolbarDisplay.class);
    private Table table;
    private Window window;
    private boolean isOpen;

    private InventoryComponent inventory;

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private Map<ItemSlot,Integer> indexes;
    private DragAndDrop dnd;

    public ToolbarDisplay() {
    }


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
        ArrayList<Actor> actors = new ArrayList<>();
        final Map<ItemSlot,Container<ItemSlot>> map = new HashMap<>();

        for (int i = 0; i < 10; i++){
            Label label = new Label(String.valueOf(i), skin.get("default", Label.LabelStyle.class));
            //set the bounds of the label
            label.setBounds(label.getX() + 15, label.getY(), label.getWidth(), label.getHeight());
            //Stack stack = new Stack();
            //stack.add(new Image(new Texture("images/itemFrame.png")));
            ItemSlot item;
            if (inventory.getItemPos(i) == null){
                //logger.info("Null Item at "+i );
                item = new ItemSlot();

                //stack.add(new Image(new Texture("images/itemFrame.png")));
            } else {
                item = new ItemSlot(
                        inventory.getItemPos(i).getComponent(ItemComponent.class).getItemTexture(),
                        inventory.getItemCount(inventory.getItemPos(i)));
                //stack.add(new Image(inventory.getItemPos(i).getComponent(ItemComponent.class).getItemTexture()));
            }
            Container<ItemSlot> container = new Container<>(item);
            container.setTouchable(Touchable.enabled);
            //container.setDebug(true);
            //item.setDebug(true);
            map.put(item, container);
            actors.add(item);
            indexes.put(item, i);
            table.add(container).pad(10, 10, 10, 10).fill();
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
        setDragItems(actors, map);
    }
    private void addActors() {
        Skin skin = new Skin(Gdx.files.internal("gardens-of-the-galaxy/gardens-of-the-galaxy.json"));
        table = new Table(skin);
        table.defaults().size(64, 64);
        ArrayList<Actor> actors = new ArrayList<>(); // list of source items in DragAndDrop
        final Map<ItemSlot,Container<ItemSlot>> map = new HashMap<>(); // map of items to their containers
        indexes = new HashMap<>(); // map of items to their index
        for (int i = 0; i < 10; i++) {

            Label label = new Label(String.valueOf(i) + " ", skin); //please please please work
            label.setColor(Color.DARK_GRAY);
            label.setAlignment(Align.topLeft);
            ItemSlot item = new ItemSlot();
            Container<ItemSlot> container = new Container<>(item);
            container.setTouchable(Touchable.enabled);
            //container.setDebug(true);
            //item.setDebug(true);
            map.put(item, container);
            indexes.put(item, i);
            actors.add(item);
            item.add(label);
            table.add(container).pad(10, 10, 10, 10).fill();
        }
        // Create a window for the inventory using the skin
        window = new Window("", skin);
        window.pad(40, 5, 5, 5); // Add padding to with so that the text doesn't go offscreen
        window.add(table); //Add the table to the window
        window.pack(); // Pack the window to the size
        window.setMovable(false);
        window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, 0); // Clip to the bottom of the window on the stage
        window.setVisible(!isOpen);
        // Add the window to the stage
        stage.addActor(window);
        dnd = new DragAndDrop();
        setDragItems(actors, map);
    }
    public void setDragItems(ArrayList<Actor> actors, Map<ItemSlot,Container<ItemSlot>> map) {


        for (Actor item : actors) {
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
                        Container<ItemSlot> con = map.get(getActor());
                        con.setActor(null);
                        con.setActor((ItemSlot) this.getActor());
                    }
                }
            });
        }

        for (Cell<Container<ItemSlot>> targetItem : table.getCells()) {
            dnd.addTarget(new DragAndDrop.Target(targetItem.getActor()) {
                Container<ItemSlot> container = targetItem.getActor();
                @Override
                public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                    return true;
                }

                @Override
                public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                    // called when left click is touched up

                    inventory.swapPosition(indexes.get(source.getActor()), indexes.get(targetItem.getActor().getActor()));
                    Integer temp =indexes.get(source.getActor());
                    indexes.put((ItemSlot) source.getActor(),indexes.get(targetItem.getActor().getActor()));
                    indexes.put(targetItem.getActor().getActor(),temp);

                    Container<ItemSlot> sourceContainer = map.get((source.getActor()));
                    map.put(container.getActor(),sourceContainer);
                    sourceContainer.setActor(container.getActor());
                    map.put((ItemSlot) payload.getDragActor(),container);
                    container.setActor((ItemSlot) payload.getDragActor());
                    entity.getEvents().trigger("updateInventory");


                }
            });

        }
    }

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
     * Dispose of Toolbar
     */
    @Override
    public void dispose() {
        super.dispose();
    }

    public void updateItemSlot(int slotNum) {

    }
}
