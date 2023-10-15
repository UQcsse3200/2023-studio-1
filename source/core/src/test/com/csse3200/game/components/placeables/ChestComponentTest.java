package com.csse3200.game.components.placeables;

import static org.junit.Assert.assertFalse;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;

import com.csse3200.game.components.inventory.InventoryDisplay;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.entities.Entity;

@ExtendWith(GameExtension.class)
public class ChestComponentTest {
    @Test
    void toggleChest(){
        ServiceLocator.registerResourceService(new ResourceService());

        Entity chest = new Entity();
        chest.addComponent(new InventoryComponent())
        .addComponent(new ChestComponent());
        //.addComponent(new InventoryDisplay("refreshChest", "toggleChest", 30, 10, false))

        //InventoryDisplay chestInv = chest.getComponent(InventoryDisplay.class);

        //assertFalse(chestInv.isOpen());     //chest is not opened
            //trigger the open chest action
        //chest.getEvents().trigger("interact");
        //assertTrue(chestInv.isOpen());      //chest is open
    }
}
