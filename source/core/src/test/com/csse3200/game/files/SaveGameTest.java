package com.csse3200.game.files;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.files.SaveGame.GameState;
public class SaveGameTest {

  @Test
  public void SetDay() {
    GameState gameState = new SaveGame.GameState();

    gameState.setDay(5);
    assertEquals(5, gameState.getDay());

    gameState.setDay(-1);
    assertEquals(-1, gameState.getDay());

    gameState.setDay(2147483647);
    assertEquals(2147483647, gameState.getDay());
  }

  @Test
  public void setHour() {
    GameState gameState = new SaveGame.GameState();

    gameState.setHour(17);
    assertEquals(17, gameState.getHour());

    gameState.setHour(-1);
    assertEquals(-1, gameState.getHour());

    gameState.setHour(2147483647);
    assertEquals(2147483647, gameState.getHour());
  }

  @Test
  public void setPlayer() {
    GameState gameState = new SaveGame.GameState();

    Entity playerTest = new Entity(EntityType.Player);

    gameState.setPlayer(playerTest);
    assertEquals(playerTest, gameState.getPlayer());
  }

  @Test
  public void setEntities() {
    GameState gameState = new SaveGame.GameState();

    Entity[] entities = { new Entity(EntityType.Astrolotl),
        new Entity(EntityType.Chicken),
        new Entity(EntityType.Cow),
        new Entity(EntityType.Plant) };

    Array<Entity> entityTest = new Array<Entity>(entities);

    gameState.setEntities(entityTest);
    assertEquals(entityTest, gameState.getEntities());
  }

  @Test
  public void setTiles() {
    GameState gameState = new SaveGame.GameState();

    Entity[] tiles = { new Entity(EntityType.Tile),
        new Entity(EntityType.Tile),
        new Entity(EntityType.Tile),
        new Entity(EntityType.Tile), };

    Array<Entity> tileTest = new Array<Entity>(tiles);

    gameState.setTiles(tileTest);
    assertEquals(tileTest, gameState.getTiles());
  }

  @Test
  public void getTractor() {
    GameState gameState = new SaveGame.GameState();
    Entity tractor = new Entity(EntityType.Tractor);
    gameState.setTractor(tractor);
    assertEquals(tractor, gameState.getTractor());
    assertEquals(EntityType.Tractor, gameState.getTractor().getType());
  }

  @Test
  public void setTractor() {
    GameState gameState = new SaveGame.GameState();
    Entity tractor = new Entity(EntityType.Tractor);
    Vector2 position = new Vector2(1, 1);
    tractor.setPosition(position);
    gameState.setTractor(tractor);
    assertEquals(tractor, gameState.getTractor());
    assertEquals(position, gameState.getTractor().getPosition());
  }
}
