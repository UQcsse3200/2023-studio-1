package com.csse3200.game.input;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.extensions.GameExtension;

@ExtendWith(GameExtension.class)
class KeyboardInputFactoryTest {
  @Test
  void shouldReturnKeyboardPlayerInput() {
    KeyboardInputFactory keyboardInputFactory = new KeyboardInputFactory();
    assertTrue(keyboardInputFactory.createForPlayer() instanceof KeyboardPlayerInputComponent);
  }
}
