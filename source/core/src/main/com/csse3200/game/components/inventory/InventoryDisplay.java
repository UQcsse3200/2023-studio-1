package com.csse3200.game.components.inventory;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.csse3200.game.components.items.ItemComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.ui.UIComponent;
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
    final Map<ItemSlot,Container<ItemSlot>> map = new HashMap<>();

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
        //stack.add(new Image(playerInventory.getItemPos(i).getComponent(ItemComponent.class).getItemTexture()));
      }
      Container<ItemSlot> container = new Container<>(item);
      //container.setDebug(true);
      container.setTouchable(Touchable.enabled);
      map.put(item, container);
      actors.add(item);
      indexes.put(item, i);
      table.add(container).pad(10, 10, 10, 10).fill();
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
    final Map<ItemSlot,Container<ItemSlot>> map = new HashMap<>();
      // Add some items to the table, to be changed once inventory item is improved
    indexes = new HashMap<>(); // map of items to their index
    for (int i = 0; i < 30; i++) {
      //Add the items to the table
      ItemSlot item = new ItemSlot(false);
      Container<ItemSlot> container = new Container<>(item);
      container.setTouchable(Touchable.enabled);
      //container.setDebug(true);
      //item.setDebug(true);
      map.put(item, container);

      table.add(container).width(70).height(70).pad(10, 10, 10, 10);
      indexes.put(item, i);
      actors.add(item);
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
  public void setDragItems(ArrayList<Actor> actors, Map<ItemSlot,Container<ItemSlot>> map) {


    for (Actor item : actors) {
      if (indexes.get(item) != null)
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
          playerInventory.swapPosition(indexes.get(source.getActor()), indexes.get(targetItem.getActor().getActor()));
          Integer temp =indexes.get(source.getActor());
          indexes.put((ItemSlot) source.getActor(),indexes.get(targetItem.getActor().getActor()));
          indexes.put(targetItem.getActor().getActor(),temp);
          Container<ItemSlot> sourceContainer = map.get((source.getActor()));
          map.put((ItemSlot) container.getActor(),sourceContainer);
          sourceContainer.setActor(container.getActor());
          map.put((ItemSlot) payload.getDragActor(),container);
          container.setActor((ItemSlot) payload.getDragActor());
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

