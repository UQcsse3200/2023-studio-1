package com.csse3200.game.components.inventory;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.ui.UIComponent;


/**
 * A ui component for displaying player stats, e.g. health.
 */
public class InventoryDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(InventoryDisplay.class);
  private Table table;
  private InventoryComponent inventory;
  private Window window;
  private boolean isOpen = false;
  private DragAndDrop dnd;
  private Map<ItemSlot,Integer> indexes;

  /**
   * Constructor for class
   * @param inventory inventory of player
   */
//  public InventoryDisplay(InventoryComponent inventory) {
//    this.inventory = inventory;
//  }
//
//  public InventoryDisplay() {
//    this.inventory = entity.getComponent(InventoryComponent.class);
//  }

  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    addActors();
    entity.getEvents().addListener("toggleInventory",this::toggleOpen);
    entity.getEvents().addListener("updateInventory",this::updateInventory);
  }

  /**
   * Re-renders the Inventory UI, when a change is made to the inventory
   */
  private void resetInventory(){
    if(!isOpen){
      return;
    }
    window.reset();
    dnd.clear();
    Skin skin = new Skin(Gdx.files.internal("gardens-of-the-galaxy/gardens-of-the-galaxy.json"));
    table = new Table(skin);
    table.defaults().size(64, 64);
    table.pad(10);
    ArrayList<Actor> actors = new ArrayList<>();
    final Map<Image,ItemSlot> map = new HashMap<>();

    for (int i = 0; i < 30; i++){
      Label label = new Label(String.valueOf(i), skin.get("default", Label.LabelStyle.class));
      //set the bounds of the label
      label.setBounds(label.getX() + 15, label.getY(), label.getWidth(), label.getHeight());
      ItemSlot item;
      if (this.inventory.getItem(i) == null) {
        //logger.info("Null Item at "+i );
        item = new ItemSlot(false);

      } else {
        item = new ItemSlot(
                this.inventory.getItemTexture(i),this.inventory.getItemCount(i),
                false);
        actors.add(item.getItemImage());
        //stack.add(new Image(inventory.getItemPos(i).getComponent(ItemComponent.class).getItemTexture()));
      }
      map.put(item.getItemImage(), item);
      if (item.getItemImage() != null) {
        item.getItemImage().setDebug(false);
      }
      indexes.put(item, i);
      table.add(item).pad(10, 10, 10, 10).fill();
      if ((i + 1) % 10 == 0) {
        //Add a new row every 10 items
        table.row();
      }
    }
    window.pad(40, 5, 5, 5);
    window.add(table);
    window.pack();
    window.setMovable(false);
    window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, 0);
    window.setVisible(isOpen);
    // Add the window to the stage
//    stage.addActor(window);
    setDragItems(actors, map);
  }

  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    table = new Table(skin);
    table.defaults().size(64, 64);
    table.pad(10);
    ArrayList<Actor> actors = new ArrayList<>();
    final Map<Image,ItemSlot> map = new HashMap<>();
      // Add some items to the table, to be changed once inventory item is improved
    indexes = new HashMap<>(); // map of items to their index
    for (int i = 0; i < 30; i++) {
      //Add the items to the table
      ItemSlot item = new ItemSlot(false);
      //item.setDebug(true);
      map.put(item.getItemImage(), item);

      table.add(item).width(70).height(70).pad(10, 10, 10, 10);
      indexes.put(item, i);
      if ((i + 1) % 10 == 0) {
        //Add a new row every 10 items
        table.row();
      }
    }

    // Create a window for the inventory using the skin
    window = new Window("Inventory", skin);
    window.pad(40, 20, 20, 20); // Add padding to with so that the text doesn't go offscreen
    window.add(table); //Add the table to the window
    window.pack(); // Pack the window to the size
    window.setMovable(false);
    window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, stage.getHeight() / 2 - window.getHeight() / 2); // Center the window on the stage
    window.setVisible(false);
    // Add the window to the stage
    stage.addActor(window);
    dnd = new DragAndDrop();
    setDragItems(actors, map);

  }
  public void setDragItems(@NotNull ArrayList<Actor> actors, Map<Image,ItemSlot> map) {
    for (Actor item : actors) {
      dnd.addSource(new DragAndDrop.Source(item) {
        DragAndDrop.Payload payload = new DragAndDrop.Payload();
        @Override
        public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
          payload.setObject(getActor());
          payload.setDragActor(getActor());
          stage.addActor(getActor());
          dnd.setDragActorPosition(50,-getActor().getHeight()/2);

          return payload;
        }

        @Override
        public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
          if (target == null) {
            ItemSlot itemSlot = map.get((Image)getActor());
            itemSlot.removeActor(getActor());
            itemSlot.add(getActor());
          }
        }
      });
    }

    for (Cell<?> targetItem : table.getCells()) {
      dnd.addTarget(new DragAndDrop.Target(targetItem.getActor()) {
        ItemSlot slot = (ItemSlot) targetItem.getActor();
        @Override
        public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
          return true;
        }

        @Override
        public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
          ItemSlot sourceSlot = map.get(((Image)source.getActor()));
          inventory.swapPosition(indexes.get(sourceSlot), indexes.get(slot));
          map.put(slot.getItemImage(), sourceSlot);
          sourceSlot.setItemImage(slot.getItemImage());
          map.put((Image) payload.getDragActor(),slot);
          slot.setItemImage((Image)payload.getDragActor());
          int currentIndex = inventory.getHeldIndex();
          if (indexes.get(slot) > 9) {
            inventory.setHeldItem(currentIndex);
          }
          else {
            inventory.setHeldItem(indexes.get(slot));
            entity.getEvents().trigger("hotkeySelection", indexes.get(slot));
          }

        }
      });

    }
  }


  /**
   * The draw stage of the UIComponent, it is handled by the stage
   * @param batch Batch to render to.
   */
  @Override
  public void draw(SpriteBatch batch) {

  }

  /**
   * Toggle the inventory open, and changes the window visibility
   */
  public void toggleOpen(){
      isOpen = !isOpen;
      window.setVisible(isOpen);
      if (isOpen) updateInventory();
  }
  /**
   * Force update of the display after update inventory event is called
   */
  public void updateInventory(){
    this.inventory = entity.getComponent(InventoryComponent.class);
    resetInventory();
  }

  /**
   * Dispose of the component
   */
  @Override
  public void dispose() {
    super.dispose();
  }
}

