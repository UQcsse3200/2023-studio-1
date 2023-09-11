package com.csse3200.game.components.inventory;

import com.csse3200.game.components.items.ItemComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * An ui component for displaying player inventory
 */
public class InventoryDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(InventoryDisplay.class);
  private Table table;
  private InventoryComponent playerInventory;
  private Window window;
  private boolean isOpen = false;
  private final Skin skin = new Skin(Gdx.files.internal("gardens-of-the-galaxy/gardens-of-the-galaxy.json"));

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
    addActors();
    entity.getEvents().addListener("toggleInventory",this::toggleOpen);
    entity.getEvents().addListener("updateInventory",this::updateInventory);
  }

  /**
   * Re-renders the Inventory UI, when a change is made to the inventory
   */
  private void resetInventory(){
    window.reset();
    table = new Table(skin);
    table.defaults().size(64, 64);
    table.pad(10);
    for (int i = 0; i < 30; i++){
      Label label = new Label(String.valueOf(i), skin.get("default", Label.LabelStyle.class));
      //set the bounds of the label
      label.setBounds(label.getX() + 15, label.getY(), label.getWidth(), label.getHeight());
      if (playerInventory.getItemPos(i) == null){
        ItemSlot item = new ItemSlot(false);
        table.add(item).pad(10, 10, 10, 10).fill();
      } else {
        ItemSlot item = new ItemSlot(
                playerInventory.getItemPos(i).getComponent(ItemComponent.class).getItemTexture(),
                playerInventory.getItemCount(playerInventory.getItemPos(i)), false);
        table.add(item).pad(10, 10, 10, 10).fill();
      }
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
    stage.addActor(window);
  }

  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    table = new Table(skin);
    table.defaults().size(64, 64);
    table.pad(10);
    // Add some items to the table, to be changed once inventory item is improved

    for (int i = 0; i < 30; i++) {
      //Add the items to the table
      ItemSlot item = new ItemSlot(false);
        table.add(item).pad(10, 10, 10, 10).fill();
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
   * @return playerInventory inventory attached to player
   */
  public InventoryComponent getInventory(){
    return this.playerInventory;
  }

  /**
   * Force update of the display after update inventory event is called
   */
  public void updateInventory(){
    playerInventory = entity.getComponent(InventoryComponent.class);
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

