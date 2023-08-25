package com.csse3200.game.components.items;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;

import static com.csse3200.game.areas.terrain.TerrainCropTileFactory.createTerrainEntity;

public class ItemActions extends Component {

    @Override
    public void create() {
        // Just in case we need constructor for later
    }

    boolean use(Vector2 pos, Entity item) {
        ItemComponent type = item.getComponent(ItemComponent.class);
        // Wasn't an item or did not have ItemComponent class
        if (type == null) {
            return false;
        }
        // Add your item here!!!
        switch (type.getItemType()) {
            case HOE -> {
                hoe(pos);
                return true;
            }
            case SHOVEL -> {
                shovel(pos);
                return true;
            }
            case SCYTHE -> {
                harvest(pos);
                return true;
            }
            case WATERING_CAN -> {
                water(pos);
                return true;
            }
            default -> {
                return false;
            }
        }
    }


    private Entity getTileInfo(Vector2 pos) {
        // Needs to query the map with given position and return a tile
        return null;
    }

    private void water(Vector2 pos) {
        Entity tile = getTileInfo(pos);
        // A water amount of 0.5 was recommended by team 7
        tile.getEvents().trigger("water", 0.5);
        // Need to reduce watering can capacity (idk how or where this should be)
    }

    private void harvest(Vector2 pos) {
        Entity tile = getTileInfo(pos);
        tile.getEvents().trigger("harvest");
    }

    private void shovel(Vector2 pos) {
        Entity tile = getTileInfo(pos);
    }

    private void hoe(Vector2 pos) {
        Entity tile = getTileInfo(pos);
        if (tile != null) {
            return;
        }
        // Make a new tile
        tile = createTerrainEntity(pos);

        // Add tile to the map for further actions
    }
}
