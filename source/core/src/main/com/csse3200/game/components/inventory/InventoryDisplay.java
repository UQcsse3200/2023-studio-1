package com.csse3200.game.components.inventory;

import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.entities.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.screens.MainGameScreen;

import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class InventoryDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(InventoryDisplay.class);
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
    entity.getEvents().addListener("updateInventory",this::updateInventory);
  }

  private void resetInventory(){
    //logger.info("Reset Inventory..........................................");
    window.reset();
    Skin skin = new Skin(Gdx.files.internal("gardens-of-the-galaxy/gardens-of-the-galaxy.json"));
    table = new Table(skin);
    table.defaults().size(64, 64);
    table.pad(10);
    for (int i = 0; i < 30; i++){
      Label label = new Label(String.valueOf(i), skin.get("default", Label.LabelStyle.class));
      //set the bounds of the label
      label.setBounds(label.getX() + 15, label.getY(), label.getWidth(), label.getHeight());
      //stack.add(new Image(new Texture("images/itemFrame.png")));
      if (playerInventory.getItemPos(i) == null){
        //logger.info("Null Item at "+i );
        ItemSlot item = new ItemSlot(false);
        table.add(item).pad(10, 10, 10, 10).fill();
      } else {
        ItemSlot item = new ItemSlot(
                playerInventory.getItemPos(i).getComponent(ItemComponent.class).getItemTexture(),
                playerInventory.getItemCount(playerInventory.getItemPos(i)), false);
        table.add(item).pad(10, 10, 10, 10).fill();
        //stack.add(new Image(playerInventory.getItemPos(i).getComponent(ItemComponent.class).getItemTexture()));
      }
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

