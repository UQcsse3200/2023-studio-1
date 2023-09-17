package com.csse3200.game.components.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Objects;
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
    ArgumentCaptor<Window> windowArgument;

    @BeforeAll
    static void Create() {
        inventory = new InventoryComponent(new ArrayList<>());
        System.out.println(inventory);

    }
    /**
     * Create a player entity.
     */
    @BeforeEach
    void createPlayer() {
        windowArgument = ArgumentCaptor.forClass(Window.class);
        stage = mock(Stage.class);
        RenderService renderService = new RenderService();
        renderService.setStage(stage);
        ServiceLocator.registerRenderService(renderService);
        ServiceLocator.registerInputService(new InputService());

        inventoryDisplay = spy(new InventoryDisplay(inventory));
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
        verify(inventoryDisplay).setDragItems(any(),any());
        Window window = windowArgument.getValue();

        Table inventorySlots = (Table) window.getChildren().begin()[1];
        for (Cell cell : inventorySlots.getCells().toArray(Cell.class))
        {
            assert !(cell.getActor() instanceof ItemSlot) || ((ItemSlot) cell.getActor()).getItemImage() == null;
        }

        assert(inventoryDisplay.getInventory() == player.getComponent(InventoryComponent.class));
        player.getComponent(KeyboardPlayerInputComponent.class).setActions(player.getComponent(PlayerActions.class));
        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.I);

        verify(inventoryDisplay).toggleOpen();

    }

    @ParameterizedTest(name = "adding {0} ShouldAddInventoryImagesParams")
    @MethodSource({"addingItemsShouldAddInventoryImagesParams"})
    void addingItemsShouldAddInventoryImages(ItemComponent component, int expected) {
        player.create();

        verify(stage).addActor(windowArgument.capture());
        Entity i1 = new Entity().addComponent(component);
        inventory.addItem(i1);
        verify(inventoryDisplay).updateInventory();
        inventoryDisplay.toggleOpen();
        Window window = windowArgument.getValue();
        assert(window.getTitleLabel().textEquals("Inventory"));
        Table inventorySlots = (Table) window.getChildren().begin()[0];
        assert Objects.equals(((ItemSlot) (inventorySlots.getCells().toArray(Cell.class))[0].getActor()).getItemImage().toString(), Integer.toString(expected));
    }

    private static Stream<Arguments> addingItemsShouldAddInventoryImagesParams() {
        return Stream.of(
                arguments(new ItemComponent("Hoe", ItemType.HOE, new Texture(Gdx.files.internal("images/tool_hoe.png"))),1)
                //arguments(new ItemComponent("Scythe", ItemType.SCYTHE, new Texture(Gdx.files.internal("images/tool_scythe.png"))),3),
                //arguments(new ItemComponent("Shovel", ItemType.SHOVEL, new Texture(Gdx.files.internal("images/tool_shovel.png"))),5)
        );
    }



}

