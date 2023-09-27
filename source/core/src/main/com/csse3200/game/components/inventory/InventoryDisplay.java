package com.csse3200.game.components.inventory;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.csse3200.game.components.items.ItemComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

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

  /**
   * Constructor for class
   * @param playerInventory inventory of player
   */
  public InventoryDisplay(InventoryComponent playerInventory) {
    this.playerInventory = playerInventory;
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
    table.defaults().size(64, 64);
    table.pad(10);
    for (int i = 0; i < 30; i++) {
      ItemSlot slot;
      if (this.playerInventory.getItemPos(i) != null) {
        slot = new ItemSlot(this.playerInventory.getItemPos(i).getComponent(ItemComponent.class).getItemTexture(), false);
      }
      else {
        slot = new ItemSlot(false);
      }

      table.add(slot).width(70).height(70).pad(10, 10, 10, 10);
      if ((i + 1) % 10 == 0) {
        table.row();
      }
      slots.add(slot);
    }

    // Create a window for the inventory using the skin
    window.pad(40, 20, 20, 20); // Add padding to with so that the text doesn't go offscreen
    window.add(table); //Add the table to the window
    window.pack(); // Pack the window to the size
    window.setMovable(false);
    window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, stage.getHeight() / 2 - window.getHeight() / 2); // Center the window on the stage
    window.setVisible(false);
    stage.addActor(window);
  }

  private void refreshTable() {
    table.clear();
    window.clear();
    for (int i = 0; i < 30; i++) {
      ItemSlot slot = slots.get(i);

      table.add(slot).width(70).height(70).pad(10, 10, 10, 10);

      if ((i + 1) % 10 == 0) {
        table.row();
      }
    }
    window.pad(40, 20, 20, 20); // Add padding to with so that the text doesn't go offscreen
    window.add(table); //Add the table to the window
    window.pack(); // Pack the window to the size
    window.setMovable(false);
    window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, stage.getHeight() / 2 - window.getHeight() / 2); // Center the window on the stage
    window.setVisible(false);
    stage.addActor(window);
  }

  /**
   * Update Inventory
   */
  private void updateInventory() {
    for (int i = 0; i < 30; i++) {
      ItemComponent item;
      Texture itemTexture;
      int itemCount;

      if (playerInventory.getItemPos(i) != null) {
        item = playerInventory.getItemPos(i).getComponent(ItemComponent.class);
        itemCount = playerInventory.getItemCount(item.getEntity());
        itemTexture = item.getItemTexture();
        ItemSlot curSlot = slots.get(i);
        curSlot.setTexture(itemTexture);

        if (curSlot.getCount() != null && !curSlot.getCount().equals(itemCount)) {
          curSlot.setCount(itemCount);
        }

        slots.set(i, curSlot);
      }
    }
    refreshTable();
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

