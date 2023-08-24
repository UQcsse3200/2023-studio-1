package com.csse3200.game.components.items;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;

public class ItemActions extends Component {

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
            case SICKLE -> {
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

    private void water(Vector2 pos) {

    }

    private void harvest(Vector2 pos) {

    }

    private void shovel(Vector2 pos) {

    }

    private void hoe(Vector2 pos) {

    }
}
