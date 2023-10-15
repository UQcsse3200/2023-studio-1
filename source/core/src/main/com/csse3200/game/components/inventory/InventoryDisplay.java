package com.csse3200.game.components.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.items.WateringCanLevelComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.csse3200.game.services.sound.EffectSoundFile;
import com.csse3200.game.services.sound.InvalidSoundFileException;
import org.jetbrains.annotations.NotNull;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.graphics.Texture;
import org.slf4j.LoggerFactory;

/**
 * An ui component for displaying player stats, e.g. health.
 */
public class InventoryDisplay extends UIComponent {
	private InventoryComponent inventory;
	private final Skin skin = ServiceLocator.getResourceService().getAsset("gardens-of-the-galaxy/gardens-of-the-galaxy.json", Skin.class);
	private final Table table = new Table(skin);
	private Window window;
	private final ArrayList<ItemSlot> slots = new ArrayList<>();
	private boolean isOpen = false;
	private DragAndDrop dnd;
	private ArrayList<Actor> actors;
	private Map<Stack, ItemSlot> map;
	private Map<ItemSlot, Integer> indexes;
	private final Integer size;
	private final Integer rowSize;
	private final boolean toolbar;
	private final String refreshEvent;
	private final String openEvent;
	private final InventoryDisplayManager inventoryDisplayManager;
	private final Map<Integer, TextTooltip> tooltips = new HashMap<>();
	private final InstantTooltipManager instantTooltipManager = new InstantTooltipManager();
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(InventoryDisplay.class);
	private boolean isPause = false;
	private boolean lastState = false;
	private Image bin = null;
 

	/**
	 * Constructor for class
	 *
	 * @param size    size of inventory
	 * @param rowSize amount of items per row
	 */
	public InventoryDisplay(String refreshEvent, String openEvent, Integer size, Integer rowSize, Boolean toolbar) {
		this.size = size;
		this.rowSize = rowSize;
		this.toolbar = toolbar;
		this.refreshEvent = refreshEvent;
		this.openEvent = openEvent;
		inventoryDisplayManager = ServiceLocator.getInventoryDisplayManager();
	}

	/**
	 * Creates reusable ui styles and adds actors to the stage.
	 */
	@Override
	public void create() {
		super.create();
		initialiseInventory();
		entity.getEvents().addListener(openEvent, this::toggleOpen);
		entity.getEvents().addListener(refreshEvent, this::refreshInventory);
		entity.getEvents().addListener(PlayerActions.events.ESC_INPUT.name(), this::setPause);
		entity.getEvents().addListener("hideUI", this::hide);
		inventoryDisplayManager.addInventoryDisplay(this);
	}

	public void setPause(){
		isPause = !isPause;
		if (isPause){
			lastState = isOpen;
			isOpen = false;
			window.setVisible(isOpen);
		} else {
			isOpen = lastState;
			window.setVisible(isOpen);
		}
	}

	/**
	 * Initialises the inventoryDisplay and adds it to the stage.
	 *
	 * @see Table for positioning options
	 */
	private void initialiseInventory() {
		window = new Window(entity.getType() + " Inventory", skin);

		// create variables needed for drag and drop
		dnd = new DragAndDrop();
		actors = new ArrayList<>();
		map = new HashMap<>();
		indexes = new HashMap<>();

		table.defaults().size(64, 64);
		table.pad(10);

		// loop through entire table and create itemSlots and add the slots to the stored array
		for (int i = 0; i < size; i++) {
			ItemSlot slot;
			if (inventory != null && this.inventory.getItem(i) != null) {
				slot = new ItemSlot(this.inventory.getItem(i).getComponent(ItemComponent.class).getItemTexture(), false);
				actors.add(slot.getDraggable());
			} else {
				slot = new ItemSlot(false);
			}

			table.add(slot).width(70).height(70).pad(10, 10, 10, 10);

			if ((i + 1) % rowSize == 0) {
				table.row();
			}
			slots.add(slot);
			map.put(slot.getDraggable(), slot);
			indexes.put(slot, i);
			if (slot.getItemImage() != null) {
				slot.getItemImage().setDebug(false);
			}
		}
		table.row();
		if (entity.getType() == EntityType.PLAYER) {
			bin = new Image(ServiceLocator.getResourceService().getAsset("images/bin.png", Texture.class));
			table.add(bin).colspan(10);
		}

		// Create a window for the inventory using the skin
		window.pad(40, 20, 20, 20);
		window.add(table);
		window.pack();
		window.setMovable(false);
		window.setVisible(false);
		stage.addActor(window);
	}

	/**
	 * Update Inventory user interface
	 */

	private void updateInventory() {
		dnd.clear();
		actors.clear(); // Clear the actors ArrayList

		for (int i = 0; i < size; i++) {
			ItemComponent item;
			Texture itemTexture;
			int itemCount;

			// if the item isn't null we will update the position, this will be in future replaced by an event
			if (inventory != null && inventory.getItem(i) != null) {
				item = inventory.getItem(i).getComponent(ItemComponent.class);
				itemCount = inventory.getItemCount(item.getEntity());
				itemTexture = item.getItemTexture();
				ItemSlot curSlot = slots.get(i);
				curSlot.setItemImage(new Image(itemTexture));
				actors.add(curSlot.getDraggable());
				curSlot.setCount(itemCount);
				map.put(curSlot.getDraggable(), curSlot);

				slots.set(i, curSlot);
			} else {
				ItemSlot curSlot = slots.get(i);
				curSlot.setItemImage(null);
				curSlot.setCount(0);
				curSlot.getDraggable().clear();
				slots.set(i, curSlot);
			}

		}
		dnd = new DragAndDrop();
		setDragItems(actors, map);
		addTooltips();
	}

	/**
	 * Set Drag Items
	 *
	 * @param actors list of actors
	 * @param map    images and their respective item slot
	 */
	public void setDragItems(@NotNull ArrayList<Actor> actors, Map<Stack, ItemSlot> map) {
		for (Actor item : actors) {
			dnd.addSource(new DragAndDrop.Source(item) {
				final DragAndDrop.Payload payload = new DragAndDrop.Payload();
				TextTooltip tooltip;

				@Override
				public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
					try {
						ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.DRAG_ITEM);
					} catch (InvalidSoundFileException e) {
						logger.error("sound not loaded");
					}
					payload.setObject(getActor());
					payload.setDragActor(getActor());
					stage.addActor(getActor());
					dnd.setDragActorPosition(50, -getActor().getHeight() / 2);
					ItemSlot slot = map.get((Stack)getActor());
					tooltips.get(indexes.get(slot)).hide();
					tooltip = tooltips.get(indexes.get(slot));
					slot.removeListener(tooltips.get(indexes.get(slot)));
					return payload;
				}

				@Override
				public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
					if (target == null) {
						ItemSlot itemSlot = map.get((Stack) getActor());
						itemSlot.add(getActor());
						itemSlot.addListener(tooltip);
					}
				}
			});
		}

		for (Cell<?> targetItem : table.getCells()) {
			if (targetItem.getActor() instanceof ItemSlot) {
				dnd.addTarget(new DragAndDrop.Target(targetItem.getActor()) {
					final ItemSlot slot = (ItemSlot) targetItem.getActor();

					@Override
					public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
						return true;
					}

					@Override
					public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
						ItemSlot sourceSlot = map.get(((Stack) source.getActor()));

						inventory.swapPosition(indexes.get(sourceSlot), indexes.get(slot));
						map.put(slot.getDraggable(), sourceSlot);

						map.put((Stack) payload.getDragActor(), slot);

						sourceSlot.setDraggable(slot.getDraggable());
						slot.setDraggable((Stack) source.getActor());

						entity.getEvents().trigger("updateToolbar");
						inventory.setHeldItem(inventory.getHeldIndex());
						addTooltips();
						try {
							ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.DROP_ITEM);
						} catch (InvalidSoundFileException e) {
							logger.error("sound not loaded");
						}
					}
				});
			} else {
				dnd.addTarget(new DragAndDrop.Target(targetItem.getActor()) {
					@Override
					public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
						return true;
					}
					@Override
					public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
						ItemSlot itemSlot = map.get((Stack) source.getActor());
						itemSlot.removeActor(source.getActor());
						itemSlot.add(source.getActor());
						inventory.removeItem(inventory.getHeldItemsEntity().get(inventory.getItemPlace().get(indexes.get(itemSlot))));
						addTooltips();
						try {
							ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.DELETE_ITEM);
						} catch (InvalidSoundFileException e) {
							logger.error("sound not loaded");
						}
					}
				});
			}
		}
	}

	public Map<ItemSlot, Integer> getIndexes() {
		return indexes;
	}

	public Map<Stack, ItemSlot> getMap() {
		return map;
	}

	public Image getBin() {
		return bin;
	}

	public ArrayList<ItemSlot> getSlots() {
		return slots;
	}

	/**
	 * Get the current window
	 *
	 * @return current window
	 */
	public Window getWindow() {
		return this.window;
	}

	/**
	 * The draw stage of the UIComponent, it is handled by the stage
	 *
	 * @param batch Batch to render to.
	 */
	@Override
	public void draw(SpriteBatch batch) {
		// Handled else where
	}

	/**
	 * Toggle the inventory open, and changes the window visibility
	 */
	public void toggleOpen() {
		if (isPause) {
			return;
		}
		isOpen = !isOpen;
		window.setVisible(isOpen);
		inventoryDisplayManager.updateDisplays();
		//Play the inventory sound effect
		try {
			ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.INVENTORY_OPEN);
		} catch (InvalidSoundFileException e) {
			logger.info("Inventory open sound not loaded");
		}
	}

	/**
	 * Hide the inventory.
	 * But why would you ever want to do that?
	 */
	public void hide() {
		isOpen = false;
		window.setVisible(false);
		inventoryDisplayManager.updateDisplays();
	}

	/**
	 * Fetches the player inventory and returns it
	 *
	 * @return inventory attached to display
	 */
	public InventoryComponent getInventory() {
		return inventory;
	}

	/**
	 * Fetch the updatedInventory and update display
	 */
	public void refreshInventory() {
		this.inventory = entity.getComponent(InventoryComponent.class);
		updateInventory();
		inventoryDisplayManager.addTargets();
		if (this.toolbar) {
			entity.getEvents().trigger("updateToolbar");
		}
	}

	/**
	 * Dispose of the component
	 */
	@Override
	public void dispose() {
		inventoryDisplayManager.removeInventoryDisplay(this);
		super.dispose();
	}

	public boolean isOpen() {
		return isOpen;
	}
	public DragAndDrop getDnd() {
		return dnd;
	}

	public void addTooltips() {
		TextTooltip tooltip;
		for (ItemSlot slot : slots) {
			int i = indexes.get(slot);
			if (inventory.getItem(i) != null) {
				ItemComponent item = inventory.getItem(i).getComponent(ItemComponent.class);
				if (Objects.equals(item.getItemName(), "watering_can")) {
					float level = item.getEntity().getComponent(WateringCanLevelComponent.class).getCurrentLevel();
					tooltip = new TextTooltip(item.getItemName() + "\n\nCurrent level is " + level, skin);
				} else {
					tooltip = new TextTooltip(item.getItemName() + "\n\n" + item.getItemDescription(),instantTooltipManager,skin);
				}
				if (tooltips.get(i) != null) {
					tooltips.get(i).hide();
					slot.removeListener(tooltips.get(i));
				}
				tooltip.getActor().setAlignment(Align.center);
				tooltip.setInstant(true);
				slot.addListener(tooltip);
				tooltips.put((i), tooltip);
			}
			else {
				if (tooltips.get(i) != null) {
					tooltips.get(i).hide();
					slot.removeListener(tooltips.get(i));
					tooltips.remove(i);
				}
			}
		}
	}
}