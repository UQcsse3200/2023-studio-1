package com.csse3200.game.components.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.csse3200.game.services.ServiceLocator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.graphics.Texture;

/**
 * An ui component for displaying player stats, e.g. health.
 */
public class InventoryDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(InventoryDisplay.class);
  private InventoryComponent inventory;
  private final Skin skin = ServiceLocator.getResourceService().getAsset("gardens-of-the-galaxy/gardens-of-the-galaxy.json", Skin.class);
  private final Table table = new Table(skin);
  private final Window window = new Window("Inventory", skin);
  private final ArrayList<ItemSlot> slots = new ArrayList<>();
  private boolean isOpen = false;
  private DragAndDrop dnd;
  private ArrayList<Actor> actors;
  private Map<Image,ItemSlot> map;
  private Map<ItemSlot,Integer> indexes;
  private final Integer size;
  private final Integer rowSize;

  /**
   * Constructor for class
   * @param size size of inventory
   * @param rowSize amount of items per row
   */
  public InventoryDisplay(Integer size, Integer rowSize) {
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
   * Initialises the inventoryDisplay and adds it to the stage.
   * @see Table for positioning options
   */
  private void initialiseInventory() {
    // create variables needed for drag and drop
    dnd = new DragAndDrop();
    actors = new ArrayList<>();
    map = new HashMap<>();
    indexes  = new HashMap<>();

    // set the table cell size and add necessary padding
    table.defaults().size(64, 64);
    table.pad(10);

    // loop through entire table and create itemSlots and add the slots to the stored array
    for (int i = 0; i < size; i++) {
      ItemSlot slot;
      if (inventory != null && this.inventory.getItem(i) != null) {
        slot = new ItemSlot(this.inventory.getItem(i).getComponent(ItemComponent.class).getItemTexture(), false);
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
    window.pad(40, 20, 20, 20);
    window.add(table);
    window.pack();
    window.setMovable(false);
    window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, stage.getHeight() / 2 - window.getHeight() / 2); // Center the window on the stage
    window.setVisible(false);
    stage.addActor(window);
    setDragItems(actors, map);
  }

  /**
   * Update Inventory user interface
   */
  private void updateInventory() {
    dnd.clear();

    for (int i = 0; i < size; i++) {
      ItemComponent item;
      Texture itemTexture;
      int itemCount;

      // if the item isn't null we will update the position, this will be in future replaced by an event
      if (inventory != null && inventory.getItem(i) != null) {
        item = inventory.getItem(i).getComponent(ItemComponent.class);
        itemCount = inventory.getItemCount(item.getEntity());
        itemTexture = item.getItemTexture();
        ItemSlot curSlot = slots.get(i);
        curSlot.setItemImage(new Image(itemTexture));
        actors.add(curSlot.getItemImage());

        if (itemCount > 0) {
          curSlot.setCount(itemCount);
        }

        map.put(curSlot.getItemImage(), curSlot);
        indexes.put(curSlot, i);

        slots.set(i, curSlot);
      }
    }
    dnd = new DragAndDrop();
    setDragItems(actors, map);
  }

  /**
   * Set Drag Items
   * @param actors list of actors
   * @param map images and their respective item slot
   */
  public void setDragItems(@NotNull ArrayList<Actor> actors, Map<Image,ItemSlot> map) {
    for (Actor item : actors) {
      dnd.addSource(new DragAndDrop.Source(item) {
        final DragAndDrop.Payload payload = new DragAndDrop.Payload();
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
        final ItemSlot slot = (ItemSlot) targetItem.getActor();
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
          entity.getEvents().trigger("updateToolbar");
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
  }

  /**
   * Fetches the player inventory and returns it
   * @return inventory attached to display
   */
  public InventoryComponent getInventory(){
    return inventory;
  }

  /**
   * Fetch the updatedInventory and update display
   */
  public void refreshInventory(){
    this.inventory = entity.getComponent(InventoryComponent.class);
    updateInventory();
    entity.getEvents().trigger("updateToolbar");
  }

  /**
   * Dispose of the component
   */
  @Override
  public void dispose() {
    super.dispose();
  }
}

