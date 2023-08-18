package com.csse3200.game.components.items.tools;

import com.csse3200.game.components.Component;

/*
 * provides selection of farming properties to be added to entities, such as:
 *  tilling, digging, watering.
 */
public class FarmActionComponent extends Component {
  public void setAction(FarmFunction ability) {
    switch (ability) {
      case DIG:
        /* do something */
      case TILL:
        /* do something */
      case WATER:
        /* do something */
      default:
        break;
    }
  }
}
