package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;

public class Item extends Component {
    private final String itemName;
    private final String itemId;

    private int itemCount;
    /**
     * Basic Item Class
     * @param name user facing name for item
     * @param id identifier for the item
     */

    public Item(String name, String id, int count) {
        itemName = name;
        itemId = id;
        itemCount = count;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemId() {
        return itemId;
    }

    public int getItemCount() {
        return itemCount;
    }

    public boolean setItemCount(int updatedItemCount) {
        itemCount = updatedItemCount;
        return true;
    }
}
