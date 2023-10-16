package com.csse3200.game.components.combat;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.quests.Quest;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage it combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 */
public class CombatStatsComponent extends Component {

  private static final Logger logger = LoggerFactory.getLogger(CombatStatsComponent.class);
  private int health;
  private int baseAttack;

  public CombatStatsComponent(int health, int baseAttack) {
    setHealth(health);
    setBaseAttack(baseAttack);
  }

  @Override
  public void create() {
    entity.getEvents().addListener("hit", this::hitFromEntity);
    entity.getEvents().addListener("lose", this::lose);
  }

  private void lose() {
    ServiceLocator.getMissionManager().getEvents().trigger("loseScreen", "You died");
  }

  /**
   * Returns true if the entity's has 0 health, otherwise false.
   *
   * @return is player dead
   */
  public boolean isDead() {
    return health <= 0;
  }

  /**
   * Returns the entity's health.
   *
   * @return entity's health
   */
  public int getHealth() {
    return health;
  }

  /**
   * Sets the entity's health. Health has a minimum bound of 0.
   *
   * @param health health
   */
  public void setHealth(int health) {
    if (health <= 0) {
      this.health = 0;
    } else if (health >= 100) {
      this.health = 100;
    } else {
      this.health = health;
    }
    if (entity != null) {
      entity.getEvents().trigger("updateHealth", this.health);
      if (isDead()) {
        ServiceLocator.getMissionManager().getEvents().trigger(
                MissionManager.MissionEvent.COMBAT_ACTOR_DEFEATED.name(), entity.getType());
      }
    }
  }

  /**
   * Adds to the player's health. The amount added can be negative.
   *
   * @param health health to add
   */
  public void addHealth(int health) {
    setHealth(this.health + health);
  }

  /**
   * Returns the entity's base attack damage.
   *
   * @return base attack damage
   */
  public int getBaseAttack() {
    return baseAttack;
  }

  /**
   * Sets the entity's attack damage. Attack damage has a minimum bound of 0.
   *
   * @param attack Attack damage
   */
  public void setBaseAttack(int attack) {
    if (attack >= 0) {
      this.baseAttack = attack;
    } else {
      logger.error("Can not set base attack to a negative attack value");
    }
  }

  public void hit(CombatStatsComponent attacker) {
    int newHealth = getHealth() - attacker.getBaseAttack();
    setHealth(newHealth);
  }

  public void handleDeath() {
    if(!Objects.equals(entity.getType(), EntityType.PLAYER)) {
      ServiceLocator.getGameArea().removeEntity(entity);
    } else {
      // Stops flashing
      health += 1;
      processDeath();
    }
  }

  private void processDeath() {
    // Takes 0.3f seconds
    //entity.getEvents().trigger("blackOut");
    entity.getEvents().trigger("bye bye");
    entity.getEvents().scheduleEvent(2.1f, "lose");
  }

  public void hitFromEntity(Entity attacker) {
    CombatStatsComponent attackerStats = attacker.getComponent(CombatStatsComponent.class);
    if (attackerStats != null) {
      hit(attackerStats);
    }
  }

  @Override
  public void earlyUpdate() {
    if (isDead()) {
      entity.getEvents().trigger("death");
      handleDeath();
    }
  }

	@Override
	public void write(Json json) {
      json.writeObjectStart(this.getClass().getSimpleName());
      json.writeValue("health", this.health);
      json.writeObjectEnd();
	}

	@Override
	public void read(Json json, JsonValue jsonValue) {
      jsonValue = jsonValue.get(this.getClass().getSimpleName());
      int healthRead = jsonValue.getInt("health");
      setHealth(healthRead);
	}
}
