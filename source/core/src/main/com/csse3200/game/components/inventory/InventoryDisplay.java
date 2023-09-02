package com.csse3200.game.components.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.screens.MainGameScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

import java.awt.*;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class InventoryDisplay extends UIComponent {
  private Table table;
  private InventoryComponent playerInventory;
  private InventoryComponent inventory;

  private Window window;

  private boolean isOpen;

  private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);
  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    addActors();
    isOpen = false;
    entity.getEvents().addListener("toggleInventory",this::toggleOpen);
    inventory = new InventoryComponent(new ArrayList<>());
    inventory.addItem(new ItemComponent("shovel", ItemType.SHOVEL, "Shovel for removing items", new Texture("images/tool_shovel.png")).getEntity());
    inventory.addItem(new ItemComponent("shovel", ItemType.SHOVEL, "Shovel for removing items", new Texture("images/tool_shovel.png")).getEntity());
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

    // Add some items to the table, to be changed once inventory item is improved
    for (int i = 0; i < 3; i++) {
      //check if item exists at this point
      for (int j = 0; j < 10; j++) {
        ItemSlot item = new ItemSlot(new Texture("images/tool_hoe.png"), 1);
        table.add(item).pad(10, 10, 10, 10).fill();
        if (j == 9) {
          //Add a new row every 10 items
          table.row();
        }
      }
    }

    // Create a window for the inventory using the skin
    window = new Window("Inventory", skin);
    window.pad(40, 10, 10, 10); // Add padding to with so that the text doesn't go offscreen
    window.add(table); //Add the table to the window
    window.pack(); // Pack the window to the size
    window.setMovable(false);
    window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, stage.getHeight() / 2 - window.getHeight() / 2); // Center the window on the stage
    window.setVisible(false);
    // Add the window to the stage
    stage.addActor(window);
  }

  /**
   * Draw stage of inventory
   * @param batch Batch to render to.
   */
  @Override
  public void draw(SpriteBatch batch)  {

    //if (inventory != null && inventory.getItemAtPoint(new Point(j, i))) {
    //          table.add(new Image(new Texture("/images/tool_shovel.png")));
    //        }
    //        //Add the items to the table
  }

  /**
   * Get inventory Component, function may be removed
   * @return inventory
   */
  public InventoryComponent getInventory() {
    return inventory;
  }

  /**
   * Toggle open state of inventory
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

  @Override
  public void dispose() {
    super.dispose();
  }
}
