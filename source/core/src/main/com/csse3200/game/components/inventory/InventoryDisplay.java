package com.csse3200.game.components.inventory;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.csse3200.game.entities.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class  InventoryDisplay extends UIComponent {
  private Table table;
  private InventoryComponent playerInventory;
  private Window window;
  private boolean isOpen;

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
  }


  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    Skin skin = new Skin(Gdx.files.internal("gardens-of-the-galaxy/gardens-of-the-galaxy.json"));
    table = new Table(skin);
    table.setDebug(true);
    table.defaults().size(64, 64);
    table.pad(10);
    ArrayList<Actor> actors = new ArrayList<Actor>();
    final Map<ItemSlot,Container<ItemSlot>> map = new HashMap<>();

    // Add some items to the table, to be changed once inventory item is improved
    for (int i = 0; i < 30; i++) {
      //Add the items to the table
      ItemSlot item = new ItemSlot(new Texture("images/tool_hoe.png"), i);
      item.setSize(64,64);
      Container<ItemSlot> container = new Container<>(item);
      container.setTouchable(Touchable.enabled);
      container.setDebug(true);
      item.setDebug(true);
      map.put(item, container);

      table.add(container).width(100).height(100).pad(10, 10, 10, 10);

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
    DragAndDrop dnd = new DragAndDrop();


    for (Actor item : actors) {
      dnd.addSource(new DragAndDrop.Source(item) {
        DragAndDrop.Payload payload = new DragAndDrop.Payload();


        @Override
        public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
          payload.setObject(getActor());
          payload.setDragActor(getActor());
          Actor parent = (Actor)item.getParent();
          stage.addActor(getActor());

          return payload;
        }

        @Override
        public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
          if (target == null) {
            Container<ItemSlot> con = map.get(getActor());
            con.setActor(null);
            con.setActor((ItemSlot) this.getActor());
            stage.clear();
            stage.addActor(window);
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
            Container<ItemSlot> sourceContainer = map.get((source.getActor()));
            map.put((ItemSlot) container.getActor(),sourceContainer);
            sourceContainer.setActor(container.getActor());
            map.put((ItemSlot) payload.getDragActor(),container);
            container.setActor((ItemSlot) payload.getDragActor());

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


  @Override
  public void dispose() {
    super.dispose();
  }
}

