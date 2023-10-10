package com.csse3200.game.components.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.csse3200.game.areas.TestGameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

/**
 * Factory to create a mock player entity for testing.
 * Only includes necessary components for testing.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */
@ExtendWith(GameExtension.class)
public class TestInventoryUI {
	Entity player;
	InventoryDisplay inventoryDisplay;
	static InventoryComponent inventory;
	Stage stage;
	InventoryDisplayManager inventoryDisplayManager;
	ArgumentCaptor<Window> windowArgument;
	//TestGameArea to register so GameMap can be accessed through the ServiceLocator
	private static final TestGameArea gameArea = new TestGameArea();

	static String[] texturePaths = {
			"images/tool_shovel.png",
			"images/tool_hoe.png",
			"images/tool_scythe.png",
			"images/selected.png",
			"images/itemFrame.png"
	};

	static String[] skinPaths = {
			"gardens-of-the-galaxy/gardens-of-the-galaxy.json"
	};

	@BeforeAll
	static void Create() {
		inventory = new InventoryComponent(new ArrayList<>());

		// Necessary for the playerActions component
		GameMap gameMap = mock(GameMap.class);
		gameArea.setGameMap(gameMap);
	}

	/**
	 * Create a player with an inventory.
	 */
	@BeforeEach
	void createPlayer() {
		ServiceLocator.registerResourceService(new ResourceService());
		ServiceLocator.getResourceService().loadTextures(texturePaths);
		ServiceLocator.getResourceService().loadSkins(skinPaths);
		ServiceLocator.getResourceService().loadAll();

		windowArgument = ArgumentCaptor.forClass(Window.class);
		stage = mock(Stage.class);
		inventoryDisplayManager = new InventoryDisplayManager(stage);
		ServiceLocator.registerInventoryDisplayManager(inventoryDisplayManager);
		RenderService renderService = new RenderService();
		renderService.setStage(stage);
		ServiceLocator.registerRenderService(renderService);
		ServiceLocator.registerInputService(new InputService());
		ServiceLocator.registerGameArea(gameArea);

		inventoryDisplay = spy(new InventoryDisplay("updateInventory", "toggleInventory", 30, 10, false));
		player =
				new Entity()
						.addComponent(new PlayerActions())
						.addComponent(new KeyboardPlayerInputComponent())
						.addComponent(inventoryDisplay)
						.addComponent(inventory);
	}

	@Test
	void testToggleInventory() {
		player.create();
		verify(inventoryDisplay).create();
		verify(stage).addActor(windowArgument.capture());
		verify(inventoryDisplay).setDragItems(any(), any());
		Window window = windowArgument.getValue();

		Table inventorySlots = (Table) window.getChildren().begin()[1];
		for (Cell<?> cell : inventorySlots.getCells().toArray(Cell.class)) {
			assert !(cell.getActor() instanceof ItemSlot) || ((ItemSlot) cell.getActor()).getItemImage() == null;
		}

		player.getComponent(KeyboardPlayerInputComponent.class).setActions(player.getComponent(PlayerActions.class));
		player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.I);

		verify(inventoryDisplay).toggleOpen();
		window.getChildren().end();

	}

	@ParameterizedTest()
	@MethodSource({"addingItemsShouldAddInventoryImagesParams"})
	void addingItemsShouldAddInventoryImages(ItemComponent component, int expected) {
		ServiceLocator.registerResourceService(new ResourceService());
		ServiceLocator.getResourceService().loadTextures(texturePaths);
		ServiceLocator.getResourceService().loadSkins(skinPaths);
		ServiceLocator.getResourceService().loadAll();

		player.create();
		ArgumentCaptor<Window> win = ArgumentCaptor.forClass(Window.class);
		verify(stage).addActor(windowArgument.capture());
		verify(stage).addActor(win.capture());
		Entity i1 = new Entity(EntityType.ITEM).addComponent(component);
		inventory.addItem(i1);
		inventoryDisplay.toggleOpen();
		Window window = win.getValue();
		assert (window.getTitleLabel().textEquals("null Inventory"));
		inventoryDisplay.refreshInventory();
		Table inventorySlots = (Table) window.getChildren().begin()[1];
		int i = 0;
		for (Cell slot : inventorySlots.getCells().toArray(Cell.class)) {
			System.out.println(slot);
			assert ((ItemSlot) slot.getActor()).getChild(0) instanceof Image;
			assert ((ItemSlot) slot.getActor()).getChild(1) instanceof Stack;
			if (i++ <= expected) {
				assert ((Stack) ((ItemSlot) slot.getActor()).getChild(1)).getChild(0) instanceof Image;
			} else {
				assert ((Stack) ((ItemSlot) slot.getActor()).getChild(1)).getChildren().isEmpty();
			}
		}
	}

	private static Stream<Arguments> addingItemsShouldAddInventoryImagesParams() {
		ServiceLocator.registerResourceService(new ResourceService());
		ServiceLocator.getResourceService().loadTextures(texturePaths);
		ServiceLocator.getResourceService().loadAll();
		return Stream.of(
				arguments(new ItemComponent("Hoe", ItemType.HOE, "images/tool_hoe.png"), 0),
				arguments(new ItemComponent("Scythe", ItemType.SCYTHE, "images/tool_scythe.png"), 1),
				arguments(new ItemComponent("Shovel", ItemType.SHOVEL, "images/tool_shovel.png"), 2),
				arguments(new ItemComponent("Item", ItemType.FERTILISER, "images/tool_shovel.png"), 3)
		);
	}

	@AfterEach
	public void cleanUp() {
		// Clears all loaded services
		ServiceLocator.clear();
	}
}

