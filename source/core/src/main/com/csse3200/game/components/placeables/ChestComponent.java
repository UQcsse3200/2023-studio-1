package com.csse3200.game.components.placeables;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.inventory.InventoryDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChestComponent extends Component  {
    private static final Logger logger = LoggerFactory.getLogger(InventoryDisplay.class);
    public void create(){
        entity.getEvents().addListener("interact", this::openChest);

        //#### may not be needed ####   as the openChest() function will call the close function if 'e' is pressed and in the chest
        // but could be called when the player walks far enough away or presses 'i' or 'esc'
        entity.getEvents().addListener("CloseChest", this::closeChest);
    }

    /**
     * When the player interacts with the chest, display its inventory
     */
    private void openChest(){
        //check if the chest is already being accessed and if so just close
        //  if (Chest in use) { closeChest(); }
        
        //TODO: Display the UI for the chest inventory, once inventory is complete
        entity.getEvents().trigger("openChest");
    }

    /**
     * Close the chest inventory display
     */
    private void closeChest(){
        //TODO: Hide the UI for the chest inventory, once inventory is complete
        entity.getEvents().trigger("openChest");
    }
}
