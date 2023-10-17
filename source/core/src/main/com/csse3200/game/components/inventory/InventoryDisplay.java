package com.csse3200.game.components.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.items.ItemActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityIndicator;
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
	private final ArrayList<EntityIndicator> entityIndicators = new ArrayList<>();

	// In your InventoryDisplay class:

	// A function to add the association:
	private TerrainComponent terrainComponent;

	private Map<Actor, Entity> actorToEntityMap = new HashMap<>();

	private Map<Stack, ItemSlot> map;
	private Map<ItemSlot, Integer> indexes;
	private final Integer size;
	private final Integer rowSize;
	private final boolean toolbar;
	private final String refreshEvent;
	private final String openEvent;
	private final InventoryDisplayManager inventoryDisplayManager;
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(InventoryDisplay.class);

	public void associateActorWithEntity(Actor actor, Entity entity) {
		actorToEntityMap.put(actor, entity);
	}

	// Method to get the Entity based on the given Actor
	public Entity getEntityFromActor(Actor actor) {
		return actorToEntityMap.get(actor);
	}
	private  TerrainTile getTileAtPosition(Vector2 mousePos) {
		Vector2 pos = ItemActions.getAdjustedPos(mousePos);
		return ServiceLocator.getGameArea().getMap().getTile(pos);
	}

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
		inventoryDisplayManager.addInventoryDisplay(this);
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
			Image deleteSlot = new Image(ServiceLocator.getResourceService().getAsset("images/bin.png", Texture.class));
			table.add(deleteSlot).colspan(10);
		}

		// Create a window for the inventory using the skin
		window.pad(40, 20, 20, 20);
		window.add(table);
		window.pack();
		window.setMovable(false);
		window.setVisible(false);
		stage.addActor(window);
		setDragItems(actors, map);
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
				curSlot.getDraggable().clear();
				curSlot.setCount(0);
				slots.set(i, curSlot);
			}

		}
		dnd = new DragAndDrop();
		setDragItems(actors, map);
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

				@Override
				public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
					payload.setObject(getActor());
					payload.setDragActor(getActor());
					stage.addActor(getActor());
					dnd.setDragActorPosition(50, -getActor().getHeight() / 2);
					return payload;
				}


				@Override
				public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
					Actor draggedActor = (Actor) payload.getDragActor();
					if (draggedActor instanceof Stack) {
						ItemSlot itemSlot = map.get((Stack) draggedActor);
						if (itemSlot != null) {
							Image image = itemSlot.getItemImage();
							Drawable drawable = image.getDrawable();
							TextureRegion region = null;
							Image spawnedItemImage = new Image(region);

							if (drawable instanceof TextureRegionDrawable) {
								region = ((TextureRegionDrawable) drawable).getRegion();
							}

							Vector2 stageCoordinates = window.getStage().screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

							System.out.println("Stage Coordinates: " + stageCoordinates); // Debug statement 1

							float windowStartX = 0.0f;
							float windowStartY = 0.0f;
							float windowEndX = windowStartX + 960.0f;
							float windowEndY = windowStartY + 414.0f;

							if (stageCoordinates.x < windowStartX || stageCoordinates.x > windowEndX || stageCoordinates.y < windowStartY || stageCoordinates.y > windowEndY) {
								TerrainTile tileAtDropPosition = getTileAtPosition(stageCoordinates);

								System.out.println("Tile at Drop Position: " + tileAtDropPosition); // Debug statement 2

								if (tileAtDropPosition != null && tileAtDropPosition.isTraversable()) {
									inventory.removeItem(inventory.getHeldItemsEntity().get(inventory.getItemPlace().get(indexes.get(itemSlot))));
//									draggedActor.remove();

									Vector2 playerPosition = ServiceLocator.getGameArea().getPlayer().getPosition();

									System.out.println("Player Position: " + playerPosition); // Debug statement 3

									GridPoint2 gridTilePosition = new GridPoint2((int) playerPosition.x + 1, (int) playerPosition.y);
									GridPoint2 spawnPosition = new GridPoint2(gridTilePosition.x + 1, gridTilePosition.y);

									System.out.println("Spawn Position: " + spawnPosition); // Debug statement 4

									spawnedItemImage.setSize(64, 64);
									spawnedItemImage.setPosition(gridTilePosition.x, gridTilePosition.y);
									TerrainComponent terrain = ServiceLocator.getGameArea().getTerrain(); // hypothetical method, ensure you have a getTerrain() or equivalent in your ServiceLocator's GameArea.
									/* Terrain need to be set before spawning */

								//	ServiceLocator.getGameArea().spawnEntityAt(entity, spawnPosition, true, true);
								}
							}
						}
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
						ItemSlot sourceSlot = map.get((Stack) (source.getActor()));
						inventory.removeItem(inventory.getHeldItemsEntity().get(inventory.getItemPlace().get(indexes.get(sourceSlot))));

					}
				});
			}
		}
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
}