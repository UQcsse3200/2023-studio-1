package com.csse3200.game.components.items.tools;

import com.csse3200.game.components.Component;

/**
 * provides selection of farming properties to be added to entities, such as:
 *  tilling, digging, watering.
 */
public class FarmAbilityComponent extends Component {

  private FarmAbilities ability;

  /**
   * Constructor
   * Takes and sets desired ability for that instance.
   */
  public FarmAbilityComponent(FarmAbilities ability) {
    this.ability = ability;
  }

  /**
   * Modifies ability, useful for individual tools
   */
  public void setAbility(FarmAbilities ability) {
     this.ability = ability;
  }

  /**
   *
   * @return ability attribute
   */
  public FarmAbilities getAbility() {
    return this.ability;
  }
}
