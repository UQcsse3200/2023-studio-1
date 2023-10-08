package com.csse3200.game.components.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.csse3200.game.components.inventory.InventoryDisplayManager;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.sound.EffectSoundFile;
import com.csse3200.game.services.sound.InvalidSoundFileException;
import com.csse3200.game.services.sound.SoundFile;
import com.csse3200.game.services.sound.SoundService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(GameExtension.class)
class InventoryHotkeyTest {
	private Entity player;
	private InventoryComponent inventoryComponent;
	private PlayerActions playerActions;
	private KeyboardPlayerInputComponent keyboardPlayerInputComponent;
	private static final Logger logger = LoggerFactory.getLogger(InventoryHotkeyTest.class);
	String[] texturePaths = {"images/tool_shovel.png"};

	@BeforeEach
	void initialiseTest() {
		ServiceLocator.registerResourceService(new ResourceService());
		ServiceLocator.getResourceService().loadTextures(texturePaths);
		ServiceLocator.getResourceService().loadAll();
		inventoryComponent = spy(new InventoryComponent(new ArrayList<>()));
		keyboardPlayerInputComponent = spy(new KeyboardPlayerInputComponent());
		ServiceLocator.registerInputService(new InputService());
		playerActions = spy(new PlayerActions());
		keyboardPlayerInputComponent.setActions(playerActions);
		player = new Entity()
				.addComponent(inventoryComponent)
				.addComponent(keyboardPlayerInputComponent)
				.addComponent(playerActions);
		player.create();
		String[] itemNames = {"Hoe", "Hoe1", "Hoe2", "Hoe3", "Hoe4", "Hoe5", "Hoe6", "Hoe7", "Hoe8", "Hoe9", "Hoe10"};
		List<Entity> items = new ArrayList<>();
		for (int i = 0; i < itemNames.length; ) {
			items.add(new Entity().addComponent(new ItemComponent(itemNames[i++], ItemType.HOE, "images/tool_shovel.png")));
		}
		inventoryComponent.setInventory(items);

		//Set up the dependencies for the item select sound
		ServiceLocator.registerSoundService(new SoundService());
		java.util.List<SoundFile> effects = new ArrayList<>();
		effects.add(EffectSoundFile.HOTKEY_SELECT);
		//Load sound file
		try {
			ServiceLocator.getSoundService().getEffectsMusicService().loadSounds(effects);
		} catch (InvalidSoundFileException e) {
			logger.info("Sound files not loaded");
		}
	}

	@ParameterizedTest
	@MethodSource({"checkHotkeySelectionTriggerParams"})
	void checkHotkeySelectionTrigger(int num, String expectedItemName) {
		player.getEvents().trigger("hotkeySelection", num);
		verify(inventoryComponent).setHeldItem(num);
		verify(playerActions).hotkeySelection(num);
		assertEquals(inventoryComponent.getHeldItem().getComponent(ItemComponent.class).getItemName(), expectedItemName);
	}

	private static Stream<Arguments> checkHotkeySelectionTriggerParams() {
		return Stream.of(
				arguments(0, "Hoe"),
				arguments(1, "Hoe1"),
				arguments(2, "Hoe2"),
				arguments(3, "Hoe3"),
				arguments(4, "Hoe4"),
				arguments(5, "Hoe5"),
				arguments(6, "Hoe6"),
				arguments(7, "Hoe7"),
				arguments(8, "Hoe8"),
				arguments(9, "Hoe9")
		);
	}

	@ParameterizedTest
	@MethodSource({"checkKeyboardInputHotkeyParams"})
	void checkKeyboardInputHotkey(int num) {
		ServiceLocator.registerInputService(new InputService());
		keyboardPlayerInputComponent.keyDown(num);
		verify(keyboardPlayerInputComponent).triggerHotKeySelection(num);
		verify(inventoryComponent).setHeldItem(num - 8 >= 0 ? num - 8 : 9);
		verify(playerActions).hotkeySelection(num - 8 >= 0 ? num - 8 : 9);
		assertEquals(inventoryComponent.getHeldIndex(), num - 8 >= 0 ? num - 8 : 9);
	}

	private static Stream<Arguments> checkKeyboardInputHotkeyParams() {
		return Stream.of(

				arguments(8),
				arguments(9),
				arguments(10),
				arguments(11),
				arguments(12),
				arguments(13),
				arguments(14),
				arguments(15),
				arguments(16),
				arguments(7)

		);
	}
}
