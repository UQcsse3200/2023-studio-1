package com.csse3200.game.components.inventory;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.csse3200.game.components.items.ItemComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.ui.UIComponent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class InventoryDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(InventoryDisplay.class);
  private Table table;
  private InventoryComponent playerInventory;
  private Window window;
  private boolean isOpen;
  private DragAndDrop dnd;
  private Map<ItemSlot,Integer> indexes;

  public InventoryDisplay(InventoryComponent playerInventory) {
    this.playerInventory = playerInventory;
  }


  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    addActors();
    isOpen = false;
    entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
    entity.getEvents().addListener("toggleInventory",this::toggleOpen);
    entity.getEvents().addListener("updateInventory",this::updateInventory);
  }

  private void resetInventory(){
    //logger.info("Reset Inventory..........................................");
    window.reset();
    dnd.clear();
    Skin skin = new Skin(Gdx.files.internal("gardens-of-the-galaxy/gardens-of-the-galaxy.json"));
    table = new Table(skin);
    table.defaults().size(64, 64);
    table.pad(10);
    ArrayList<Actor> actors = new ArrayList<Actor>();
    final Map<Image,ItemSlot> map = new HashMap<>();

    for (int i = 0; i < 30; i++){
      Label label = new Label(String.valueOf(i), skin.get("default", Label.LabelStyle.class));
      //set the bounds of the label
      label.setBounds(label.getX() + 15, label.getY(), label.getWidth(), label.getHeight());
      //stack.add(new Image(new Texture("images/itemFrame.png")));
      ItemSlot item;
      if (playerInventory.getItemPos(i) == null) {
        //logger.info("Null Item at "+i );
        item = new ItemSlot(false);

      } else {
        item = new ItemSlot(
                playerInventory.getItemPos(i).getComponent(ItemComponent.class).getItemTexture(),
                false);
        actors.add(item.getItemImage());
        //stack.add(new Image(playerInventory.getItemPos(i).getComponent(ItemComponent.class).getItemTexture()));
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
      //table.add(stack).pad(10, 10, 10, 10).fill();
    }
    //window = new Window("", skin);
    window.pad(40, 5, 5, 5); // Add padding to with so that the text doesn't go offscreen
    window.add(table); //Add the table to the window
    window.pack(); // Pack the window to the size
    window.setMovable(false);
    window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, 0); // Clip to the bottom of the window on the stage
    window.setVisible(isOpen);
    // Add the window to the stage
    stage.addActor(window);
    setDragItems(actors, map);
  }

  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    Skin skin = new Skin(Gdx.files.internal("gardens-of-the-galaxy/gardens-of-the-galaxy.json"));
    table = new Table(skin);
    table.defaults().size(64, 64);
    table.pad(10);
    ArrayList<Actor> actors = new ArrayList<Actor>();
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
          System.out.println(item);

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
          return true;
        }

        @Override
        public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
          ItemSlot sourceSlot = map.get((source.getActor()));
          playerInventory.swapPosition(indexes.get(sourceSlot), indexes.get(slot));
          map.put(slot.getItemImage(), sourceSlot);
          sourceSlot.setItemImage(slot.getItemImage());
          map.put((Image) payload.getDragActor(),slot);
          slot.setItemImage((Image)payload.getDragActor());
          entity.getEvents().trigger("updateInventory");

        }
      });

    }
  }

  @Override
  public void draw(SpriteBatch batch)  {
    // draw is handled by the stage
  }

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
   * Updates the player's health on the ui.
   * @param health player health
   */
  public void updatePlayerHealthUI(int health) {
    CharSequence text = String.format("Health: %d", health);
  }

  public InventoryComponent getInventory(){
    return this.playerInventory;
  }

  public void updateInventory(){
    playerInventory = entity.getComponent(InventoryComponent.class);
    resetInventory();

  }


  @Override
  public void dispose() {
    super.dispose();
  }
}

