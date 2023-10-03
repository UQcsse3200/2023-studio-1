package com.csse3200.game.components.inventory;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.stream.Stream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.entities.EntityType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

/**
 * Factory to create a mock player entity for testing.
 * Only includes necessary components for testing.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */
@ExtendWith(GameExtension.class)
public class TestToolbarUI {
    Entity player;
    ToolbarDisplay toolbarDisplay;
    static InventoryComponent inventory;
    ArgumentCaptor<Window> windowArgument;
    Stage stage;


    @BeforeAll
    static void Create() {
         inventory = new InventoryComponent(new ArrayList<>());
    }
    /**
     * Create a player with an inventory.
     */
    @BeforeEach
    void createPlayer() {
        stage = mock(Stage.class);
        windowArgument = ArgumentCaptor.forClass(Window.class);
        RenderService renderService = new RenderService();
        renderService.setStage(stage);

        ServiceLocator.registerRenderService(renderService);
        ServiceLocator.registerInputService(new InputService());
        inventory = new InventoryComponent(new ArrayList<>());
        toolbarDisplay = spy(new ToolbarDisplay());

        player =
                new Entity()
                        .addComponent(new PlayerActions())
                        .addComponent(new KeyboardPlayerInputComponent())
                        .addComponent(toolbarDisplay)
                        .addComponent(inventory);
    }
    @Test
    void testToggleToolbar() {
        player.create();
        verify(toolbarDisplay).create();
        verify(stage).addActor(windowArgument.capture());
        Window window = windowArgument.getValue();

        Table inventorySlots = (Table) window.getChildren().begin()[1];
        for (Cell<?> cell : inventorySlots.getCells().toArray(Cell.class))
        {
            assert !(cell.getActor() instanceof ItemSlot) || ((ItemSlot) cell.getActor()).getItemImage() == null;
        }

        player.getComponent(KeyboardPlayerInputComponent.class).setActions(player.getComponent(PlayerActions.class));
        player.getComponent(KeyboardPlayerInputComponent.class).keyDown(Input.Keys.I);
       //verify(toolbarDisplay).updateInventory();
        verify(toolbarDisplay).toggleOpen();
    }
    @ParameterizedTest
    @MethodSource({"addingItemsShouldAddInventoryImagesParams"})
    void addingItemsShouldAddInventoryImages(ItemComponent component, int expected) {
        player.create();
        ArgumentCaptor<Window> win = ArgumentCaptor.forClass(Window.class);
        verify(stage).addActor(windowArgument.capture());
        verify(stage).addActor(win.capture());
        Entity i1 = new Entity(EntityType.ITEM).addComponent(component);
        inventory.addItem(i1);
        toolbarDisplay.updateInventory();
        toolbarDisplay.toggleOpen();
        Window window = windowArgument.getValue();
        Table inventorySlots = (Table) window.getChildren().begin()[1];
        int i = 0;
        for (Cell slot:inventorySlots.getCells().toArray(Cell.class) ) {
            assert ((ItemSlot) slot.getActor()).getChild(0) instanceof Image;
            assert ((ItemSlot) slot.getActor()).getChild(1) instanceof Stack;
            assert ((ItemSlot) slot.getActor()).getChild(2) instanceof Label;
            assert Integer.parseInt(((Label) ((ItemSlot) slot.getActor()).getChild(2)).getText().toString().trim()) == (i + 1) % 10;
            if (i++ == 0) {
                assert ((Stack) ((ItemSlot) slot.getActor()).getChild(1)).getChild(0) instanceof Image;
            }
            else {
                assert ((Stack) ((ItemSlot) slot.getActor()).getChild(1)).getChildren().isEmpty() ;
            }
        }
    }

    private static Stream<Arguments> addingItemsShouldAddInventoryImagesParams() {
        return Stream.of(
                arguments(new ItemComponent("Hoe", ItemType.HOE, new Texture(Gdx.files.internal("images/tool_hoe.png"))),0),
                arguments(new ItemComponent("Scythe", ItemType.SCYTHE, new Texture(Gdx.files.internal("images/tool_scythe.png"))),1),
                arguments(new ItemComponent("Shovel", ItemType.SHOVEL, new Texture(Gdx.files.internal("images/tool_shovel.png"))),2),
                arguments(new ItemComponent("Item", ItemType.FERTILISER, new Texture(Gdx.files.internal("images/tool_shovel.png"))),3),
                arguments(new ItemComponent("Hoe", ItemType.HOE, new Texture(Gdx.files.internal("images/tool_hoe.png"))),4),
                arguments(new ItemComponent("Scythe", ItemType.SCYTHE, new Texture(Gdx.files.internal("images/tool_scythe.png"))),5),
                arguments(new ItemComponent("Shovel", ItemType.SHOVEL, new Texture(Gdx.files.internal("images/tool_shovel.png"))),6),
                arguments(new ItemComponent("Item", ItemType.FERTILISER, new Texture(Gdx.files.internal("images/tool_shovel.png"))),7),
                arguments(new ItemComponent("Hoe", ItemType.HOE, new Texture(Gdx.files.internal("images/tool_hoe.png"))),8),
                arguments(new ItemComponent("Scythe", ItemType.SCYTHE, new Texture(Gdx.files.internal("images/tool_scythe.png"))),9)

        );
    }



}
