package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;

public class Item extends Component {
    private final String itemName;
    private final String itemId;

    /**
     * Basic Item Class
     * @param name user facing name for item
     * @param id identifier for the item
     */
    public Item(String name, String id) {
        itemName = name;
        itemId = id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemId() {
        return itemId;
    }
}
