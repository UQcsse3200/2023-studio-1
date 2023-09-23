package com.csse3200.game.input;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.extensions.GameExtension;

@ExtendWith(GameExtension.class)
class InputFactoryTest {

  @Test
  void shouldGiveKeyboardFactoryType() {
    InputFactory keyboardInputFactory = InputFactory.createFromInputType(InputFactory.InputType.KEYBOARD);
    assertTrue(keyboardInputFactory instanceof KeyboardInputFactory);
  }

  @Test
  void shouldGiveNoFactory() {
    InputFactory invalidInputFactory = InputFactory.createFromInputType(null);
    assertNull(invalidInputFactory);
  }
}
