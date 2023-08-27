package com.csse3200.game.components.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class InventoryDisplay extends UIComponent {
  private Table table;
  private InventoryComponent inventory;

  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    addActors();

    entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
  }

  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    Table table = new Table();
    table.defaults().size(64, 64); // Set the default cell size
    table.pad(10); // Add some padding around the table

    // Add some items to the table
    for (int i = 0; i < 16; i++) {
      // Create an image for the item using a drawable from the skin
      Image item = new Image(skin.getDrawable("item" + i));
      // Add the item to the table and fill the cell
      table.add(item).fill();
      // Add a new row every 4 items
      if ((i + 1) % 4 == 0) {
        table.row();
      }
    }

    // Create a window for the inventory using the skin
    Window window = new Window("Inventory", skin);
    window.pad(20); // Add some padding around the window
    window.add(table); // Add the table to the window
    window.pack(); // Pack the window to its preferred size
    window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, stage.getHeight() / 2 - window.getHeight() / 2); // Center the window on the stage

    // Add the window to the stage
    stage.addActor(window);
  }

  @Override
  public void draw(SpriteBatch batch)  {
    // draw is handled by the stage
  }

  /**
   * Updates the player's health on the ui.
   * @param health player health
   */
  public void updatePlayerHealthUI(int health) {
    CharSequence text = String.format("Health: %d", health);
  }

  @Override
  public void dispose() {
    super.dispose();
  }
}
