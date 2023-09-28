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
import com.badlogic.gdx.graphics.Texture;


/**
 * A ui component for displaying player stats, e.g. health.
 */
public class InventoryDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(InventoryDisplay.class);
  private InventoryComponent playerInventory;
  private final Skin skin = new Skin(Gdx.files.internal("gardens-of-the-galaxy/gardens-of-the-galaxy.json"));
  private Table table = new Table(skin);
  private Window window = new Window("Inventory", skin);
  private ArrayList<ItemSlot> slots = new ArrayList<>();
  private boolean isOpen = false;
  private DragAndDrop dnd;
  private ArrayList<Actor> actors;
  private Map<Image,ItemSlot> map;
  private Map<ItemSlot,Integer> indexes;
  private Integer size;
  private Integer rowSize;

  /**
   * Constructor for class
   * @param playerInventory inventory of player
   */
  public InventoryDisplay(InventoryComponent playerInventory, Integer size, Integer rowSize) {
    this.playerInventory = playerInventory;
    this.size = size;
    this.rowSize = rowSize;
  }

  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    initialiseInventory();
    entity.getEvents().addListener("toggleInventory",this::toggleOpen);
    entity.getEvents().addListener("updateInventory",this::refreshInventory);
  }

  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void initialiseInventory() {
    dnd = new DragAndDrop();
    actors = new ArrayList<>();
    map = new HashMap<>();
    indexes  = new HashMap<>();
    table.defaults().size(64, 64);
    table.pad(10);
    for (int i = 0; i < size; i++) {
      ItemSlot slot;
      if (this.playerInventory.getItemPos(i) != null) {
        slot = new ItemSlot(this.playerInventory.getItemPos(i).getComponent(ItemComponent.class).getItemTexture(), false);
        actors.add(slot.getItemImage());
      } else {
        slot = new ItemSlot(false);
      }

      table.add(slot).width(70).height(70).pad(10, 10, 10, 10);
      if ((i + 1) % rowSize == 0) {
        table.row();
      }
      slots.add(slot);
      map.put(slot.getItemImage(), slot);
      indexes.put(slot, i);
      if (slot.getItemImage() != null) {
        slot.getItemImage().setDebug(false);
      }
    }

    // Create a window for the inventory using the skin
    window.pad(40, 20, 20, 20); // Add padding to with so that the text doesn't go offscreen
    window.add(table); //Add the table to the window
    window.pack(); // Pack the window to the size
    window.setMovable(false);
    window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, stage.getHeight() / 2 - window.getHeight() / 2); // Center the window on the stage
    window.setVisible(false);
    stage.addActor(window);
    setDragItems(actors, map);
  }

  private void refreshTable() {
    table.clear();
    for (int i = 0; i < size; i++) {
      ItemSlot slot = slots.get(i);

      table.add(slot).width(70).height(70).pad(10, 10, 10, 10);

      if ((i + 1) % rowSize == 0) {
        table.row();
      }
    }
  }

  /**
   * Update Inventory
   */
  private void updateInventory() {
    dnd.clear();
    actors.clear();
    map.clear();
    indexes.clear();

    for (int i = 0; i < size; i++) {
      ItemComponent item;
      Texture itemTexture;
      int itemCount;

      if (playerInventory.getItemPos(i) != null) {
        item = playerInventory.getItemPos(i).getComponent(ItemComponent.class);
        itemCount = playerInventory.getItemCount(item.getEntity());
        itemTexture = item.getItemTexture();
        ItemSlot curSlot = slots.get(i);
        curSlot.setTexture(itemTexture);
        actors.add(curSlot.getItemImage());

        if (curSlot.getCount() != null && !curSlot.getCount().equals(itemCount)) {
          curSlot.setCount(itemCount);
        }

        map.put(curSlot.getItemImage(), curSlot);
        indexes.put(curSlot, i);

        slots.set(i, curSlot);
      }
    }
    refreshTable();
  }

  /**
   * set
   * @param actors
   * @param map
   */
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
          playerInventory.swapPosition(indexes.get(sourceSlot), indexes.get(slot));
          map.put(slot.getItemImage(), sourceSlot);
          sourceSlot.setItemImage(slot.getItemImage());
          map.put((Image) payload.getDragActor(),slot);
          slot.setItemImage((Image)payload.getDragActor());
          int currentIndex = playerInventory.getHeldIndex();
          if (indexes.get(slot) > 9) {
            playerInventory.setHeldItem(currentIndex);
          }
          else {
            playerInventory.setHeldItem(indexes.get(slot));
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
      entity.getEvents().trigger("updateInventory");
      isOpen = !isOpen;
      window.setVisible(isOpen);
  }

  /**
   * Fetches the player inventory and returns it
   * @return playerInventory inventory attached to player
   */
  public InventoryComponent getInventory(){
    return this.playerInventory;
  }

  public void refreshInventory(){
    playerInventory = entity.getComponent(InventoryComponent.class);
    updateInventory();
  }

  /**
   * Dispose of the component
   */
  @Override
  public void dispose() {
    super.dispose();
  }
}

