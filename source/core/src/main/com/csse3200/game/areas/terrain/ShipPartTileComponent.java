package com.csse3200.game.areas.terrain;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.services.ServiceLocator;

/**
 * A tile that drops a ship part on destroy. Can contain a ship debris
 * which must be destroyed first if it exists.
 */
public class ShipPartTileComponent extends Component {
    private Entity shipDebris;

    public ShipPartTileComponent() {
        shipDebris = null;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("destroy", this::destroyTile);
    }

    /**
     * Set the ship debris associated with this tile.
     * @param shipDebris
     */
    public void addShipDebris(Entity shipDebris) {
        this.shipDebris = shipDebris;
        this.shipDebris.setPosition(entity.getPosition());
        ServiceLocator.getEntityService().register(this.shipDebris);
    }

    /**
     * A ship debris is present in this tile.
     * @return true if there is ship debris present
     */
    private boolean containsShipDebris() {
        return shipDebris != null;
    }

    /**
     * Trigger the ship debris' destroy event and remove it
     * from the tile.
     */
    private void destroyShipDebris() {
        shipDebris.getEvents().trigger("destroy", null);
        shipDebris = null;
    }

    /**
     * Destroys the tile. If a ship debris is present, destroy it first.
     * Drops a ship part on final destroy.
     *
     * @param tile the terrain tile that will become unoccupied once the ship
     *             tile is destroyed.
     */
    private void destroyTile(TerrainTile tile) {
        if (containsShipDebris()) {
            destroyShipDebris();
        } else {
            // drop a ship part
            Entity item = ItemFactory.createEgg(); // the ship is made of eggs apparently
            item.setPosition(entity.getPosition());
            ServiceLocator.getEntityService().register(item);

            // remove self from the terrain tile & self-destruct
            tile.removeOccupant();
            entity.dispose();
        }
    }
}
