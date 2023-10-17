package com.csse3200.game.components.placeables;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.inventory.InventoryDisplay;

import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChestComponent extends Component  {
    private static final Logger logger = LoggerFactory.getLogger(ChestComponent.class);

    @Override
    public void create(){
        entity.getEvents().addListener("interact", this::toggleChest);
    }

    /**
     * When the player interacts with the chest, display its inventory
     *  but close the chest if it is already open
     */
    private void toggleChest(){
        //check if the chest is already being accessed and if so just close
        InventoryDisplay chestInventory = entity.getComponent(InventoryDisplay.class);

        entity.getEvents().trigger("toggleChest");      //open up the chest's inventory
        //player.getEvents().trigger("toggleInventory");  //open up the player's inventory

        //check to see if the chest is open or not
        if (chestInventory.isOpen()){   
            logger.info("close chest");
            ServiceLocator.getGameArea().getPlayer().getEvents().trigger(PlayerActions.events.FREEZE.name());

            return; 
        }
        ServiceLocator.getGameArea().getPlayer().getEvents().trigger(PlayerActions.events.UNFREEZE.name());
            
        logger.info("Open chest");
    }
}
