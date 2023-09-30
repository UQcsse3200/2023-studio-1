package com.csse3200.game.entities;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;
import java.util.function.Function;

/**
 * Handles periodic spawning of one entity
 */
public class EntitySpawner {
    /**
     * Maximum number of entities that can be spawned in one cycle
     */
    private final int maxSpawnCount;
    /**
     * Method that creates the entity
     */
    private final Function<Entity, Entity> spawner;
    /**
     * The player entity of the game
     */
    private final Entity player;
    /**
     * Linear growth rate of number of entities spawned each spawn cycle.
     */
    private final int growthRate;
    /**
     * Number of entities to spawn this cycle.
     */
    private int spawnCount;
    /**
     * The hour that the entities will be spawned or the hour after which a
     * randomGoal will be determined.
     */
    private final int spawnHour;
    /**
     * The game area to spawn the entities on.
     */
    private GameArea gameArea;
    /**
     * True iff time of day is after spawnHour for this spawn cycle.
     */
    private boolean hourReached;
    /**
     * Determines range, [0, randomRange], from which randomGoal will be randomly selected.
     * Set to 0 to disable and spawn exactly on spawnHour. Maximum of 23.
     */
    private final int randomRange;
    /**
     * The number of hours after spawnHour that must pass before a spawn occurs. Is determined randomly using
     * randomRange.
     */
    private int randomGoal;
    /**
     * The number of hours since a randomGoal has been determined.
     */
    private int randomCount;
    /**
     * Minimum number of times spawnHour must occur between spawns
     */
    private final int daysBetweenSpawns;
    /**
     * Number of times spawnHour has occurred this spawn cycle
     */
    private int dayCounter;
    /**
     * Counts the number of specific entities in the game currently.
     */
    private int entityCounter;
    /**
     * Max threshold or maximum amount of a specific type of entity in the game.
     * Determines if spawning can occur on not.
     */
    private final int maxEntitiesBeforeSpawn;

    /**
     * Constructor for EntitySpawner
     *
     * @param maxSpawnCount maximum number of entities that can be spawned in one cycle
     * @param spawner method that creates the entity
     * @param player the player entity of the game
     * @param growthRate linear growth rate of number of entities spawned each spawn cycle
     * @param initialSpawnCount the initial number of entities to be spawned
     * @param spawnHour the hour that the entities will be spawned or the hour after which a
     *                  randomGoal will be determined.
     * @param randomRange max number of hours that the entity may spawn after spawnHour
     * @param daysBetweenSpawns minimum number of times spawnHour must occur between spawns
     */
    public EntitySpawner(int maxSpawnCount, Function<Entity, Entity> spawner, Entity player, int growthRate,
                         int initialSpawnCount, int spawnHour, int randomRange, int daysBetweenSpawns) {
        this.maxSpawnCount = maxSpawnCount;
        this.spawner = spawner;
        this.player = player;
        this.growthRate = growthRate;
        this.spawnCount = initialSpawnCount;
        this.spawnHour = spawnHour;
        this.randomRange = randomRange % 24;
        this.daysBetweenSpawns = daysBetweenSpawns;
        randomCount = 0;
        hourReached = false;
        dayCounter = 0;
        this.entityCounter = 0;
        this.maxEntitiesBeforeSpawn = 10; //Arbitrary number for now.
    }

    /**
     * Set GameArea
     *
     * @param gameArea GameArea to spawn entities
     */
    public void setGameArea(GameArea gameArea) {
        this.gameArea = gameArea;
    }

    /**
     * Begin periodic spawning
     */
    public void startSpawner() {
        ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::hourUpdate);
    }

    /**
     * Handles each hour trigger - responsible for calling spawnNPC() at correct time
     */
    public void hourUpdate() {
        int hour = ServiceLocator.getTimeService().getHour();

        //Track number of spawnHours that have occurred
        if (hour == spawnHour) {
            dayCounter++;
        }
        //Ensures that the number of NPC entities doesn't overcrowd the game.
        if (this.entityCounter > this.maxEntitiesBeforeSpawn) {
            return;
        }
        //Only progress to randomisation or spawning if enough time
        //has passed between spawns
        //When day counter is above days between spawns, a new set of spawns can occur.
        if (dayCounter < daysBetweenSpawns) {
            return;
        }

        if (hour == spawnHour && !hourReached) {
            hourReached = true;
            // help the spawning behaviour of game become more randomised, giving a better experience.
            randomGoal = (int) (Math.random() * randomRange);
        }

         if (hourReached) {
             //If randomRange == 0 (not doing randomisation) or have completed
             // randomisation count - begin spawning

             if (randomCount == randomGoal) {
                spawnEntities();

             } else {
                randomCount++;
             }
        }
    }

    /**
     * Spawns entities onto the GameArea
     */
    public void spawnEntities() {
        //Reset for next spawn
        dayCounter = 0;
        randomCount = 0;
        randomGoal = 0;
        hourReached = false;

        //Spawn entities
        for (int i = 0; i < spawnCount; i++) {
            //Get random traverseable tile to spawn the entity
            List<GridPoint2> traverseables = gameArea.getMap().getTraversableTileCoordinates();
            int randomTileIndex = (int) Math.floor(Math.random() * (traverseables.size() - 1));
            GridPoint2 position = traverseables.get(randomTileIndex);

            //Create entity and spawn on gameArea
            Entity entity = spawner.apply(player);
            gameArea.spawnEntityAt(entity, position, true, true);
            setInGameEntitiesCount(ServiceLocator.getEntityService().getEntities(), entity);
        }

        //Apply growth rate
        if (maxSpawnCount >= (spawnCount + growthRate)) {
            spawnCount = (spawnCount + growthRate);
        }
    }

    /**
     * Sets the entity counter of a specific type of entity.
     */
    private void setInGameEntitiesCount(Array<Entity> entities, Entity specificEntity) {
        this.entityCounter = 0; //ensure that its reset everytime this is called.
        for (int i = 0; i < entities.size; i++) {
            Entity currentEntity = entities.get(i);
            if (currentEntity.getType() == null) {
                return;
            }
            if (currentEntity.getType().equals(specificEntity.getType())) {
                this.entityCounter += 1;
            }
        }
    }
}