package com.csse3200.game.entities;

import com.csse3200.game.entities.factories.TractorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import com.csse3200.game.areas.terrain.*;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.ComponentType;
import com.csse3200.game.components.ConeLightComponent;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.npc.AnimalAnimationController;
import com.csse3200.game.components.npc.GhostAnimationController;
import com.csse3200.game.components.npc.TamableComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.player.ItemPickupComponent;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.components.player.PlayerAnimationController;
import com.csse3200.game.components.ship.ShipAnimationController;
import com.csse3200.game.components.ship.ShipLightComponent;
import com.csse3200.game.components.ship.ShipProgressComponent;
import com.csse3200.game.components.ship.ShipTimeSkipComponent;
import com.csse3200.game.components.tractor.TractorActions;
import com.csse3200.game.entities.factories.ShipDebrisFactory;
import com.csse3200.game.entities.factories.ShipFactory;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.FactoryService;
import com.csse3200.game.services.ServiceLocator;

/**
 * Core entity class. Entities exist in the game and are updated each frame. All
 * entities have a
 * position and scale, but have no default behaviour. Components should be added
 * to an entity to
 * give it specific behaviour. This class should not be inherited or modified
 * directly.
 *
 * <p>
 * Example use:
 *
 * <pre>
 * Entity player = new Entity()
 *         .addComponent(new RenderComponent())
 *         .addComponent(new PlayerControllerComponent());
 * ServiceLocator.getEntityService().register(player);
 * </pre>
 */
public class Entity implements Json.Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Entity.class);
    private static int nextId = 0;
    private static final String EVT_NAME_POS = "setPosition";
    private static final String COMPONENTS_STRING = "components";
    private final int id;
    private EntityType type;
    private final IntMap<Component> components;
    private final EventHandler eventHandler;
    private boolean enabled = true;
    private boolean created = false;
    private Vector2 position = Vector2.Zero.cpy();
    private Vector2 scale = new Vector2(1, 1);
    private Array<Component> createdComponents;

    public Entity() {
        this.type = EntityType.DUMMY;
        id = nextId;
        nextId++;

        components = new IntMap<>(4);
        eventHandler = new EventHandler();
    }

    public Entity(EntityType type) {
        this.type = type;
        id = nextId;
        nextId++;

        components = new IntMap<>(4);
        eventHandler = new EventHandler();
    }

    /**
     * Enable or disable an entity. Disabled entities do not run update() or
     * earlyUpdate() on their
     * components, but can still be disposed.
     *
     * @param enabled true for enable, false for disable.
     */
    public void setEnabled(boolean enabled) {
        logger.debug("Setting enabled={} on entity {}", enabled, this);
        this.enabled = enabled;
    }

    /**
     * Get the entity's game position.
     *
     * @return position
     */
    public Vector2 getPosition() {
        return position.cpy(); // Cpy gives us pass-by-value to prevent bugs
    }

    /**
     * Set the entity's game position.
     *
     * @param position new position.
     */
    public void setPosition(Vector2 position) {
        this.position = position.cpy();
        getEvents().trigger(EVT_NAME_POS, position.cpy());
    }

    public void setCenterPosition(Vector2 position) {
        this.position = position.cpy().mulAdd(getScale(), -0.5f);
    }

    /**
     * Set the entity's game position.
     *
     * @param x new x position
     * @param y new y position
     */
    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
        getEvents().trigger(EVT_NAME_POS, position.cpy());
    }

    /**
     * Set the entity's game position and optionally notifies listeners.
     *
     * @param position new position.
     * @param notify   true to notify (default), false otherwise
     */
    public void setPosition(Vector2 position, boolean notify) {
        this.position = position;
        if (notify) {
            getEvents().trigger(EVT_NAME_POS, position);
        }
    }

    /**
     * Get the entity's scale. Used for rendering and physics bounding box
     * calculations.
     *
     * @return Scale in x and y directions. 1 = 1 metre.
     */
    public Vector2 getScale() {
        return scale.cpy(); // Cpy gives us pass-by-value to prevent bugs
    }

    /**
     * Set the entity's scale.
     *
     * @param scale new scale in metres
     */
    public void setScale(Vector2 scale) {
        this.scale = scale.cpy();
    }

    /**
     * Set the entity's scale.
     *
     * @param x width in metres
     * @param y height in metres
     */
    public void setScale(float x, float y) {
        this.scale.x = x;
        this.scale.y = y;
    }

    /**
     * Set the entity's width and scale the height to maintain aspect ratio.
     *
     * @param x width in metres
     */
    public void scaleWidth(float x) {
        this.scale.y = this.scale.y / this.scale.x * x;
        this.scale.x = x;
    }

    /**
     * Set the entity's height and scale the width to maintain aspect ratio.
     *
     * @param y height in metres
     */
    public void scaleHeight(float y) {
        this.scale.x = this.scale.x / this.scale.y * y;
        this.scale.y = y;
    }

    /**
     * Get the entity's center position
     *
     * @return center position
     */
    public Vector2 getCenterPosition() {
        return getPosition().mulAdd(getScale(), 0.5f);
    }

    /**
     * Get a component of type T on the entity.
     *
     * @param type The component class, e.g. RenderComponent.class
     * @param <T>  The component type, e.g. RenderComponent
     * @return The entity component, or null if nonexistent.
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> type) {
        ComponentType componentType = ComponentType.getFrom(type);
        return (T) components.get(componentType.getId());
    }

    /**
     * Add a component to the entity. Can only be called before the entity is
     * registered in the world.
     *
     * @param component The component to add. Only one component of a type can be
     *                  added to an entity.
     * @return Itself
     */
    public Entity addComponent(Component component) {
        if (created) {
            logger.error(
                    "Adding {} to {} after creation is not supported and will be ignored", component, this);
            return this;
        }
        ComponentType componentType = ComponentType.getFrom(component.getClass());
        if (components.containsKey(componentType.getId())) {
            logger.error(
                    "Attempted to add multiple components of class {} to {}. Only one component of a class "
                            + "can be added to an entity, this will be ignored.",
                    component,
                    this);
            return this;
        }
        components.put(componentType.getId(), component);
        component.setEntity(this);

        return this;
    }

    /**
     * Dispose of the entity. This will dispose of all components on this entity.
     */
    public void dispose() {
        for (Component component : createdComponents) {
            component.dispose();
        }
        ServiceLocator.getEntityService().unregister(this);
    }

    /**
     * Create the entity and start running. This is called when the entity is
     * registered in the world,
     * and should not be called manually.
     */
    public void create() {
        if (created) {
            logger.error(
                    "{} was created twice. Entity should only be registered with the entity service once.",
                    this);
            return;
        }
        createdComponents = components.values().toArray();
        for (Component component : createdComponents) {
            component.create();
        }
        created = true;
    }

    /**
     * Perform an early update on all components. This is called by the entity
     * service and should not
     * be called manually.
     */
    public void earlyUpdate() {
        if (!enabled) {
            return;
        }
        for (Component component : createdComponents) {
            component.triggerEarlyUpdate();
        }
    }

    /**
     * Perform an update on all components. This is called by the entity service and
     * should not be
     * called manually.
     */
    public void update() {
        if (!enabled) {
            return;
        }
        getEvents().update();
        for (Component component : createdComponents) {
            component.triggerUpdate();
        }
    }

    public void togglePauseAnimations(boolean pausePlayer) {
        if (!pausePlayer) {
            for (Component component : createdComponents) {
                if (component instanceof KeyboardPlayerInputComponent ||
                        component instanceof PlayerAnimationController ||
                        component instanceof AnimalAnimationController ||
                        component instanceof GhostAnimationController ||
                        component instanceof TamableComponent ||
                        component instanceof ItemPickupComponent) {
                    return;
                }
            }
        }
        for (Component component : createdComponents) {
            if (component instanceof PlayerAnimationController) {
                return;
            }
        }
        for (Component component : createdComponents) {
            if (component instanceof AnimationRenderComponent animationRenderComponent) {
                animationRenderComponent.togglePauseAnimation();
            }
        }
    }

    /**
     * This entity's unique ID. Used for equality checks
     *
     * @return unique ID
     */
    public int getId() {
        return id;
    }

    /**
     * Get the event handler attached to this entity. Can be used to trigger events
     * from an attached
     * component, or listen to events from a component.
     *
     * @return entity's event handler
     */
    public EventHandler getEvents() {
        return eventHandler;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Entity entity) && entity.getId() == this.getId();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Entity{id=%d} {type=%s}", id, type == null ? "none" : type.toString());
    }

    /**
     * Writes to the json info about entities.
     * Writes the entities x,y coordinates
     * ALso loops through the entities associated components and writes information
     * to the json about
     * the component.
     * note each component should have a override write function
     *
     * @param json which is a valid Json that is written to
     */
    public void write(Json json) {
        // Should be gone but incase double check
        if (getType() == EntityType.ITEM || getType() == null) {
            return;
        }

        json.writeValue("Entity", getType());
        float posX = position.x;
        float posY = position.y;
        json.writeValue("x", posX);
        json.writeValue("y", posY);
        json.writeObjectStart(COMPONENTS_STRING);
        if (createdComponents == null) {
            return;
        }
        for (Component c : createdComponents) {
            c.write(json);
        }
        json.writeObjectEnd();
    }

    /**
     * Writes the item to the json file
     *
     * @param json which is a valid Json that is written to
     */
    public void writeItem(Json json) {
        json.writeValue("name", this.getComponent(ItemComponent.class).getItemName());
        if (createdComponents != null) {
            json.writeObjectStart(COMPONENTS_STRING);
            for (Component c : createdComponents) {
                c.write(json);
            }
            json.writeObjectEnd();
        }
    }

    /**
     * Reads the item entity from the json file
     *
     * @param json    which is a valid Json that is read from
     * @param jsonMap which is a valid JsonValue that is read from
     */
    public void readItem(Json json, JsonValue jsonMap) {
        if (createdComponents != null) {
            for (Component c : createdComponents) {
                c.read(json, jsonMap);
            }
        }
    }

    /**
     * Reads the json file and creates the entities based on the information in the
     * json file
     *
     * @param json    which is a valid Json that is read from
     * @param jsonMap which is a valid JsonValue that is read from
     */
    public void read(Json json, JsonValue jsonMap) {
        // This method creates an Entity but will not use that Entity for anything that
        // is being remade as
        // it makes the code duplication extremely high as it is a whole factory here

        // Saves the position
        position = new Vector2(jsonMap.getFloat("x"), jsonMap.getFloat("y"));

        // Gets the type of Entity
        String value = jsonMap.getString("Entity");
        try {
            type = EntityType.valueOf(value);
        } catch (IllegalArgumentException e) {
            type = null;
            return;
        }

        switch (type) {
            case TRACTOR:
                readTractor(jsonMap);
                break;
            case TILE:
                readTile(json, jsonMap);
                break;
            case SHIP_PART_TILE:
                readShipPartTile(json, jsonMap);
                break;
            case SHIP_DEBRIS:
                readShipDebris();
                break;
            case SHIP:
                readShip(json, jsonMap);
                break;
            case PLAYER:
                readPlayer(json, jsonMap);
                break;
            default:
                if (FactoryService.getNpcFactories().containsKey(type)) {
                    // Makes a new NPC
                    Entity npc = FactoryService.getNpcFactories().get(type).get();
                    if (npc.getComponent(TamableComponent.class) != null) {
                        npc.getComponent(TamableComponent.class).read(json, jsonMap.get(COMPONENTS_STRING));
                    }
                    ServiceLocator.getGameArea().spawnEntity(npc);
                    npc.setPosition(position);
                } else if (FactoryService.getPlaceableFactories().containsKey(type.toString())) {
                    // Makes a new Placeable
                    Entity placeable = FactoryService.getPlaceableFactories().get(type.toString()).get();
                    placeable.setPosition(position);
                    if (placeable.getType() == EntityType.CHEST) {
                        placeable.getComponent(InventoryComponent.class).read(json, jsonMap.get(COMPONENTS_STRING));
                    }
                    ServiceLocator.getGameArea().getMap().getTile(position).setOccupant(placeable);
                    ServiceLocator.getGameArea().spawnEntity(placeable);
                }
                break;
        }
    }

    private void readPlayer(Json json, JsonValue jsonMap) {
        // Does not make a new player, instead just updates the current one
        InventoryComponent inventoryComponent = new InventoryComponent();
        JsonValue inv = jsonMap.get(COMPONENTS_STRING);
        inventoryComponent.read(json, inv);
        this.addComponent(inventoryComponent);
    }

    private void readShip(Json json, JsonValue jsonMap) {
        Entity ship = ShipFactory.createShip();

        ServiceLocator.getGameArea().spawnEntity(ship);

        ShipAnimationController shipAnimationController = ship.getComponent(ShipAnimationController.class);
        shipAnimationController.read(json, jsonMap.get(COMPONENTS_STRING).get(ShipAnimationController.class.getSimpleName()));

        ShipProgressComponent progressComponent = ship.getComponent(ShipProgressComponent.class);
        progressComponent.read(json, jsonMap.get(COMPONENTS_STRING).get(ShipProgressComponent.class.getSimpleName()));

        ShipTimeSkipComponent shipTimeSkipComponent = ship.getComponent(ShipTimeSkipComponent.class);
        JsonValue shipTimeSkipComponentJson = jsonMap.get(COMPONENTS_STRING).get(ShipTimeSkipComponent.class.getSimpleName());
        shipTimeSkipComponent.read(json, shipTimeSkipComponentJson);

        ShipLightComponent shipLightComponent = ship.getComponent(ShipLightComponent.class);
        JsonValue shipLightComponentJson = jsonMap.get(COMPONENTS_STRING)
                .get(ShipLightComponent.class.getSimpleName());
        shipLightComponent.read(json, shipLightComponentJson);

        ship.setPosition(position);
    }

    private void readShipDebris() {
        Entity shipDebris = ShipDebrisFactory.createShipDebris();
        ServiceLocator.getGameArea().spawnEntity(shipDebris);
        shipDebris.setPosition(position);

        TerrainTile debrisTerrainTile = ServiceLocator.getGameArea().getMap().getTile(position);
        debrisTerrainTile.setOccupant(shipDebris);
        debrisTerrainTile.setOccupied();
    }

    private void readShipPartTile(Json json, JsonValue jsonMap) {
        Entity partTile = ShipPartTileFactory.createShipPartTile(position);
        ServiceLocator.getGameArea().spawnEntity(partTile);
        partTile.setPosition(position);

        TerrainTile partTerrainTile = ServiceLocator.getGameArea().getMap().getTile(position);
        if (partTerrainTile.isOccupied() && partTerrainTile.getOccupant().getType() == EntityType.SHIP_DEBRIS) {
            // remove the ship debris that is occupying the terrain tile, since it will
            // be recreated by the ShipPartTileComponent
            Entity unneededShipDebris = partTerrainTile.getOccupant();
            unneededShipDebris.dispose();
            partTerrainTile.removeOccupant();

            partTile.getComponent(ShipPartTileComponent.class).read(json, jsonMap);

            partTerrainTile.setOccupant(partTile);
            partTerrainTile.setOccupied();
        }
    }

    private void readTile(Json json, JsonValue jsonMap) {
        Entity tile = TerrainCropTileFactory.createTerrainEntity(position);
        tile.getComponent(CropTileComponent.class).read(json, jsonMap);
        ServiceLocator.getGameArea().spawnEntity(tile);
        tile.setPosition(position);
        TerrainTile terrainTile = ServiceLocator.getGameArea().getMap().getTile(tile.getPosition());
        terrainTile.setOccupant(tile);
    }

    /**
     * Gets the type of entity
     *
     * @return the type of entity from EntityType enum
     */
    public EntityType getType() {
        return type;
    }

    public void readTractor(JsonValue jsonMap) {
        // Make a new tractor
        Entity tractor = TractorFactory.createTractor();
        JsonValue tractorActions = jsonMap.get(COMPONENTS_STRING).get("TractorActions");
        tractor.getComponent(TractorActions.class).setMuted(tractorActions.getBoolean("isMuted"));
        JsonValue lightJsonMap = jsonMap.get(COMPONENTS_STRING).get("ConeLightComponent");
        if (lightJsonMap.getBoolean("isActive")) {
            tractor.getComponent(ConeLightComponent.class).toggleLight();
        }
        ServiceLocator.getGameArea().setTractor(tractor);
        tractor.setPosition(position);
        ServiceLocator.getGameArea().spawnEntity(tractor);
    }
}
