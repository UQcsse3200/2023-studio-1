package com.csse3200.game.components.inventory;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.InventoryComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Game Screen and does something when one of the
 * events is triggered.
 */
public class Inventory extends Component {
    private static final Logger logger = LoggerFactory.getLogger(Inventory.class);

    private boolean isOpen = false;

    public Inventory(InventoryComponent inventory) {

    }

    public boolean isOpen() { return isOpen; }

    public void toggleOpen() {
        isOpen = !isOpen;
    }
    @Override
    public void create() {
        entity.getEvents().addListener("exit", this::onExit);
    }

    /**
     * Swaps to the Main Menu screen.
     */
    private void onExit() {
        logger.info("Exiting main game screen");;
    }
}
