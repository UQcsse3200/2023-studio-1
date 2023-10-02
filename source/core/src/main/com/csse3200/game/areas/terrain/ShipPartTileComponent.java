package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.entities.factories.ShipDebrisFactory;
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
     *
     * @param shipDebris entity to be added
     */
    public void addShipDebris(Entity shipDebris) {
        this.shipDebris = shipDebris;
        this.shipDebris.setPosition(entity.getPosition());
        ServiceLocator.getGameArea().spawnEntity(this.shipDebris);
    }

    /**
     * A ship debris is present in this tile.
     *
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
            Entity item = ItemFactory.createShipPart(); // the ship is made of ship stuff now
            item.setCenterPosition(entity.getCenterPosition());
            ServiceLocator.getEntityService().register(item);

            // remove self from the terrain tile & self-destruct
            if (tile != null) tile.removeOccupant();
            entity.dispose();
        }
    }

    @Override
    public void write(Json json) {
        json.writeObjectStart(this.getClass().getSimpleName());
        json.writeValue("shipDebris", shipDebris);
        json.writeObjectEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonMap) {
        jsonMap = jsonMap.get("components").get(this.getClass().getSimpleName());
        JsonValue debrisData = jsonMap.get("shipDebris");
        if (debrisData.get("Entity") != null) {
            Entity newShipDebris = ShipDebrisFactory.createShipDebris(null);
            addShipDebris(newShipDebris);
        } else {
            shipDebris = null;
        }
    }
}
