package com.csse3200.game.components.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.screens.MainGameScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

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
    inventory.addItem(new ItemComponent("shovel", ItemType.SHOVEL, "Shovel for removing items").getEntity());
    inventory.addItem(new ItemComponent("shovel", ItemType.SHOVEL, "Shovel for removing items").getEntity());
    logger.info(String.valueOf(inventory));
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
    for (int i = 0; i < 30; i++) {
      //Add the items to the table
      table.add(new Image(new Texture("images/itemFrame.png"))).pad(10, 10, 10, 10).fill();
      //table.add(new Image(new Texture("images/itemFrame.png")));
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

  @Override
  public void draw(SpriteBatch batch)  {
  }

  public InventoryComponent getInventory() {
    return inventory;
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

  @Override
  public void dispose() {
    super.dispose();
  }
}
