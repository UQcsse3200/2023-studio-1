package com.csse3200.game.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntityTypeTest {
  EntityType[] enumValues;
  Set<String> expectedEnumNames;

  @BeforeEach
  public void setUp() {
    enumValues = EntityType.values();

    // Expected enum names.
    // https://github.com/UQcsse3200/2023-studio-1/wiki/Save---Load-game
    expectedEnumNames = new HashSet<>(Arrays.asList(
        "PLAYER", "TRACTOR", "PLANT", "DECAYING_PLANT", "TILE", "COW",
        "CHICKEN", "ASTROLOTL", "OXYGEN_EATER", "ITEM", "FIRE_FLIES",
        "QUESTGIVER", "QUESTGIVER_INDICATOR", "SPRINKLER","SHIP", "SHIP_DEBRIS", "SHIP_PART_TILE",
            "GATE", "FENCE", "CHEST", "PUMP", "LIGHT", "FIRE_FLIES", "DRAGONFLY", "BAT", "DUMMY", "GOLDEN_STATUE"));
  }

  /*
   * If you are failing this below test you didn't follow the documentation
   * from t1 about adding new enum types. you must add
   * new enum values to factoryService if it
   * needs to be loaded into the game.
   * any components need write functions implemented.
   * after you complete that you can add the working enum
   * type to the list of approved enums at the top of this
   * this is all in the documentation at
   * https://github.com/UQcsse3200/2023-studio-1/wiki/Save---Load-game
   * at the bottom
   * 
   */

  @Test
  void testEnumValuesAddedFollowedProcedure() {

    for (EntityType enumValue : enumValues) {
      if (!expectedEnumNames.contains(enumValue.name())) {
        fail("Enum value '" + enumValue.name()
            + "' is not recognized. Please view https://github.com/UQcsse3200/2023-studio-1/wiki/Save---Load-game for instructions.");
      }
    }
  }

  /*
   * If you are failing this below test you didn't follow the documentation
   * from t1 about adding new enum types. you must add
   * new enum values to factoryService if it
   * needs to be loaded into the game.
   * any components need write functions implemented.
   * after you complete that you can add the working enum
   * type to the list of approved enums at the top of this
   * this is all in the documentation at
   * https://github.com/UQcsse3200/2023-studio-1/wiki/Save---Load-game
   * at the bottom
   * 
   */

  @Test
  void testEnumValuesRemovedFollowedProcedure() {

    // Check if any expected enum names are missing.
    for (String expectedEnumName : expectedEnumNames) {
      boolean found = false;
      for (EntityType enumValue : enumValues) {
        if (enumValue.name().equals(expectedEnumName)) {
          found = true;
          break;
        }
      }
      if (!found) {
        fail("Expected enum value '" + expectedEnumName + "' is missing.");
      }
    }
  }

  @Test
  void testOxygenReturn() {
    assertEquals(EntityType.COW.getOxygenRate(), 0);
    assertEquals(EntityType.CHICKEN.getOxygenRate(), 0);
    assertEquals(EntityType.GATE.getOxygenRate(), 0);
    assertEquals(EntityType.FENCE.getOxygenRate(), 0);
  }
}